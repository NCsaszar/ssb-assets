package com.secure_sentinel.accounts.accounts_service.utils;

import java.util.Random;

public class AccountUtils {
    public static String generateAccountNumber(Integer userId) {
        String prefix = "9384";
        long timestamp = System.currentTimeMillis();
        Integer randomPart = new Random().nextInt(999999999);

        return prefix + userId + timestamp + String.format("%09d", randomPart);
    }

    public static String maskAccountNumber(String accountNumber) {
        return "XXXX-XXXX-XXXX-" + accountNumber.substring(accountNumber.length() - 4);
    }
}