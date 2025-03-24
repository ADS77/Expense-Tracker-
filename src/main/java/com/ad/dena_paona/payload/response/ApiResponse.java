package com.ad.dena_paona.payload.response;

import lombok.Data;

import java.util.List;
@Data
public class ApiResponse <T>{
    private List<T> data;
    private int count;
}
