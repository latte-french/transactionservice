package controllersTesting;

import com.despegar.http.client.*;
import com.despegar.sparkjava.test.SparkServer;
import controllers.ExceptionController;
import controllers.UserController;
import dataStore.DatabaseCreation;
import model.User;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import service.impl.UserServiceImpl;
import spark.servlet.SparkApplication;
import utils.DatabaseCleanup;
import utils.ModelsInitialization;

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
    public static SparkServer<UserControllerTestSparkApplication> testServerUsers =
            new SparkServer<>(UserControllerTestSparkApplication.class, 9999);


    @Before
    public void resetDatabase() {
        DatabaseCleanup.prepareDatabase();
        ModelsInitialization.init();
    }

    @Test
    /*positive test*/
    public void GetUser() throws HttpClientException {
        GetMethod get = testServerUsers.get("/users/1", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void GetUsers() throws HttpClientException {
        GetMethod get = testServerUsers.get("/users", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(200, httpResponse.code());
    }

    
    @Test
    /*positive test*/
    public void GetUserAccounts() throws HttpClientException {
        GetMethod get = testServerUsers.get("/users/2/accounts", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void PostUser() throws HttpClientException {
        String jsonString = "{'firstName':'Maria','lastName':'Teresa'}";

        PostMethod post = testServerUsers.post("/users", jsonString, false);
        HttpResponse httpResponse = testServerUsers.execute(post);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void PutUserChangeFirstName() throws HttpClientException {
        String jsonString = "{'firstName':'Angela'}";

        PutMethod put = testServerUsers.put("/users/1", jsonString, false);
        HttpResponse httpResponse = testServerUsers.execute(put);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void PutUserChangeLastName() throws HttpClientException {
        String jsonString = "{'lastName':'Wolf'}";

        PutMethod put = testServerUsers.put("/users/1", jsonString, false);
        HttpResponse httpResponse = testServerUsers.execute(put);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void PutUserChangeFirstAndLastNames() throws HttpClientException {
        String jsonString = "{'firstName':'Angela','lastName':'Wolf'}";

        PutMethod put = testServerUsers.put("/users/1", jsonString, false);
        HttpResponse httpResponse = testServerUsers.execute(put);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void DeleteUser() throws HttpClientException {
        DeleteMethod delete = testServerUsers.delete("/users/1", false);
        HttpResponse httpResponse = testServerUsers.execute(delete);

        assertEquals(200, httpResponse.code());
    }

    
    @Test
    /*negative test on non-existing user*/
    public void GetNonExistingUser() throws HttpClientException {

        GetMethod get = testServerUsers.get("/users/7", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(404, httpResponse.code());
        assertEquals("User with id 7 doesn't exist", new String(httpResponse.body()));
    }

    
     @Test
    /*negative test on non-existing user*/
    public void GetUserEmptyDatabase() throws HttpClientException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        GetMethod get = testServerUsers.get("/users/2", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(404, httpResponse.code());
        assertEquals("No users exist in the database", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test on empty database*/
    public void GetUsersEmptyDatabase() throws HttpClientException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        GetMethod get = testServerUsers.get("/users", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(404, httpResponse.code());
        assertEquals("No users exist in the database", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test on non-existing user*/
    public void GetAccountsNonExistingUser() throws HttpClientException {

        GetMethod get = testServerUsers.get("/users/7/accounts", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(404, httpResponse.code());
        assertEquals("User with id 7 doesn't exist", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test on empty database*/
    public void GetUserAccountsEmptyDatabase() throws HttpClientException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        GetMethod get = testServerUsers.get("/users/1/accounts", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(404, httpResponse.code());
        assertEquals("No users exist in the database", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test when no accounts exist for this user*/
    public void GetNoAccountsBelongToUser() throws HttpClientException {

        GetMethod get = testServerUsers.get("/users/3/accounts", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(404, httpResponse.code());
        assertEquals("No accounts belong to the user with id = 3", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test on non-existing user*/
    public void PutNonExistingUser() throws HttpClientException {
        String jsonString = "{'firstName':'Angela','lastName':'Wolf'}";

        PutMethod put = testServerUsers.put("/users/4", jsonString, false);
        HttpResponse httpResponse = testServerUsers.execute(put);

        assertEquals(404, httpResponse.code());
        assertEquals("User with id 4 doesn't exist", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test on empty database*/
    public void PutUserEmptyDatabase() throws HttpClientException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        String jsonString = "{'firstName':'Angela','lastName':'Wolf'}";

        PutMethod put = testServerUsers.put("/users/4", jsonString, false);
        HttpResponse httpResponse = testServerUsers.execute(put);

        assertEquals(404, httpResponse.code());
        assertEquals("No users exist in the database", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test on non-existing user*/
    public void DeleteNonExistingUser() throws HttpClientException {
        ArrayList<User> users = ModelsInitialization.usersForTest;
        users.remove(0);

        DeleteMethod delete = testServerUsers.delete("/users/4", false);
        HttpResponse httpResponse = testServerUsers.execute(delete);

        assertEquals(404, httpResponse.code());
        assertEquals("User with id 4 doesn't exist", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test on empty database*/
    public void DeleteUserEmpty() throws HttpClientException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        DeleteMethod delete = testServerUsers.delete("/users/1", false);
        HttpResponse httpResponse = testServerUsers.execute(delete);

        assertEquals(404, httpResponse.code());
        assertEquals("No users exist in the database", new String(httpResponse.body()));
    }

}

