package dataStore;

import dataStore.statementsCreation.AccountStatementCreation;
import dataStore.utils.EntityConverters;
import dataStore.utils.StatementExecution;
import model.Account;
import model.StatementModel;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class AccountStore {

    private static ResultSet result;
    private static StatementModel statementModel;
    private static ArrayList<StatementModel> statementModels;

    public static Account getAccountFromDB(BigInteger id) throws SQLException {
        Account account = new Account();
        statementModel = AccountStatementCreation.getAccountStatement(id);

        result = StatementExecution.prepareAndExecuteQuery(statementModel);
        if (result.next()) {account = EntityConverters.convertFromEntityToAccount(result);}
        return account;
    }

    public static List<Account> getAccountsFromDB () throws SQLException{
        List<Account> accountCollection = new ArrayList<Account>();
        statementModel = AccountStatementCreation.getAccountsStatement();

        result = StatementExecution.prepareAndExecuteQuery(statementModel);
        while (result.next()){
            accountCollection.add(EntityConverters.convertFromEntityToAccount(result));
        }
        return accountCollection;
    }

    public static List<Account> getAccountsOfUserFromDB(BigInteger id) throws SQLException{
        List<Account> accountCollection = new ArrayList<Account>();
        statementModel = AccountStatementCreation.getAccountsOfUserStatement(id);

        result = StatementExecution.prepareAndExecuteQuery(statementModel);
        while (result.next()){
            accountCollection.add(EntityConverters.convertFromEntityToAccount(result));
        }
        return accountCollection;
    }

    public static void putAccountToDB(Account account, BigInteger userId) throws SQLException{
        statementModels = new ArrayList<>();

        statementModels.add(AccountStatementCreation.putAccountStatement(account));
        statementModels.add(AccountStatementCreation.putUserAccountDependencyStatement(account.getId(), userId));

        StatementExecution.prepareAndExecuteStatements(statementModels);
    }

    public static void removeAccountFromDB (BigInteger id) throws SQLException{
        statementModels = new ArrayList<>();
        statementModels.add(AccountStatementCreation.removeAccountStatement(id));
        statementModels.add(AccountStatementCreation.removeUserAccountDependencyStatement(id));

        StatementExecution.prepareAndExecuteStatements(statementModels);
    }

    public static void updateAccountInDB(Account account, Account accountChanges) throws SQLException{
        statementModel = AccountStatementCreation.updateAccountStatement(account, accountChanges);
        StatementExecution.prepareAndExecuteStatement(statementModel);
    }

}