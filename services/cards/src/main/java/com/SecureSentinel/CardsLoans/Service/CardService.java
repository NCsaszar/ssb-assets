package com.SecureSentinel.CardsLoans.Service;

import com.SecureSentinel.CardsLoans.DTO.UpdateUserCardDTO;
import com.SecureSentinel.CardsLoans.DTO.CreateUserCardDTO;
import com.SecureSentinel.CardsLoans.DTO.ViewUserCardDTO;
import com.SecureSentinel.CardsLoans.Exceptions.UserCardException;
import com.SecureSentinel.CardsLoans.Mapper.UserCardMapper;
import com.SecureSentinel.CardsLoans.Model.CardOffer;
import com.SecureSentinel.CardsLoans.Model.UserCard;
import com.SecureSentinel.CardsLoans.Repository.CardOfferRepository;
import com.SecureSentinel.CardsLoans.Repository.UserCardRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Transactional
public class CardService {
    private final UserCardRepository userCardRepository;
    private final CardOfferRepository cardOfferRepository;
    private final UserCardMapper userCardMapper;
    private final EmailService emailService;


    @Autowired
    public CardService(UserCardRepository userCardRepository, CardOfferRepository cardOfferRepository, UserCardMapper userCardMapper, ApiService apiService, EmailService emailService) {
        this.userCardRepository = userCardRepository;
        this.cardOfferRepository = cardOfferRepository;
        this.userCardMapper = userCardMapper;
        this.emailService = emailService;
    }

    @Transactional
    public ViewUserCardDTO createUserCard(CreateUserCardDTO createUserCardDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            int userId = Integer.parseInt(authentication.getName());

            UserCard addedCard = userCardMapper.CreateDTOtoUserCard(createUserCardDTO);
            addedCard.setUserID(userId);

            userCardRepository.save(addedCard);

            emailService.sendCardConfirmationEmail(userId);

            System.out.println("User card created successfully for user ID: " + userId);
            return userCardMapper.userCardtoView(addedCard);
        } catch (NumberFormatException e) {
            System.err.println("Failed to parse user ID from authentication name: " + e.getMessage());
            throw new IllegalStateException("Invalid user ID in authentication context", e);
        } catch (Exception e) {
            System.err.println("An error occurred while creating the user card: " + e.getMessage());
            throw new RuntimeException("Failed to create user card", e);
        }
    }

    @Transactional
    public String deleteCard(Integer cardID) {
        try {
            UserCard existingCard = userCardRepository.findById(cardID)
                    .orElseThrow(() -> new UserCardException("UserCard not found with id: " + cardID));

            existingCard.setCardStatus(false);
            existingCard.setEndDate(new Date());

            userCardRepository.save(existingCard);

            emailService.cardFreezeConfirmation(existingCard.getUserID());

            System.out.println("UserCard with ID " + cardID + " has been deleted successfully.");
            return "UserCard Deleted!";
        } catch (UserCardException e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while deleting the user card: " + e.getMessage());
            throw new RuntimeException("Failed to delete user card", e);
        }
    }

    @Transactional
    public String activateCard(Integer cardID) {
        try {
            UserCard existingCard = userCardRepository.findById(cardID)
                    .orElseThrow(() -> new UserCardException("UserCard not found with id: " + cardID));

            if (existingCard.isCardStatus()) {
                System.out.println("Card with ID " + cardID + " is already active. No need to activate.");
                return "Card already active, no need to activate.";
            }

            existingCard.setCardStatus(true);
            existingCard.setEndDate(null);

            userCardRepository.save(existingCard);

            System.out.println("UserCard with ID " + cardID + " has been activated successfully.");
            return "Card Activated";
        } catch (UserCardException e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while activating the user card: " + e.getMessage());
            throw new RuntimeException("Failed to activate user card", e);
        }
    }


    @Transactional
    public String updateUserCard(Integer cardId, UpdateUserCardDTO updatedCardDTO) {
        try {
            UserCard existingCard = userCardRepository.findById(cardId)
                    .orElseThrow(() -> new UserCardException("UserCard not found with id: " + cardId));

            userCardMapper.updateCardFromDTO(updatedCardDTO, existingCard);

            userCardRepository.save(existingCard);

            System.out.println("UserCard with ID " + cardId + " has been updated successfully.");
            return "Card Updated!";
        } catch (UserCardException e) {
            System.err.println("Error: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while updating the user card: " + e.getMessage());
            throw new RuntimeException("Failed to update user card", e);
        }
    }


    public List<CardOffer> getCardOffersbyCreditLimit(Integer creditLimit) {
        try {
            List<CardOffer> cardOffers = cardOfferRepository.findCardOfferByCreditLimit(creditLimit);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userRole = authentication.getAuthorities().isEmpty() ? "" : authentication.getAuthorities().iterator().next().getAuthority();

            if (Objects.equals(userRole, "ROLE_CUSTOMER")) {
                return cardOffers.stream()
                        .filter(CardOffer::isCardOfferStatus)
                        .collect(Collectors.toList());
            } else {
                return new ArrayList<>(cardOffers);
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while fetching card offers: " + e.getMessage());
            throw new RuntimeException("Failed to fetch card offers", e);
        }
    }

    public CardOffer createOffer(CardOffer cardOffer) {
        try {
            return cardOfferRepository.save(cardOffer);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while creating a card offer: " + e.getMessage());
            throw new RuntimeException("Failed to create card offer", e);
        }
    }

    public CardOffer updateOffer(Integer cardOfferId, CardOffer updatedCardOffer) {
        try {
            CardOffer existingCardOffer = cardOfferRepository.getById(cardOfferId);

            updatedCardOffer.setCardOfferId(existingCardOffer.getCardOfferId());

            existingCardOffer = updatedCardOffer;

            return cardOfferRepository.save(existingCardOffer);
        } catch (Exception e) {
            System.err.println("An unexpected error occurred while updating the card offer: " + e.getMessage());
            throw new RuntimeException("Failed to update card offer", e);
        }
    }

    public List<ViewUserCardDTO> searchUserCards(Integer cardID, Integer userID, Integer accountID) {
        List<UserCard> userCards = userCardRepository.findUserCardsByCardIDAndUserIDAndAccountID(cardID, userID, accountID);

        String userRole = getUserRole();

        return userCards.stream()
                .filter(card -> userRole.equals("ROLE_CUSTOMER") ? card.isCardStatus() : true)
                .map(userCardMapper::userCardtoView)
                .collect(Collectors.toList());
    }

    public String getUserRole() {
        return SecurityContextHolder.getContext().getAuthentication()
                .getAuthorities()
                .stream()
                .findFirst()
                .map(Object::toString)
                .orElse("");
    }



}
