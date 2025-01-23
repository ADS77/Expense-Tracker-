package com.ad.dena_paona.entity;

import com.ad.dena_paona.payload.request.LoanRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "dena")
public class LoanBorrowed {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private Long lenderId;

    @Column(nullable = false)
    private String lenderName;

    public static LoanBorrowed of(LoanRequest loanRequest, String lenderName) {
        LoanBorrowed loanBorrowed = new LoanBorrowed();
        loanBorrowed.setLenderId(loanRequest.getLenderId());
        loanBorrowed.setAmount(loanRequest.getLoanAmount());
        loanBorrowed.setLenderName(lenderName);
        loanBorrowed.setUserId(loanRequest.getBorrowerId());
        return loanBorrowed;
    }
}
