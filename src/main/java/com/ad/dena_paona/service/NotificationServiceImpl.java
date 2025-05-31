package com.ad.dena_paona.service;

import com.ad.dena_paona.payload.response.LoanNotificationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final LoanService loanService;
    private static final int DAYS_IN_ADVANCE_FOR_DUE_SOON = 7; // Notify 7 days in advance

    @Autowired
    public NotificationServiceImpl(LoanService loanService) {
        this.loanService = loanService;
    }

    @Override
    public void processAndSendDueDateNotifications() {
        logger.info("Starting processAndSendDueDateNotifications job.");

        List<LoanNotificationInfo> dueSoonNotifications = loanService.getDueSoonNotifications(DAYS_IN_ADVANCE_FOR_DUE_SOON);
        logger.info("Found {} loans due soon.", dueSoonNotifications.size());
        for (LoanNotificationInfo notification : dueSoonNotifications) {
            // In a real system, this would involve sending an email, SMS, or push notification.
            // For now, we just log it.
            logger.info("NOTIFICATION (DUE SOON): Loan ID: {}, Borrower ID: {}, Email: {}, Amount: {}, Due Date: {}",
                    notification.getLoanId(),
                    notification.getUserIdToNotify(),
                    notification.getUserEmailToNotify(),
                    notification.getAmountDue(),
                    notification.getDueDate());
            // System.out.println("Notification (DUE SOON): " + notification.toString()); // Alternative logging
        }

        List<LoanNotificationInfo> overdueNotifications = loanService.getOverdueNotifications();
        logger.info("Found {} overdue loans.", overdueNotifications.size());
        for (LoanNotificationInfo notification : overdueNotifications) {
            // Log overdue notifications
            logger.info("NOTIFICATION (OVERDUE): Loan ID: {}, Borrower ID: {}, Email: {}, Amount: {}, Due Date: {}",
                    notification.getLoanId(),
                    notification.getUserIdToNotify(),
                    notification.getUserEmailToNotify(),
                    notification.getAmountDue(),
                    notification.getDueDate());
            // System.out.println("Notification (OVERDUE): " + notification.toString()); // Alternative logging
        }
        logger.info("Finished processAndSendDueDateNotifications job.");
    }
}
