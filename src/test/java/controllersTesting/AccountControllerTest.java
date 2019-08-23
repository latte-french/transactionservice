package controllersTesting;

import com.despegar.http.client.*;
import com.despegar.sparkjava.test.SparkServer;
import controllers.AccountController;
import controllers.ExceptionController;
import model.Account;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import service.impl.AccountServiceImpl;
import spark.servlet.SparkApplication;
import utils.DatabaseCleanup;
import utils.ModelsInitialization;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class AccountControllerTest {
    public static class AccountControllerTestSparkApplication implements SparkApplication {

        @Override
        public void init() {
            new AccountController(new AccountServiceImpl()).init();
            new ExceptionController().init();
        }
    }

    @ClassRule
    public static SparkServer<AccountControllerTestSparkApplication> testServer =
            new SparkServer<>(AccountControllerTestSparkApplication.class, 9999);


    @Before
    public void resetDatabase() {
        DatabaseCleanup.prepareDatabase();
        ModelsInitialization.init();
    }

    @Test
    /*positive test*/
    public void GetAccount() throws HttpClientException {
        Account account = ModelsInitialization.accountForTest;

        GetMethod get = testServer.get("/accounts/4000123412341234", false);
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(200, httpResponse.code());
        assertEquals(account.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void GetAccounts() throws HttpClientException {
        ArrayList<Account> accounts = ModelsInitialization.accountsForTest;

        GetMethod get = testServer.get("/accounts", false);
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(200, httpResponse.code());
        assertEquals(accounts.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void PostAccount() throws HttpClientException {
        String jsonString = "{'balance':'5','currency':'RUB','userId':'2'}";
        Account account = new Account(new BigInteger("4000123412341237"),5.0,"RUB");

        PostMethod post = testServer.post("/accounts", jsonString, false);
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(200, httpResponse.code());
        assertEquals(account.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void PutAccountChangeBalance() throws HttpClientException {
        Account account = ModelsInitialization.accountForTest;
        String jsonString = "{'balance':'5'}";

        PutMethod put = testServer.put("/accounts/4000123412341234", jsonString, false);
        HttpResponse httpResponse = testServer.execute(put);

        account.setBalance(5.0);
        assertEquals(200, httpResponse.code());
        assertEquals(account.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void PutAccountChangeCurrency() throws HttpClientException {
        Account account = ModelsInitialization.accountForTest;
        String jsonString = "{'currency':'EUR'}";

        PutMethod put = testServer.put("/accounts/4000123412341234", jsonString, false);
        HttpResponse httpResponse = testServer.execute(put);

        account.setCurrency("EUR");
        assertEquals(200, httpResponse.code());
        assertEquals(account.toString(), new String(httpResponse.body()));
    }

    @Test
    /*positive test*/
    public void DeleteAccount() throws HttpClientException {
        ArrayList<Account> accounts = ModelsInitialization.accountsForTest;
        accounts.remove(0);

        DeleteMethod delete = testServer.delete("/accounts/4000123412341234", false);
        HttpResponse httpResponseDelete = testServer.execute(delete);

        assertEquals(200, httpResponseDelete.code());

        GetMethod get = testServer.get("/accounts", false);
        HttpResponse httpResponseGet = testServer.execute(get);

        assertEquals(accounts.toString(), new String(httpResponseGet.body()));
    }
}