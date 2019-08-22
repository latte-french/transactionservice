package model.exceptions;

import lombok.AllArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
public class NoSuchUserException extends RuntimeException {

    private final BigInteger id;

    public String getMessage() {
        return "User with id " + id + " doesn't exist";
    }
}
