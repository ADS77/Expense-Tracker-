package com.ad.dena_paona.service;
import com.ad.dena_paona.entity.Loan;
import com.ad.dena_paona.entity.LoanStatus;
import com.ad.dena_paona.entity.User;
import com.ad.dena_paona.exception.LoanCreationException;
import com.ad.dena_paona.exception.UpdatePaonaException;
import com.ad.dena_paona.payload.request.GiveLoanRequest;
import com.ad.dena_paona.payload.request.TakeLoanRequest;
import com.ad.dena_paona.repository.LoanRepository;
import com.ad.dena_paona.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
public class LoanServiceImpl implements LoanService {
    private UserRepository userRepository;
    private LoanRepository loanRepository;

    public LoanServiceImpl(UserRepository userRepository, LoanRepository loanRepository) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public <T> String giveLoan(GiveLoanRequest giveLoanRequest) {
        Long borrowerId = giveLoanRequest.getBorrowerId();
        Long lenderId = giveLoanRequest.getLenderId();
        int loanAmount = giveLoanRequest.getLoanAmount();
        int currentLoanAmount = 0;
        boolean exists = doesExist(lenderId, borrowerId);
        if(exists) {
            currentLoanAmount = getCurrentLoanAmount(lenderId, borrowerId);
            updatePaonaAmount(borrowerId, lenderId, currentLoanAmount + loanAmount);
            updateDenaAmount(borrowerId, lenderId, currentLoanAmount + loanAmount);
        }
        else {
            insertIntoDena(lenderId, borrowerId, currentLoanAmount + loanAmount);
            insertIntoPaona(lenderId, borrowerId,currentLoanAmount + loanAmount);
        }
        Optional<User> borrower = Optional.empty();
        Optional<User> lender = Optional.empty();
        borrower = userRepository.findById(giveLoanRequest.getBorrowerId());
        lender = userRepository.findById(lenderId);
        if (borrower.isPresent() && lender.isPresent()){
            Loan loan = new Loan();
            loan.setAmount(loanAmount);
            loan.setDescription(giveLoanRequest.getDescription());
            loan.setDueDate(giveLoanRequest.getDueDate());
            loan.setBorrower(borrower.get().getUserName());
            loan.setLender(lender.get().getUserName());
            loan.setLoanStatus(LoanStatus.LEND);
            loan.setLoanDate(LocalDate.now());
            if(isLoanCreated(loan)){
                log.info("Loan giving to : {}", loan.getBorrower());
            }
        }
        return "loan given successfully!";
    }


    protected void updateDenaAmount(Long borrowerId, Long lenderId, int loanAmount) {
        try {
            entityManager.createNativeQuery(
                            "UPDATE dena SET amount = :amount WHERE lenderId = :lenderId AND userId = :borrowerId")
                    .setParameter("lenderId", lenderId)
                    .setParameter("borrowerId", borrowerId)
                    .setParameter("amount", loanAmount)
                    .executeUpdate();
            log.info("Successfully updated dena for lenderId: {}, borrowerId: {}, loanAmount: {}", lenderId, borrowerId, loanAmount);
        } catch (Exception e) {
            log.error("Exception during updating dena for lenderId: {}, borrowerId: {}, loanAmount: {}. Error: {}", lenderId, borrowerId, loanAmount, e.getMessage());
            throw e;
        }
    }


    protected void updatePaonaAmount(Long borrowerId, Long lenderId, int loanAmount) {
        try {
            entityManager.createNativeQuery(
                            "UPDATE paona SET amount = :amount WHERE userId = :lenderId AND borrowerId = :borrowerId")
                    .setParameter("lenderId", lenderId)
                    .setParameter("borrowerId", borrowerId)
                    .setParameter("amount", loanAmount)
                    .executeUpdate();
            log.info("Successfully updated paona for lenderId: {}, borrowerId: {}, loanAmount: {}", lenderId, borrowerId, loanAmount);
        } catch (Exception e) {
            log.error("Exception during updating paona for lenderId: {}, borrowerId: {}, loanAmount: {}. Error: {}", lenderId, borrowerId, loanAmount, e.getMessage());
            throw e;
        }
    }

    private boolean doesExist(Long lenderId, Long borrowerId){
        boolean exist = false;
        String query = "SELECT EXISTS(SELECT 1 FROM paona WHERE userId = :userId AND borrowerId = :borrowerId)";
        Object paonaResult = entityManager.createNativeQuery(query)
                .setParameter("borrowerId", borrowerId)
                .setParameter("userId", lenderId)
                .getSingleResult();
        query = "SELECT EXISTS(SELECT 1 FROM dena WHERE userId = :borrowerId AND lenderId = :lenderId)";
        Object denaResult = entityManager.createNativeQuery(query)
                .setParameter("lenderId", lenderId)
                .setParameter("borrowerId", borrowerId)
                .getSingleResult();
        int paonaExists = ((Number) paonaResult).intValue();
        int denaExists = ((Number) denaResult).intValue();
        if(paonaExists == denaExists && paonaExists + denaExists == 2){
            exist = true;
        }
        return exist;
    }

    protected void insertIntoPaona(Long lenderId, Long borrowerId, int loanAmount) {
        try {
            entityManager.createNativeQuery(
                            "INSERT INTO paona (userId,borrowerId, amount) VALUES (:lenderId, :borrowerId, :amount)")
                    .setParameter("lenderId", lenderId)
                    .setParameter("amount", loanAmount)
                    .setParameter("borrowerId", borrowerId)
                    .executeUpdate();
            log.info("Successfully inserted into paona for lenderId: {}, borrowerId: {}, loanAmount: {}", lenderId, borrowerId, loanAmount);
        } catch (Exception e) {
            log.error("Exception during inserting into paona for lenderId: {}, borrowerId: {}, loanAmount: {}. Error: {}", lenderId, borrowerId, loanAmount, e.getMessage());
            throw e;
        }
    }

    protected void insertIntoDena(Long lenderId, Long borrowerId, int loanAmount) {
        try {
            entityManager.createNativeQuery(
                            "INSERT INTO dena (userId,lenderId, amount) VALUES (:borrowerId, :lenderId, :amount)")
                    .setParameter("lenderId", lenderId)
                    .setParameter("amount", loanAmount)
                    .setParameter("borrowerId", borrowerId)
                    .executeUpdate();
            log.info("Successfully inserted into dena for lenderId: {}, borrowerId: {}, loanAmount: {}", lenderId, borrowerId, loanAmount);
        } catch (Exception e) {
            log.error("Exception during inserting dena for lenderId: {}, borrowerId: {}, loanAmount: {}. Error: {}", lenderId, borrowerId, loanAmount, e.getMessage());
            throw e;
        }
    }

    private boolean isLoanCreated(Loan loan) {
        try {
            loanRepository.save(loan);
        }
        catch (LoanCreationException e){
            log.error(e.getMessage());
        }
        return true;
    }


    public int getCurrentLoanAmount(Long lenderId, Long borrowerId) {
        try {
            Integer currPaona = Optional.ofNullable(
                    (Integer) entityManager.createNativeQuery(
                                    "SELECT amount FROM paona WHERE borrowerId = :borrowerId AND userId = :userId")
                            .setParameter("borrowerId", borrowerId)
                            .setParameter("userId", lenderId)
                            .getSingleResult()
            ).orElse(0);

            Integer currDena = Optional.ofNullable(
                    (Integer) entityManager.createNativeQuery(
                                    "SELECT amount FROM dena WHERE userId = :borrowerId AND lenderId = :userId")
                            .setParameter("borrowerId", borrowerId)
                            .setParameter("userId", lenderId)
                            .getSingleResult()
            ).orElse(0);
            if(!currPaona.equals(currDena)){
                log.error("dena and paona amount is doesn't match");
            }
            return currPaona;
        } catch (NoResultException e) {
            log.info("No result found for the given lenderId and borrowerId");
            return 0;
        }
    }


    @Override
    @Transactional
    public <T> String takeLoan(TakeLoanRequest takeLoanRequest) {
        Long borrowerId = takeLoanRequest.getBorrowerId();
        Long lenderId = takeLoanRequest.getLenderId();
        int loanAmount = takeLoanRequest.getLoanAmount();
        int currentLoanAmount = 0;
        boolean exists = doesExist(lenderId, borrowerId);
        if(exists) {
            currentLoanAmount = getCurrentLoanAmount(lenderId, borrowerId);
            updatePaonaAmount(borrowerId, lenderId, currentLoanAmount + loanAmount);
            updateDenaAmount(borrowerId, lenderId, currentLoanAmount + loanAmount);
        }
        else {
            insertIntoDena(lenderId, borrowerId, currentLoanAmount + loanAmount);
            insertIntoPaona(lenderId, borrowerId,currentLoanAmount + loanAmount);
        }
        Optional<User> borrower = Optional.empty();
        Optional<User> lender = Optional.empty();
        borrower = userRepository.findById(takeLoanRequest.getBorrowerId());
        lender = userRepository.findById(lenderId);
        if (borrower.isPresent() && lender.isPresent()){
            Loan loan = new Loan();
            loan.setAmount(loanAmount);
            loan.setDescription(takeLoanRequest.getDescription());
            loan.setDueDate(takeLoanRequest.getDueDate());
            loan.setBorrower(borrower.get().getUserName());
            loan.setLender(lender.get().getUserName());
            loan.setLoanStatus(LoanStatus.BORROW);
            loan.setLoanDate(LocalDate.now());
            if(isLoanCreated(loan)){
                log.info("Loan taken from : {}", loan.getLender());
            }
        }
        return "loan taken successfully";
    }


}
