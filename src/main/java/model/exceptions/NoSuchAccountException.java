package model.exceptions;

import lombok.AllArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
public class NoSuchAccountException extends Exception {

    private final BigInteger id;

    public String getMessage() {
        return "Account with id " + id + " doesn't exist";
    }
}
