package service.impl;

import dataStore.UserStore;
import model.User;
import model.exceptions.NoSuchUserException;
import model.exceptions.NoUsersExistException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import javax.inject.Singleton;
import java.math.BigInteger;
import java.util.List;

@Singleton
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static UserServiceImpl instance;

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserServiceImpl();
        }
        return instance;
    }
    public User getUser(BigInteger id) {
        User user = UserStore.getUserFromDB(id);
        if (user.getId() == null)  {
            LOGGER.error("User with id " + id + " doesn't exist");
            throw new NoSuchUserException(id);
        }
        return user;
    }

    public void createUser(User user) {
        UserStore.putUserToDB(user);
    }

    public List<User> getUsers() throws NoUsersExistException{
        List<User> users = UserStore.getUsersFromDB();
        if (users.size() == 0)  {
            LOGGER.error("No users exist in the database");
            throw new NoUsersExistException();
        }
        return users;
    }

    public void removeUser(BigInteger id) throws NoSuchUserException{
        if (getUser(id) != null) {
            UserStore.removeUserFromDB(id);
            UserStore.removeUsersAccountsFromDB(id);
            UserStore.removeUserAccountDependencyFromDB(id);
        }
    }

    public void updateUser(User user) throws NoSuchUserException{
        if (getUser(user.getId()) != null) {
            UserStore.updateUserInDB(user);
        }
    }
}
