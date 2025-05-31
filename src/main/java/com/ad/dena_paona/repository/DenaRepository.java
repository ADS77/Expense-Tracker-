package com.ad.dena_paona.repository;

import com.ad.dena_paona.entity.LoanBorrowed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DenaRepository extends JpaRepository<LoanBorrowed, Long> {
    @Query("select u from LoanBorrowed u where u.userId = ?1")
    List<LoanBorrowed> getDenaListOfUser(Long lenderId);
}
