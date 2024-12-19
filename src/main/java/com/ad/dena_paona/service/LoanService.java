 package com.ad.dena_paona.service;

 import com.ad.dena_paona.payload.request.GiveLoanRequest;
 import com.ad.dena_paona.payload.request.TakeLoanRequest;
 import org.springframework.stereotype.Service;

 @Service
 public interface LoanService {

     String giveLoan(GiveLoanRequest request);
     String takeLoan(TakeLoanRequest request);

 }
