package com.SecureSentinel.CardsLoans.Controller;

import com.SecureSentinel.CardsLoans.DTO.UpdateUserCardDTO;
import com.SecureSentinel.CardsLoans.DTO.CreateUserCardDTO;
import com.SecureSentinel.CardsLoans.DTO.ViewUserCardDTO;
import com.SecureSentinel.CardsLoans.Mapper.UserCardMapper;
import com.SecureSentinel.CardsLoans.Model.CardOffer;
import com.SecureSentinel.CardsLoans.Service.ApiService;
import com.SecureSentinel.CardsLoans.Service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CardControllerImpl implements CardController {

    private final CardService cardService;
    private final UserCardMapper mapper;
    private final ApiService apiService;

    @Autowired
    public CardControllerImpl(CardService cardService, UserCardMapper mapper, ApiService apiService) {
        this.cardService = cardService;
        this.mapper = mapper;
        this.apiService = apiService;
    }

    @Override
    public ResponseEntity<ViewUserCardDTO> createCard(CreateUserCardDTO cardDTO) {
        return new ResponseEntity<>(cardService.createUserCard(cardDTO),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ViewUserCardDTO>> searchUserCards(Integer cardID, Integer userID, Integer accountID) {
        return new ResponseEntity<>(cardService.searchUserCards(cardID,userID,accountID),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateCard(Integer cardID, UpdateUserCardDTO updatedCardDTO) {
        return new ResponseEntity<>(cardService.updateUserCard(cardID,updatedCardDTO),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteCard(Integer cardID) {
        return new ResponseEntity<>(cardService.deleteCard(cardID),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> activateCard(Integer cardID) {
        return new ResponseEntity<>( cardService.activateCard(cardID), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<CardOffer>> getCreditOffers(Integer creditLimit) {
        return new ResponseEntity<>(cardService.getCardOffersbyCreditLimit(creditLimit),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CardOffer> createOffer(CardOffer cardOffer) {
        return new ResponseEntity<>(cardService.createOffer(cardOffer),HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CardOffer> updateOffer(Integer cardOfferId, CardOffer cardOffer) {
        return new ResponseEntity<>(cardService.updateOffer(cardOfferId,cardOffer),HttpStatus.OK);
    }
}

