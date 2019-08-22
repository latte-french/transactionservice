package service;

import model.User;
import model.exceptions.NoSuchUserException;
import model.exceptions.NoUsersExistException;

import java.math.BigInteger;
import java.util.List;

public interface UserService {
    User getUser(BigInteger id) throws NoSuchUserException;

    List<User> getUsers() throws NoUsersExistException;

    void createUser(User user);

    void removeUser(BigInteger id) throws NoSuchUserException;

    void updateUser(User user) throws NoSuchUserException;
}
