package com.management.building.controller.tuya.auth;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.management.building.dto.response.tuyaCloud.TuyaReponse;
import com.management.building.dto.response.tuyaCloud.auth.TuyaTokenResult;
import com.management.building.service.tuya.TuyaApiClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@Validated
@RequestMapping("/auth2.0-tuya")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TuyaAuthController {
    TuyaApiClient apiClient;

    @GetMapping("/token")
    public TuyaReponse<TuyaTokenResult> getAccessToken() {
        var result = apiClient.getAccessToken();
        return result;
    }

    @GetMapping("/token/refresh")
    public TuyaReponse<TuyaTokenResult> getAccessToken(@RequestHeader("refresh_token") String refreshToken) {
        var result = apiClient.refreshAccessToken(refreshToken);
        return result;
    }
}
