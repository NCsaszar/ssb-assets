package com.SecureSentinel.CardsLoans.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@Table(name = "UserCard")
public class UserCard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column (name = "card_id")
    private int cardID;

    @Column(name ="account_id")

    private int accountID;

    @Column (name = "user_id")

    private int userID;
    
    @Column (name = "card_number")
    private BigInteger cardNumber;
    
    @Column(name = "expiration_date")
    private Date expirationDate;

    @Column(name = "card_status")
    private boolean cardStatus = true;

    private int pin;
    
    @Column(name = "start_date")
    private Date startDate = new Date();
    
    @Column(name = "end_date")
    private Date endDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_type_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CardType cardType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_offer_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CardOffer cardOffer;

    public UserCard() {

        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.YEAR, 5);

        this.expirationDate = c.getTime();
        this.cardNumber = generateRandomCardNumber();
    }


    private BigInteger generateRandomCardNumber() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder randomDigits = new StringBuilder();

        randomDigits.append(1 + secureRandom.nextInt(9));

        for (int i = 1; i < 16; i++) {
            int digit = secureRandom.nextInt(10);
            randomDigits.append(digit);
        }

        return new BigInteger(randomDigits.toString());
    }


}
