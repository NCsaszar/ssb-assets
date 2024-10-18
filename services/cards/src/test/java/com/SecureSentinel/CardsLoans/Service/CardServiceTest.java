package com.SecureSentinel.CardsLoans.Service;

import com.SecureSentinel.CardsLoans.DTO.CreateUserCardDTO;
import com.SecureSentinel.CardsLoans.DTO.UpdateUserCardDTO;
import com.SecureSentinel.CardsLoans.DTO.ViewUserCardDTO;
import com.SecureSentinel.CardsLoans.Mapper.UserCardMapper;
import com.SecureSentinel.CardsLoans.Model.CardOffer;
import com.SecureSentinel.CardsLoans.Model.UserCard;
import com.SecureSentinel.CardsLoans.Repository.CardOfferRepository;
import com.SecureSentinel.CardsLoans.Repository.UserCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigInteger;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CardServiceTest {


    @Mock
    private UserCardRepository userCardRepository;

    @Mock
    private CardOfferRepository cardOfferRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserCardMapper userCardMapper;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CardService cardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUserCard() {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getName()).thenReturn("123");

        CreateUserCardDTO createUserCardDTO = new CreateUserCardDTO();
        createUserCardDTO.setUserID(123);
        createUserCardDTO.setAccountID(456);
        createUserCardDTO.setPin(789);
        createUserCardDTO.setCardTypeId(1);
        createUserCardDTO.setCardOffer(new CardOffer());

        UserCard addedCard = new UserCard();
        addedCard.setUserID(createUserCardDTO.getUserID());
        addedCard.setAccountID(createUserCardDTO.getAccountID());
        addedCard.setPin(createUserCardDTO.getPin());
        addedCard.setCardOffer(createUserCardDTO.getCardOffer());

        when(userCardMapper.CreateDTOtoUserCard(createUserCardDTO)).thenReturn(addedCard);

        when(userCardRepository.save(any(UserCard.class))).thenReturn(addedCard);

        doNothing().when(emailService).sendCardConfirmationEmail(Mockito.anyInt());

        ViewUserCardDTO result = cardService.createUserCard(createUserCardDTO);

        verify(userCardRepository).save(addedCard);
        verify(emailService).sendCardConfirmationEmail(123);

    }

    @Test
    void deleteCard() {
        Integer cardID = 123;

        UserCard existingCard = new UserCard();
        existingCard.setUserID(456);

        when(userCardRepository.findById(cardID)).thenReturn(Optional.of(existingCard));

        when(userCardRepository.save(existingCard)).thenReturn(existingCard);

        doNothing().when(emailService).cardFreezeConfirmation(existingCard.getUserID());

        String result = cardService.deleteCard(cardID);

        assertEquals("UserCard Deleted!", result);
        assertFalse(existingCard.isCardStatus());
        verify(userCardRepository).save(existingCard);
        verify(emailService).cardFreezeConfirmation(existingCard.getUserID());
    }

    @Test
    void activateCard() {
        Integer cardID = 123;

        UserCard existingCard = new UserCard();
        existingCard.setCardID(cardID);
        existingCard.setAccountID(789);
        existingCard.setUserID(456);
        existingCard.setCardNumber(new BigInteger("1234567890123456"));
        existingCard.setCardStatus(false);
        existingCard.setPin(1234);
        existingCard.setStartDate(new Date());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, 5);
        existingCard.setExpirationDate(c.getTime());

        when(userCardRepository.findById(cardID)).thenReturn(Optional.of(existingCard));

        String result = cardService.activateCard(cardID);

        verify(userCardRepository).save(existingCard);

        assertTrue(existingCard.isCardStatus());
        assertNull(existingCard.getEndDate());

        assertEquals("Card Activated", result);
    }

    @Test
    void updateUserCard() {
        Integer cardID = 123;

        UserCard existingCard = new UserCard();
        existingCard.setCardID(cardID);
        existingCard.setAccountID(789);
        existingCard.setUserID(456);
        existingCard.setCardNumber(new BigInteger("1234567890123456"));
        existingCard.setCardStatus(true);
        existingCard.setPin(1234);
        existingCard.setStartDate(new Date());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, 5);
        existingCard.setExpirationDate(c.getTime());

        UpdateUserCardDTO updatedCardDTO = new UpdateUserCardDTO();
        updatedCardDTO.setPin(4321);

        when(userCardRepository.findById(cardID)).thenReturn(Optional.of(existingCard));

        doNothing().when(userCardMapper).updateCardFromDTO(updatedCardDTO, existingCard);

        String result = cardService.updateUserCard(cardID, updatedCardDTO);

        verify(userCardRepository).save(existingCard);

        assertEquals("Card Updated!", result);
    }

    @Test
    void getCardOffersbyCreditLimit() {
        Integer creditLimit = 5000;

        List<CardOffer> cardOffers = new ArrayList<>();
        CardOffer activeCardOffer = new CardOffer();
        activeCardOffer.setCardOfferStatus(true);
        CardOffer inactiveCardOffer = new CardOffer();
        inactiveCardOffer.setCardOfferStatus(false);
        cardOffers.add(activeCardOffer);
        cardOffers.add(inactiveCardOffer);

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(cardOfferRepository.findCardOfferByCreditLimit(creditLimit)).thenReturn(cardOffers);

        List<CardOffer> result = cardService.getCardOffersbyCreditLimit(creditLimit);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(activeCardOffer));
        assertFalse(result.contains(inactiveCardOffer));

        verify(cardOfferRepository, times(1)).findCardOfferByCreditLimit(creditLimit);
    }

    @Test
    void createOffer() {
        CardOffer cardOffer = new CardOffer();
        cardOffer.setCardOfferId(1);

        when(cardOfferRepository.save(cardOffer)).thenReturn(cardOffer);

        CardOffer result = cardService.createOffer(cardOffer);

        assertNotNull(result);
        assertEquals(cardOffer.getCardOfferId(), result.getCardOfferId());

        verify(cardOfferRepository, times(1)).save(cardOffer);
    }


    @Test
    void updateOffer() {
        CardOffer existingCardOffer = new CardOffer();
        existingCardOffer.setCardOfferId(1);

        CardOffer updatedCardOffer = new CardOffer();
        updatedCardOffer.setCardOfferId(existingCardOffer.getCardOfferId());

        when(cardOfferRepository.getById(1)).thenReturn(existingCardOffer);
        when(cardOfferRepository.save(updatedCardOffer)).thenReturn(updatedCardOffer);

        CardOffer result = cardService.updateOffer(1, updatedCardOffer);

        assertNotNull(result);
        assertEquals(updatedCardOffer.getCardOfferId(), result.getCardOfferId());

        verify(cardOfferRepository, times(1)).getById(1);
        verify(cardOfferRepository, times(1)).save(updatedCardOffer);
    }

    @Test
    void searchUserCards() {
//        List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
//        Authentication authentication = new UsernamePasswordAuthenticationToken("user", "password", authorities);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        CardService cardService = mock(CardService.class);
//
//        // Stub the getUserRole() method on cardService
//        when(cardService.getUserRole()).thenReturn("ROLE_CUSTOMER");
//
//        // Stub the searchUserCards() method on cardService
//        List<UserCard> userCards = new ArrayList<>();
//        UserCard userCard1 = new UserCard();
//        userCard1.setCardID(1);
//        userCard1.setUserID(101);
//        userCard1.setAccountID(201);
//        userCard1.setCardStatus(true);
//        userCards.add(userCard1);
//
//        // Stub the behavior of userCardRepository and userCardMapper as needed
//
//        // Call the method under test
//        List<ViewUserCardDTO> result = cardService.searchUserCards(null, 101, null);
//
//        assertEquals(1, result.size());
//        ViewUserCardDTO expectedResult1 = new ViewUserCardDTO();
//        expectedResult1.setCardID(1);
//        expectedResult1.setAccountID(201);
//        assertEquals(expectedResult1, result.get(0));
//
//        verify(userCardRepository, times(1)).findUserCardsByCardIDAndUserIDAndAccountID(1, 101, 201);
//
//        verify(userCardMapper, times(1)).userCardtoView(userCard1);
    }

}