package com.secure_sentinel.accounts.accounts_service.controller;

import com.secure_sentinel.accounts.accounts_service.dto.AccountCreationRequestDTO;
import com.secure_sentinel.accounts.accounts_service.dto.AccountDTO;
import com.secure_sentinel.accounts.accounts_service.dto.AccountWIthTransactions;
import com.secure_sentinel.accounts.accounts_service.dto.DetailedAccountDTO;
import com.secure_sentinel.accounts.accounts_service.exceptions.ApiError;
import com.secure_sentinel.accounts.accounts_service.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@AllArgsConstructor
public class AccountController extends BaseController {
    private final AccountService accountService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    @Operation(summary = "Get all bank accounts", description = "Retrieve bank accounts with filtering and sorting " +
            "options")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully", content =
            @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "No accounts found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<CustomResponse<Page<AccountDTO>>> getAllAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String sortBy,
            @RequestParam(required = false) String accountType,
            @RequestParam(required = false) Double minBalance,
            @RequestParam(required = false) Double maxBalance,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        Page<AccountDTO> accounts = accountService.getAllAccounts(page, size, sortBy, accountType,
                minBalance, maxBalance, isActive,
                startDate, endDate);
        return respondWith(accounts, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Get account by account number", description = "Retrieve account details by account number")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/by-account/{accountNumber}")
    public ResponseEntity<CustomResponse<AccountDTO>> getAccountByNumber(@PathVariable String accountNumber) {
        AccountDTO accountDTO = accountService.getAccountByNumber(accountNumber);
        return respondWith(accountDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Retrieve detailed account information by account id", description = "Retrieve detailed " +
            "account information by account id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account found"),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    @GetMapping("/details/{accountId}")
    public ResponseEntity<CustomResponse<DetailedAccountDTO>> getAccountDetails(@PathVariable Integer accountId) {
        DetailedAccountDTO detailedAccountDTO = accountService.getAccountDetails(accountId);
        return respondWith(detailedAccountDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Get accounts by user ID", description = "Retrieve a list of accounts for a given user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully", content =
            @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema =
            @Schema(implementation = CustomResponse.class)))
    })
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<CustomResponse<List<AccountDTO>>> getAccountByUserId(@PathVariable Integer userId,
                                                                               @RequestParam(name
                                                                                       = "active", required = false) Boolean active) {
        List<AccountDTO> accountDTOS;
        if (Boolean.TRUE.equals(active)) {
            accountDTOS = accountService.getActiveAccountsByUserId(userId);
        } else {
            accountDTOS = accountService.getAccountsByUserId(userId);
        }
        return respondWith(accountDTOS, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Create a new account", description = "Create a new account with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Account created successfully", content =
            @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Validation error", content =
            @Content(schema = @Schema(implementation = ApiError.class)))
    })
    @PostMapping
    public ResponseEntity<CustomResponse<AccountDTO>> createAccount(@Valid @RequestBody AccountCreationRequestDTO request) {
        AccountDTO createdAccount = accountService.createAccount(request);
        return respondWith(createdAccount, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Deactivate an account by ID", description = "Deactivate an account using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    @PatchMapping("/deactivate/by-account/{accountId}")
    public ResponseEntity<?> deactivateAccount(@PathVariable Integer accountId) {
        accountService.deactivateAccount(accountId);
        return respondWithNoContent();
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Activate an account by ID", description = "Activate an account using its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account activated successfully"),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    @PatchMapping("/activate/by-account/{accountId}")
    public ResponseEntity<?> activateAccount(@PathVariable Integer accountId) {
        accountService.activateAccount(accountId);
        return respondWithNoContent();
    }

    @PostMapping("/upload")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @Operation(summary = "Create bank accounts from a CSV/XLSX file", description = "Create bank accounts from a " +
            "CSV/XLSX file")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        String contentType = file.getContentType();
        if (file.getSize() > 50 * 1024 * 1024) { // 50 MB limit
            System.out.println("File Too large");
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File size exceeds the maximum limit of " +
                    "50 MB");
        }
        if (!Arrays.asList("application/vnd.ms-excel", "text/csv").contains(contentType)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type");
        }
        try {
            accountService.processFile(file);
            return ResponseEntity.ok("Accounts created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing file");
        }
    }

    @GetMapping("/all-for-batch")
    @Operation(summary = "Get all bank accounts for batch processing", description = "Retrieve all bank accounts " +
            "without pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Accounts retrieved successfully", content =
            @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "No accounts found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<CustomResponse<List<AccountDTO>>> getAllAccountsForBatch() {
        List<AccountDTO> accounts = accountService.getAllAccountsForBatch();
        return respondWith(accounts, HttpStatus.OK);
    }

    @GetMapping("/all-with-transactions")
    public ResponseEntity<List<AccountWIthTransactions>> getAllAccountsWithTransactions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        List<AccountWIthTransactions> accountsWithTransactions =
                accountService.getAllAccountsWithTransactions(startDate, endDate);
        return ResponseEntity.ok(accountsWithTransactions);
    }
}