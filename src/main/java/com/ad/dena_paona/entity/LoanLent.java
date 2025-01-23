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
@Table(name = "paona")
public class LoanLent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private Long borrowerId;

    @Column(nullable = false)
    private String borrowerName;

    public static LoanLent of(LoanRequest loanRequest, String borrowerName) {
        LoanLent loanLent = new LoanLent();
        loanLent.setBorrowerName(borrowerName);
        loanLent.setBorrowerId(loanRequest.getBorrowerId());
        loanLent.setAmount(loanRequest.getLoanAmount());
        loanLent.setUserId(loanRequest.getLenderId());
        return loanLent;
    }

}
