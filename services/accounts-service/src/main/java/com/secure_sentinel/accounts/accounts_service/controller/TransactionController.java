package com.secure_sentinel.accounts.accounts_service.controller;

import com.secure_sentinel.accounts.accounts_service.dto.DepositFundsDTO;
import com.secure_sentinel.accounts.accounts_service.dto.PaymentDTO;
import com.secure_sentinel.accounts.accounts_service.dto.TransactionDTO;
import com.secure_sentinel.accounts.accounts_service.dto.TransferFundsDTO;
import com.secure_sentinel.accounts.accounts_service.exceptions.ApiError;
import com.secure_sentinel.accounts.accounts_service.model.enums.TransactionType;
import com.secure_sentinel.accounts.accounts_service.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@AllArgsConstructor
public class TransactionController extends BaseController {
    private final TransactionService transactionService;

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/by-account/{accountId}")
    @Operation(summary = "Get transactions for an account", description = "Retrieve transactions for a specific " +
            "account ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully", content =
            @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "Account not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<CustomResponse<Page<TransactionDTO>>> getTransactionsForAccount(
            @PathVariable Integer accountId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateTime,desc") String sortBy,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) String transactionTypeString) {

        TransactionType transactionType = null;
        if (transactionTypeString != null && !transactionTypeString.isEmpty()) {
            transactionType = TransactionType.valueOf(transactionTypeString.toUpperCase());
        }

        Page<TransactionDTO> transactions = transactionService.getTransactionsForAccount(accountId, page, size,
                sortBy, startDate, endDate, minAmount, maxAmount, transactionType);
        return respondWith(transactions, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    @Operation(summary = "Get all transactions", description = "Filter sort transactions in datasource")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully", content =
            @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "404", description = "No transactions found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<CustomResponse<Page<TransactionDTO>>> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateTime,desc") String sortBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) String transactionTypeStr,
            @RequestParam(required = false) String lastFour) {

        TransactionType transactionType = null;
        if (transactionTypeStr != null && !transactionTypeStr.isEmpty()) {
            transactionType = TransactionType.valueOf(transactionTypeStr.toUpperCase());
        }

        Page<TransactionDTO> transactions = transactionService.getAllTransactions(page, size,
                sortBy, startDate, endDate, minAmount, maxAmount, transactionType, lastFour);
        return respondWith(transactions, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/by-account/{accountId}/all")
    public ResponseEntity<CustomResponse<List<TransactionDTO>>> getAllTransactionsForAccount(@PathVariable Integer accountId) {
        List<TransactionDTO> transactions = transactionService.getAllTransactionsForAccount(accountId);
        return respondWith(transactions, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @PostMapping("/payment")
    public ResponseEntity<?> makePayment(@Valid @RequestBody PaymentDTO requestDTO) {
        transactionService.makePayment(requestDTO);
        return respondWithNoContent();
    }


    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @PostMapping("/deposit")
    public ResponseEntity<?> depositFunds(@Valid @RequestBody DepositFundsDTO requestDTO) {
        transactionService.depositFunds(requestDTO);
        return respondWithNoContent();
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @PostMapping("/transfer")
    @Operation(summary = "Create a new transfer transaction", description = "Create a new transfer transaction")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transfer successful", content =
            @Content(schema = @Schema(implementation = CustomResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Validation error", content =
            @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<?> transferFunds(@Valid @RequestBody TransferFundsDTO requestDTO) {
        transactionService.transferFunds(requestDTO);
        return respondWithNoContent();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reverse/{transactionId}")
    @Operation(summary = "Reverse a transaction", description = "Reverse a specific transaction by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaction reversed successfully", content =
            @Content(schema = @Schema(implementation = TransactionDTO.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<?> reverseTransaction(@PathVariable Integer transactionId) {
        transactionService.reverseTransaction(transactionId);
        return respondWithNoContent();
    }

    //    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/download")
    @Operation(summary = "Download users transactions", description = "Download users transactions")
    public ResponseEntity<byte[]> downloadTransactions(@RequestParam Integer accountId,
                                                       @RequestParam String format,
                                                       @RequestParam(required = false) @DateTimeFormat(iso =
                                                               DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
                                                       @RequestParam(required = false) @DateTimeFormat(iso =
                                                               DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) throws IOException {

        byte[] data = transactionService.exportTransactions(accountId, startDate, endDate, format);
        String mimeType = switch (format.toLowerCase()) {
            case "csv" -> "text/csv";
            case "pdf" -> "application/pdf";
            case "xlsx" -> "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions." + format)
                .contentType(MediaType.parseMediaType(mimeType))
                .body(data);
    }
}