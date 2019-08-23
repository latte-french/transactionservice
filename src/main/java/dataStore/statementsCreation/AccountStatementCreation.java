package dataStore.statementsCreation;

import model.Account;
import model.StatementModel;

import java.math.BigInteger;
import java.util.ArrayList;

public class AccountStatementCreation {
    private static String statementMessage;
    private static ArrayList<String> statementObjects;

    public static StatementModel getAccountStatement(BigInteger id) {
        statementMessage ="SELECT * FROM accounts WHERE id = ?";
        statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());
        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel getAccountsStatement () {
        statementMessage = "SELECT * FROM accounts";
        return new StatementModel(statementMessage);
    }

    public static StatementModel getAccountsOfUserStatement(BigInteger id) {
        statementMessage = "SELECT * FROM accounts WHERE id IN "+
                "(SELECT account_id from user_accounts where user_id = ?)";
        statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());
        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel putAccountStatement(Account account) {
        statementMessage = "INSERT INTO accounts VALUES (?,?,?)";
        statementObjects = new ArrayList<>();
        statementObjects.add(account.getId().toString());
        statementObjects.add(account.getBalance().toString());
        statementObjects.add(account.getCurrency());

        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel putUserAccountDependencyStatement(BigInteger accountId, BigInteger userId){
        statementMessage = "INSERT INTO user_accounts VALUES (null,?,?)";
        statementObjects = new ArrayList<>();
        statementObjects.add(userId.toString());
        statementObjects.add(accountId.toString());

        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel removeAccountStatement (BigInteger id) {
        statementMessage = "DELETE FROM accounts WHERE id = ?";
        statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());

        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel removeUserAccountDependencyStatement(BigInteger id){
        statementMessage = "DELETE FROM user_accounts WHERE account_id = ?";
        statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());

        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel updateAccountStatement(Account account){

       statementObjects = new ArrayList<>();

        if (account.getBalance() != null){
            statementMessage = "UPDATE accounts SET balance = ? WHERE id = ?";
            statementObjects.add(account.getBalance().toString());
            statementObjects.add(account.getId().toString());
        }
        if (account.getCurrency() != null){
            statementMessage = "UPDATE accounts SET currency = ? WHERE id = ?";
            statementObjects.add(account.getCurrency());
            statementObjects.add(account.getId().toString());
        }

        return new StatementModel(statementMessage, statementObjects);
    }

}
