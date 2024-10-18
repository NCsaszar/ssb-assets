package com.SecureSentinel.CardsLoans.Controller;

import com.SecureSentinel.CardsLoans.DTO.*;
import com.SecureSentinel.CardsLoans.Exceptions.ApiError;
import com.SecureSentinel.CardsLoans.Model.Loan;
import com.SecureSentinel.CardsLoans.Model.UserCard;
import com.SecureSentinel.CardsLoans.Model.UserLoan;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/loans")
public interface LoanController {
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/loan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Loan created successfully", content =
            @Content(schema = @Schema(implementation = ViewUserCardDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Validation error", content =
            @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan);
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @PostMapping("/user-loan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User Loan created successfully", content =
            @Content(schema = @Schema(implementation = ViewUserCardDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Validation error", content =
            @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ViewUserLoanDTO> createUserLoan(@RequestBody CreateUserLoanDTO createUserLoanDTO);
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @GetMapping("/loan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loans found"),
            @ApiResponse(responseCode = "404", description = "Loans not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<ViewLoanDTO>> viewLoans(
            @RequestParam(required = false) Integer loanID,
            @RequestParam(required = false) String loanTypeName);
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/approve")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Loans to Approve Found"),
            @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<ViewUserLoanDTO>> userLoansToApprove();
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @GetMapping("/user-loan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Loans found"),
            @ApiResponse(responseCode = "404", description = "User Loans not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<UserLoan>> viewUserLoan(@RequestParam(required = false) Integer userLoanID,
                                                        @RequestParam(required = false) Integer accountID,
                                                        @RequestParam(required = false) Integer userID);

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @GetMapping("/user-loan/view")
    public ResponseEntity<ViewUserLoanDTO> userloanView(@RequestParam Integer userLoanID);
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/approve")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "User Loan Approved successfully"),
            @ApiResponse(responseCode = "404", description = "User Loan not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<String> approveUserLoan(@RequestParam Integer userLoanID);
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/loan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Loan Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Loan not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<String> updateLoan(@RequestBody UpdateLoanDTO updateLoanDTO, @RequestParam Integer loanID);
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping ("/user-loan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "User Loan Deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User Loan not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<String> deleteUserLoan(@RequestParam Integer userLoanID);
}
