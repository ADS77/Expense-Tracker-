package com.ad.dena_paona.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
