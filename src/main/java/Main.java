import controllers.*;
import dataStore.DatabaseSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.port;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {

        port(8080);
        LOGGER.info("Application is starting");
        initAllProcesses();
    }

    private static void initAllProcesses(){

        DatabaseSetup.initDatabase();
        AccountController accountController = AccountController.getInstance();
        UserController userController = UserController.getInstance();
        TransferController transferController = TransferController.getInstance();
        ExceptionController exceptionController = ExceptionController.getInstance();
    }

}
