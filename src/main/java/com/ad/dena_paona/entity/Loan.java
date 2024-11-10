 package com.ad.dena_paona.entity;

 import jakarta.persistence.*;
 import lombok.Getter;
 import lombok.RequiredArgsConstructor;
 import lombok.Setter;

 import java.math.BigDecimal;
 import java.time.LocalDate;
 import java.time.LocalDateTime;
 import java.util.Objects;

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

     @Column
     private String description;

     @Column
     private String borrower;

     @Column(nullable = false)
     private String lender;
     @Column(nullable = false)
     private LoanStatus loanStatus;

     @Column(nullable = false)
     private LocalDateTime createdAt = LocalDateTime.now();

     @Override
     public String toString() {
         return "Loan{" +
                 "loanId=" + loanId +
                 ", amount=" + amount +
                 ", loanDate=" + loanDate +
                 ", dueDate=" + dueDate +
                 ", description='" + description + '\'' +
                 ", borrower='" + borrower + '\'' +
                 ", lender='" + lender + '\'' +
                 ", loanStatus=" + loanStatus +
                 ", createdAt=" + createdAt +
                 '}';
     }

     @Override
     public boolean equals(Object o) {
         if (this == o) return true;
         if (o == null || getClass() != o.getClass()) return false;
         Loan loan = (Loan) o;
         return amount == loan.amount && Objects.equals(loanId, loan.loanId) && Objects.equals(loanDate, loan.loanDate) && Objects.equals(dueDate, loan.dueDate)  && Objects.equals(description, loan.description) && Objects.equals(borrower, loan.borrower) && Objects.equals(lender, loan.lender) && loanStatus == loan.loanStatus && Objects.equals(createdAt, loan.createdAt);
     }

     @Override
     public int hashCode() {
         return Objects.hash(loanId, amount, loanDate, dueDate, description, borrower, lender, loanStatus, createdAt);
     }
 }
