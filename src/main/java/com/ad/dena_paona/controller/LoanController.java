 package com.ad.dena_paona.controller;

 import com.ad.dena_paona.entity.Loan;
 import com.ad.dena_paona.entity.User;
 import com.ad.dena_paona.payload.request.GiveLoanRequest;
 import com.ad.dena_paona.service.LoanService;
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
     // Get total lent amount for a user
//     @GetMapping("/lent")
//     public BigDecimal getTotalLentAmount(@RequestParam String owner) {
//         return loanService.getTotalLentAmount(owner);
//     }
//
//     // Get total borrowed amount for a user
//     @GetMapping("/borrowed")
//     public BigDecimal getTotalBorrowedAmount(@RequestParam String lender) {
//         return loanService.getTotalBorrowedAmount(lender);
//     }


    @PostMapping("/give-loan")
    public ResponseEntity<String> giveLoan(@RequestBody GiveLoanRequest giveLoanRequest){
         String response = loanService.giveLoan(giveLoanRequest);
         return ResponseEntity.ok(response);
    }

     @GetMapping("/{id}")
     public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
         Loan loan = loanService.getLoanById(id);
         if (loan == null) {
             return ResponseEntity.notFound().build();
         }
         return ResponseEntity.ok(loan);
     }

     @PostMapping("/create")
     public Loan createLoan(@RequestBody Loan loan) {
         return loanService.createLoan(loan);
     }

     @PutMapping("/{id}")
     public ResponseEntity<Loan> updateLoan(@PathVariable Long id, @RequestBody Loan loanDetails) {
         Loan updatedLoan = loanService.updateLoan(id, loanDetails);
         if (updatedLoan == null) {
             return ResponseEntity.notFound().build();
         }
         return ResponseEntity.ok(updatedLoan);
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
         loanService.deleteLoan(id);
         return ResponseEntity.noContent().build();
     }
 }
