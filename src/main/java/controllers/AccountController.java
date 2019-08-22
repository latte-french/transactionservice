package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Account;
import service.AccountService;
import service.impl.AccountServiceImpl;

import java.math.BigInteger;

import static spark.Spark.*;


public class AccountController {

    private static AccountController instance;
    final AccountService accountService = AccountServiceImpl.getInstance();

    public static AccountController getInstance() {
        if (instance == null) {
            instance = new AccountController();
        }
        return instance;
    }

    public AccountController() {

        get("/accounts", (req, res) -> {
            return accountService.getAccounts();
        });

        get("/accounts/:id", (req, res) -> {
            BigInteger id = new BigInteger(req.params("id"));
            return accountService.getAccount(id);
        });

        get("users/:id/accounts", (req, res) -> {
            BigInteger id = new BigInteger(req.params("id"));
            return accountService.getAccountsOfUser(id);
        });

        post("/accounts", (req, res) -> {
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(req.body()).getAsJsonObject();
            Account.counter.add(BigInteger.ONE);
            Account account = new Account(Account.counter, obj.get("balance").getAsDouble(), obj.get("currency").getAsString());
            BigInteger userId = obj.get("userId").getAsBigInteger();
            accountService.createAccount(account,userId);
            return ("OK");
        });

        put("/accounts/:id", (req, res) -> {
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(req.body()).getAsJsonObject();
            Account account = new Account();
            if (obj.get("balance") != null) {account.setBalance(obj.get("balance").getAsDouble());}
            if (obj.get("currency") != null) {account.setCurrency(obj.get("currency").getAsString());}
            account.setId(new BigInteger(req.params("id")));
            accountService.updateAccount(account);
            return ("OK");
        });

        delete("/accounts/:id", (req, res) -> {
            BigInteger id = new BigInteger(req.params("id"));
            accountService.removeAccount(id);
            return ("OK");
        });

    }
}
