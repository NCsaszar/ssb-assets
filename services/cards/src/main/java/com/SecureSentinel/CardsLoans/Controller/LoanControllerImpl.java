package com.SecureSentinel.CardsLoans.Controller;

import com.SecureSentinel.CardsLoans.DTO.*;
import com.SecureSentinel.CardsLoans.Model.Loan;
import com.SecureSentinel.CardsLoans.Model.UserLoan;
import com.SecureSentinel.CardsLoans.Service.LoanService;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class LoanControllerImpl implements LoanController {

    private final LoanService loanService;

    public LoanControllerImpl(LoanService loanService) {
        this.loanService = loanService;
    }

    @Override
    public ResponseEntity<Loan> createLoan(Loan loan) {
        return new ResponseEntity<>(loanService.createLoan(loan),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ViewUserLoanDTO> createUserLoan(CreateUserLoanDTO createUserLoanDTO) {
        return new ResponseEntity<>(loanService.createUserLoan(createUserLoanDTO),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ViewLoanDTO>> viewLoans(Integer loanID, String loanTypeName) {
        return new ResponseEntity<>(loanService.viewLoans(loanTypeName,loanID),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<ViewUserLoanDTO>> userLoansToApprove() {
        return new ResponseEntity<>(loanService.loansToApprove(),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<UserLoan>> viewUserLoan(Integer userLoanID, Integer accountID, Integer userID) {

        return new ResponseEntity<>(loanService.filterUserLoans(userLoanID,accountID,userID),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> approveUserLoan(Integer userLoanID) {
        return new ResponseEntity<>(loanService.approveUserLoan(userLoanID),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> updateLoan(UpdateLoanDTO updateLoanDTO, Integer loanID) {

        return new ResponseEntity<>(loanService.updateLoan(updateLoanDTO,loanID),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteUserLoan(Integer userLoanID) {

        return new ResponseEntity<>(loanService.deleteUserLoan(userLoanID),HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ViewUserLoanDTO> userloanView(Integer userLoanID){
        return new ResponseEntity<>(loanService.getUserLoanInfo(userLoanID),HttpStatus.OK);
    }

}
