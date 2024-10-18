package com.SecureSentinel.CardsLoans.Mapper;

import com.SecureSentinel.CardsLoans.DTO.CreateUserCardDTO;
import com.SecureSentinel.CardsLoans.DTO.UpdateUserCardDTO;
import com.SecureSentinel.CardsLoans.DTO.ViewUserCardDTO;
import com.SecureSentinel.CardsLoans.Model.CardOffer;
import com.SecureSentinel.CardsLoans.Model.CardType;
import com.SecureSentinel.CardsLoans.Model.UserCard;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-09T22:44:31-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Private Build)"
)
@Component
public class UserCardMapperImpl implements UserCardMapper {

    @Override
    public UserCard CreateDTOtoUserCard(CreateUserCardDTO source) {
        if ( source == null ) {
            return null;
        }

        UserCard userCard = new UserCard();

        userCard.setCardType( mapCardType( source.getCardTypeId() ) );
        userCard.setCardOffer( mapCardOffer( source.getCardOffer() ) );
        userCard.setAccountID( source.getAccountID() );
        userCard.setUserID( source.getUserID() );
        userCard.setPin( source.getPin() );

        return userCard;
    }

    @Override
    public void updateCardFromDTO(UpdateUserCardDTO updateDTO, UserCard userCard) {
        if ( updateDTO == null ) {
            return;
        }

        userCard.setPin( updateDTO.getPin() );
    }

    @Override
    public ViewUserCardDTO userCardtoView(UserCard source) {
        if ( source == null ) {
            return null;
        }

        ViewUserCardDTO viewUserCardDTO = new ViewUserCardDTO();

        viewUserCardDTO.setCardType( sourceCardTypeCardType( source ) );
        viewUserCardDTO.setCardOffer( sourceCardOfferCardOfferName( source ) );
        viewUserCardDTO.setCardID( source.getCardID() );
        viewUserCardDTO.setAccountID( source.getAccountID() );
        viewUserCardDTO.setCardStatus( source.isCardStatus() );
        viewUserCardDTO.setCardNumber( source.getCardNumber() );
        viewUserCardDTO.setPin( source.getPin() );
        viewUserCardDTO.setStartDate( source.getStartDate() );
        viewUserCardDTO.setExpirationDate( source.getExpirationDate() );

        return viewUserCardDTO;
    }

    private String sourceCardTypeCardType(UserCard userCard) {
        if ( userCard == null ) {
            return null;
        }
        CardType cardType = userCard.getCardType();
        if ( cardType == null ) {
            return null;
        }
        String cardType1 = cardType.getCardType();
        if ( cardType1 == null ) {
            return null;
        }
        return cardType1;
    }

    private String sourceCardOfferCardOfferName(UserCard userCard) {
        if ( userCard == null ) {
            return null;
        }
        CardOffer cardOffer = userCard.getCardOffer();
        if ( cardOffer == null ) {
            return null;
        }
        String cardOfferName = cardOffer.getCardOfferName();
        if ( cardOfferName == null ) {
            return null;
        }
        return cardOfferName;
    }
}
