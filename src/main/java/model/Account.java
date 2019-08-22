package model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data public class Account {

    public static BigInteger counter = new BigInteger("4000123412341237");
    private BigInteger id;
    private Double balance;
    private String currency;
}
