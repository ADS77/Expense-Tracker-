 package com.ad.dena_paona.repository;

 import com.ad.dena_paona.entity.Loan;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.Query;
 import org.springframework.stereotype.Repository;

 import java.math.BigDecimal;
 import java.util.List;
 @Repository
 public interface LoanRepository extends JpaRepository<Loan, Long> {
//     List<Loan> findByUserId(Long usrId);
//     List<Loan> findByContactId(Long contactId);

//     @Query("SELECT COALESCE(SUM(l.amount), 0) FROM Loan l WHERE l.owner = :owner")
//     BigDecimal getTotalLentAmount(String owner);
//
//     @Query("SELECT COALESCE(SUM(l.amount), 0) FROM Loan l WHERE l.lender = :lender")
//     BigDecimal getTotalBorrowedAmount(String lender);

 }
