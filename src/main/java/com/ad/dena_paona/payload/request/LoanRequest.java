package com.ad.dena_paona.payload.request;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@Data
public class LoanRequest implements Serializable {
    @NonNull
    private Long lenderId;
    @NonNull
    private Long borrowerId;
    @Min(1)
    private int loanAmount;
    @NonNull
    private String  description;
    private LocalDate dueDate;

}
