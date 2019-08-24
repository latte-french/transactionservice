package controllers;

import controllers.utils.RequestConverters;
import model.Transfer;
import service.TransferService;

import static spark.Spark.get;
import static spark.Spark.post;

public class TransferController {

    private static TransferService transferService;

    public TransferController(TransferService transferService){
        this.transferService = transferService;
    }

    public static void init(){

        post("/transfers", (req, res) -> {
            Transfer transfer = RequestConverters.getTransferFromPostTransferRequest(req);
            transferService.createTransfer(transfer);
            return transfer;
        });

        get("/transfers", (req, res) -> {
            return transferService.getTransfers();
        });

    }
}
