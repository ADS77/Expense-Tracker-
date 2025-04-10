package com.ad.dena_paona.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse implements Serializable {
    private String userName;
    private String accessToken;
    private String refreshToken;

    public AuthResponse(String userName, String accessToken) {
        this.userName = userName;
        this.accessToken = accessToken;
    }

    public AuthResponse(String userName, String accessToken, String refreshToken) {
        this.userName = userName;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}

