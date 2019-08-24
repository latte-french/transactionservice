package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transfer {
    BigInteger accountFromId;
    BigInteger accountToId;
    Double sumToTransfer;
    Double sumTransferred;
    Timestamp transferredAt;
}
