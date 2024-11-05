package com.ad.dena_paona.payload.request;

import com.ad.dena_paona.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Getter
@Setter
public class GiveLoanRequest {
    private User user;
    private int loanAmount;
    private LocalDate dueDate;
    private String description;
}
