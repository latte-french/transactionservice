import controllers.*;
import dataStore.DatabaseCreation;
import dataStore.DatabaseInitialization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.AccountService;
import service.TransferService;
import service.UserService;
import service.impl.AccountServiceImpl;
import service.impl.TransferServiceImpl;
import service.impl.UserServiceImpl;

import static spark.Spark.port;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {

        port(8080);
        LOGGER.info("Application is starting");
        initAllProcesses();
    }

    private static void initAllProcesses(){

        AccountService accountService = new AccountServiceImpl();
        UserService userService = new UserServiceImpl();
        TransferService transferService = new TransferServiceImpl(accountService);

        DatabaseCreation.initDatabase();
        DatabaseInitialization.populateDatabase();
        new AccountController(accountService).init();
        new UserController(userService).init();
        new TransferController(transferService).init();
        new ExceptionController().init();
    }

}
