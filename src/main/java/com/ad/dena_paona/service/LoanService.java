 package com.ad.dena_paona.service;

 import com.ad.dena_paona.entity.Loan;
 import com.ad.dena_paona.payload.request.GiveLoanRequest;
 import com.ad.dena_paona.payload.request.TakeLoanRequest;
 import org.springframework.stereotype.Service;

 import java.util.List;

 @Service
 public interface LoanService {

     String giveLoan(GiveLoanRequest request);

     String takeLoan(TakeLoanRequest request);

     List<Loan>getLoanListAsLender(Long lenderId);

     List<Loan> getLoanListAsBorrower(Long borrowerId);

 }
