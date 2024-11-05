 package com.ad.dena_paona.entity;

 import jakarta.persistence.*;
 import lombok.Getter;
 import lombok.RequiredArgsConstructor;
 import lombok.Setter;

 import java.math.BigDecimal;
 import java.time.LocalDate;
 import java.time.LocalDateTime;

 @Entity
 @Getter
 @Setter
 @RequiredArgsConstructor
 public class Loan {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long loanId;

     @Column(nullable = false)
     private int amount;

     @Column(nullable = false)
     private LocalDate loanDate;

     @Column
     private LocalDate dueDate;

     @Column(nullable = false)
     private LoanStatus status;

     @Column
     private String description;

     @Column
     private String borrower;

     @Column
     private String lender;
     @Column(nullable = false)
     private LoanStatus loanStatus;

     @Column(nullable = false)
     private LocalDateTime createdAt = LocalDateTime.now();

 }
