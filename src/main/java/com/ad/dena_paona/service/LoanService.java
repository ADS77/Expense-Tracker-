 package com.ad.dena_paona.service;

 import com.ad.dena_paona.entity.Loan;
 import com.ad.dena_paona.entity.LoanStatus;
 import com.ad.dena_paona.entity.User;
 import com.ad.dena_paona.payload.request.GiveLoanRequest;
 import com.ad.dena_paona.repository.LoanRepository;
 import jakarta.persistence.EntityManager;
 import jakarta.persistence.PersistenceContext;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;

 import java.math.BigDecimal;
 import java.time.LocalDate;
 import java.time.LocalDateTime;
 import java.util.List;

 @Service
 public class LoanService {
     @Autowired
     private LoanRepository loanRepository;
     @PersistenceContext
     private EntityManager entityManager;
     private static final Logger logger = LoggerFactory.getLogger(LoanService.class);

//     public BigDecimal getTotalLentAmount(String owner) {
//         return loanRepository.getTotalLentAmount(owner);
//     }
//
//     public BigDecimal getTotalBorrowedAmount(String lender) {
//         return loanRepository.getTotalBorrowedAmount(lender);
//     }
     public Loan getLoanById(Long id){
         return loanRepository.findById(id).orElse(null);
     }

     @Transactional
     public String giveLoan(GiveLoanRequest giveLoanRequest){
         User borrower = giveLoanRequest.getUser();
         int loanAmount = giveLoanRequest.getLoanAmount();
         Long borrowerId = borrower.getUserId();
         Loan loan = new Loan();
         loan.setAmount(loanAmount);
         loan.setDescription(giveLoanRequest.getDescription());
         loan.setCreatedAt(LocalDateTime.now());
         loan.setDueDate(giveLoanRequest.getDueDate());
         loan.setBorrower(borrower.getUserName());
         loan.setLoanStatus(LoanStatus.LEND);
         loan.setLoanDate(LocalDate.now());
         logger.info("loan-> + {}",loan.getCreatedAt().toString());
         loanRepository.save(loan);
         updatePaona(borrowerId, loanAmount);

         logger.info("borrower :{}", borrower.getUserName());
         return borrower.getUserName();
     }

     private void updatePaona(Long borrowerId, int loanAmount) {
         Integer currentAmount = (Integer) entityManager.createNativeQuery(
                         "SELECT amount FROM paona WHERE userId = :userId")
                 .setParameter("userId", borrowerId)
                 .getResultStream()
                 .findFirst()
                 .orElse(null);
         logger.info("current loan : {}", currentAmount);

         if(currentAmount != null){
             entityManager.createNativeQuery("UPDATE paona l l.amount = : newAmount Where l.userId = :userId")
                     .setParameter("newAmount", currentAmount + loanAmount)
                     .setParameter("userId", borrowerId)
                     .executeUpdate();
         }
         else{
             entityManager.createNativeQuery("INSERT INTO paona(userId, amount) Values (:userId, :amount)")
                     .setParameter("userId", borrowerId)
                     .setParameter("amount",loanAmount)
                     .executeUpdate();
         }

     }

     public Loan createLoan(Loan loan){
         logger.info("Creating loan for : " +loan.getLender());
         return loanRepository.save(loan);
     }

     public Loan updateLoan(Long id, Loan loanDetails){
         Loan loan = getLoanById(id);
         if(loan != null){
             loan.setAmount(loanDetails.getAmount());
             loan.setLoanDate(loanDetails.getLoanDate());
             loan.setStatus(loanDetails.getStatus());
             loan.setDueDate(loanDetails.getDueDate());
             return loanRepository.save(loan);
         }
         return  null;
     }

     public void deleteLoan(Long loanId){
         loanRepository.deleteById(loanId);
     }
     public void deleteLoad(Loan loan){
         loanRepository.delete(loan);
     }
 }
