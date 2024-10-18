package com.SecureSentinel.CardsLoans.Batch.FieldSetMapper;

import com.SecureSentinel.CardsLoans.Model.Loan;
import com.SecureSentinel.CardsLoans.Model.LoanType;
import com.SecureSentinel.CardsLoans.Repository.LoanTypeRepository;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class CustomLoanFieldSetMapper implements FieldSetMapper<Loan> {

    @Autowired
    private LoanTypeRepository loanTypeRepository;

    @Override
    public Loan mapFieldSet(FieldSet fieldSet) {
        Loan loan = new Loan();
        loan.setLoanID(fieldSet.readInt("loanID"));
        loan.setAnnualPercentageRate(fieldSet.readDouble("annualPercentageRate"));
        loan.setTermMonths(fieldSet.readInt("termMonths"));
        loan.setLoanStatus(Boolean.parseBoolean(fieldSet.readString("loanStatus")));
        loan.setMaxAmount(fieldSet.readInt("maxAmount"));
        loan.setMinAmount(fieldSet.readInt("minAmount"));

        int loanTypeId = fieldSet.readInt("loan_type_id");
        LoanType loanType = loanTypeRepository.findById(loanTypeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid loan_type_id: " + loanTypeId));
        loan.setLoanType(loanType);

        return loan;
    }
}