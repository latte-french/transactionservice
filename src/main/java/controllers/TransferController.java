package controllers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Transfer;
import service.TransferService;

import java.sql.Timestamp;
import java.util.Date;

import static spark.Spark.get;
import static spark.Spark.post;

public class TransferController {

    private static TransferService transferService;

    public TransferController(TransferService transferService){
        this.transferService = transferService;
    }

    public static void init(){

        post("/transfers", (req, res) -> {
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(req.body()).getAsJsonObject();
            Transfer transfer = new Transfer();
            transfer.setAccountFromId(obj.get("from").getAsBigInteger());
            transfer.setAccountToId(obj.get("to").getAsBigInteger());
            transfer.setSumToTransfer(obj.get("money").getAsDouble());
            transfer.setTransferredAt(new Timestamp(new Date().getTime()));
            transferService.createTransfer(transfer);
            return ("OK");
        });

        get("/transfers", (req, res) -> {
            return transferService.getTransfers();
        });

    }
}
