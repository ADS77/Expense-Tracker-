package com.ad.dena_paona.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DBOperation {
    private static final Logger logger = LoggerFactory.getLogger(DBOperation.class);

    protected boolean checkExistsOrNot(Long lenderId, Long borrowerId, EntityManager entityManager){
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


    protected void insertIntoDena(Long lenderId, Long borrowerId, int loanAmount, EntityManager entityManager) {
        try {
            entityManager.createNativeQuery(
                            "INSERT INTO dena (userId,lenderId, amount) VALUES (:borrowerId, :lenderId, :amount)")
                    .setParameter("lenderId", lenderId)
                    .setParameter("amount", loanAmount)
                    .setParameter("borrowerId", borrowerId)
                    .executeUpdate();
            logger.info("Successfully inserted into dena for lenderId: {}, borrowerId: {}, loanAmount: {}", lenderId, borrowerId, loanAmount);
        } catch (Exception e) {
            logger.error("Exception during inserting dena for lenderId: {}, borrowerId: {}, loanAmount: {}. Error: {}", lenderId, borrowerId, loanAmount, e.getMessage());
            throw e;
        }
    }

    protected void insertIntoPaona(Long lenderId, Long borrowerId, int loanAmount, EntityManager entityManager) {
        try {
            entityManager.createNativeQuery(
                            "INSERT INTO paona (userId,borrowerId, amount) VALUES (:lenderId, :borrowerId, :amount)")
                    .setParameter("lenderId", lenderId)
                    .setParameter("amount", loanAmount)
                    .setParameter("borrowerId", borrowerId)
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
                logger.error("dena and paona amount is doesn't match");
            }
            return currPaona;
        } catch (NoResultException e) {
            logger.info("No result found for the given lenderId and borrowerId");
            return 0;
        }
    }

    protected void updatePaonaAmount(Long borrowerId, Long lenderId, int loanAmount, EntityManager entityManager) {
        try {
            entityManager.createNativeQuery(
                            "UPDATE paona SET amount = :amount WHERE userId = :lenderId AND borrowerId = :borrowerId")
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

    protected void updateDenaAmount(Long borrowerId, Long lenderId, int loanAmount ,EntityManager entityManager) {
        try {
            entityManager.createNativeQuery(
                            "UPDATE dena SET amount = :amount WHERE lenderId = :lenderId AND userId = :borrowerId")
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


}
