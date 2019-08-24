package controllersTesting;

import com.despegar.http.client.GetMethod;
import com.despegar.http.client.HttpClientException;
import com.despegar.http.client.HttpResponse;
import com.despegar.http.client.PostMethod;
import com.despegar.sparkjava.test.SparkServer;
import controllers.AccountController;
import controllers.ExceptionController;
import controllers.TransferController;
import dataStore.DatabaseCreation;
import model.Transfer;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import service.impl.AccountServiceImpl;
import service.impl.TransferServiceImpl;
import spark.servlet.SparkApplication;
import utils.DatabaseCleanup;
import utils.ModelsInitialization;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class TransferControllerTest {
    public static class TransferControllerTestSparkApplication implements SparkApplication {

        @Override
        public void init() {
            new TransferController(new TransferServiceImpl(new AccountServiceImpl())).init();
            new AccountController(new AccountServiceImpl()).init();
            new ExceptionController().init();
        }
    }

    @ClassRule
    public static SparkServer<TransferControllerTestSparkApplication> testServer =
            new SparkServer<>(TransferControllerTestSparkApplication.class, 9999);


    @Before
    public void resetDatabase() {
        DatabaseCleanup.prepareDatabase();
        ModelsInitialization.init();
    }

    @Ignore
    @Test
    /*positive test*/
    public void PostTransfer() throws HttpClientException {
        String jsonString = "{'from':'4000123412341234','to':'4000123412341235','money':'5'}";

        PostMethod post = testServer.post("/transfers", jsonString, false);
        HttpResponse httpResponse = testServer.execute(post);

        String expectedTransfer = new Transfer(new BigInteger("4000123412341234"),
                new BigInteger("4000123412341235"), 5.0, 0.0755,
                new Timestamp(new Date().getTime())).toString();
        String transferInResponse = new String(httpResponse.body());

        String transferInResponseWithoutTime = transferInResponse.substring(0, transferInResponse.indexOf("transferredAt"));
        String expectedTransferWithoutTime = expectedTransfer.substring(0, expectedTransfer.indexOf("transferredAt"));

        assertEquals(200, httpResponse.code());
        assertEquals(expectedTransferWithoutTime, transferInResponseWithoutTime);

        GetMethod getAccountFrom = testServer.get("/accounts/4000123412341234", false);
        httpResponse = testServer.execute(getAccountFrom);

        assertEquals(200, httpResponse.code());
        assertEquals(ModelsInitialization.accountTransferredFrom.toString(), new String(httpResponse.body()));

        GetMethod getAccountTo = testServer.get("/accounts/4000123412341235", false);
        httpResponse = testServer.execute(getAccountTo);

        assertEquals(200, httpResponse.code());
        assertEquals(ModelsInitialization.accountTransferredTo.toString(), new String(httpResponse.body()));
    }

    @Ignore
    @Test
    /*positive test*/
    public void GetTransfers() throws HttpClientException {
        ArrayList<Transfer> transfers = ModelsInitialization.transfersForTest;

        GetMethod get = testServer.get("/transfers", false);
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(200, httpResponse.code());
        assertEquals(transfers.toString(), new String(httpResponse.body()));
    }

    @Ignore
    @Test
    /*negative test when accountFrom doesn't exist*/
    public void PostTransferAccountFromNonExist() throws HttpClientException {
        String jsonString = "{'from':'1','to':'4000123412341234','money':'5'}";

        PostMethod post = testServer.post("/transfers", jsonString, false);
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(404, httpResponse.code());
        assertEquals("Account with id 1 doesn't exist", new String(httpResponse.body()));

        GetMethod getAccountTo = testServer.get("/accounts/4000123412341234", false);
        httpResponse = testServer.execute(getAccountTo);

        assertEquals(200, httpResponse.code());
        assertEquals(ModelsInitialization.accountForTest.toString(), new String(httpResponse.body()));

    }

    @Ignore
    @Test
    /*negative test when accountTo doesn't exist*/
    public void PostTransferAccountToNonExist() throws HttpClientException {
        String jsonString = "{'from':'4000123412341234','to':'1','money':'5'}";

        PostMethod post = testServer.post("/transfers", jsonString, false);
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(404, httpResponse.code());
        assertEquals("Account with id 1 doesn't exist", new String(httpResponse.body()));

        GetMethod getAccountFrom = testServer.get("/accounts/4000123412341234", false);
        httpResponse = testServer.execute(getAccountFrom);

        assertEquals(200, httpResponse.code());
        assertEquals(ModelsInitialization.accountForTest.toString(), new String(httpResponse.body()));

    }

    @Ignore
    @Test
    /*negative test when no accounts in database*/
    public void PostTransferNoAccountsExist() throws HttpClientException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        String jsonString = "{'from':'4000123412341234','to':'4000123412341235','money':'5'}";

        PostMethod post = testServer.post("/transfers", jsonString, false);
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(404, httpResponse.code());
        assertEquals("No accounts exist in the database", new String(httpResponse.body()));
    }

    @Ignore
    @Test
    /*negative test when balance is not enough*/
    public void PostTransferBalanceNotEnough() throws HttpClientException {
        String jsonString = "{'from':'4000123412341234','to':'4000123412341235','money':'10000'}";

        PostMethod post = testServer.post("/transfers", jsonString, false);
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(404, httpResponse.code());
        assertEquals("Account with id 4000123412341234 can't transfer 10000.0 RUB" +
                ", the balance is only 23.56 RUB", new String(httpResponse.body()));

        GetMethod getAccountFrom = testServer.get("/accounts/4000123412341234", false);
        httpResponse = testServer.execute(getAccountFrom);

        assertEquals(200, httpResponse.code());
        assertEquals(ModelsInitialization.accountsForTest.get(0).toString(), new String(httpResponse.body()));

        GetMethod getAccountTo = testServer.get("/accounts/4000123412341235", false);
        httpResponse = testServer.execute(getAccountTo);

        assertEquals(200, httpResponse.code());
        assertEquals(ModelsInitialization.accountsForTest.get(1).toString(), new String(httpResponse.body()));
    }
}
