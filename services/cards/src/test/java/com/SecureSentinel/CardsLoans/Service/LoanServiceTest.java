package com.SecureSentinel.CardsLoans.Service;

import com.SecureSentinel.CardsLoans.DTO.CreateUserLoanDTO;
import com.SecureSentinel.CardsLoans.DTO.UpdateLoanDTO;
import com.SecureSentinel.CardsLoans.DTO.ViewLoanDTO;
import com.SecureSentinel.CardsLoans.DTO.ViewUserLoanDTO;
import com.SecureSentinel.CardsLoans.Mapper.LoanMapper;
import com.SecureSentinel.CardsLoans.Model.Loan;
import com.SecureSentinel.CardsLoans.Model.UserLoan;
import com.SecureSentinel.CardsLoans.Repository.LoanRepository;
import com.SecureSentinel.CardsLoans.Repository.UserLoanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoanServiceTest {
    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserLoanRepository userLoanRepository;

    @Mock
    private LoanMapper loanMapper;

    @Mock
    private EmailService emailService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void createUserLoan() {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(authentication.getName()).thenReturn("123");

        CreateUserLoanDTO createUserLoanDTO = new CreateUserLoanDTO();
        createUserLoanDTO.setLoanID(1);
        createUserLoanDTO.setLoanAmount(5000);

        Loan loan = new Loan();
        loan.setLoanID(1);
        loan.setMinAmount(1000);
        loan.setMaxAmount(10000);

        UserLoan userLoan = new UserLoan();
        userLoan.setLoan(loan);
        userLoan.setUserID(123);
        userLoan.setLoanAmount(5000);

        when(loanRepository.getById(createUserLoanDTO.getLoanID())).thenReturn(loan);
        when(loanMapper.createUserLoanFromDTO(createUserLoanDTO)).thenReturn(userLoan);
        when(userLoanRepository.save(any(UserLoan.class))).thenReturn(userLoan);

        ViewUserLoanDTO result = loanService.createUserLoan(createUserLoanDTO);

        verify(userLoanRepository).save(userLoan);
        verify(emailService).sendLoanConfirmationEmail(123);
        assertNotNull(result);
    }

    @Test
    void createLoan() {
        Loan loan = new Loan();
        loan.setLoanID(1);
        loan.setMinAmount(1000);
        loan.setMaxAmount(5000);

        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        Loan result = loanService.createLoan(loan);

        verify(loanRepository).save(loan);
        assertEquals(loan, result);
    }

    @Test
    void viewLoans() {
        String loanTypeName = "Personal Loan";
        Integer loanID = 1;

        Loan loan = new Loan();
        loan.setLoanID(loanID);
        loan.setLoanStatus(true);

        ViewLoanDTO viewLoanDTO = new ViewLoanDTO();

        List<Loan> loans = new ArrayList<>();
        loans.add(loan);

        List<ViewLoanDTO> expectedViewLoans = new ArrayList<>();
        expectedViewLoans.add(viewLoanDTO);

        when(loanRepository.findLoanByLoanTypeNameOOrLoanID(loanTypeName, loanID)).thenReturn(loans);
        when(loanMapper.loanToView(loan)).thenReturn(viewLoanDTO);

        List<ViewLoanDTO> result = loanService.viewLoans(loanTypeName, loanID);

        verify(loanRepository).findLoanByLoanTypeNameOOrLoanID(loanTypeName, loanID);
        verify(loanMapper).loanToView(loan);
        assertEquals(expectedViewLoans, result);
    }

    @Test
    void approveUserLoan() {
        int userLoanID = 1;
        UserLoan userLoan = new UserLoan();
        userLoan.setUserLoanID(userLoanID);
        userLoan.setUserID(123);
        userLoan.setLoanStatus(false);

        when(userLoanRepository.findById(userLoanID)).thenReturn(Optional.of(userLoan));
        when(userLoanRepository.save(userLoan)).thenReturn(userLoan);

        String result = loanService.approveUserLoan(userLoanID);

        assertEquals("Loan Approved!", result);
        assertTrue(userLoan.isLoanStatus());
        assertNotNull(userLoan.getLoanStartDate());

        verify(userLoanRepository).findById(userLoanID);
        verify(userLoanRepository).save(userLoan);
        verify(emailService).sendLoanApprovalEmail(userLoan.getUserID());
    }

    @Test
    void deleteUserLoan() {
        int userLoanID = 1;
        UserLoan userLoan = new UserLoan();
        userLoan.setUserLoanID(userLoanID);
        userLoan.setLoanStatus(true);

        when(userLoanRepository.findById(userLoanID)).thenReturn(Optional.of(userLoan));
        when(userLoanRepository.save(userLoan)).thenReturn(userLoan);

        String result = loanService.deleteUserLoan(userLoanID);

        assertEquals("User Loan deleted!", result);
        assertFalse(userLoan.isLoanStatus());
        assertNotNull(userLoan.getLoanEndDate());

        verify(userLoanRepository).findById(userLoanID);
        verify(userLoanRepository).save(userLoan);
    }

    @Test
    void getUserLoanInfo() {
        int userLoanID = 1;
        UserLoan userLoan = new UserLoan();
        userLoan.setUserLoanID(userLoanID);

        ViewUserLoanDTO viewUserLoanDTO = new ViewUserLoanDTO();

        when(userLoanRepository.findById(userLoanID)).thenReturn(Optional.of(userLoan));
        when(loanMapper.userLoanToView(userLoan)).thenReturn(viewUserLoanDTO);

        ViewUserLoanDTO result = loanService.getUserLoanInfo(userLoanID);

        assertEquals(viewUserLoanDTO, result);

        verify(userLoanRepository).findById(userLoanID);
        verify(loanMapper).userLoanToView(userLoan);
    }

    @Test
    void deleteLoan() {
        int loanID = 1;
        Loan loan = new Loan();

        when(loanRepository.findById(loanID)).thenReturn(Optional.of(loan));

        loanService.deleteLoan(loanID);

        verify(loanRepository).findById(loanID);
        verify(loanRepository).delete(loan);
    }

    @Test
    void loansToApprove() {
        UserLoan loan1 = new UserLoan();
        loan1.setLoanStatus(false);
        loan1.setLoanStartDate(null);

        UserLoan loan2 = new UserLoan();
        loan2.setLoanStatus(false);
        loan2.setLoanStartDate(null);

        UserLoan loan3 = new UserLoan();
        loan3.setLoanStatus(true);
        loan3.setLoanStartDate(new Date());

        when(userLoanRepository.findAll()).thenReturn(Arrays.asList(loan1, loan2, loan3));

        ViewUserLoanDTO viewLoan1 = new ViewUserLoanDTO();
        ViewUserLoanDTO viewLoan2 = new ViewUserLoanDTO();

        when(loanMapper.userLoanToView(loan1)).thenReturn(viewLoan1);
        when(loanMapper.userLoanToView(loan2)).thenReturn(viewLoan2);

        List<ViewUserLoanDTO> result = loanService.loansToApprove();

        assertEquals(2, result.size());
        assertTrue(result.contains(viewLoan1));
        assertTrue(result.contains(viewLoan2));

        verify(userLoanRepository).findAll();
        verify(loanMapper).userLoanToView(loan1);
        verify(loanMapper).userLoanToView(loan2);
        verify(loanMapper, never()).userLoanToView(loan3);
    }

    @Test
    void updateLoan() {
        int loanID = 1;
        UpdateLoanDTO updateLoanDTO = new UpdateLoanDTO();
        Loan existingLoan = new Loan();

        when(loanRepository.findById(loanID)).thenReturn(Optional.of(existingLoan));
        when(loanMapper.updateLoanFromDTO(updateLoanDTO, existingLoan)).thenReturn(existingLoan); // Use thenReturn() for non-void methods
        when(loanRepository.save(existingLoan)).thenReturn(existingLoan);

        String result = loanService.updateLoan(updateLoanDTO, loanID);

        assertEquals("Loan updated!", result);
        verify(loanRepository).findById(loanID);
        verify(loanMapper).updateLoanFromDTO(updateLoanDTO, existingLoan);
        verify(loanRepository).save(existingLoan);

    }

    @Test
    void filterUserLoans() {
        Integer userLoanID = 1;
        Integer userID = 2;
        Integer accountID = 3;

        List<UserLoan> userLoans = new ArrayList<>();
        userLoans.add(new UserLoan());

        when(userLoanRepository.findUserLoansByUserLoanIDAndUserIDAndAccountID(userLoanID, accountID, userID)).thenReturn(userLoans);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        List<UserLoan> result = loanService.filterUserLoans(userLoanID, userID, accountID);

        assertEquals(userLoans, result);
        verify(userLoanRepository).findUserLoansByUserLoanIDAndUserIDAndAccountID(userLoanID, accountID, userID);
        verify(securityContext).getAuthentication();
        verify(authentication).getAuthorities();
    }
}