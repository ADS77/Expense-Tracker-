package com.ad.dena_paona.service;

import com.ad.dena_paona.entity.Loan;
import com.ad.dena_paona.entity.LoanStatus;
import com.ad.dena_paona.entity.User;
import com.ad.dena_paona.exception.LoanCreationException;
import com.ad.dena_paona.payload.request.GiveLoanRequest;
import com.ad.dena_paona.payload.request.TakeLoanRequest;
import com.ad.dena_paona.repository.LoanRepository;
import com.ad.dena_paona.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LoanServiceImpl implements LoanService {
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final DBOperation dbOperation;

    public LoanServiceImpl(UserRepository userRepository,
                           LoanRepository loanRepository,
                           DBOperation dbOperation) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.dbOperation = dbOperation;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public String giveLoan(GiveLoanRequest giveLoanRequest) {
        Long borrowerId = giveLoanRequest.getBorrowerId();
        Long lenderId = giveLoanRequest.getLenderId();
        int loanAmount = giveLoanRequest.getLoanAmount();
        Optional<User> borrower;
        Optional<User> lender;
        borrower = userRepository.findById(giveLoanRequest.getBorrowerId());
        lender = userRepository.findById(lenderId);
        if (borrower.isPresent() && lender.isPresent()){
            Loan loan = new Loan();
            loan.setAmount(loanAmount);
            loan.setDescription(giveLoanRequest.getDescription());
            loan.setDueDate(giveLoanRequest.getDueDate());
            loan.setBorrowerId(borrowerId);
            loan.setLenderId(lenderId);
            loan.setBorrowerName(borrower.get().getUserName());
            loan.setLenderName(lender.get().getUserName());
            loan.setLoanStatus(LoanStatus.LEND);
            loan.setLoanDate(LocalDate.now());
            if(isLoanCreated(loan)){
                log.info("Loan giving to : {}", loan.getBorrowerName());
            }
        }

        int currentLoanAmount = 0;
        if(dbOperation.checkExistsOrNot(lenderId, borrowerId, entityManager)) {
            currentLoanAmount = dbOperation.getCurrentLoanAmount(lenderId, borrowerId, entityManager);
            dbOperation.updatePaonaAmount(borrowerId, lenderId, currentLoanAmount + loanAmount, entityManager);
            dbOperation.updateDenaAmount(borrowerId, lenderId, currentLoanAmount + loanAmount, entityManager);
        }
        else {
            dbOperation.insertIntoDena(lenderId, borrowerId, currentLoanAmount + loanAmount, entityManager);
            dbOperation.insertIntoPaona(lenderId, borrowerId,currentLoanAmount + loanAmount, entityManager);
        }

        return "loan given successfully!";
    }

    private boolean isLoanCreated(Loan loan) {
        try {
            loanRepository.save(loan);
            log.info("Loan created, loadId: {}", loan.getLoanId());
        }
        catch (LoanCreationException e){
            log.error(e.getMessage());
        }
        return true;
    }

    @Override
    @Transactional
    public String takeLoan(TakeLoanRequest takeLoanRequest) {
        Long borrowerId = takeLoanRequest.getBorrowerId();
        Long lenderId = takeLoanRequest.getLenderId();
        int loanAmount = takeLoanRequest.getLoanAmount();
        int currentLoanAmount = 0;
        boolean exists = dbOperation.checkExistsOrNot(lenderId, borrowerId, entityManager);
        if(exists) {
            currentLoanAmount = dbOperation.getCurrentLoanAmount(lenderId, borrowerId, entityManager);
            dbOperation.updatePaonaAmount(borrowerId, lenderId, currentLoanAmount + loanAmount,entityManager);
            dbOperation.updateDenaAmount(borrowerId, lenderId, currentLoanAmount + loanAmount, entityManager);
        }
        else {
            dbOperation.insertIntoDena(lenderId, borrowerId, currentLoanAmount + loanAmount, entityManager);
            dbOperation.insertIntoPaona(lenderId, borrowerId, currentLoanAmount + loanAmount, entityManager);
        }
        Optional<User> borrower;
        Optional<User> lender;
        borrower = userRepository.findById(takeLoanRequest.getBorrowerId());
        lender = userRepository.findById(lenderId);
        if (borrower.isPresent() && lender.isPresent()){
            Loan loan = new Loan();
            loan.setAmount(loanAmount);
            loan.setDescription(takeLoanRequest.getDescription());
            loan.setDueDate(takeLoanRequest.getDueDate());
            loan.setBorrowerId(borrowerId);
            loan.setLenderId(lenderId);
            loan.setBorrowerName(borrower.get().getUserName());
            loan.setLenderName(lender.get().getUserName());
            loan.setLoanStatus(LoanStatus.BORROW);
            loan.setLoanDate(LocalDate.now());
            if(isLoanCreated(loan)){
                log.info("Loan taken from : {}", loan.getLenderName());
            }
        }
        return "loan taken successfully";
    }

    @Override
    public List<Loan> getLoanListAsLender(Long lenderId) {
        return loanRepository.getLoansOfLender(lenderId);
    }

    @Override
    public List<Loan> getLoanListAsBorrower(Long borrowerId) {
        return loanRepository.getLoansOfBorrower(borrowerId);
    }
}
