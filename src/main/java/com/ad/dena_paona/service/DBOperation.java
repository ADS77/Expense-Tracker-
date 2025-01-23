package com.ad.dena_paona.service;
import com.ad.dena_paona.entity.Loan;
import com.ad.dena_paona.repository.LoanRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DBOperation {
    private static final Logger logger = LoggerFactory.getLogger(DBOperation.class);
    private final LoanRepository loanRepository;

    public DBOperation(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    public void updateDenaAndPaona(Long borrowerId,
                                      Long lenderId,
                                      int loanAmount,
                                      EntityManager entityManager){
        updateDenaAmount(borrowerId, lenderId, loanAmount, entityManager);
        updatePaonaAmount(borrowerId, lenderId, loanAmount, entityManager);
    }
    public boolean saveLoan(Loan loan){
        try {
            loanRepository.save(loan);
            logger.info("Loan created lenderId : {} borrowerId : {}", loan.getLenderName(), loan.getLenderName());
            return  true;
        }
        catch (Exception e){
            logger.error("Can't save loan!");
            return false;
        }
    }
    protected boolean checkExistsOrNot(Long lenderId, Long borrowerId,String lenderName, String borrowerName,EntityManager entityManager){
        boolean exist = false;
        String query = "SELECT EXISTS(SELECT 1 FROM paona WHERE user_id = :userId AND borrower_id = :borrowerId)";
        Object paonaResult = entityManager.createNativeQuery(query)
                .setParameter("borrowerId", borrowerId)
                .setParameter("userId", lenderId)
                .getSingleResult();
        query = "SELECT EXISTS(SELECT 1 FROM dena WHERE user_id = :userId AND lender_id = :lenderId)";
        Object denaResult = entityManager.createNativeQuery(query)
                .setParameter("lenderId", lenderId)
                .setParameter("userId", borrowerId)
                .getSingleResult();
        int paonaExists = ((Number) paonaResult).intValue();
        int denaExists = ((Number) denaResult).intValue();
        if(paonaExists == denaExists && paonaExists + denaExists == 2){
            exist = true;
        }
        return exist;
    }

    private void insertIntoDena(Long lenderId, Long borrowerId,String lenderName, String borrowerName, int loanAmount, EntityManager entityManager) {
        try {
            entityManager.createNativeQuery(
                            "INSERT INTO dena (user_id,lender_id, amount, lender_name) VALUES (:borrowerId, :lenderId, :amount, :lenderName)")
                    .setParameter("lenderId", lenderId)
                    .setParameter("amount", loanAmount)
                    .setParameter("borrowerId", borrowerId)
                    .setParameter("lenderName",lenderName)
                    .executeUpdate();
            logger.info("Successfully inserted into dena for lenderId: {}, borrowerId: {}, loanAmount: {}", lenderId, borrowerId, loanAmount);
        } catch (Exception e) {
            logger.error("Exception during inserting dena for lenderId: {}, borrowerId: {}, loanAmount: {}. Error: {}", lenderId, borrowerId, loanAmount, e.getMessage());
            throw e;
        }
    }

    private void insertIntoPaona(Long lenderId, Long borrowerId,String lenderName, String borrowerName, int loanAmount, EntityManager entityManager) {
        try {
            entityManager.createNativeQuery(
                            "INSERT INTO paona (user_id,borrower_id, amount, borrower_name) VALUES (:lenderId, :borrowerId, :amount, :borrowerName)")
                    .setParameter("lenderId", lenderId)
                    .setParameter("amount", loanAmount)
                    .setParameter("borrowerId", borrowerId)
                    .setParameter("borrowerName", borrowerName)
                    .executeUpdate();
            logger.info("Successfully inserted into paona for lenderId: {}, borrowerId: {}, loanAmount: {}", lenderId, borrowerId, loanAmount);
        } catch (Exception e) {
            logger.error("Exception during inserting into paona for lenderId: {}, borrowerId: {}, loanAmount: {}. Error: {}", lenderId, borrowerId, loanAmount, e.getMessage());
            throw e;
        }
    }

    protected int getCurrentLoanAmount(Long lenderId, Long borrowerId, EntityManager entityManager) {
        try {
            Integer currPaona = Optional.ofNullable(
                    (Integer) entityManager.createNativeQuery(
                                    "SELECT amount FROM paona WHERE borrower_id = :borrowerId AND userId = :userId")
                            .setParameter("borrowerId", borrowerId)
                            .setParameter("userId", lenderId)
                            .getSingleResult()
            ).orElse(0);

            Integer currDena = Optional.ofNullable(
                    (Integer) entityManager.createNativeQuery(
                                    "SELECT amount FROM dena WHERE user_id = :borrowerId AND lender_id = :userId")
                            .setParameter("borrowerId", borrowerId)
                            .setParameter("userId", lenderId)
                            .getSingleResult()
            ).orElse(0);
            if(!currPaona.equals(currDena)){
                logger.error("dena and paona amount is doesn't match");
            }
            return currPaona;
        } catch (NoResultException e) {
            logger.info("No result found for the given lenderId:{} and borrowerId:{}",lenderId, borrowerId);
            return 0;
        }
    }

    private void updatePaonaAmount(Long borrowerId, Long lenderId, int loanAmount, EntityManager entityManager) {
        try {
            entityManager.createNativeQuery(
                            "UPDATE paona SET amount = :amount WHERE user_id = :lender_id AND borrower_id = :borrowerId")
                    .setParameter("lenderId", lenderId)
                    .setParameter("borrowerId", borrowerId)
                    .setParameter("amount", loanAmount)
                    .executeUpdate();
            logger.info("Successfully updated paona for lenderId: {}, borrowerId: {}, loanAmount: {}", lenderId, borrowerId, loanAmount);
        } catch (Exception e) {
            logger.error("Exception during updating paona for lenderId: {}, borrowerId: {}, loanAmount: {}. Error: {}", lenderId, borrowerId, loanAmount, e.getMessage());
            throw e;
        }
    }

    private void updateDenaAmount(Long borrowerId, Long lenderId, int loanAmount ,EntityManager entityManager) {
        try {
            entityManager.createNativeQuery(
                            "UPDATE dena SET amount = :amount WHERE lender_id = :lenderId AND user_id = :borrowerId")
                    .setParameter("lenderId", lenderId)
                    .setParameter("borrowerId", borrowerId)
                    .setParameter("amount", loanAmount)
                    .executeUpdate();
            logger.info("Successfully updated dena for lenderId: {}, borrowerId: {}, loanAmount: {}", lenderId, borrowerId, loanAmount);
        } catch (Exception e) {
            logger.error("Exception during updating dena for lenderId: {}, borrowerId: {}, loanAmount: {}. Error: {}", lenderId, borrowerId, loanAmount, e.getMessage());
            throw e;
        }
    }


    public void insertIndenaAndPaona(Long lenderId, Long borrowerId, String lenderName, String borrowerName, int loanAmount, EntityManager entityManager) {
        insertIntoDena(lenderId,borrowerId,lenderName, borrowerName, loanAmount, entityManager);
        insertIntoPaona(lenderId, borrowerId, lenderName, borrowerName, loanAmount, entityManager);
    }
}
