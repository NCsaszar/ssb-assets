package com.SecureSentinel.CardsLoans.Batch.Writer;

import com.SecureSentinel.CardsLoans.Model.CardOffer;
import com.SecureSentinel.CardsLoans.Model.Loan;
import com.SecureSentinel.CardsLoans.Repository.CardOfferRepository;
import com.SecureSentinel.CardsLoans.Repository.LoanRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class LoanWriter implements ItemWriter<Loan> {

    private final LoanRepository loanRepository;

    public LoanWriter(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public void write(Chunk<? extends Loan> chunk) throws Exception {
        if(loanRepository != null) {
            loanRepository.saveAll(chunk);
        }
    }
}
