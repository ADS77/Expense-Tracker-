package com.ad.dena_paona.payload.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class TakeLoanRequest {
    private Long lenderId;
    private Long borrowerId;
    private int loanAmount;
    private String  description;
    private LocalDate dueDate;
}
