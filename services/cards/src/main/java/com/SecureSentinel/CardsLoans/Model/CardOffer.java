package com.SecureSentinel.CardsLoans.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "card_offer")
public class CardOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int cardOfferId;
    private String cardOfferName;
    private double apr;
    @Column(name = "credit_limit")
    private int creditLimit;

    @Column(name="card_offer_status")
    private boolean cardOfferStatus = true;

    @JsonIgnore
    @OneToMany(mappedBy = "cardOffer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserCard> userCards;
}
