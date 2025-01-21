package com.ad.dena_paona.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class GiveLoanRequest {
    private Long lenderId;
    private Long borrowerId;
    private int loanAmount;
    private String  description;
    private LocalDate dueDate;

}
