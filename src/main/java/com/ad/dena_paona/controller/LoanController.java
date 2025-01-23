 package com.ad.dena_paona.controller;

 import com.ad.dena_paona.entity.Loan;
 import com.ad.dena_paona.entity.LoanBorrowed;
 import com.ad.dena_paona.entity.LoanLent;
 import com.ad.dena_paona.payload.request.LoanRequest;
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
    public ResponseEntity<String> giveLoan(@RequestBody LoanRequest giveLoanRequest){
         String response = loanService.giveLoan(giveLoanRequest);
         return ResponseEntity.ok(response);
    }

     @PostMapping("/take-loan")
    public ResponseEntity<String> takeLoan(@RequestBody LoanRequest takeLoanRequest){
         String response = loanService.takeLoan(takeLoanRequest);
         return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/get-lender-loans", method = RequestMethod.GET)
    public ResponseEntity getLoansOfLender(@RequestParam Long lenderId){
        List<Loan> loans = loanService.getLoanListAsLender(lenderId);
        if(loans.size() > 0){
            return ResponseEntity.ok(loans);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO loans found for lenderId : "+lenderId);
        }
    }

     @RequestMapping(value = "/get-borrower-loans", method = RequestMethod.GET)
     public ResponseEntity getLoansOfBorrower(@RequestParam Long borrowerId) {
         List<Loan> loans = loanService.getLoanListAsBorrower(borrowerId);
         if (loans.size() > 0) {
             return ResponseEntity.ok(loans);
         } else {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("NO loans found for borrowerId : " + borrowerId);

         }
     }

     @RequestMapping(value = "get-dena", method = RequestMethod.GET)
     public ResponseEntity getDenaOfUser(@RequestParam Long userId) {
         List<LoanBorrowed> denaList = loanService.getDenaListOf(userId);
         if (denaList.size() > 0) {
             return ResponseEntity.ok(denaList);
         } else {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No dena till now...");
         }
     }

     @RequestMapping(value = "get-paona", method = RequestMethod.GET)
     public ResponseEntity getPaonaOfUser(@RequestParam Long userId) {
         List<LoanLent> denaList = loanService.getPaonaListOf(userId);
         if (denaList.size() > 0) {
             return ResponseEntity.ok(denaList);
         } else {
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No paona till now...");
         }
     }

 }
