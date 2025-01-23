 package com.ad.dena_paona.service;

 import com.ad.dena_paona.entity.Loan;
 import com.ad.dena_paona.payload.request.LoanRequest;
 import org.springframework.stereotype.Service;

 import java.util.List;

 @Service
 public interface LoanService {

     String giveLoan(LoanRequest request);

     String takeLoan(LoanRequest request);

     List<Loan>getLoanListAsLender(Long lenderId);

     List<Loan> getLoanListAsBorrower(Long borrowerId);

 }
