package com.management.building.service.tuya;

import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;

import com.management.building.dto.response.tuyaCloud.TuyaReponse;
import com.management.building.dto.response.tuyaCloud.auth.TuyaTokenResult;

public interface TuyaApiClient {
    TuyaReponse<TuyaTokenResult> getAccessToken();

    TuyaReponse<TuyaTokenResult> refreshAccessToken(String refreshToken);

    <T> TuyaReponse<T> get(
            String url,
            ParameterizedTypeReference<TuyaReponse<T>> responseType);

    <T> TuyaReponse<T> get(
            String url,
            ParameterizedTypeReference<TuyaReponse<T>> responseType,
            Map<String, String> queryParams);

    <T> TuyaReponse<T> post(
            String url,
            Object requestBody,
            ParameterizedTypeReference<TuyaReponse<T>> responseType);

    <T> TuyaReponse<T> put(
            String url,
            Object requestBody,
            ParameterizedTypeReference<TuyaReponse<T>> responseType);

    <T> TuyaReponse<T> delete(
            String url,
            ParameterizedTypeReference<TuyaReponse<T>> responseType);
}
