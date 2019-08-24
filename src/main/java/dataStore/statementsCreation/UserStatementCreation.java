package dataStore.statementsCreation;

import model.StatementModel;
import model.User;

import java.math.BigInteger;
import java.util.ArrayList;

public class UserStatementCreation {

    private static String statementMessage;
    private static ArrayList<String> statementObjects;

    public static StatementModel getUserStatement(BigInteger id) {
        statementMessage = "SELECT * FROM users WHERE id = ?";
        statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());

        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel getUsersStatement() {
        statementMessage = "SELECT * FROM users";
        return new StatementModel(statementMessage);
    }

    public static StatementModel putUserStatement(User user){
        statementMessage = "INSERT INTO users VALUES (?,?,?)";
        statementObjects = new ArrayList<>();
        statementObjects.add(user.getId().toString());
        statementObjects.add(user.getFirstName());
        statementObjects.add(user.getLastName());

        return new StatementModel(statementMessage, statementObjects);

    }

    public static StatementModel removeUserStatement (BigInteger id){
        statementMessage = "DELETE FROM users WHERE id = ?";
        statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());

        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel removeUsersAccountsStatement(BigInteger id){
        statementMessage = "DELETE FROM accounts WHERE id in (SELECT account_id FROM user_accounts WHERE user_id = ?)";
        statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());

        return new StatementModel(statementMessage, statementObjects);

    }

    public static StatementModel removeUserAccountDependencyStatement(BigInteger id){
        statementMessage = "DELETE FROM user_accounts WHERE user_id = ?";
        statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());

        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel updateUserStatement(User user, User userChanges){

        statementObjects = new ArrayList<>();

        if (userChanges.getFirstName() != null){
            statementMessage = "UPDATE users SET first_name = ? WHERE id = ?";
            statementObjects.add(userChanges.getFirstName());
            statementObjects.add(user.getId().toString());
        }
        if (userChanges.getLastName() != null) {
            statementMessage = "UPDATE users SET last_name = ? WHERE id = ?";
            statementObjects.add(userChanges.getLastName());
            statementObjects.add(user.getId().toString());
        }

        return new StatementModel(statementMessage, statementObjects);

    }
}
