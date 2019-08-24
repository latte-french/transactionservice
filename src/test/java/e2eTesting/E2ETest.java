package e2eTesting;

import com.despegar.http.client.*;
import com.despegar.sparkjava.test.SparkServer;
import controllers.AccountController;
import controllers.ExceptionController;
import controllers.TransferController;
import controllers.UserController;
import model.Account;
import model.Transfer;
import model.User;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import service.impl.AccountServiceImpl;
import service.impl.TransferServiceImpl;
import service.impl.UserServiceImpl;
import spark.servlet.SparkApplication;
import utils.DatabaseCleanup;
import utils.ModelsInitialization;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class E2ETest {
    public static class E2ETestSparkApplication implements SparkApplication {

        @Override
        public void init() {
            new AccountController(new AccountServiceImpl()).init();
            new TransferController(new TransferServiceImpl(new AccountServiceImpl())).init();
            new UserController(new UserServiceImpl()).init();
            new ExceptionController().init();
        }
    }

    @ClassRule
    public static SparkServer<E2ETest.E2ETestSparkApplication> testServerE2E =
            new SparkServer<>(E2ETest.E2ETestSparkApplication.class, 9996);


    @Before
    public void resetDatabase() {
        DatabaseCleanup.prepareDatabase();
        ModelsInitialization.init();
    }

    @Test
    /*positive test*/
    public void GetAccount() throws HttpClientException {
        Account account = ModelsInitialization.accountForTest;

        GetMethod get = testServerE2E.get("/accounts/4000123412341234", false);
        HttpResponse httpResponse = testServerE2E.execute(get);

        assertEquals(200, httpResponse.code());
        assertEquals(account.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void GetAccounts() throws HttpClientException {
        ArrayList<Account> accounts = ModelsInitialization.accountsForTest;

        GetMethod get = testServerE2E.get("/accounts", false);
        HttpResponse httpResponse = testServerE2E.execute(get);

        assertEquals(200, httpResponse.code());
        assertEquals(accounts.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void PostAccount() throws HttpClientException {
        String jsonString = "{'balance':'5','currency':'RUB','userId':'2'}";
        Account account = new Account(new BigInteger("4000123412341237"),5.0,"RUB");

        PostMethod post = testServerE2E.post("/accounts", jsonString, false);
        HttpResponse httpResponse = testServerE2E.execute(post);

        assertEquals(200, httpResponse.code());
        assertEquals(account.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void PutAccountChangeBalanceAndCurrency() throws HttpClientException {
        Account account = ModelsInitialization.accountForTest;
        String jsonString = "{'balance':'5','currency':'EUR'}";

        PutMethod put = testServerE2E.put("/accounts/4000123412341234", jsonString, false);
        HttpResponse httpResponse = testServerE2E.execute(put);

        account.setBalance(5.0);
        account.setCurrency("EUR");

        assertEquals(200, httpResponse.code());
        assertEquals(account.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void DeleteAccount() throws HttpClientException {
        ArrayList<Account> accounts = ModelsInitialization.accountsForTest;
        accounts.remove(0);

        DeleteMethod delete = testServerE2E.delete("/accounts/4000123412341234", false);
        HttpResponse httpResponse = testServerE2E.execute(delete);

        assertEquals(200, httpResponse.code());

        GetMethod get = testServerE2E.get("/accounts", false);
        httpResponse = testServerE2E.execute(get);

        assertEquals(accounts.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void GetUser() throws HttpClientException {
        User user = ModelsInitialization.userForTest;

        GetMethod get = testServerE2E.get("/users/1", false);
        HttpResponse httpResponse = testServerE2E.execute(get);

        assertEquals(200, httpResponse.code());
        assertEquals(user.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void GetUsers() throws HttpClientException {
        ArrayList<User> users = ModelsInitialization.usersForTest;

        GetMethod get = testServerE2E.get("/users", false);
        HttpResponse httpResponse = testServerE2E.execute(get);

        assertEquals(200, httpResponse.code());
        assertEquals(users.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void GetUserAccounts() throws HttpClientException {
        ArrayList<Account> userAccounts = ModelsInitialization.userAccountsForTest;

        GetMethod get = testServerE2E.get("/users/2/accounts", false);
        HttpResponse httpResponse = testServerE2E.execute(get);

        assertEquals(200, httpResponse.code());
        assertEquals(userAccounts.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void PostUser() throws HttpClientException {
        String jsonString = "{'firstName':'Maria','lastName':'Teresa'}";
        User user = new User(new BigInteger("4"),"Maria","Teresa");

        PostMethod post = testServerE2E.post("/users", jsonString, false);
        HttpResponse httpResponse = testServerE2E.execute(post);

        assertEquals(200, httpResponse.code());
        assertEquals(user.toString(), new String(httpResponse.body()));
    }
    
    @Test
    /*positive test*/
    public void PutUserChangeFirstAndLastNames() throws HttpClientException {
        User user = ModelsInitialization.userForTest;
        String jsonString = "{'firstName':'Angela','lastName':'Wolf'}";

        PutMethod put = testServerE2E.put("/users/1", jsonString, false);
        HttpResponse httpResponse = testServerE2E.execute(put);

        user.setFirstName("Angela");
        user.setLastName("Wolf");
        assertEquals(200, httpResponse.code());
        assertEquals(user.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void DeleteUser() throws HttpClientException {
        ArrayList<User> users = ModelsInitialization.usersForTest;
        users.remove(0);

        DeleteMethod delete = testServerE2E.delete("/users/1", false);
        HttpResponse httpResponse = testServerE2E.execute(delete);

        assertEquals(200, httpResponse.code());

        GetMethod get = testServerE2E.get("/users", false);
        httpResponse = testServerE2E.execute(get);

        assertEquals(users.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void PostTransfer() throws HttpClientException {
        String jsonString = "{'from':'4000123412341234','to':'4000123412341235','money':'5'}";

        PostMethod post = testServerE2E.post("/transfers", jsonString, false);
        HttpResponse httpResponse = testServerE2E.execute(post);

        String expectedTransfer = new Transfer(new BigInteger("4000123412341234"),
                new BigInteger("4000123412341235"), 5.0, 0.0755,
                new Timestamp(new Date().getTime())).toString();
        String transferInResponse = new String(httpResponse.body());

        String transferInResponseWithoutTime = transferInResponse.substring(0, transferInResponse.indexOf("transferredAt"));
        String expectedTransferWithoutTime = expectedTransfer.substring(0, expectedTransfer.indexOf("transferredAt"));

        assertEquals(200, httpResponse.code());
        assertEquals(expectedTransferWithoutTime, transferInResponseWithoutTime);

        GetMethod getAccountFrom = testServerE2E.get("/accounts/4000123412341234", false);
        httpResponse = testServerE2E.execute(getAccountFrom);

        assertEquals(200, httpResponse.code());
        assertEquals(ModelsInitialization.accountTransferredFrom.toString(), new String(httpResponse.body()));

        GetMethod getAccountTo = testServerE2E.get("/accounts/4000123412341235", false);
        httpResponse = testServerE2E.execute(getAccountTo);

        assertEquals(200, httpResponse.code());
        assertEquals(ModelsInitialization.accountTransferredTo.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void GetTransfers() throws HttpClientException {
        ArrayList<Transfer> transfers = ModelsInitialization.transfersForTest;

        GetMethod get = testServerE2E.get("/transfers", false);
        HttpResponse httpResponse = testServerE2E.execute(get);

        assertEquals(200, httpResponse.code());
        assertEquals(transfers.toString(), new String(httpResponse.body()));
    }

}
