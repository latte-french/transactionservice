package dataStore.statementsCreation;

import model.Account;
import model.StatementModel;

import java.math.BigInteger;
import java.util.ArrayList;

public class AccountStatementCreation {

    public static StatementModel getAccountStatement(BigInteger id) {
        String statementMessage ="SELECT * FROM accounts WHERE id = ?";
        ArrayList<String> statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());
        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel getAccountsStatement () {
        String statementMessage = "SELECT * FROM accounts";
        return new StatementModel(statementMessage);
    }

    public static StatementModel getAccountsOfUserStatement(BigInteger id) {
        String statementMessage = "SELECT * FROM accounts WHERE id IN "+
                "(SELECT account_id from user_accounts where user_id = ?)";
        ArrayList<String> statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());
        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel putAccountStatement(Account account) {
        String statementMessage = "INSERT INTO accounts VALUES (?,?,?)";
        ArrayList<String> statementObjects = new ArrayList<>();
        statementObjects.add(account.getId().toString());
        statementObjects.add(account.getBalance().toString());
        statementObjects.add(account.getCurrency());

        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel putUserAccountDependencyStatement(BigInteger accountId, BigInteger userId){
        String statementMessage = "INSERT INTO user_accounts VALUES (null,?,?)";
        ArrayList<String> statementObjects = new ArrayList<>();
        statementObjects.add(userId.toString());
        statementObjects.add(accountId.toString());

        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel removeAccountStatement (BigInteger id) {
        String statementMessage = "DELETE FROM accounts WHERE id = ?";
        ArrayList<String> statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());

        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel removeUserAccountDependencyStatement(BigInteger id){
        String statementMessage = "DELETE FROM user_accounts WHERE account_id = ?";
        ArrayList<String> statementObjects = new ArrayList<>();
        statementObjects.add(id.toString());

        return new StatementModel(statementMessage, statementObjects);
    }

    public static StatementModel updateAccountStatement(Account account){
        return updateAccountStatement(account, account);
    }

    public static StatementModel updateAccountStatement(Account account, Account accountChanges){

        String statementMessage;
        ArrayList<String> statementObjects = new ArrayList<>();

        if ((accountChanges.getBalance() != null) && (accountChanges.getCurrency() == null)){
            statementMessage = "UPDATE accounts SET balance = ? WHERE id = ?";
            statementObjects.add(accountChanges.getBalance().toString());
        }
        else if ((accountChanges.getBalance() == null) && (accountChanges.getCurrency() != null)){
            statementMessage = "UPDATE accounts SET currency = ? WHERE id = ?";
            statementObjects.add(accountChanges.getCurrency());
        }
        else{
            statementMessage = "UPDATE accounts SET balance = ?, currency = ? WHERE id = ?";
            statementObjects.add(accountChanges.getBalance().toString());
            statementObjects.add(accountChanges.getCurrency());
        }

        statementObjects.add(account.getId().toString());
        return new StatementModel(statementMessage, statementObjects);
    }
}
