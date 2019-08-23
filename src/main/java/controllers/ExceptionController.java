package controllers;

import model.exceptions.*;

import java.sql.SQLException;

import static spark.Spark.*;

public class ExceptionController {

    public static void init() {

        exception(NoSuchAccountException.class, (exception, request, response) -> {
            response.status(404);
            response.body(exception.getMessage());
        });

        exception(SQLException.class, (exception, request, response) -> {
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
