package com.smoothstack.userservice.util;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EncryptionUtil {

    private static String getSecret() {
        String secretName = "encryption-util-key";
        Region region = Region.of("us-east-1");

        SecretsManagerClient client = SecretsManagerClient.builder()
                .region(region)
                .build();

        GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

        GetSecretValueResponse getSecretValueResponse;

        try {
            getSecretValueResponse = client.getSecretValue(getSecretValueRequest);
            return getSecretValueResponse.secretString();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving secret: " + e.getMessage());
        }
    }

    private static SecretKeySpec getSecretKeySpec() {
        try {
            String secretJson = getSecret();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(secretJson);
            String secretKey = jsonNode.get("encryption-util-key").asText();
            byte[] keyBytes = secretKey.getBytes();
            return new SecretKeySpec(keyBytes, "AES");
        } catch (Exception e) {
            throw new RuntimeException("Error creating SecretKeySpec: " + e.getMessage());
        }
    }

    public static String encrypt(String data) throws Exception {
        SecretKeySpec secretKey = getSecretKeySpec();
        Cipher cipher = Cipher.getInstance("AES"); //Advanced Encryption Standard
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getUrlEncoder().encodeToString(encryptedData);
    }

    public static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec secretKey = getSecretKeySpec();
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedData = Base64.getUrlDecoder().decode(encryptedData);
        byte[] decryptedData = cipher.doFinal(decodedData);
        return new String(decryptedData);
    }
}
