package com.ad.dena_paona.payload.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateUserRequest implements Serializable {
    @NonNull
    private String userName;
    @NonNull
    private String email;
    @NonNull
    private String password;
    @NonNull
    private String contactNumber;
}
