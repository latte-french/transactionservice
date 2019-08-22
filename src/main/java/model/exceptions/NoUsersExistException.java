package model.exceptions;

public class NoUsersExistException extends  RuntimeException{
    public String getMessage() {
        return "No users exist in the database";
    }
}
