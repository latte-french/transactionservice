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
    public void testGetUserApi() throws HttpClientException {
        GetMethod get = testServerUsers.get("/users/1", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void testGetUsersApi() throws HttpClientException {
        GetMethod get = testServerUsers.get("/users", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(200, httpResponse.code());
    }

    
    @Test
    /*positive test*/
    public void testGetUserAccountsApi() throws HttpClientException {
        GetMethod get = testServerUsers.get("/users/2/accounts", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void testPostUserApi() throws HttpClientException {
        String jsonString = "{'firstName':'Maria','lastName':'Teresa'}";

        PostMethod post = testServerUsers.post("/users", jsonString, false);
        HttpResponse httpResponse = testServerUsers.execute(post);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void testPutUserChangeFirstNameApi() throws HttpClientException {
        String jsonString = "{'firstName':'Angela'}";

        PutMethod put = testServerUsers.put("/users/1", jsonString, false);
        HttpResponse httpResponse = testServerUsers.execute(put);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void testPutUserChangeLastNameApi() throws HttpClientException {
        String jsonString = "{'lastName':'Wolf'}";

        PutMethod put = testServerUsers.put("/users/1", jsonString, false);
        HttpResponse httpResponse = testServerUsers.execute(put);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void testPutUserChangeFirstAndLastNamesApi() throws HttpClientException {
        String jsonString = "{'firstName':'Angela','lastName':'Wolf'}";

        PutMethod put = testServerUsers.put("/users/1", jsonString, false);
        HttpResponse httpResponse = testServerUsers.execute(put);

        assertEquals(200, httpResponse.code());
    }

    @Test
    /*positive test*/
    public void testDeleteUserApi() throws HttpClientException {
        DeleteMethod delete = testServerUsers.delete("/users/1", false);
        HttpResponse httpResponse = testServerUsers.execute(delete);

        assertEquals(200, httpResponse.code());
    }

    
    @Test
    /*negative test on non-existing user*/
    public void testGetNonExistingUserApi() throws HttpClientException {

        GetMethod get = testServerUsers.get("/users/7", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(404, httpResponse.code());
        assertEquals("User with id 7 doesn't exist", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test on non-existing user*/
    public void testGetUserEmptyDatabaseApi() throws HttpClientException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        GetMethod get = testServerUsers.get("/users/2", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(404, httpResponse.code());
        assertEquals("No users exist in the database", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test on empty database*/
    public void testGetUsersEmptyDatabaseApi() throws HttpClientException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        GetMethod get = testServerUsers.get("/users", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(404, httpResponse.code());
        assertEquals("No users exist in the database", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test on non-existing user*/
    public void testGetAccountsNonExistingUserApi() throws HttpClientException {

        GetMethod get = testServerUsers.get("/users/7/accounts", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(404, httpResponse.code());
        assertEquals("User with id 7 doesn't exist", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test on empty database*/
    public void testGetUserAccountsEmptyDatabaseApi() throws HttpClientException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        GetMethod get = testServerUsers.get("/users/1/accounts", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(404, httpResponse.code());
        assertEquals("No users exist in the database", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test when no accounts exist for this user*/
    public void testGetNoAccountsBelongToUserApi() throws HttpClientException {

        GetMethod get = testServerUsers.get("/users/3/accounts", false);
        HttpResponse httpResponse = testServerUsers.execute(get);

        assertEquals(404, httpResponse.code());
        assertEquals("No accounts belong to the user with id = 3", new String(httpResponse.body()));
    }

    @Test
    /*negative test on non-existing user*/
    public void testPutNonExistingUserApi() throws HttpClientException {
        String jsonString = "{'firstName':'Angela','lastName':'Wolf'}";

        PutMethod put = testServerUsers.put("/users/4", jsonString, false);
        HttpResponse httpResponse = testServerUsers.execute(put);

        assertEquals(404, httpResponse.code());
        assertEquals("User with id 4 doesn't exist", new String(httpResponse.body()));
    }

    @Test
    /*negative test on empty database*/
    public void testPutUserEmptyDatabaseApi() throws HttpClientException {
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
    public void testDeleteNonExistingUserApi() throws HttpClientException {
        ArrayList<User> users = ModelsInitialization.usersForTest;
        users.remove(0);

        DeleteMethod delete = testServerUsers.delete("/users/4", false);
        HttpResponse httpResponse = testServerUsers.execute(delete);

        assertEquals(404, httpResponse.code());
        assertEquals("User with id 4 doesn't exist", new String(httpResponse.body()));
    }

    
    @Test
    /*negative test on empty database*/
    public void testDeleteUserEmptyApi() throws HttpClientException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        DeleteMethod delete = testServerUsers.delete("/users/1", false);
        HttpResponse httpResponse = testServerUsers.execute(delete);

        assertEquals(404, httpResponse.code());
        assertEquals("No users exist in the database", new String(httpResponse.body()));
    }

}

