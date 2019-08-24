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
    public void GetUser() throws SQLException, NoSuchUserException {
        assertEquals(ModelsInitialization.userForTest, userService.getUser(new BigInteger("1")));
    }

    @Test
    /*positive test*/
    public void GetUsers() throws SQLException {
        assertEquals(ModelsInitialization.usersForTest, userService.getUsers());
    }

    @Test
    /*positive test*/
    public void GetUserAccounts() throws SQLException{
        assertEquals(ModelsInitialization.userAccountsForTest, 
                userService.getAccountsOfUser(new BigInteger("2")));
    }

    @Test
    /*positive test*/
    public void PostUser() throws SQLException {
        User user = new User(new BigInteger("4"),"Maria","Teresa");

        userService.createUser(user);

        assertEquals(user, userService.getUser(new BigInteger("4")));
    }

    @Test
    /*positive test*/
    public void PutUserChangeFirstName() throws SQLException {
        User user = ModelsInitialization.userForTest;
        User userChanges = new User();
        userChanges.setFirstName("Angela");

        userService.updateUser(user, userChanges);

        user.setFirstName("Angela");
        assertEquals(user, userService.getUser(new BigInteger("1")));
    }

    @Test
    /*positive test*/
    public void PutUserChangeLastName() throws SQLException {
        User user = ModelsInitialization.userForTest;
        User userChanges = new User();
        userChanges.setLastName("Wolf");

        userService.updateUser(user, userChanges);

        user.setLastName("Wolf");
        assertEquals(user, userService.getUser(new BigInteger("1")));
    }

    @Test
    /*positive test*/
    public void PutUserChangeFirstAndLastNames() throws SQLException {
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
    public void DeleteUser() throws SQLException {
        ArrayList<User> users = ModelsInitialization.usersForTest;
        users.remove(0);

        userService.removeUser(new BigInteger("1"));

        assertEquals(users, userService.getUsers());
    }


    @Test(expected = NoSuchUserException.class)
    /*negative test on non-existing user*/
    public void GetNonExistingUser() throws SQLException {
        User user = userService.getUser(new BigInteger("4"));
    }


     @Test(expected = NoUsersExistException.class)
    /*negative test on non-existing user*/
    public void GetUserEmptyDatabase() throws SQLException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        User user = userService.getUser(new BigInteger("1"));
    }


    @Test(expected = NoUsersExistException.class)
    /*negative test on empty database*/
    public void GetUsersEmptyDatabase() throws SQLException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        userService.getUsers();
    }

    @Test(expected = NoSuchUserException.class)
    /*negative test on non-existing user*/
    public void GetAccountsNonExistingUser() throws SQLException {
        userService.getAccountsOfUser(new BigInteger("4"));
    }

    @Test(expected = NoUsersExistException.class)
    /*negative test on empty database*/
    public void GetUserAccountsEmptyDatabase() throws SQLException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        userService.getAccountsOfUser(new BigInteger("1"));
    }

    @Test(expected = NoUserAccountsException.class)
    /*negative test when no accounts exist for this user*/
    public void GetNoAccountsBelongToUser() throws SQLException {

        userService.getAccountsOfUser(new BigInteger("3"));
    }

    @Test(expected = NoSuchUserException.class)
    /*negative test on non-existing user*/
    public void PutNonExistingUser() throws SQLException {

        User user = ModelsInitialization.userForTest;
        user.setId(new BigInteger("4"));
        User userChanges = new User();
        userChanges.setFirstName("Angela");
        userChanges.setLastName("Wolf");

        userService.updateUser(user, userChanges);
    }

    @Test(expected = NoUsersExistException.class)
    /*negative test on empty database*/
    public void PutUserEmptyDatabase() throws SQLException {
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
    public void DeleteNonExistingUser() throws SQLException {
        userService.removeUser(new BigInteger("4"));
    }

    @Test(expected = NoUsersExistException.class)
    /*negative test on empty database*/
    public void DeleteUserEmpty() throws SQLException {
        DatabaseCleanup.cleanDatabase();
        DatabaseCreation.initDatabase();

        userService.removeUser(new BigInteger("1"));
    }
}
