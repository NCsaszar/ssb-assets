package com.SecureSentinel.CardsLoans.Batch.Processor;

import com.SecureSentinel.CardsLoans.Model.CardOffer;
import com.SecureSentinel.CardsLoans.Repository.CardOfferRepository;
import lombok.Getter;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
@Getter
public class CardOfferProcessor implements ItemProcessor<CardOffer, CardOffer>, StepExecutionListener {

    private HashMap<CardOffer, CardOffer> duplicateOffers = new HashMap<>();

    @Autowired
    private final CardOfferRepository cardOfferRepository;

    public CardOfferProcessor(CardOfferRepository cardOfferRepository) {
        this.cardOfferRepository = cardOfferRepository;
    }

    @Override
    public CardOffer process(CardOffer item) throws Exception {

        CardOffer existingCardOffer = cardOfferRepository.findCardOfferByCardOfferName(item.getCardOfferName());

            if (existingCardOffer != null) {
                duplicateOffers.put(item, existingCardOffer);
                return null;
            }
            return item;

    }
}

