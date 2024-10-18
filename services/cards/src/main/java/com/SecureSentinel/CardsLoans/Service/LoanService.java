package com.SecureSentinel.CardsLoans.Service;

import com.SecureSentinel.CardsLoans.DTO.*;
import com.SecureSentinel.CardsLoans.Mapper.LoanMapper;
import com.SecureSentinel.CardsLoans.Model.Loan;
import com.SecureSentinel.CardsLoans.Model.UserLoan;
import com.SecureSentinel.CardsLoans.Repository.LoanRepository;
import com.SecureSentinel.CardsLoans.Repository.UserLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LoanService {
    private final LoanRepository loanRepository;
    private final UserLoanRepository userLoanRepository;
    private final LoanMapper loanMapper;
    private final EmailService emailService;


    @Autowired
    public LoanService(LoanRepository loanRepository, UserLoanRepository userLoanRepository, LoanMapper loanMapper, EmailService emailService) {
        this.loanRepository = loanRepository;
        this.userLoanRepository = userLoanRepository;
        this.loanMapper = loanMapper;
        this.emailService = emailService;
    }

    public ViewUserLoanDTO createUserLoan(CreateUserLoanDTO createUserLoanDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            int userId = Integer.parseInt(authentication.getName());
            Loan loan = loanRepository.getById(createUserLoanDTO.getLoanID());

            if (createUserLoanDTO.getLoanAmount() >= loan.getMinAmount() &&
                    createUserLoanDTO.getLoanAmount() <= loan.getMaxAmount()) {
                UserLoan userLoan = loanMapper.createUserLoanFromDTO(createUserLoanDTO);
                userLoan.setLoan(loan);
                userLoan.setUserID(userId);
                userLoanRepository.save(userLoan);
                emailService.sendLoanConfirmationEmail(userId);
                return loanMapper.userLoanToView(userLoan);
            }
        } catch (Exception e) {
            throw new RuntimeException("Please enter a valid loan amount!", e);
        }
        return null;
    }

    public Loan createLoan(Loan loan) {
        loanRepository.save(loan);
        return loan;
    }


    public List<ViewLoanDTO> viewLoans(String loanTypeName, Integer loanID) {
        try {
            List<Loan> loans = loanRepository.findLoanByLoanTypeNameOOrLoanID(loanTypeName, loanID);
            String userRole = getUserRole();

            return loans.stream()
                    .filter(loan -> userRole.equals("ROLE_CUSTOMER") ? loan.isLoanStatus() : true)
                    .map(loanMapper::loanToView)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve loans", e);
        }
    }

    private String getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().isEmpty() ? "" : authentication.getAuthorities().iterator().next().getAuthority();
    }


    public String approveUserLoan(int userLoanID) {
        try {
            Optional<UserLoan> optionalUserLoan = userLoanRepository.findById(userLoanID);
            if (optionalUserLoan.isPresent()) {
                UserLoan userLoan = optionalUserLoan.get();
                userLoan.setLoanStatus(true);
                userLoan.setLoanStartDate(new Date());
                userLoanRepository.save(userLoan);
                emailService.sendLoanApprovalEmail(userLoan.getUserID());
                return "Loan Approved!";
            } else {
                throw new IllegalArgumentException("User loan with ID " + userLoanID + " not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to approve user loan", e);
        }
    }

    public String deleteUserLoan(int userLoanID) {
        try {
            Optional<UserLoan> optionalUserLoan = userLoanRepository.findById(userLoanID);
            if (optionalUserLoan.isPresent()) {
                UserLoan userLoan = optionalUserLoan.get();
                userLoan.setLoanStatus(false);
                userLoan.setLoanEndDate(new Date());
                userLoanRepository.save(userLoan);
                return "User Loan deleted!";
            } else {
                throw new IllegalArgumentException("User loan with ID " + userLoanID + " not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete user loan", e);
        }
    }

    public ViewUserLoanDTO getUserLoanInfo(int userLoanID) {
        try {
            Optional<UserLoan> optionalUserLoan = userLoanRepository.findById(userLoanID);
            if (optionalUserLoan.isPresent()) {
                UserLoan userLoan = optionalUserLoan.get();
                return loanMapper.userLoanToView(userLoan);
            } else {
                throw new IllegalArgumentException("User loan with ID " + userLoanID + " not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user loan info", e);
        }
    }

    public void deleteLoan(int loanID) {
        try {
            Optional<Loan> optionalLoan = loanRepository.findById(loanID);
            if (optionalLoan.isPresent()) {
                loanRepository.delete(optionalLoan.get());
            } else {
                throw new IllegalArgumentException("Loan with ID " + loanID + " not found");
            }
        } catch (Exception e) {
            // Log the error or throw a custom exception
            throw new RuntimeException("Failed to delete loan", e);
        }
    }

    public List<ViewUserLoanDTO> loansToApprove() {
        try {
            List<UserLoan> allLoans = userLoanRepository.findAll();
            return allLoans.stream()
                    .filter(userLoan -> !userLoan.isLoanStatus() && userLoan.getLoanStartDate() == null)
                    .map(loanMapper::userLoanToView)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve loans for approval", e);
        }
    }


    public String updateLoan(UpdateLoanDTO updateLoanDTO, int loanID) {
        try {
            Optional<Loan> optionalLoan = loanRepository.findById(loanID);
            if (optionalLoan.isPresent()) {
                Loan loan = optionalLoan.get();
                loanMapper.updateLoanFromDTO(updateLoanDTO, loan);
                loanRepository.save(loan);
                return "Loan updated!";
            } else {
                throw new IllegalArgumentException("Loan with ID " + loanID + " not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to update loan", e);
        }
    }

    public List<UserLoan> filterUserLoans(Integer userLoanID, Integer userID, Integer accountID){

        List<UserLoan> test = userLoanRepository.findUserLoansByUserLoanIDAndUserIDAndAccountID(userLoanID,accountID,userID);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userRole = authentication.getAuthorities().isEmpty() ? "" : authentication.getAuthorities().iterator().next().getAuthority();



        return  test;
    }


}
