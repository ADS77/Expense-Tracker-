package com.ad.dena_paona.repository;

import com.ad.dena_paona.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByUserId(Long usrId);
    List<Loan> findByContactId(Long contactId);
}
