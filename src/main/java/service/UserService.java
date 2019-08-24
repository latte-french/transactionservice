package service;

import model.Account;
import model.User;
import model.exceptions.NoSuchUserException;
import model.exceptions.NoUserAccountsException;
import model.exceptions.NoUsersExistException;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    User getUser(BigInteger id) throws NoSuchUserException, SQLException;

    List<User> getUsers() throws NoUsersExistException, SQLException;

    List<Account> getAccountsOfUser(BigInteger userId) throws NoUserAccountsException, SQLException;

    void createUser(User user) throws SQLException;

    void removeUser(BigInteger id) throws NoSuchUserException, SQLException;

    void updateUser(User user, User userChanges) throws NoSuchUserException, SQLException;
}
