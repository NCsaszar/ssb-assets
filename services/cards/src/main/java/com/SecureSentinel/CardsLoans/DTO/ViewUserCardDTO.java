package com.SecureSentinel.CardsLoans.DTO;

import lombok.Data;

import java.math.BigInteger;
import java.util.Date;

@Data
public class ViewUserCardDTO {
    private int cardID;
    private int accountID;
    private BigInteger cardNumber;
    private String cardOffer;
    private boolean cardStatus;
    private int pin;
    private String cardType;
    private Date startDate;
    private Date expirationDate;
}
