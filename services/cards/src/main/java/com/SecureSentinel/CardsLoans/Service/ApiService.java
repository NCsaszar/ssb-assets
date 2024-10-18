package com.SecureSentinel.CardsLoans.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class ApiService {

    private final String apiKey = "demo";

    @Value("${gateway.url}")
    private String gatewayUrl;

    public JsonNode getTopGainersAndLosers() {
        String apiUrl = "https://www.alphavantage.co/query?function=TOP_GAINERS_LOSERS&apikey=" + apiKey;
        return callApi(apiUrl);
    }

    public JsonNode getCompanyInfo(String symbol) {
        String apiUrl = "https://www.alphavantage.co/query?function=OVERVIEW&symbol=" + symbol + "&apikey=" + apiKey;
        return callApi(apiUrl);
    }

    public JsonNode getStockQuote(String symbol) {
        String apiUrl = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;
        return callApi(apiUrl);
    }

    public JsonNode getMonthlyInfo(String symbol) {
        String apiUrl = "https://www.alphavantage.co/query?function=TIME_SERIES_MONTHLY&symbol=" + symbol + "&apikey=" + apiKey;
        return callApi(apiUrl);
    }

    public void depositMoney(Integer accountId, Double amount) {
        String apiUrl = "http://" + gatewayUrl +"/api/v1/transactions/deposit";

        String requestBody = "{ \"accountId\": " + accountId +", \"amount\": " + amount + " }";

        postApi(apiUrl, requestBody);
    }

    public void transferMoney(Integer accountId, Double amount) {
        String apiUrl = "http://" + gatewayUrl +"/api/v1/transactions/transfer";

        String requestBody = "{ \"sourceAccountId\": "+ accountId + ", \"targetAccountId\": 1, \"amount\": " + amount + " }";

        postApi(apiUrl, requestBody);
    }

    public JsonNode getUserInfo(Integer userId){
        String apiUrl = "http://" + gatewayUrl +"/api/v1/users/" + userId;

        return callUser(apiUrl);
    }

    public boolean checkActive(Integer userId){
        JsonNode jsonNode = getUserInfo(userId);
        return jsonNode.get("isActive").asBoolean();
    }


    private JsonNode callUser(String apiUrl) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = null;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.setRequestProperty("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJ1c2VySWQiOjY1Mywic3ViIjoidXNlcjEiLCJpYXQiOjE3MDU1OTM3NjQsImV4cCI6MTkwNTYyOTc2NH0.oiDGzkVNL_ja3Xog9CBl3FFZMo8SpM9EZnvTfZF9c0k");

            conn.setRequestProperty("Content-Type", "application/json");

            conn.setDoOutput(true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            jsonResponse = objectMapper.readTree(reader);

            reader.close();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

    private JsonNode callApi(String apiUrl) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = null;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            conn.setRequestProperty("Content-Type", "application/json");

            conn.setDoOutput(true);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            jsonResponse = objectMapper.readTree(reader);

            reader.close();
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

    private JsonNode postApi(String apiUrl, String requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonResponse = null;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiQURNSU4iLCJ1c2VySWQiOjY1Mywic3ViIjoidXNlcjEiLCJpYXQiOjE3MDU1OTM3NjQsImV4cCI6MTkwNTYyOTc2NH0.oiDGzkVNL_ja3Xog9CBl3FFZMo8SpM9EZnvTfZF9c0k");

            conn.setRequestProperty("Content-Type", "application/json");

            conn.setDoOutput(true);

            try (OutputStream outputStream = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                outputStream.write(input, 0, input.length);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                jsonResponse = objectMapper.readTree(reader);
            }

            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

}