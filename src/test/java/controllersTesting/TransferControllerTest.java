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
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import service.impl.AccountServiceImpl;
import service.impl.TransferServiceImpl;
import spark.servlet.SparkApplication;
import utils.DatabaseCleanup;
import utils.ModelsInitialization;

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
    public static SparkServer<TransferControllerTestSparkApplication> testServerTransfers =
            new SparkServer<>(TransferControllerTestSparkApplication.class, 9998);


    @Before
    public void resetDatabase() {
        DatabaseCleanup.prepareDatabase();
        ModelsInitialization.init();
    }

    
    @Test
    /*positive test*/
    public void PostTransfer() throws HttpClientException {
        String jsonString = "{'from':'4000123412341234','to':'4000123412341235','money':'5'}";

        PostMethod post = testServerTransfers.post("/transfers", jsonString, false);
        HttpResponse httpResponse = testServerTransfers.execute(post);

        assertEquals(200, httpResponse.code());
      }


    @Test
    /*positive test*/
    public void GetTransfers() throws HttpClientException {
        GetMethod get = testServerTransfers.get("/transfers", false);
        HttpResponse httpResponse = testServerTransfers.execute(get);

        assertEquals(200, httpResponse.code());
    }

    
    @Test
    /*negative test when accountFrom doesn't exist*/
    public void PostTransferAccountFromNonExist() throws HttpClientException {
        String jsonString = "{'from':'1','to':'4000123412341234','money':'5'}";

        PostMethod post = testServerTransfers.post("/transfers", jsonString, false);
        HttpResponse httpResponse = testServerTransfers.execute(post);

        assertEquals(404, httpResponse.code());
        assertEquals("Account with id 1 doesn't exist", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test when accountTo doesn't exist*/
    public void PostTransferAccountToNonExist() throws HttpClientException {
        String jsonString = "{'from':'4000123412341234','to':'1','money':'5'}";

        PostMethod post = testServerTransfers.post("/transfers", jsonString, false);
        HttpResponse httpResponse = testServerTransfers.execute(post);

        assertEquals(404, httpResponse.code());
        assertEquals("Account with id 1 doesn't exist", new String(httpResponse.body()));

    }

    
    @Test
    /*negative test when no accounts in database*/
    public void PostTransferNoAccountsExist() throws HttpClientException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        String jsonString = "{'from':'4000123412341234','to':'4000123412341235','money':'5'}";

        PostMethod post = testServerTransfers.post("/transfers", jsonString, false);
        HttpResponse httpResponse = testServerTransfers.execute(post);

        assertEquals(404, httpResponse.code());
        assertEquals("No accounts exist in the database", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test when balance is not enough*/
    public void PostTransferBalanceNotEnough() throws HttpClientException {
        String jsonString = "{'from':'4000123412341234','to':'4000123412341235','money':'10000'}";

        PostMethod post = testServerTransfers.post("/transfers", jsonString, false);
        HttpResponse httpResponse = testServerTransfers.execute(post);

        assertEquals(404, httpResponse.code());
        assertEquals("Account with id 4000123412341234 can't transfer 10000.0 RUB" +
                ", the balance is only 23.56 RUB", new String(httpResponse.body()));

    }
}
