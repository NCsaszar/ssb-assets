package com.SecureSentinel.CardsLoans.Controller;

import com.SecureSentinel.CardsLoans.DTO.*;
import com.SecureSentinel.CardsLoans.Exceptions.ApiError;
import com.SecureSentinel.CardsLoans.Model.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/cards")
public interface CardController {
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @PostMapping("/user-card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Card created successfully", content =
            @Content(schema = @Schema(implementation = ViewUserCardDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Validation error", content =
            @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<ViewUserCardDTO> createCard(@RequestBody CreateUserCardDTO cardDTO);

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @GetMapping("/user-card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cards found"),
            @ApiResponse(responseCode = "404", description = "Cards not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<ViewUserCardDTO>> searchUserCards(
            @RequestParam(required = false) Integer cardID,
            @RequestParam(required = false) Integer userID,
            @RequestParam(required = false) Integer accountID
    );
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/user-card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Card Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<String> updateCard(@RequestParam Integer cardID, @RequestBody UpdateUserCardDTO updatedCardDTO);
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @DeleteMapping("/user-card")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Card deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<String> deleteCard(@RequestParam Integer cardID);
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/activate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Card activated successfully"),
            @ApiResponse(responseCode = "404", description = "Card not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<String> activateCard(@RequestParam Integer cardID);

    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @GetMapping("/credit-offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credit Offers found"),
            @ApiResponse(responseCode = "404", description = "Credit Offers not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<List<CardOffer>> getCreditOffers(@RequestParam(required = false) Integer creditLimit);

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/credit-offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Credit Offer created successfully", content =
            @Content(schema = @Schema(implementation = ViewUserCardDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request - Validation error", content =
            @Content(schema = @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<CardOffer> createOffer(@RequestBody CardOffer cardOffer);

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/credit-offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Credit Offer Updated successfully"),
            @ApiResponse(responseCode = "404", description = "Credit Offer not found", content = @Content(schema =
            @Schema(implementation = ApiError.class)))
    })
    public ResponseEntity<CardOffer> updateOffer(@RequestParam Integer cardOfferId, @RequestBody CardOffer cardOffer);
}
