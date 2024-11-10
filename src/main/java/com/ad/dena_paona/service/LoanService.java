 package com.ad.dena_paona.service;
 import com.ad.dena_paona.entity.Loan;
 import com.ad.dena_paona.entity.LoanStatus;
 import com.ad.dena_paona.entity.User;
 import com.ad.dena_paona.payload.request.GiveLoanRequest;
 import com.ad.dena_paona.payload.request.TakeLoanRequest;
 import com.ad.dena_paona.repository.LoanRepository;
 import com.ad.dena_paona.repository.UserRepository;
 import jakarta.persistence.EntityManager;
 import jakarta.persistence.PersistenceContext;
 import org.slf4j.Logger;
 import org.slf4j.LoggerFactory;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.stereotype.Service;
 import org.springframework.transaction.annotation.Transactional;
 import java.time.LocalDate;
 import java.time.LocalDateTime;
 import java.util.Arrays;
 import java.util.Optional;

 @Service
 public class LoanService {
     @Autowired
     private LoanRepository loanRepository;
     @Autowired
     private UserRepository userRepository;
     @PersistenceContext
     private EntityManager entityManager;
     private static final Logger logger = LoggerFactory.getLogger(LoanService.class);
     public Loan getLoanById(Long id){
         return loanRepository.findById(id).orElse(null);
     }

     @Transactional
     public String handleGiveLoan(GiveLoanRequest giveLoanRequest){
         Long borrowerId = giveLoanRequest.getBorrowerId();
         int loanAmount = giveLoanRequest.getLoanAmount();
         Optional<User> borrower = Optional.empty();
         try {
             borrower = userRepository.findById(borrowerId);
         } catch (Exception e) {
             throw new RuntimeException("Borrower not found with id: " + borrowerId);
         }
         Loan loan = new Loan();
         if (borrower.isPresent()){
             loan.setAmount(loanAmount);
             loan.setDescription(giveLoanRequest.getDescription());
             loan.setCreatedAt(LocalDateTime.now());
             loan.setDueDate(giveLoanRequest.getDueDate());
             loan.setBorrower(borrower.get().getUserName());
             loan.setLoanStatus(LoanStatus.LEND);
             loan.setLoanDate(LocalDate.now());
             loan.setLender("ami");
         }
         else {
             throw new RuntimeException("Borrower not found.");
         }
         logger.info("loan-> + {}",loan.toString());
         boolean isLoanCreated = createLoan(loan);
         if(isLoanCreated){
             try {
                 updatePaona(borrowerId, loanAmount, loan.getBorrower());
             } catch (Exception e) {
                 throw new RuntimeException("Problem in updating paona");
             }
         }
         else return "Problem in creating loan for userId: " + borrowerId;


         logger.info("borrower :{}", borrower.get().getUserName());
         return "loan given successfully!";
     }


     private void updatePaona(Long borrowerId, int loanAmount, String borrowerName) {
         Integer currentAmount = (Integer) entityManager.createNativeQuery(
                         "SELECT amount FROM paona WHERE userId = :userId")
                 .setParameter("userId", borrowerId)
                 .getResultStream()
                 .findFirst()
                 .orElse(null);
         logger.info("current loan : {}", currentAmount);

         if(currentAmount != null){
             entityManager.createNativeQuery("UPDATE paona SET amount = :newAmount WHERE userId = :userId")
                     .setParameter("newAmount", currentAmount + loanAmount)
                     .setParameter("userId", borrowerId)
                     .executeUpdate();

         }
         else{
             entityManager.createNativeQuery("INSERT INTO paona(userId, amount,borrowerName) Values (:userId, :amount, :borrowerName)")
                     .setParameter("userId", borrowerId)
                     .setParameter("amount",loanAmount)
                     .setParameter("borrowerName", borrowerName)
                     .executeUpdate();
         }

     }

     public boolean createLoan(Loan loan){
         logger.info("Creating loan for : " +loan.getBorrower());
         try {
             loanRepository.save(loan);
             return true;
         } catch (Exception e) {
             throw new RuntimeException(Arrays.toString(e.getStackTrace()));
         }

     }

     public Loan updateLoan(Long id, Loan loanDetails){
         Loan loan = getLoanById(id);
         if(loan != null){
             loan.setAmount(loanDetails.getAmount());
             loan.setLoanDate(loanDetails.getLoanDate());
             loan.setDueDate(loanDetails.getDueDate());
             loan.setLoanStatus(loanDetails.getLoanStatus());
             return loanRepository.save(loan);
         }
         return  null;
     }

     public void deleteLoan(Loan loan){
         loanRepository.delete(loan);
     }

     public void deleteLoanByLoanId(Long id) {
         loanRepository.deleteById(id);
     }

     @Transactional
     public String handleTakeLoan(TakeLoanRequest takeLoanRequest) {
         Long lenderId = takeLoanRequest.getLenderId();
         int loanAmount = takeLoanRequest.getAmount();
         Optional<User> lender = Optional.empty();
         try {
             lender = userRepository.findById(lenderId);
         } catch (Exception e) {
             throw new RuntimeException("Lender not found with id: " + lenderId);
         }
         Loan loan = new Loan();
         if (lender.isPresent()){
             loan.setAmount(loanAmount);
             loan.setDescription(takeLoanRequest.getDescription());
             loan.setCreatedAt(LocalDateTime.now());
             loan.setDueDate(takeLoanRequest.getDueDate());
             loan.setBorrower("ami");
             loan.setLoanStatus(LoanStatus.BORROW);
             loan.setLoanDate(LocalDate.now());
             loan.setLender(lender.get().getUserName());
         }
         else {
             throw new RuntimeException("Lender not found.");
         }
         logger.info("loan-> + {}",loan.toString());
         boolean isLoanCreated = createLoan(loan);
         if(isLoanCreated){
             try {
                 updateDena(lenderId, loanAmount, loan.getLender());
             } catch (Exception e) {
                 throw new RuntimeException("Problem in updating dena");
             }
         }
         else return "Problem in creating loan for userId: " + lenderId;


         logger.info("lender :{}", lender.get().getUserName());
         return "loan taken successfully!";

     }

     private void updateDena(Long lenderId, int loanAmount,String lenderName) {
         Integer currentAmount = (Integer) entityManager.createNativeQuery(
                         "SELECT amount FROM dena WHERE userId = :userId")
                 .setParameter("userId", lenderId)
                 .getResultStream()
                 .findFirst()
                 .orElse(null);
         logger.info("current loan : {}", currentAmount);

         if(currentAmount != null){
             entityManager.createNativeQuery("UPDATE dena SET amount = :newAmount WHERE userId = :userId")
                     .setParameter("newAmount", currentAmount + loanAmount)
                     .setParameter("userId", lenderId)
                     .executeUpdate();

         }
         else{
             entityManager.createNativeQuery("INSERT INTO dena(userId, amount,lenderName) Values (:userId, :amount, :lenderName)")
                     .setParameter("userId", lenderId)
                     .setParameter("amount",loanAmount)
                     .setParameter("lenderName",lenderName)
                     .executeUpdate();
         }
     }
 }
