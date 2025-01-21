 package com.ad.dena_paona.controller;

 import com.ad.dena_paona.entity.Loan;
 import com.ad.dena_paona.payload.request.GiveLoanRequest;
 import com.ad.dena_paona.payload.request.TakeLoanRequest;
 import com.ad.dena_paona.service.LoanService;
 import org.springframework.http.HttpStatus;
 import org.springframework.http.ResponseEntity;
 import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/get-lender-loans", method = RequestMethod.GET)
    public ResponseEntity getLoansOfLender(@RequestParam Long lenderId){
        List<Loan> loans = loanService.getLoanListAsLender(lenderId);
        if(loans.size() > 0){
            return ResponseEntity.ok(loans);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "get-borrower-loans", method = RequestMethod.GET)
    public ResponseEntity getLoansOfBorrower(@RequestParam Long borrowerId){
         List<Loan> loans = loanService.getLoanAsBorrower(borrowerId);
         if(loans.size() > 0){
             return ResponseEntity.ok(loans);
         }
         else {
             return new ResponseEntity(HttpStatus.NOT_FOUND);
         }
    }

 }
