package com.SecureSentinel.CardsLoans.Batch;

import com.SecureSentinel.CardsLoans.Batch.FieldSetMapper.CustomLoanFieldSetMapper;
import com.SecureSentinel.CardsLoans.Batch.Listener.CardOfferStepListener;
import com.SecureSentinel.CardsLoans.Batch.Processor.CardOfferProcessor;
import com.SecureSentinel.CardsLoans.Batch.Processor.LoanProcessor;
import com.SecureSentinel.CardsLoans.Batch.Writer.CardOfferWriter;
import com.SecureSentinel.CardsLoans.Batch.Writer.LoanWriter;
import com.SecureSentinel.CardsLoans.Model.CardOffer;
import com.SecureSentinel.CardsLoans.Model.Loan;
import lombok.Data;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

@Data
@Configuration
public class BatchConfiguration {

    private final PlatformTransactionManager transactionManager;
    private final CardOfferProcessor cardOfferProcessor;
    private final CardOfferWriter cardOfferWriter;
    private final LoanWriter loanWriter;
    private final CustomLoanFieldSetMapper customLoanFieldSetMapper;
    private final LoanProcessor loanProcessor;

    public BatchConfiguration(PlatformTransactionManager transactionManager, CardOfferProcessor cardOfferProcessor, CardOfferWriter cardOfferWriter, LoanWriter loanWriter, CustomLoanFieldSetMapper customLoanFieldSetMapper, LoanProcessor loanProcessor) {
        this.transactionManager = transactionManager;
        this.cardOfferProcessor = cardOfferProcessor;
        this.cardOfferWriter = cardOfferWriter;
        this.loanWriter = loanWriter;
        this.customLoanFieldSetMapper = customLoanFieldSetMapper;
        this.loanProcessor = loanProcessor;
    }




    @Bean
    public FlatFileItemReader<CardOffer> reader() throws IOException {

        return new FlatFileItemReaderBuilder<CardOffer>()
                .name("cardItemReader")
                .resource(new ClassPathResource("user_card.csv"))
                .delimited()
                .names(new String[]{"card_offer_id", "card_offer_name", "apr", "credit_limit", "card_offer_status"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<CardOffer>() {{
                    setTargetType(CardOffer.class);
                }})
                .linesToSkip(1)
                .saveState(false)
                .maxItemCount(10000)
                .build();
    }

    @Bean
    public FlatFileItemReader<Loan> loanReader() throws IOException {
        LineTokenizer tokenizer = new DelimitedLineTokenizer() {{
            setNames("loanID", "loan_type_id", "annualPercentageRate", "termMonths", "maxAmount", "minAmount", "loanStatus");
        }};

        DefaultLineMapper<Loan> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(customLoanFieldSetMapper);

        return new FlatFileItemReaderBuilder<Loan>()
                .name("loanItemReader")
                .resource(new ClassPathResource("loan.csv"))
                .lineMapper(lineMapper)
                .linesToSkip(1)
                .saveState(false)
                .maxItemCount(10000)
                .build();
    }



    public CardOfferStepListener cardOfferStepListener() {
        return new CardOfferStepListener(cardOfferProcessor);
    }

    @Bean
    public Step cardOfferStep(JobRepository jobRepository) throws IOException {
        return new StepBuilder("cardOffer-step", jobRepository)
                .<CardOffer, CardOffer>chunk(10000)
                .reader(reader())
                .processor(cardOfferProcessor)
                .writer(cardOfferWriter)
                .transactionManager(transactionManager)
                .listener(cardOfferStepListener())
                .build();
    }

    @Bean
    public Step loanStep(JobRepository jobRepository) throws IOException {
        return new StepBuilder("loan-step", jobRepository)
                .<Loan, Loan>chunk(10000)
                .reader(loanReader())
                .processor(loanProcessor)
                .writer(loanWriter)
                .transactionManager(transactionManager)
                .build();
    }

    @Bean
    public Job loanJob(JobRepository jobRepository, Step loanStep) {
        return new JobBuilder("LoanJob", jobRepository)
                .start(loanStep)
                .build();
    }


    @Bean
    public Job cardOfferJob(JobRepository jobRepository, Step cardOfferStep) {
        return new JobBuilder("CardOfferJob", jobRepository)
                .start(cardOfferStep)
                .build();
    }
}
