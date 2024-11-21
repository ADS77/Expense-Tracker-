 package com.ad.dena_paona.service;
 import com.ad.dena_paona.payload.request.GiveLoanRequest;
 import com.ad.dena_paona.payload.request.TakeLoanRequest;
 import org.springframework.stereotype.Service;
 @Service
 public interface LoanService {
     public <T> String giveLoan(GiveLoanRequest giveLoanRequest);
     public <T> String takeLoan(TakeLoanRequest takeLoanRequest);

 }
