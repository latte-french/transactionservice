package service.impl;

import dataStore.AccountStore;
import dataStore.UserStore;
import model.Account;
import model.User;
import model.exceptions.NoSuchUserException;
import model.exceptions.NoUserAccountsException;
import model.exceptions.NoUsersExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public User getUser(BigInteger id) throws SQLException, NoSuchUserException {
        User user = UserStore.getUserFromDB(id);
        if (user.getId() == null)  {
            LOGGER.error("User with id " + id + " doesn't exist");
            throw new NoSuchUserException(id);
        }
        return user;
    }

    public List<Account> getAccountsOfUser (BigInteger id) throws NoUserAccountsException, SQLException{

        List<Account> userAccounts = new ArrayList<>();

        if (getUser(id) != null) {
            userAccounts = AccountStore.getAccountsOfUserFromDB(id);
                if (userAccounts.size() == 0) {
                    LOGGER.error("No accounts belong to the user with id = " + id);
                    throw new NoUserAccountsException(id);
                }
        }
        return userAccounts;
    }

    public List<User> getUsers() throws NoUsersExistException, SQLException{
        List<User> users = UserStore.getUsersFromDB();
        if (users.size() == 0)  {
            LOGGER.error("No users exist in the database");
            throw new NoUsersExistException();
        }
        return users;
    }

    public void createUser(User user) throws SQLException {
        UserStore.putUserToDB(user);
    }

    public void removeUser(BigInteger id) throws NoSuchUserException, SQLException{
        if (getUser(id) != null) {
            UserStore.removeUserFromDB(id);
        }
    }

    public void updateUser(User user, User userChanges) throws NoSuchUserException, SQLException{
        if (getUser(user.getId()) != null) {
            UserStore.updateUserInDB(user, userChanges);
        }
    }
}
