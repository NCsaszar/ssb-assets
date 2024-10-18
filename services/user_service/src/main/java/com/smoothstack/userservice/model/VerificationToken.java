package com.smoothstack.userservice.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class VerificationToken {

    private int tokenId;
    private int userId;
    private String token;
    private Timestamp expiration;
    private boolean isUsed;
    private Timestamp lastVerificationRequest;

    // Constructors
    public VerificationToken() {}

    public VerificationToken(int tokenId, int userId, String token, Timestamp expiration, boolean isUsed, Timestamp lastVerificationRequest) {
        this.tokenId = tokenId;
        this.userId = userId;
        this.token = token;
        this.expiration = expiration;
        this.isUsed = isUsed;
        this.lastVerificationRequest = lastVerificationRequest;
    }
}
