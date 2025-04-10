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
}
