package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

    public static volatile BigInteger counter = new BigInteger("3");
    private BigInteger id;
    private String firstName;
    private String lastName;

    public static synchronized void increaseCounter(){
        counter = counter.add(BigInteger.ONE);
    }
}
