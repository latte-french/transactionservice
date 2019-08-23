package controllers;

import controllers.utils.RequestConverters;
import model.User;
import service.UserService;

import java.math.BigInteger;

import static spark.Spark.*;

public class UserController {

    private static UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    public static void init(){

         get("/users", (req, res) -> {
            return userService.getUsers();
        });

        get("/users/:id", (req, res) -> {
            BigInteger id = new BigInteger(req.params("id"));
            return userService.getUser(id);
        });

        get("users/:id/accounts", (req, res) -> {
            BigInteger id = new BigInteger(req.params("id"));
            return userService.getAccountsOfUser(id);
        });


        post("/users", (req, res) -> {
            User user = RequestConverters.getUserFromPostUserRequest(req);
            userService.createUser(user);
            return "OK";
        });

        put("/users/:id", (req, res) -> {
            User user = RequestConverters.getUserFromPutUserRequest(req);
            userService.updateUser(user);
            return "OK";
        });

        delete("/users/:id", (req, res) -> {
            BigInteger id = new BigInteger(req.params("id"));
            userService.removeUser(id);
            return "OK";
        });

    }
}
