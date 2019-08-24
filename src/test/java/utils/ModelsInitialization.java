package utils;

import model.Account;
import model.User;

import java.math.BigInteger;
import java.util.ArrayList;

public class ModelsInitialization{

    public static ArrayList<Account> accountsForTest;
    public static Account accountForTest;
    public static ArrayList<User> usersForTest;
    public static User userForTest;
    public static ArrayList<Account> userAccountsForTest;
    public static Account accountTransferredFrom;
    public static Account accountTransferredTo;

    public static void init() {

        initializeAccountsForTest();
        accountForTest = accountsForTest.get(0);

        initializeUsersForTest();
        userForTest = usersForTest.get(0);

        initializeUserAccountsForTest();

        accountTransferredFrom = new Account(new BigInteger("4000123412341234"), 18.56, "RUB");
        accountTransferredTo = new Account(new BigInteger("4000123412341235"), 5.8755, "USD");
    }

    private static void initializeAccountsForTest(){
        accountsForTest = new ArrayList<>();
        accountsForTest.add(new Account(new BigInteger("4000123412341234"), 23.56, "RUB"));
        accountsForTest.add(new Account(new BigInteger("4000123412341235"), 5.8, "USD"));
        accountsForTest.add(new Account(new BigInteger("4000123412341236"), 102.13, "EUR"));
    }

    private static void initializeUsersForTest(){
        usersForTest = new ArrayList<>();
        usersForTest.add(new User(new BigInteger("1"),"Alex", "Smith"));
        usersForTest.add(new User(new BigInteger("2"),"Clint", "Eastwood"));
        usersForTest.add(new User(new BigInteger("3"),"Peter", "Pan"));
    }

    private static void initializeUserAccountsForTest(){
        userAccountsForTest = new ArrayList<>();
        userAccountsForTest.add(new Account(new BigInteger("4000123412341235"), 5.8, "USD"));
        userAccountsForTest.add(new Account(new BigInteger("4000123412341236"), 102.13, "EUR"));
    }
}
