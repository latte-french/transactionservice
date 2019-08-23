package service;

import model.User;
import model.exceptions.NoSuchUserException;
import model.exceptions.NoUsersExistException;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;

public interface UserService {
    User getUser(BigInteger id) throws NoSuchUserException, SQLException;

    List<User> getUsers() throws NoUsersExistException, SQLException;

    void createUser(User user) throws SQLException;

    void removeUser(BigInteger id) throws NoSuchUserException, SQLException;

    void updateUser(User user) throws NoSuchUserException, SQLException;
}
