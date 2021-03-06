package dataStore;

import dataStore.statementsCreation.UserStatementCreation;
import dataStore.utils.EntityConverters;
import dataStore.utils.StatementExecution;
import model.StatementModel;
import model.User;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserStore {


    public static User getUserFromDB(BigInteger id) throws SQLException {
        User user = new User();
        StatementModel statementModel = UserStatementCreation.getUserStatement(id);

        ResultSet result = StatementExecution.prepareAndExecuteQuery(statementModel);
        if (result.next()) {user = EntityConverters.convertFromEntityToUser(result);}

        return user;
    }

    public static List<User> getUsersFromDB() throws SQLException{
        List<User> userCollection = new ArrayList<User>();

        StatementModel statementModel = UserStatementCreation.getUsersStatement();
        ResultSet result = StatementExecution.prepareAndExecuteQuery(statementModel);
        while (result.next()){
            userCollection.add(EntityConverters.convertFromEntityToUser(result));
        }
        return userCollection;
    }

    public static void putUserToDB(User user) throws SQLException{
        StatementModel statementModel = UserStatementCreation.putUserStatement(user);
        StatementExecution.prepareAndExecuteStatement(statementModel);

    }

    public static void removeUserFromDB (BigInteger id) throws SQLException{
        ArrayList<StatementModel> statementModels = new ArrayList<>();
        statementModels.add(UserStatementCreation.removeUserStatement(id));
        statementModels.add(UserStatementCreation.removeUsersAccountsStatement(id));
        statementModels.add(UserStatementCreation.removeUserAccountDependencyStatement(id));
        StatementExecution.prepareAndExecuteStatements(statementModels);
    }

    public static void updateUserInDB(User user, User userChanges) throws SQLException{

        StatementModel statementModel = UserStatementCreation.updateUserStatement(user, userChanges);
        StatementExecution.prepareAndExecuteStatement(statementModel);

    }
}
