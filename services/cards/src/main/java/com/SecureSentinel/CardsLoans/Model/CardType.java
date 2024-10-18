package com.SecureSentinel.CardsLoans.Model;

import java.util.List;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "CardType")
public class CardType {

    @Id
    private int cardTypeId;
    private String cardType;

    @OneToMany(mappedBy = "cardType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserCard> userCards;

    public CardType(int cardTypeId, String cardType) {
        this.cardTypeId = cardTypeId;
        this.cardType = cardType;
    }

    public CardType() {

    }
}
