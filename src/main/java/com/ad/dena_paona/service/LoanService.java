package com.ad.dena_paona.service;

import com.ad.dena_paona.entity.Loan;
import com.ad.dena_paona.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;

    public List<Loan> getLoansByUserId(Long userId){
        return loanRepository.findByUserId(userId);
    }

    public List<Loan> getLoansByContactId(Long contactId){
        return  loanRepository.findByContactId(contactId);
    }
    public Loan getLoanById(Long id){
        return loanRepository.findById(id).orElse(null);
    }

    public Loan createLoan(Loan loan){
        return loanRepository.save(loan);
    }

    public Loan updateLoan(Long id, Loan loanDetails){
        Loan loan = getLoanById(id);
        if(loan != null){
            loan.setAmount(loanDetails.getAmount());
            loan.setLoanDate(loanDetails.getLoanDate());
            loan.setStatus(loanDetails.getStatus());
            loan.setDueDate(loanDetails.getDueDate());
            return loanRepository.save(loan);
        }
        return  null;
    }

    public void deleteLoan(Long loanId){
        loanRepository.deleteById(loanId);
    }
    public void deleteLoad(Loan loan){
        loanRepository.delete(loan);
    }
}
