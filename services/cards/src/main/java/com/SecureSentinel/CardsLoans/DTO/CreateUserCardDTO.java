package com.SecureSentinel.CardsLoans.DTO;

import com.SecureSentinel.CardsLoans.Model.CardOffer;
import com.SecureSentinel.CardsLoans.Model.CardType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUserCardDTO {
    @NotBlank(message = "UserID is mandatory")
    private int userID;
    @NotBlank(message = "AccountID is mandatory")
    private int accountID;
    @NotBlank(message = "Pin is mandatory")
    private int pin;
    @NotBlank(message = "Card Type ID is mandatory")
    private int cardTypeId;
    @NotBlank(message = "Card Offer is mandatory")
    private CardOffer cardOffer;

}
