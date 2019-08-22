package model.exceptions;

import lombok.AllArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
public class NoUserAccountsException extends RuntimeException {
    private final BigInteger id;

    public String getMessage() {
        return ("No accounts belong to the user with id = " + id);
    }
}
