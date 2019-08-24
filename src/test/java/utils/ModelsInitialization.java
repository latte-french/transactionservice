package utils;

import model.Account;
import model.Transfer;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ModelsInitialization{

    public static ArrayList<Account> accountsForTest;
    public static Account accountForTest;
    public static ArrayList<User> usersForTest;
    public static User userForTest;
    public static ArrayList<Account> userAccountsForTest;
    public static Account accountTransferredFrom;
    public static Account accountTransferredTo;
    public static ArrayList<Transfer> transfersForTest;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelsInitialization.class);

    public static void init() {

        initializeAccountsForTest();
        accountForTest = accountsForTest.get(0);

        initializeUsersForTest();
        userForTest = usersForTest.get(0);

        initializeUserAccountsForTest();

        accountTransferredFrom = new Account(new BigInteger("4000123412341234"), 18.56, "RUB");
        accountTransferredTo = new Account(new BigInteger("4000123412341235"), 5.8755, "USD");

        initializeTransfersForTest();
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

    private static void initializeTransfersForTest(){
        transfersForTest = new ArrayList<>();
        try {
            transfersForTest.add(new Transfer(new BigInteger("4000123412341234"),
                    new BigInteger("4000123412341235"), 3.0, 6.7,
                    new Timestamp(dateFormat.parse("2019-08-24 05:03:04.697").getTime())));
            transfersForTest.add(new Transfer(new BigInteger("4000123412341235"),
                    new BigInteger("4000123412341236"), 21.7, 378.123,
                    new Timestamp(dateFormat.parse("2019-08-24 06:03:04.697").getTime())));
        }
        catch(ParseException e){
            LOGGER.error(e.getMessage());
        }
    }
}
