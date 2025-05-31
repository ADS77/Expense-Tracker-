package com.ad.dena_paona.scheduler;

import com.ad.dena_paona.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotificationScheduler {

    private static final Logger logger = LoggerFactory.getLogger(NotificationScheduler.class);
    private final NotificationService notificationService;

    @Autowired
    public NotificationScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // Schedule to run once a day at 2 AM, for example.
    // Cron expression: second, minute, hour, day of month, month, day(s) of week
    // See https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/scheduling/support/CronExpression.html
    @Scheduled(cron = "0 0 2 * * ?") // Run daily at 2:00 AM
    // For testing, you might want a more frequent schedule, e.g., every 5 minutes: "0 */5 * * * ?"
    // Or every minute: "0 * * * * ?"
    public void scheduleDueDateNotifications() {
        logger.info("NotificationScheduler: Triggering scheduled due date notification processing.");
        notificationService.processAndSendDueDateNotifications();
        logger.info("NotificationScheduler: Finished scheduled due date notification processing.");
    }
}
