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
        int currentLoanAmount = getCurrentLoanAmount(lenderId, borrowerId);


        Optional<User> borrower = Optional.empty();
        Optional<User> lender = Optional.empty();
        borrower = userRepository.findById(borrowerId);
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
                log.info("Loan given to{}", loan.getBorrower());
            }
            boolean isPaonaUpdateSuccess = updatePaona(lenderId, borrowerId, currentLoanAmount + loanAmount);
            if(!isPaonaUpdateSuccess){
                try {
                    insertIntoPaona(lenderId, borrowerId,currentLoanAmount+loanAmount);
                }catch (UpdatePaonaException e){
                    log.error("error inserting into paona");
                }
            }
        }
        else {
            insertIntoPaona(lenderId, borrowerId,currentLoanAmount+loanAmount);
        }


        return "loan given successfully!";
    }

    protected void insertIntoPaona(Long lenderId, Long borrowerId, int loanAmount) {
        try {
            entityManager.createNativeQuery(
                            "INSERT INTO paona (lenderId, amount, borrowerId) VALUES (:lenderId, :amount, :borrowerId)")
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
            Integer currAmount = (Integer) entityManager.createNativeQuery(
                            "SELECT amount FROM paona WHERE borrowerId = :borrowerId AND lenderId = :lenderId")
                    .setParameter("borrowerId", borrowerId)
                    .setParameter("lenderId", lenderId)
                    .getSingleResult();

            return currAmount != null ? currAmount : 0;

        } catch (NoResultException e) {
            log.info("No result found for the given lenderId and borrowerId");
            return 0;
        } catch (Exception e) {
            log.error("Exception during fetching currentLoan from paona db: ", e);
            return 0;
        }
    }


    private boolean updatePaona(Long lenderId, Long borrowerId, int loanAmount) {
        try {
            int rowsUpdated = entityManager.createNativeQuery(
                            "UPDATE paona SET amount = :amount WHERE lenderId = :lenderId AND borrowerId = :borrowerId")
                    .setParameter("lenderId", lenderId)
                    .setParameter("amount", loanAmount)
                    .setParameter("borrowerId", borrowerId)
                    .executeUpdate();
            return rowsUpdated > 0;
        }
        catch (UpdatePaonaException e){
            log.error("Failed to update paona for lenderId: {}, borrowerId: {}. Error: {}", lenderId, borrowerId, e.getMessage());
            return false;
        }
    }


    @Override
    public <T> String takeLoan(TakeLoanRequest takeLoanRequest) {
        return "";
    }
}
