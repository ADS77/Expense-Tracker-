 package com.ad.dena_paona.controller;

 import com.ad.dena_paona.payload.request.GiveLoanRequest;
 import com.ad.dena_paona.payload.request.TakeLoanRequest;
 import com.ad.dena_paona.service.LoanService;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.PostMapping;
 import org.springframework.web.bind.annotation.RequestBody;
 import org.springframework.web.bind.annotation.RequestMapping;
 import org.springframework.web.bind.annotation.RestController;
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
