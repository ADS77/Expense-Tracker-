 package com.ad.dena_paona.entity;

 import jakarta.persistence.*;
 import lombok.*;

 import java.time.LocalDate;

 @Entity
 @Getter
 @Setter
 @RequiredArgsConstructor
 @Data
 @EqualsAndHashCode
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

     @Column
     private String description;

     @Column
     private String borrowerName;

     @Column(nullable = false)
     private Long borrowerId;

     @Column(nullable = false)
     private Long lenderId;

     @Column
     private String lenderName;

     @Enumerated(EnumType.STRING)
     @Column(nullable = false)
     private LoanStatus loanStatus;

 }
