package com.ad.dena_paona.payload.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TakeLoanRequest {
    private int amount;
    private String description;
    private LocalDate dueDate;
    private Long lenderId;
}
