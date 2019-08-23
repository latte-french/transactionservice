package model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data public class Account {

    private BigInteger id;
    private Double balance;
    private String currency;
    public static volatile BigInteger counter = new BigInteger("4000123412341236");

    public static void increaseCounter(){
        counter = counter.add(BigInteger.ONE);
    }
}

