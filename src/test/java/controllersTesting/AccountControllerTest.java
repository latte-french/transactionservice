package controllersTesting;

import com.despegar.http.client.*;
import com.despegar.sparkjava.test.SparkServer;
import controllers.AccountController;
import controllers.ExceptionController;
import dataStore.DatabaseCreation;
import model.Account;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import service.impl.AccountServiceImpl;
import spark.servlet.SparkApplication;
import utils.DatabaseCleanup;
import utils.ModelsInitialization;

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
    public static SparkServer<AccountControllerTestSparkApplication> testServerAccounts =
            new SparkServer<>(AccountControllerTestSparkApplication.class, 9997);


    @Before
    public void resetDatabase() {
        DatabaseCleanup.prepareDatabase();
        ModelsInitialization.init();
    }

    @Test
    /*positive test*/
    public void GetAccount() throws HttpClientException {
        GetMethod get = testServerAccounts.get("/accounts/4000123412341234", false);
        HttpResponse httpResponse = testServerAccounts.execute(get);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void GetAccounts() throws HttpClientException {
        GetMethod get = testServerAccounts.get("/accounts", false);
        HttpResponse httpResponse = testServerAccounts.execute(get);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void PostAccount() throws HttpClientException {
        String jsonString = "{'balance':'5','currency':'RUB','userId':'2'}";

        PostMethod post = testServerAccounts.post("/accounts", jsonString, false);
        HttpResponse httpResponse = testServerAccounts.execute(post);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void PutAccountChangeBalance() throws HttpClientException {
        String jsonString = "{'balance':'5'}";

        PutMethod put = testServerAccounts.put("/accounts/4000123412341234", jsonString, false);
        HttpResponse httpResponse = testServerAccounts.execute(put);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void PutAccountChangeCurrency() throws HttpClientException {
        String jsonString = "{'currency':'EUR'}";

        PutMethod put = testServerAccounts.put("/accounts/4000123412341234", jsonString, false);
        HttpResponse httpResponse = testServerAccounts.execute(put);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void PutAccountChangeBalanceAndCurrency() throws HttpClientException {
        String jsonString = "{'balance':'5','currency':'EUR'}";

        PutMethod put = testServerAccounts.put("/accounts/4000123412341234", jsonString, false);
        HttpResponse httpResponse = testServerAccounts.execute(put);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void DeleteAccount() throws HttpClientException {
        DeleteMethod delete = testServerAccounts.delete("/accounts/4000123412341234", false);
        HttpResponse httpResponse = testServerAccounts.execute(delete);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*negative test on non-existing account*/
    public void GetNonExistingAccount() throws HttpClientException {
        GetMethod get = testServerAccounts.get("/accounts/1", false);
        HttpResponse httpResponse = testServerAccounts.execute(get);

        assertEquals(404, httpResponse.code());
        assertEquals("Account with id 1 doesn't exist", new String(httpResponse.body()));
    }

    @Test
    /*negative test on empty database*/
    public void GetAccountEmptyDatabase() throws HttpClientException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        GetMethod get = testServerAccounts.get("/accounts/4000123412341234", false);
        HttpResponse httpResponse = testServerAccounts.execute(get);

        assertEquals(404, httpResponse.code());
        assertEquals("No accounts exist in the database", new String(httpResponse.body()));
    }

    @Test
    /*negative test on empty accounts table*/
    public void GetAccountsEmpty() throws HttpClientException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        GetMethod get = testServerAccounts.get("/accounts", false);
        HttpResponse httpResponse = testServerAccounts.execute(get);

        assertEquals(404, httpResponse.code());
        assertEquals("No accounts exist in the database", new String(httpResponse.body()));
    }

    @Test
    /*negative test on non-existing account*/
    public void PutNonExistingAccount() throws HttpClientException {
        String jsonString = "{'currency':'EUR'}";

        PutMethod put = testServerAccounts.put("/accounts/1", jsonString, false);
        HttpResponse httpResponse = testServerAccounts.execute(put);

        assertEquals(404, httpResponse.code());
        assertEquals("Account with id 1 doesn't exist", new String(httpResponse.body()));
    }

    @Test
    /*negative test on empty database*/
    public void PutAccountEmptyDatabase() throws HttpClientException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();
        String jsonString = "{'currency':'EUR'}";

        PutMethod put = testServerAccounts.put("/accounts/4000123412341234", jsonString, false);
        HttpResponse httpResponse = testServerAccounts.execute(put);

        assertEquals(404, httpResponse.code());
        assertEquals("No accounts exist in the database", new String(httpResponse.body()));
    }

    @Test
    /*negative test on non-existing account*/
    public void DeleteNonExistingAccount() throws HttpClientException {
        ArrayList<Account> accounts = ModelsInitialization.accountsForTest;
        accounts.remove(0);

        DeleteMethod delete = testServerAccounts.delete("/accounts/1", false);
        HttpResponse httpResponse = testServerAccounts.execute(delete);

        assertEquals(404, httpResponse.code());
        assertEquals("Account with id 1 doesn't exist", new String(httpResponse.body()));
    }

    @Test
    /*negative test on empty database*/
    public void DeleteAccountEmpty() throws HttpClientException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        DeleteMethod delete = testServerAccounts.delete("/accounts/4000123412341234", false);
        HttpResponse httpResponse = testServerAccounts.execute(delete);

        assertEquals(404, httpResponse.code());
        assertEquals("No accounts exist in the database", new String(httpResponse.body()));
    }


}