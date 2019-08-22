package controllers;

import model.exceptions.*;

import static spark.Spark.*;

public class ExceptionController {

    private static ExceptionController instance;

    public static ExceptionController getInstance() {
        if (instance == null) {
            instance = new ExceptionController();
        }
        return instance;
    }

    public ExceptionController() {

        exception(NoSuchAccountException.class, (exception, request, response) -> {
            response.status(404);
            response.body(exception.getMessage());
        });

        exception(NoAccountsExistException.class, (exception, request, response) -> {
            response.status(404);
            response.body(exception.getMessage());
        });

        exception(NoUserAccountsException.class, (exception, request, response) -> {
            response.status(404);
            response.body(exception.getMessage());
        });

        exception(NoTransfersExistException.class, (exception, request, response) -> {
            response.status(404);
            response.body(exception.getMessage());
        });

        exception(BalanceNotEnoughException.class, (exception, request, response) -> {
            response.status(404);
            response.body(exception.getMessage());
        });

        exception(NoSuchUserException.class, (exception, request, response) -> {
            response.status(404);
            response.body(exception.getMessage());
        });

        exception(NoUsersExistException.class, (exception, request, response) -> {
            response.status(404);
            response.body(exception.getMessage());
        });
    }
}
