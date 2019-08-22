package model.exceptions;

public class NoAccountsExistException extends RuntimeException {
    public String getMessage() {
        return "No accounts exist in the database";
    }
}
