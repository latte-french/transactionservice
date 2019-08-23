package utils;

import model.Account;

import java.math.BigInteger;
import java.util.ArrayList;

public class ModelsInitialization{
    public static Account accountForTest;
    public static ArrayList<Account> accountsForTest;

    public static void init() {
        accountForTest = new Account(new BigInteger("4000123412341234"), 23.56, "RUB");

        accountsForTest = new ArrayList<>();
        accountsForTest.add(new Account(new BigInteger("4000123412341234"), 23.56, "RUB"));
        accountsForTest.add(new Account(new BigInteger("4000123412341235"), 5.8, "USD"));
        accountsForTest.add(new Account(new BigInteger("4000123412341236"), 102.13, "EUR"));

    }
}
