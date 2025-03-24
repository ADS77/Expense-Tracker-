 package com.ad.dena_paona.service;

 import com.ad.dena_paona.entity.Loan;
 import com.ad.dena_paona.entity.LoanBorrowed;
 import com.ad.dena_paona.entity.LoanLent;
 import com.ad.dena_paona.payload.request.LoanRequest;
 import com.ad.dena_paona.payload.response.ApiResponse;
 import org.springframework.stereotype.Service;

 import java.util.List;

 @Service
 public interface LoanService {

     String giveLoan(LoanRequest request);

     String takeLoan(LoanRequest request);

     List<Loan> getLoanListAsLender(Long lenderId);

     List<Loan> getLoanListAsBorrower(Long borrowerId);

     List<LoanLent> getPaonaListOfUserById(Long userId);

     List<LoanBorrowed> getDenaListOfUserById(Long userId);

     int getTotalDenaOfUer(Long userId);

     int getTotalPaonaOfUser(Long userId);

     // Transaction detail a user lend to a specific borrower
     ApiResponse getDetailPaonaFromBorrower(Long borrowerId, Long userId);

     // Transaction detail a user borrowed from a specific lender
     ApiResponse getDetailDenaForLender(Long lenderId, Long userId);
 }
