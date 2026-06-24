package com.api.vendas.ui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ApiClient {

    private static final String BASE_URL = "http://localhost:8080/api/v1";
    private String jwtToken;
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    public ApiClient() {
        this.client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public boolean login(String email, String senha) {
        try {
            Map<String, String> credentials = new HashMap<>();
            credentials.put("email", email);
            credentials.put("senha", senha);
            String json = objectMapper.writeValueAsString(credentials);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/auth/login"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                Map<String, String> result = objectMapper.readValue(response.body(), new TypeReference<Map<String, String>>(){});
                this.jwtToken = result.get("token");
                return true;
            } else {
                System.err.println("Login failed: " + response.statusCode() + " - " + response.body());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public <T> T get(String endpoint, TypeReference<T> responseType) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .GET();

        if (jwtToken != null) {
            requestBuilder.header("Authorization", "Bearer " + jwtToken);
        }

        HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return objectMapper.readValue(response.body(), responseType);
        } else {
            throw new RuntimeException("GET request failed with status " + response.statusCode() + ": " + response.body());
        }
    }

    public <T> T post(String endpoint, Object body, TypeReference<T> responseType) throws Exception {
        return sendWithBody(endpoint, body, responseType, "POST");
    }

    public <T> T put(String endpoint, Object body, TypeReference<T> responseType) throws Exception {
        return sendWithBody(endpoint, body, responseType, "PUT");
    }

    public void delete(String endpoint) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .DELETE();

        if (jwtToken != null) {
            requestBuilder.header("Authorization", "Bearer " + jwtToken);
        }

        HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 300) {
            throw new RuntimeException("DELETE request failed with status " + response.statusCode() + ": " + response.body());
        }
    }

    private <T> T sendWithBody(String endpoint, Object body, TypeReference<T> responseType, String method) throws Exception {
        String json = objectMapper.writeValueAsString(body);
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + endpoint))
                .header("Content-Type", "application/json");

        if (method.equals("POST")) {
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(json));
        } else if (method.equals("PUT")) {
            requestBuilder.PUT(HttpRequest.BodyPublishers.ofString(json));
        }

        if (jwtToken != null) {
            requestBuilder.header("Authorization", "Bearer " + jwtToken);
        }

        HttpResponse<String> response = client.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            if (responseType == null || response.body().isEmpty()) {
                return null;
            }
            return objectMapper.readValue(response.body(), responseType);
        } else {
            throw new RuntimeException(method + " request failed with status " + response.statusCode() + ": " + response.body());
        }
    }
}
