package com.ad.dena_paona.repository;

import com.ad.dena_paona.entity.LoanLent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaonaRepository extends JpaRepository<LoanLent, Long> {
    @Query("select u from LoanLent u where u.userId = ?1")
    List<LoanLent> getPaonaListOfUser(Long lenderId);
}
