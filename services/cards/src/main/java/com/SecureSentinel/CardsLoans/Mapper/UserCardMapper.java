package com.SecureSentinel.CardsLoans.Mapper;

import com.SecureSentinel.CardsLoans.DTO.UpdateUserCardDTO;
import com.SecureSentinel.CardsLoans.DTO.CreateUserCardDTO;
import com.SecureSentinel.CardsLoans.DTO.ViewUserCardDTO;
import com.SecureSentinel.CardsLoans.Model.CardOffer;
import com.SecureSentinel.CardsLoans.Model.UserCard;
import com.SecureSentinel.CardsLoans.Model.CardType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserCardMapper {

    @Mapping(target = "expirationDate", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "cardStatus", ignore = true)
    @Mapping(target = "cardID", ignore = true)
    @Mapping(target = "cardNumber", ignore = true)
    @Mapping(target = "cardType", source = "cardTypeId", qualifiedByName = "mapCardType")
    @Mapping(target = "cardOffer", source = "cardOffer", qualifiedByName = "mapCardOffer")
    UserCard CreateDTOtoUserCard(CreateUserCardDTO source);

    @Named("mapCardType")
    default CardType mapCardType(int cardTypeId) {
        return cardTypeId == 1 ? new CardType(1, "Debit") : new CardType(2, "Credit");
    }

    @Named("mapCardOffer")
    default CardOffer mapCardOffer(CardOffer cardOffer) {
        CardOffer newCardOffer = new CardOffer();
        newCardOffer.setCardOfferId(cardOffer.getCardOfferId());
        newCardOffer.setCardOfferName(cardOffer.getCardOfferName());
        newCardOffer.setApr(cardOffer.getApr());
        newCardOffer.setCreditLimit(cardOffer.getCreditLimit());
        newCardOffer.setUserCards(cardOffer.getUserCards());
        return newCardOffer;
    }

    @Mapping(target = "expirationDate", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "cardStatus", ignore = true)
    @Mapping(target = "cardNumber", ignore = true)
    @Mapping(target = "cardType", ignore = true)
    @Mapping(target = "pin", source = "pin")
    void updateCardFromDTO(UpdateUserCardDTO updateDTO, @MappingTarget UserCard userCard);

    @Mapping(target = "cardType", source = "cardType.cardType")
    @Mapping(target = "cardOffer", source = "cardOffer.cardOfferName")
    @Mapping(target = "cardID", source = "cardID")
    @Mapping(target = "accountID", source ="accountID")
    @Mapping(target = "cardStatus", source = "cardStatus")
    ViewUserCardDTO userCardtoView(UserCard source);

}
