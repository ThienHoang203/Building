package com.management.building.service.tuya;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.management.building.dto.response.tuyaCloud.TuyaReponse;
import com.management.building.dto.response.tuyaCloud.auth.TuyaTokenResult;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TuyaApiClientImplement implements TuyaApiClient {
    @NonFinal
    @Value("${tuya.client-id}")
    String clientId;
    @NonFinal
    @Value("${tuya.client-secret}")
    String clientSecret;
    @NonFinal
    @Value("${tuya.base-url}")
    String baseUrl;

    @NonFinal
    private String cachedAccessToken;
    @NonFinal
    private String cachedRefreshToken;
    @NonFinal
    private long tokenExpireTime;

    RestTemplate restTemplate;
    ObjectMapper objectMapper;

    @Override
    public TuyaReponse<TuyaTokenResult> getAccessToken() {
        String url = "/v1.0/token?grant_type=1";
        return executeRequest(
                HttpMethod.GET,
                url,
                new ParameterizedTypeReference<TuyaReponse<TuyaTokenResult>>() {
                },
                null,
                null);
    }

    @Override
    public TuyaReponse<TuyaTokenResult> refreshAccessToken(String refreshToken) {
        String url = "/v1.0/token/" + refreshToken;
        return executeRequest(
                HttpMethod.GET,
                url,
                new ParameterizedTypeReference<TuyaReponse<TuyaTokenResult>>() {
                },
                null,
                null);
    }

    @Override
    public <T> TuyaReponse<T> get(String url, ParameterizedTypeReference<TuyaReponse<T>> responseType) {
        return executeRequest(HttpMethod.GET, url, responseType, null, null);
    }

    @Override
    public <T> TuyaReponse<T> get(String url, ParameterizedTypeReference<TuyaReponse<T>> responseType,
            Map<String, String> queryParams) {
        String fullUrl = buildUrlWithParams(url, queryParams);
        return executeRequest(HttpMethod.GET, fullUrl, responseType, null, null);
    }

    @Override
    public <T> TuyaReponse<T> post(String url, Object requestBody,
            ParameterizedTypeReference<TuyaReponse<T>> responseType) {
        return executeRequest(HttpMethod.POST, url, responseType, requestBody, null);
    }

    @Override
    public <T> TuyaReponse<T> put(String url, Object requestBody,
            ParameterizedTypeReference<TuyaReponse<T>> responseType) {
        return executeRequest(HttpMethod.PUT, url, responseType, requestBody, null);
    }

    @Override
    public <T> TuyaReponse<T> delete(String url, ParameterizedTypeReference<TuyaReponse<T>> responseType) {
        return executeRequest(HttpMethod.DELETE, url, responseType, null, null);
    }

    private <T> TuyaReponse<T> executeRequest(
            HttpMethod method,
            String url,
            ParameterizedTypeReference<TuyaReponse<T>> responseType,
            Object requestBody,
            Map<String, String> customHeaders) {
        try {
            String timestamp = String.valueOf(System.currentTimeMillis());
            String nonce = generateNonce();
            String body = requestBody != null ? convertToJson(requestBody) : "";

            String signature = createSignature(method.name(), body, url, timestamp, nonce);

            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("client_id", clientId);
            headers.set("access_token", cachedAccessToken);
            headers.set("sign", signature);
            headers.set("sign_method", "HMAC-SHA256");
            headers.set("t", timestamp);
            headers.set("nonce", nonce);
            headers.set("Content-Type", "application/json");

            if (customHeaders != null) {
                customHeaders.forEach(headers::set);
            }

            // Tạo entity
            HttpEntity<?> entity = requestBody != null
                    ? new HttpEntity<>(requestBody, headers)
                    : new HttpEntity<>(headers);

            String fullUrl = baseUrl + url;
            log.debug("Calling Tuya API: {} {}", method, fullUrl);

            // Call API
            ResponseEntity<TuyaReponse<T>> response = restTemplate.exchange(
                    fullUrl,
                    method,
                    entity,
                    responseType);

            TuyaReponse<T> result = response.getBody();

            // Check if token expired
            if (result != null && !result.getSuccess() && isTokenExpiredError(result)) {
                log.info("Access token expired, refreshing...");
                refreshAccessToken();
                // Retry request với token mới
                return executeRequest(method, url, responseType, requestBody, customHeaders);
            }
            if (result != null && result.getSuccess()) {
                log.debug("API call successful: {}", url);
            } else {
                log.warn("API call failed: {}, Error: {}", url,
                        result != null ? result.getMsg() : "Unknown error");
            }
            return result;

        } catch (Exception e) {
            log.error("Error calling Tuya API: {} {}", method, url, e);
            throw new RuntimeException("Error calling Tuya API: " + url, e);
        }
    }

    private String buildUrlWithParams(String url, Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }

        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append("?");

        params.forEach((key, value) -> {
            if (value != null && !value.isEmpty()) {
                urlBuilder.append(key).append("=").append(value).append("&");
            }
        });

        // delete the last '&'
        if (urlBuilder.charAt(urlBuilder.length() - 1) == '&') {
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }

        return urlBuilder.toString();
    }

    private String generateNonce() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    private String convertToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to JSON", e);
        }
    }

    private String createSignature(String method, String body, String url, String timestamp, String nonce) {
        try {
            String contentHash = body.isEmpty()
                    ? "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855"
                    : sha256(body);

            // create signature by using HMAC-SHA256
            String stringToSign = method + "\n" + contentHash + "\n" + "\n" + url;
            String str = clientId + (cachedAccessToken != null ? cachedAccessToken
                    : "") + timestamp + nonce + stringToSign;
            log.debug("StringToSign: {}", stringToSign);
            return hmacSha256(str, clientSecret);
        } catch (Exception e) {
            log.error("Error creating signature", e);
            throw new RuntimeException("Error creating signature", e);
        }
    }

    private String hmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash).toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Error generating HMAC-SHA256", e);
        }
    }

    private String sha256(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    private boolean isTokenExpiredError(TuyaReponse<?> response) {
        return response.getCode() == 1010;
    }

    /**
    * Refresh access token
    */
    private void refreshAccessToken() {
        if (cachedRefreshToken == null) {
            log.warn("No refresh token available, fetching new access token");
            fetchNewAccessToken();
            return;
        }

        TuyaReponse<TuyaTokenResult> tokenResponse = refreshAccessToken(cachedRefreshToken);

        if (tokenResponse != null && tokenResponse.getSuccess()) {
            TuyaTokenResult tokenResult = tokenResponse.getResult();
            cachedAccessToken = tokenResult.getAccessToken();
            cachedRefreshToken = tokenResult.getRefreshToken();
            tokenExpireTime = System.currentTimeMillis() +
                    (tokenResult.getExpireTime() - 300) * 1000L;

            log.info("Access token refreshed successfully");
        } else {
            log.error("Failed to refresh token, fetching new token");
            fetchNewAccessToken();
        }
    }

    private void fetchNewAccessToken() {
        TuyaReponse<TuyaTokenResult> tokenResponse = getAccessToken();

        if (tokenResponse != null && tokenResponse.getSuccess()) {
            TuyaTokenResult tokenResult = tokenResponse.getResult();
            cachedAccessToken = tokenResult.getAccessToken();
            cachedRefreshToken = tokenResult.getRefreshToken();

            // Set thời gian hết hạn (trừ đi 5 phút để đảm bảo refresh trước khi hết hạn)
            tokenExpireTime = System.currentTimeMillis() +
                    (tokenResult.getExpireTime() - 300) * 1000L;

            log.info("New access token obtained, expires in {} seconds", tokenResult.getExpireTime());
        } else {
            throw new RuntimeException("Failed to obtain access token");
        }
    }
}
