package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.User;
import service.UserService;
import service.impl.UserServiceImpl;

import java.math.BigInteger;

import static spark.Spark.*;

public class UserController {

    private static UserController instance;
    private final UserService userService = UserServiceImpl.getInstance();

    public static UserController getInstance() {
        if (instance == null) {
            instance = new UserController();
        }
        return instance;
    }

    public UserController() {

         get("/users", (req, res) -> {
            return userService.getUsers();
        });

        get("/users/:id", (req, res) -> {
            BigInteger id = new BigInteger(req.params("id"));
            return userService.getUser(id);
        });

        post("/users", (req, res) -> {
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(req.body()).getAsJsonObject();
            User.counter.add(BigInteger.ONE);
            User user = new User(User.counter, obj.get("firstName").getAsString(), obj.get("lastName").getAsString());
            userService.createUser(user);
            return user;
        });

        put("/users/:id", (req, res) -> {
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(req.body()).getAsJsonObject();
            User user = new User();
            if (obj.get("firstName") != null) {user.setFirstName(obj.get("firstName").getAsString());}
            if (obj.get("lastName") != null) {user.setLastName(obj.get("lastName").getAsString());}
            user.setId(new BigInteger(req.params("id")));
            userService.updateUser(user);
            return res;
        });

        delete("/users/:id", (req, res) -> {
            BigInteger id = new BigInteger(req.params("id"));
            userService.removeUser(id);
            return res;
        });

    }
}
