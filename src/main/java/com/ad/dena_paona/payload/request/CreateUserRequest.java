package com.ad.dena_paona.payload.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String userName;
    private String email;
    private String password;
    private String contactNumber;
}
