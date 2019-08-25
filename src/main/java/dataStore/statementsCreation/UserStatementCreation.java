package dataStore.statementsCreation;

import model.StatementModel;
import model.User;

import java.math.BigInteger;
import java.util.ArrayList;

public class UserStatementCreation {

    public static StatementModel getUserStatement(BigInteger id) {
        String statementMessage = "SELECT * FROM users WHERE id = ?";
        ArrayList<String> statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());

        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel getUsersStatement() {
        String statementMessage = "SELECT * FROM users";
        return new StatementModel(statementMessage);
    }

    public static StatementModel putUserStatement(User user){
        String statementMessage = "INSERT INTO users VALUES (?,?,?)";
        ArrayList<String> statementObjects = new ArrayList<>();
        statementObjects.add(user.getId().toString());
        statementObjects.add(user.getFirstName());
        statementObjects.add(user.getLastName());

        return new StatementModel(statementMessage, statementObjects);

    }

    public static StatementModel removeUserStatement (BigInteger id){
        String statementMessage = "DELETE FROM users WHERE id = ?";
        ArrayList<String> statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());

        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel removeUsersAccountsStatement(BigInteger id){
        String statementMessage = "DELETE FROM accounts WHERE id in (SELECT account_id FROM user_accounts WHERE user_id = ?)";
        ArrayList<String> statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());

        return new StatementModel(statementMessage, statementObjects);

    }

    public static StatementModel removeUserAccountDependencyStatement(BigInteger id){
        String statementMessage = "DELETE FROM user_accounts WHERE user_id = ?";
        ArrayList<String> statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());

        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel updateUserStatement(User user, User userChanges){

        String statementMessage;
        ArrayList<String> statementObjects = new ArrayList<>();

        if ((userChanges.getFirstName() != null) && (userChanges.getLastName() == null)){
            statementMessage = "UPDATE users SET first_name = ? WHERE id = ?";
            statementObjects.add(userChanges.getFirstName());
        }
        else if ((userChanges.getFirstName() == null) &&(userChanges.getLastName() != null)) {
            statementMessage = "UPDATE users SET last_name = ? WHERE id = ?";
            statementObjects.add(userChanges.getLastName());
        }
        else{
            statementMessage = "UPDATE users SET first_name = ?, last_name = ? WHERE id = ?";
            statementObjects.add(userChanges.getFirstName());
            statementObjects.add(userChanges.getLastName());
        }

        statementObjects.add(user.getId().toString());
        return new StatementModel(statementMessage, statementObjects);
    }
}
