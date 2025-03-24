 package com.ad.dena_paona.repository;

 import com.ad.dena_paona.entity.Loan;
 import org.springframework.data.domain.Page;
 import org.springframework.data.domain.Pageable;
 import org.springframework.data.jpa.repository.JpaRepository;
 import org.springframework.data.jpa.repository.Query;
 import org.springframework.stereotype.Repository;

 import java.util.List;
 @Repository
 public interface LoanRepository extends JpaRepository<Loan, Long> {
  @Query("select u from Loan u where u.lenderId = ?1")
  List<Loan> getLoansOfLender(Long lenderId);

  @Query("select u from Loan u where u.borrowerId = ?1")
  List<Loan> getLoansOfBorrower(Long borrowerId);

  @Query("select u from Loan u where  u.borrowerId = ?1 and u.lenderId = ?2 ORDER BY u.loanDate desc ")
  Page<Loan> getTransactionHistory(Long borrowerId, Long lenderId, Pageable pagable);

 }
