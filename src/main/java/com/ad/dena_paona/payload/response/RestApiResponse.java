package com.ad.dena_paona.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestApiResponse<T> implements Serializable {
    private String message;
    private HttpStatus status;
    private T data;
    private ErrorDetails error;

    public RestApiResponse() {
    }

    public RestApiResponse(HttpStatus status, T data) {
        this.status = status;
        this.data = data;
    }

    public RestApiResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public RestApiResponse(String message, HttpStatus status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }

    public RestApiResponse(String message, HttpStatus status, T data, ErrorDetails error) {
        this.message = message;
        this.status = status;
        this.data = data;
        this.error = error;
    }
}
