package com.ad.dena_paona.payload.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanNotificationInfo {

    private Long loanId;
    private Long userIdToNotify; // ID of the user (borrower or lender)
    private String userEmailToNotify; // Email of the user
    private BigDecimal amountDue;
    private LocalDate dueDate;
    private NotificationType notificationType;

    public enum NotificationType {
        DUE_SOON,
        OVERDUE
    }

    // Constructors
    public LoanNotificationInfo() {
    }

    public LoanNotificationInfo(Long loanId, Long userIdToNotify, String userEmailToNotify, BigDecimal amountDue, LocalDate dueDate, NotificationType notificationType) {
        this.loanId = loanId;
        this.userIdToNotify = userIdToNotify;
        this.userEmailToNotify = userEmailToNotify;
        this.amountDue = amountDue;
        this.dueDate = dueDate;
        this.notificationType = notificationType;
    }

    // Getters and Setters
    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getUserIdToNotify() {
        return userIdToNotify;
    }

    public void setUserIdToNotify(Long userIdToNotify) {
        this.userIdToNotify = userIdToNotify;
    }

    public String getUserEmailToNotify() {
        return userEmailToNotify;
    }

    public void setUserEmailToNotify(String userEmailToNotify) {
        this.userEmailToNotify = userEmailToNotify;
    }

    public BigDecimal getAmountDue() {
        return amountDue;
    }

    public void setAmountDue(BigDecimal amountDue) {
        this.amountDue = amountDue;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
    }

    @Override
    public String toString() {
        return "LoanNotificationInfo{" +
                "loanId=" + loanId +
                ", userIdToNotify=" + userIdToNotify +
                ", userEmailToNotify='" + userEmailToNotify + '\'' +
                ", amountDue=" + amountDue +
                ", dueDate=" + dueDate +
                ", notificationType=" + notificationType +
                '}';
    }
}
