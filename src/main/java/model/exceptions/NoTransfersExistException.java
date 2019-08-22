package model.exceptions;

public class NoTransfersExistException extends RuntimeException {
    public String getMessage() {
        return "No transfers exist in the database";
    }
}
