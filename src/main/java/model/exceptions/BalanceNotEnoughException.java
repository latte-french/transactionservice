package model.exceptions;

import lombok.AllArgsConstructor;
import model.Account;

@AllArgsConstructor
public class BalanceNotEnoughException extends RuntimeException {
    private final Account account;
    private final Double sumToTransfer;

    public String getMessage() {
        return "Account with id " + account.getId() + " can't tranfer " + sumToTransfer + " " + account.getCurrency() +
                ", the balance is only " + account.getBalance() + " " + account.getCurrency();
    }
}
