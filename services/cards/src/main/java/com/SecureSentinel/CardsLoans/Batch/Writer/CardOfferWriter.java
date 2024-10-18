package com.SecureSentinel.CardsLoans.Batch.Writer;

import com.SecureSentinel.CardsLoans.Model.CardOffer;
import com.SecureSentinel.CardsLoans.Repository.CardOfferRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class CardOfferWriter implements ItemWriter<CardOffer> {

    private final CardOfferRepository cardOfferRepository;

    public CardOfferWriter(CardOfferRepository cardOfferRepository) {
        this.cardOfferRepository = cardOfferRepository;
    }

    @Override
    public void write(Chunk<? extends CardOffer> chunk) throws Exception {
        if(cardOfferRepository != null) {
            cardOfferRepository.saveAll(chunk);
        }
    }
}
