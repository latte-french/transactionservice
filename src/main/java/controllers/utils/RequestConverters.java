package controllers.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Account;
import model.Transfer;
import model.User;
import spark.Request;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

public class RequestConverters {

    public static JsonParser parser = new JsonParser();

    public static Account getAccountFromPostAccountRequest(Request request){
        JsonObject obj = parser.parse(request.body()).getAsJsonObject();
        Account.increaseCounter();
        return new Account(Account.counter, obj.get("balance").getAsDouble(), obj.get("currency").getAsString());
    }

    public static BigInteger getUserIdFromPostAccountRequest(Request request){
        JsonObject obj = parser.parse(request.body()).getAsJsonObject();
        return obj.get("userId").getAsBigInteger();
    }

    public static Account getAccountFromPutAccountRequest(Request request){
        JsonObject obj = parser.parse(request.body()).getAsJsonObject();
        Account account = new Account();
        if (obj.get("balance") != null) {account.setBalance(obj.get("balance").getAsDouble());}
        if (obj.get("currency") != null) {account.setCurrency(obj.get("currency").getAsString());}
        return account;
    }

    public static User getUserFromPostUserRequest(Request request){
        JsonObject obj = parser.parse(request.body()).getAsJsonObject();
        User.increaseCounter();
        return new User(User.counter, obj.get("firstName").getAsString(), obj.get("lastName").getAsString());
    }

    public static User getUserFromPutUserRequest(Request request){
        JsonObject obj = parser.parse(request.body()).getAsJsonObject();
        User user = new User();
        if (obj.get("firstName") != null) {user.setFirstName(obj.get("firstName").getAsString());}
        if (obj.get("lastName") != null) {user.setLastName(obj.get("lastName").getAsString());}
        user.setId(new BigInteger(request.params("id")));
        return user;
    }

    public static Transfer getTransferFromPostTransferRequest(Request request){
        JsonObject obj = parser.parse(request.body()).getAsJsonObject();
        Transfer transfer = new Transfer();
        transfer.setAccountFromId(obj.get("from").getAsBigInteger());
        transfer.setAccountToId(obj.get("to").getAsBigInteger());
        transfer.setSumToTransfer(obj.get("money").getAsDouble());
        transfer.setTransferredAt(new Timestamp(new Date().getTime()));
        return transfer;
    }
}
