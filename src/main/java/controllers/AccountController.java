package controllers;

import controllers.utils.RequestConverters;
import model.Account;
import service.AccountService;

import java.math.BigInteger;

import static spark.Spark.*;

public class AccountController {

    private static AccountService accountService;

    public AccountController(AccountService accountService){
         this.accountService = accountService;
     }

    public static void init() {

        get("/accounts", (req, res) -> {
            return accountService.getAccounts();
        });

        get("/accounts/:id", (req, res) -> {
            BigInteger id = new BigInteger(req.params("id"));
            return accountService.getAccount(id);
        });

        post("/accounts", (req, res) -> {
            Account account = RequestConverters.getAccountFromPostAccountRequest(req);
            BigInteger userId = RequestConverters.getUserIdFromPostAccountRequest(req);
            accountService.createAccount(account,userId);
            return accountService.getAccount(account.getId());
        });

        put("/accounts/:id", (req, res) -> {
            BigInteger id = new BigInteger(req.params("id"));
            Account account = accountService.getAccount(id);
            Account accountChanges = RequestConverters.getAccountFromPutAccountRequest(req);
            accountService.updateAccount(account, accountChanges);
            return accountService.getAccount(id);
        });

        delete("/accounts/:id", (req, res) -> {
            BigInteger id = new BigInteger(req.params("id"));
            accountService.removeAccount(id);
            return ("Account deleted");
        });

    }

}
