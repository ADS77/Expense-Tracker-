package com.ad.dena_paona.payload.response;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;
@Data
public class ApiResponse <T>{
    private List<T> data;
    private int count;
    private HttpStatus status;
    private String message;
}
