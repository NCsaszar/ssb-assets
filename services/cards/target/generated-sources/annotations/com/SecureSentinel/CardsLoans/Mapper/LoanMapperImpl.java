package com.SecureSentinel.CardsLoans.Mapper;

import com.SecureSentinel.CardsLoans.DTO.CreateLoanDTO;
import com.SecureSentinel.CardsLoans.DTO.CreateUserLoanDTO;
import com.SecureSentinel.CardsLoans.DTO.UpdateLoanDTO;
import com.SecureSentinel.CardsLoans.DTO.ViewLoanDTO;
import com.SecureSentinel.CardsLoans.DTO.ViewUserLoanDTO;
import com.SecureSentinel.CardsLoans.Model.Loan;
import com.SecureSentinel.CardsLoans.Model.LoanType;
import com.SecureSentinel.CardsLoans.Model.UserLoan;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-06-09T22:44:31-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.2 (Private Build)"
)
@Component
public class LoanMapperImpl implements LoanMapper {

    @Override
    public ViewLoanDTO loanToView(Loan source) {
        if ( source == null ) {
            return null;
        }

        ViewLoanDTO viewLoanDTO = new ViewLoanDTO();

        viewLoanDTO.setLoanType( sourceLoanTypeLoanTypeName( source ) );
        viewLoanDTO.setLoanStatus( source.isLoanStatus() );
        viewLoanDTO.setLoanID( source.getLoanID() );
        viewLoanDTO.setAnnualPercentageRate( source.getAnnualPercentageRate() );
        viewLoanDTO.setTermMonths( source.getTermMonths() );
        viewLoanDTO.setMaxAmount( source.getMaxAmount() );
        viewLoanDTO.setMinAmount( source.getMinAmount() );

        return viewLoanDTO;
    }

    @Override
    public ViewUserLoanDTO userLoanToView(UserLoan source) {
        if ( source == null ) {
            return null;
        }

        ViewUserLoanDTO viewUserLoanDTO = new ViewUserLoanDTO();

        viewUserLoanDTO.setLoanID( sourceLoanLoanID( source ) );
        viewUserLoanDTO.setUserLoanID( source.getUserLoanID() );
        viewUserLoanDTO.setUserID( source.getUserID() );
        viewUserLoanDTO.setAccountID( source.getAccountID() );
        viewUserLoanDTO.setLoanAmount( source.getLoanAmount() );
        viewUserLoanDTO.setLoanStatus( source.isLoanStatus() );
        viewUserLoanDTO.setLoanStartDate( source.getLoanStartDate() );
        viewUserLoanDTO.setLoanEndDate( source.getLoanEndDate() );

        return viewUserLoanDTO;
    }

    @Override
    public UserLoan createUserLoanFromDTO(CreateUserLoanDTO createUserLoanDTO) {
        if ( createUserLoanDTO == null ) {
            return null;
        }

        UserLoan userLoan = new UserLoan();

        userLoan.setUserID( createUserLoanDTO.getUserID() );
        userLoan.setAccountID( createUserLoanDTO.getAccountID() );
        userLoan.setLoanAmount( createUserLoanDTO.getLoanAmount() );

        return userLoan;
    }

    @Override
    public Loan updateLoanFromDTO(UpdateLoanDTO source, Loan loan) {
        if ( source == null ) {
            return loan;
        }

        loan.setLoanStatus( source.isLoanStatus() );
        loan.setAnnualPercentageRate( source.getAnnualPercentageRate() );
        loan.setTermMonths( source.getTermMonths() );
        loan.setMaxAmount( source.getMaxAmount() );
        loan.setMinAmount( source.getMinAmount() );

        return loan;
    }

    @Override
    public Loan createLoanFromDTO(CreateLoanDTO source) {
        if ( source == null ) {
            return null;
        }

        Loan loan = new Loan();

        loan.setAnnualPercentageRate( source.getAnnualPercentageRate() );
        loan.setTermMonths( source.getTermMonths() );
        loan.setMaxAmount( source.getMaxAmount() );
        loan.setMinAmount( source.getMinAmount() );

        return loan;
    }

    private String sourceLoanTypeLoanTypeName(Loan loan) {
        if ( loan == null ) {
            return null;
        }
        LoanType loanType = loan.getLoanType();
        if ( loanType == null ) {
            return null;
        }
        String loanTypeName = loanType.getLoanTypeName();
        if ( loanTypeName == null ) {
            return null;
        }
        return loanTypeName;
    }

    private int sourceLoanLoanID(UserLoan userLoan) {
        if ( userLoan == null ) {
            return 0;
        }
        Loan loan = userLoan.getLoan();
        if ( loan == null ) {
            return 0;
        }
        int loanID = loan.getLoanID();
        return loanID;
    }
}
