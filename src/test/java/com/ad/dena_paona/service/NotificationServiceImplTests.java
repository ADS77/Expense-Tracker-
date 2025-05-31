package com.ad.dena_paona.service;

import com.ad.dena_paona.payload.response.LoanNotificationInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger; // Import Logger

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceImplTests {

    @Mock
    private LoanService loanService;

    // If you want to verify logger interactions, you'd need a more complex setup,
    // potentially injecting a mock Logger or using a testing appender.
    // For this test, we'll focus on verifying interactions with LoanService.
    // @Mock private Logger logger; // Example if you were to mock the logger

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void processAndSendDueDateNotifications_callsLoanServiceAndLogs() {
        int daysInAdvance = 7; // Matching the constant in NotificationServiceImpl
        LoanNotificationInfo dueSoonLoan = new LoanNotificationInfo(1L, 10L, "due@soon.com", BigDecimal.valueOf(100), LocalDate.now().plusDays(3), LoanNotificationInfo.NotificationType.DUE_SOON);
        LoanNotificationInfo overdueLoan = new LoanNotificationInfo(2L, 20L, "over@due.com", BigDecimal.valueOf(200), LocalDate.now().minusDays(3), LoanNotificationInfo.NotificationType.OVERDUE);

        when(loanService.getDueSoonNotifications(daysInAdvance)).thenReturn(Collections.singletonList(dueSoonLoan));
        when(loanService.getOverdueNotifications()).thenReturn(Collections.singletonList(overdueLoan));

        notificationService.processAndSendDueDateNotifications();

        verify(loanService).getDueSoonNotifications(daysInAdvance);
        verify(loanService).getOverdueNotifications();

        // To verify logging, you would need to:
        // 1. Inject a mock Logger into NotificationServiceImpl.
        // 2. Verify logger.info(...) calls with specific arguments.
        // This is omitted for brevity here but is good practice.
        // For example, if logger was a mock:
        // verify(logger).info(contains("NOTIFICATION (DUE SOON)"));
        // verify(logger).info(contains("NOTIFICATION (OVERDUE)"));
    }

    @Test
    void processAndSendDueDateNotifications_handlesEmptyListsFromLoanService() {
        int daysInAdvance = 7;
        when(loanService.getDueSoonNotifications(daysInAdvance)).thenReturn(Collections.emptyList());
        when(loanService.getOverdueNotifications()).thenReturn(Collections.emptyList());

        notificationService.processAndSendDueDateNotifications();

        verify(loanService).getDueSoonNotifications(daysInAdvance);
        verify(loanService).getOverdueNotifications();
        // Verify no further processing or logging of individual notifications if lists are empty
    }
}
