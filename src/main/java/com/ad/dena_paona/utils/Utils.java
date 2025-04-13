package com.ad.dena_paona.utils;

import com.ad.dena_paona.payload.response.RestApiResponse;
import org.springframework.http.HttpStatus;

public class Utils {

    public static <T>RestApiResponse<T> buildSuccessRestResponse (HttpStatus status, T klass){
        return new RestApiResponse<>(status, klass);
    }

    public static <T>RestApiResponse<T> buildErrorRestResponse (HttpStatus status, String message){
        return new RestApiResponse<>(status, message);
    }
}
