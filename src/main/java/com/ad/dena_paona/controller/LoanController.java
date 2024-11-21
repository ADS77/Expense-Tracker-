 package com.ad.dena_paona.controller;

 import com.ad.dena_paona.entity.Loan;
 import com.ad.dena_paona.entity.User;
 import com.ad.dena_paona.payload.request.GiveLoanRequest;
 import com.ad.dena_paona.payload.request.TakeLoanRequest;
 import com.ad.dena_paona.service.LoanService;
 import org.springframework.beans.factory.annotation.Autowired;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;

 import java.math.BigDecimal;
 import java.util.Date;
 import java.util.List;
@RestController
@RequestMapping("/DP/loan")
 public class LoanController {
     private final LoanService loanService;

     public LoanController(LoanService loanService) {
         this.loanService = loanService;
     }

    @PostMapping("/give-loan")
    public ResponseEntity<String> giveLoan(@RequestBody GiveLoanRequest giveLoanRequest){
         String response = loanService.giveLoan(giveLoanRequest);
         return ResponseEntity.ok(response);
    }
    @PostMapping("/take_loan")
    public ResponseEntity<String> takeLoan(@RequestBody TakeLoanRequest takeLoanRequest){
         String response = loanService.takeLoan(takeLoanRequest);
         return ResponseEntity.ok(response);
    }

 }
