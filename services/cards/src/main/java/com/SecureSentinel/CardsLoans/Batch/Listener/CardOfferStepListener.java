package com.SecureSentinel.CardsLoans.Batch.Listener;

import com.SecureSentinel.CardsLoans.Batch.Processor.CardOfferProcessor;
import com.SecureSentinel.CardsLoans.Model.CardOffer;
import com.opencsv.CSVWriter;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CardOfferStepListener implements StepExecutionListener {

    @Autowired
    private final CardOfferProcessor cardOfferProcessor;

    public CardOfferStepListener(CardOfferProcessor cardOfferProcessor) {
        this.cardOfferProcessor = cardOfferProcessor;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getStatus() == BatchStatus.COMPLETED) {
            HashMap<CardOffer, CardOffer> duplicateOffers = cardOfferProcessor.getDuplicateOffers();
            String jobParameter = getJobParameter(stepExecution);
            writeDuplicatesToCsv(duplicateOffers, jobParameter);
        }
        return ExitStatus.COMPLETED;
    }

    private String getJobParameter(StepExecution stepExecution) {
        JobParameters jobParameters = stepExecution.getJobParameters();
        return jobParameters.getString("JobID");
    }

    private void writeDuplicatesToCsv(HashMap<CardOffer, CardOffer> duplicateOffers, String jobParameter) {
        String[] header = { "OfferID", "CardType", "InterestRate", "ExpiryDate", "Type" };
        String fileName = "duplicate_offers_" + jobParameter + ".csv";
        try (CSVWriter writer = new CSVWriter(new FileWriter(fileName))) {
            writer.writeNext(header);
            for (Map.Entry<CardOffer, CardOffer> entry : duplicateOffers.entrySet()) {
                CardOffer original = entry.getKey();
                CardOffer duplicate = entry.getValue();
                String[] originalData = {
                        String.valueOf(original.getCardOfferId()),
                        original.getCardOfferName(),
                        String.valueOf(original.getApr()),
                        String.valueOf(original.getCreditLimit()),
                        "Original"
                };
                String[] duplicateData = {
                        String.valueOf(duplicate.getCardOfferId()),
                        duplicate.getCardOfferName(),
                        String.valueOf(duplicate.getApr()),
                        String.valueOf(duplicate.getCreditLimit()),
                        "Duplicate"
                };
                writer.writeNext(originalData);
                writer.writeNext(duplicateData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
