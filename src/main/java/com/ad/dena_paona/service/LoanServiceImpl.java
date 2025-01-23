package com.ad.dena_paona.service;

import com.ad.dena_paona.entity.*;
import com.ad.dena_paona.exception.LoanCreationException;
import com.ad.dena_paona.exception.UserNotFoundException;
import com.ad.dena_paona.payload.request.LoanRequest;
import com.ad.dena_paona.repository.LoanRepository;
import com.ad.dena_paona.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public String giveLoan(LoanRequest loanRequest) {
        Long borrowerId = loanRequest.getBorrowerId();
        Long lenderId = loanRequest.getLenderId();
        int loanAmount = loanRequest.getLoanAmount();
        Optional<User> borrower;
        Optional<User> lender;
        borrower = userRepository.findById(loanRequest.getBorrowerId());
        lender = userRepository.findById(lenderId);
        int prevLoan;
        if (borrower.isPresent() && lender.isPresent()){
            if (dbOperation.checkExistsOrNot(lenderId, borrowerId, entityManager)) {
                prevLoan = dbOperation.getCurrentLoanAmount(lenderId, borrowerId, entityManager);
                dbOperation.updateDenaAndPaona(borrowerId, lenderId, prevLoan + loanAmount, entityManager);
                boolean isLoanSaved = dbOperation.saveLoan(Loan.of(loanRequest, borrower.get().getUserName(), lender.get().getUserName(), LoanStatus.LEND));
                if (isLoanSaved) {
                    log.info("Loan saved");
                } else {
                    log.error("Failed to save loan");
                    throw new LoanCreationException("Failed to save loan while giving loan to" + borrower.get().getUserName());
                }
            } else {
                if (dbOperation.saveDenaAndPaona(loanRequest, lender.get().getUserName(), borrower.get().getUserName())) {
                    log.info("Dena Paona updated successfully!");
                } else {
                    log.error("Failed to update dena paona");
                }
            }
        }else {
            if(borrower.isEmpty()){
                throw new UserNotFoundException("Borrower not found for borrowerId :" + borrowerId);
            }
            throw new UserNotFoundException("Lender not found for lenderId :" + lenderId);
        }

        return "loan given successfully!";
    }


    @Override
    @Transactional
    public String takeLoan(LoanRequest loanRequest) {
        Long borrowerId = loanRequest.getBorrowerId();
        Long lenderId = loanRequest.getLenderId();
        int loanAmount = loanRequest.getLoanAmount();
        int prevLoan;
        Optional<User> borrower;
        Optional<User> lender;
        borrower = userRepository.findById(borrowerId);
        lender = userRepository.findById(lenderId);
        if (borrower.isPresent() && lender.isPresent()){
            boolean exists = dbOperation.checkExistsOrNot(lenderId, borrowerId, entityManager);
            if(exists) {
                prevLoan = dbOperation.getCurrentLoanAmount(lenderId, borrowerId, entityManager);
                dbOperation.updateDenaAndPaona(borrowerId, lenderId, prevLoan + loanAmount,entityManager);
                boolean isLoanSaved = dbOperation.saveLoan(Loan.of(loanRequest,borrower.get().getUserName(), lender.get().getUserName(),LoanStatus.LEND));
                if (isLoanSaved) {
                    log.info("Loan saved, taking loan from:"+lender.get().getUserName());
                } else {
                    log.error("Failed to save loan");
                    throw new LoanCreationException("Failed to save loan while taking loan from " + lender.get().getUserName());
                }
            } else {
                if (dbOperation.saveDenaAndPaona(loanRequest, lender.get().getUserName(), borrower.get().getUserName())) {
                    log.info("Dena Paona updated successfully!");
                } else {
                    log.error("Failed to update dena paona");
                }
            }
        }else {
            if(borrower.isEmpty()){
                throw new UserNotFoundException("Borrower not found for borrowerId :" + borrowerId);
            }
            throw new UserNotFoundException("Lender not found for lenderId :" + lenderId);
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

    @Override
    public List<LoanLent> getPaonaListOf(Long userId) {
        List<LoanLent> paonaList = dbOperation.getPaonaList(userId);
        if (!(paonaList.size() > 0)) {
            log.info("No paona entity found!");
        }
        return paonaList;
    }

    @Override
    public List<LoanBorrowed> getDenaListOf(Long userId) {
        List<LoanBorrowed> denaList = dbOperation.getDenaList(userId);
        if (!(denaList.size() > 0)) {
            log.info("No dena entity found!");
        }
        return denaList;
    }
}
