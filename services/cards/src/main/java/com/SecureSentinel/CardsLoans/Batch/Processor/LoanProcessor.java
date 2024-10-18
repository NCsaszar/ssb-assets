package com.SecureSentinel.CardsLoans.Batch.Processor;

import com.SecureSentinel.CardsLoans.Model.CardOffer;
import com.SecureSentinel.CardsLoans.Model.Loan;
import com.SecureSentinel.CardsLoans.Repository.CardOfferRepository;
import com.SecureSentinel.CardsLoans.Repository.LoanRepository;
import lombok.Getter;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;

@Component
@Getter
public class LoanProcessor implements ItemProcessor<Loan, Loan>, StepExecutionListener {

    private HashSet<Loan> duplicateOffers = new HashSet<>();

    @Autowired
    private final LoanRepository loanRepository;

    public LoanProcessor(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @Override
    public Loan process(Loan item) throws Exception {

       if(duplicateOffers.contains(item)){
           return null;
       }

       duplicateOffers.add(item);
        return item;

    }
}