package controllersTesting;

import com.despegar.http.client.*;
import com.despegar.sparkjava.test.SparkServer;
import controllers.ExceptionController;
import controllers.UserController;
import model.Account;
import model.User;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import service.impl.UserServiceImpl;
import spark.servlet.SparkApplication;
import utils.DatabaseCleanup;
import utils.ModelsInitialization;

import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UserControllerTest {
    public static class UserControllerTestSparkApplication implements SparkApplication {

        @Override
        public void init() {
            new UserController(new UserServiceImpl()).init();
            new ExceptionController().init();
        }
    }

    @ClassRule
    public static SparkServer<UserControllerTestSparkApplication> testServer =
            new SparkServer<>(UserControllerTestSparkApplication.class, 9999);


    @Before
    public void resetDatabase() {
        DatabaseCleanup.prepareDatabase();
        ModelsInitialization.init();
    }

    @Ignore
    @Test
    /*positive test*/
    public void GetUser() throws HttpClientException {
        User user = ModelsInitialization.userForTest;

        GetMethod get = testServer.get("/users/1", false);
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(200, httpResponse.code());
        assertEquals(user.toString(), new String(httpResponse.body()));
    }

    @Ignore
    @Test
    /*positive test*/
    public void GetUsers() throws HttpClientException {
        ArrayList<User> users = ModelsInitialization.usersForTest;

        GetMethod get = testServer.get("/users", false);
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(200, httpResponse.code());
        assertEquals(users.toString(), new String(httpResponse.body()));
    }

    @Ignore
    @Test
    /*positive test*/
    public void GetUserAccounts() throws HttpClientException {
        ArrayList<Account> userAccounts = ModelsInitialization.userAccountsForTest;

        GetMethod get = testServer.get("/users/2/accounts", false);
        HttpResponse httpResponse = testServer.execute(get);

        assertEquals(200, httpResponse.code());
        assertEquals(userAccounts.toString(), new String(httpResponse.body()));
    }

    @Ignore
    @Test
    /*positive test*/
    public void PostUser() throws HttpClientException {
        String jsonString = "{'firstName':'Maria','lastName':'Teresa'}";
        User user = new User(new BigInteger("4"),"Maria","Teresa");

        PostMethod post = testServer.post("/users", jsonString, false);
        HttpResponse httpResponse = testServer.execute(post);

        assertEquals(200, httpResponse.code());
        assertEquals(user.toString(), new String(httpResponse.body()));
    }

    @Ignore
    @Test
    /*positive test*/
    public void PutUserChangeFirstName() throws HttpClientException {
        User user = ModelsInitialization.userForTest;
        String jsonString = "{'firstName':'Angela'}";

        PutMethod put = testServer.put("/users/1", jsonString, false);
        HttpResponse httpResponse = testServer.execute(put);

        user.setFirstName("Angela");
        assertEquals(200, httpResponse.code());
        assertEquals(user.toString(), new String(httpResponse.body()));
    }

    @Ignore
    @Test
    /*positive test*/
    public void PutUserChangeLastName() throws HttpClientException {
        User user = ModelsInitialization.userForTest;
        String jsonString = "{'lastName':'Wolf'}";

        PutMethod put = testServer.put("/users/1", jsonString, false);
        HttpResponse httpResponse = testServer.execute(put);

        user.setLastName("Wolf");
        assertEquals(200, httpResponse.code());
        assertEquals(user.toString(), new String(httpResponse.body()));
    }

    @Ignore
    @Test
    /*positive test*/
    public void DeleteUser() throws HttpClientException {
        ArrayList<User> users = ModelsInitialization.usersForTest;
        users.remove(0);

        DeleteMethod delete = testServer.delete("/users/1", false);
        HttpResponse httpResponse = testServer.execute(delete);

        assertEquals(200, httpResponse.code());

        GetMethod get = testServer.get("/users", false);
        httpResponse = testServer.execute(get);

        assertEquals(users.toString(), new String(httpResponse.body()));
    }
}
