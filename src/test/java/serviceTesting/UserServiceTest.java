package serviceTesting;

import dataStore.DatabaseCreation;
import model.User;
import model.exceptions.NoSuchUserException;
import model.exceptions.NoUserAccountsException;
import model.exceptions.NoUsersExistException;
import org.junit.Before;
import org.junit.Test;
import service.UserService;
import service.impl.UserServiceImpl;
import utils.DatabaseCleanup;
import utils.ModelsInitialization;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class UserServiceTest {

    public static UserService userService = new UserServiceImpl();

    @Before
    public void resetDatabase() {
        DatabaseCleanup.prepareDatabase();
        ModelsInitialization.init();
    }

    @Test
    /*positive test*/
    public void testGetUserImpl() throws SQLException, NoSuchUserException {
        assertEquals(ModelsInitialization.userForTest, userService.getUser(new BigInteger("1")));
    }

    @Test
    /*positive test*/
    public void testGetUsersImpl() throws SQLException {
        assertEquals(ModelsInitialization.usersForTest, userService.getUsers());
    }

    @Test
    /*positive test*/
    public void testGetUserAccountsImpl() throws SQLException{
        assertEquals(ModelsInitialization.userAccountsForTest, 
                userService.getAccountsOfUser(new BigInteger("2")));
    }

    @Test
    /*positive test*/
    public void testPostUserImpl() throws SQLException {
        User user = new User(new BigInteger("4"),"Maria","Teresa");

        userService.createUser(user);

        assertEquals(user, userService.getUser(new BigInteger("4")));
    }

    @Test
    /*positive test*/
    public void testPutUserChangeFirstNameImpl() throws SQLException {
        User user = ModelsInitialization.userForTest;
        User userChanges = new User();
        userChanges.setFirstName("Angela");

        userService.updateUser(user, userChanges);

        user.setFirstName("Angela");
        assertEquals(user, userService.getUser(new BigInteger("1")));
    }

    @Test
    /*positive test*/
    public void testPutUserChangeLastNameImpl() throws SQLException {
        User user = ModelsInitialization.userForTest;
        User userChanges = new User();
        userChanges.setLastName("Wolf");

        userService.updateUser(user, userChanges);

        user.setLastName("Wolf");
        assertEquals(user, userService.getUser(new BigInteger("1")));
    }

    @Test
    /*positive test*/
    public void testPutUserChangeFirstAndLastNamesImpl() throws SQLException {
        User user = ModelsInitialization.userForTest;
        User userChanges = new User();
        userChanges.setFirstName("Angela");
        userChanges.setLastName("Wolf");

        userService.updateUser(user, userChanges);

        user.setFirstName("Angela");
        user.setLastName("Wolf");
        assertEquals(user, userService.getUser(new BigInteger("1")));
    }

    @Test
    /*positive test*/
    public void testDeleteUserImpl() throws SQLException {
        ArrayList<User> users = ModelsInitialization.usersForTest;
        users.remove(0);

        userService.removeUser(new BigInteger("1"));

        assertEquals(users, userService.getUsers());
    }


    @Test(expected = NoSuchUserException.class)
    /*negative test on non-existing user*/
    public void testGetNonExistingUserImpl() throws SQLException {
        User user = userService.getUser(new BigInteger("4"));
    }


    @Test(expected = NoUsersExistException.class)
    /*negative test on non-existing user*/
    public void testGetUserEmptyDatabaseImpl() throws SQLException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        User user = userService.getUser(new BigInteger("1"));
    }


    @Test(expected = NoUsersExistException.class)
    /*negative test on empty database*/
    public void testGetUsersEmptyDatabaseImpl() throws SQLException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        userService.getUsers();
    }

    @Test(expected = NoSuchUserException.class)
    /*negative test on non-existing user*/
    public void testGetAccountsNonExistingUserImpl() throws SQLException {
        userService.getAccountsOfUser(new BigInteger("4"));
    }

    @Test(expected = NoUsersExistException.class)
    /*negative test on empty database*/
    public void testGetUserAccountsEmptyDatabaseImpl() throws SQLException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        userService.getAccountsOfUser(new BigInteger("1"));
    }

    @Test(expected = NoUserAccountsException.class)
    /*negative test when no accounts exist for this user*/
    public void testGetNoAccountsBelongToUserImpl() throws SQLException {

        userService.getAccountsOfUser(new BigInteger("3"));
    }

    @Test(expected = NoSuchUserException.class)
    /*negative test on non-existing user*/
    public void testPutNonExistingUserImpl() throws SQLException {

        User user = ModelsInitialization.userForTest;
        user.setId(new BigInteger("4"));
        User userChanges = new User();
        userChanges.setFirstName("Angela");
        userChanges.setLastName("Wolf");

        userService.updateUser(user, userChanges);
    }

    @Test(expected = NoUsersExistException.class)
    /*negative test on empty database*/
    public void testPutUserEmptyDatabaseImpl() throws SQLException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        User user = ModelsInitialization.userForTest;
        User userChanges = new User();
        userChanges.setFirstName("Angela");
        userChanges.setLastName("Wolf");

        userService.updateUser(user, userChanges);
    }

    @Test(expected = NoSuchUserException.class)
    /*negative test on non-existing user*/
    public void testDeleteNonExistingUserImpl() throws SQLException {
        userService.removeUser(new BigInteger("4"));
    }

    @Test(expected = NoUsersExistException.class)
    /*negative test on empty database*/
    public void testDeleteUserEmptyImpl() throws SQLException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        userService.removeUser(new BigInteger("1"));
    }
}
