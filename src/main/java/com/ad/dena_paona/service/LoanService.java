 package com.ad.dena_paona.service;

 import com.ad.dena_paona.entity.Loan;
 import com.ad.dena_paona.entity.LoanBorrowed;
 import com.ad.dena_paona.entity.LoanLent;
 import com.ad.dena_paona.payload.request.LoanRequest;
 import com.ad.dena_paona.payload.response.ApiResponse;
 import com.ad.dena_paona.payload.response.LoanNotificationInfo; // Add this import
 import org.springframework.stereotype.Service;

 import java.util.List; // Ensure this import is present

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

     // Transaction detail a user lent to a specific borrower
     ApiResponse getDetailPaonaFromBorrower(Long borrowerId, Long userId);

     // Transaction detail a user borrowed from a specific lender
     ApiResponse getDetailDenaForLender(Long lenderId, Long userId);

     List<LoanNotificationInfo> getDueSoonNotifications(int daysInAdvance);

     List<LoanNotificationInfo> getOverdueNotifications();
 }
