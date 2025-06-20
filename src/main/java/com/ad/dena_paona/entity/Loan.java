 package com.ad.dena_paona.entity;

 import com.ad.dena_paona.model.LoanStatus;
 import com.ad.dena_paona.payload.request.LoanRequest;
 import jakarta.persistence.*;
 import lombok.Getter;
 import lombok.RequiredArgsConstructor;
 import lombok.Setter;

 import java.time.LocalDate;
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

     public Loan(Long borrowerId,Long lenderId, LocalDate loanDate){
         this.lenderId = lenderId;
         this.borrowerId = borrowerId;
         this.loanDate = loanDate;
     }

     @Override
     public String toString() {
         return "Loan{" +
                 "loanId=" + loanId +
                 ", amount=" + amount +
                 ", loanDate=" + loanDate +
                 ", dueDate=" + dueDate +
                 ", description='" + description + '\'' +
                 ", borrowerName='" + borrowerName + '\'' +
                 ", borrowerId=" + borrowerId +
                 ", lenderId=" + lenderId +
                 ", lenderName='" + lenderName + '\'' +
                 ", loanStatus=" + loanStatus +
                 '}';
     }

     public static Loan of (LoanRequest loanRequest, String borrowerName, String lenderName, LoanStatus loanStatus) {
         Loan loan = new Loan();
         loan.setAmount(loanRequest.getLoanAmount());
         loan.setDescription(loanRequest.getDescription());
         loan.setDueDate(loanRequest.getDueDate());
         loan.setBorrowerId(loanRequest.getBorrowerId());
         loan.setLenderId(loanRequest.getLenderId());
         loan.setBorrowerName(borrowerName);
         loan.setLenderName(lenderName);
         loan.setLoanStatus(loanStatus);
         loan.setLoanDate(LocalDate.now());
         return loan;
     }

     @Override
     public boolean equals(Object o) {
         if (this == o) return true;
         if (!(o instanceof Loan loan)) return false;

         if (amount != loan.amount) return false;
         if (!Objects.equals(loanId, loan.loanId)) return false;
         if (!Objects.equals(loanDate, loan.loanDate)) return false;
         if (!Objects.equals(dueDate, loan.dueDate)) return false;
         if (!Objects.equals(description, loan.description)) return false;
         if (!Objects.equals(borrowerName, loan.borrowerName)) return false;
         if (!Objects.equals(borrowerId, loan.borrowerId)) return false;
         if (!Objects.equals(lenderId, loan.lenderId)) return false;
         if (!Objects.equals(lenderName, loan.lenderName)) return false;
         return loanStatus == loan.loanStatus;
     }

     @Override
     public int hashCode() {
         int result = loanId != null ? loanId.hashCode() : 0;
         result = 31 * result + amount;
         result = 31 * result + (loanDate != null ? loanDate.hashCode() : 0);
         result = 31 * result + (dueDate != null ? dueDate.hashCode() : 0);
         result = 31 * result + (description != null ? description.hashCode() : 0);
         result = 31 * result + (borrowerName != null ? borrowerName.hashCode() : 0);
         result = 31 * result + (borrowerId != null ? borrowerId.hashCode() : 0);
         result = 31 * result + (lenderId != null ? lenderId.hashCode() : 0);
         result = 31 * result + (lenderName != null ? lenderName.hashCode() : 0);
         result = 31 * result + (loanStatus != null ? loanStatus.hashCode() : 0);
         return result;
     }
 }
