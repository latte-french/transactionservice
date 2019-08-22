package service.impl;

import dataStore.UserStore;
import model.User;
import model.exceptions.NoSuchUserException;
import model.exceptions.NoUsersExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.math.BigInteger;
import java.util.List;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static UserServiceImpl instance;
    final UserStore userStore = UserStore.getInstance();

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }
    public User getUser(BigInteger id) {
        User user = userStore.getUserFromDB(id);
        if (user.getId() == null)  {
            LOGGER.error("User with id " + id + " doesn't exist");
            throw new NoSuchUserException(id);
        }
        return user;
    }

    public void createUser(User user) {
        userStore.putUserToDB(user);
    }

    public List<User> getUsers() throws NoUsersExistException{
        List<User> users = userStore.getUsersFromDB();
        if (users.size() == 0)  {
            LOGGER.error("No users exist in the database");
            throw new NoUsersExistException();
        }
        return users;
    }

    public void removeUser(BigInteger id) throws NoSuchUserException{
        if (getUser(id) != null) {
            userStore.removeUserFromDB(id);
            userStore.removeUsersAccountsFromDB(id);
            userStore.removeUserAccountDependencyFromDB(id);
        }
    }

    public void updateUser(User user) throws NoSuchUserException{
        if (getUser(user.getId()) != null) {
            userStore.updateUserInDB(user);
        }
    }
}
