package com.ad.dena_paona.service;

import com.ad.dena_paona.entity.Loan;
import com.ad.dena_paona.entity.User;
import com.ad.dena_paona.payload.response.LoanNotificationInfo;
import com.ad.dena_paona.repository.LoanRepository;
import com.ad.dena_paona.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanServiceImplTests {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    private Loan testLoan;
    private User testBorrower;

    @BeforeEach
    void setUp() {
        testBorrower = new User();
        testBorrower.setUserId(1L);
        testBorrower.setEmail("borrower@example.com");
        testBorrower.setUserName("testBorrower");

        testLoan = new Loan();
        testLoan.setLoanId(100L);
        testLoan.setBorrowerId(testBorrower.getUserId());
        testLoan.setLenderId(2L);
        testLoan.setAmount(1000); // int amount
        testLoan.setDueDate(LocalDate.now().plusDays(5));
        // Loan status is not used for active/paid determination in current logic
    }

    @Test
    void getDueSoonNotifications_shouldReturnNotificationsForDueSoonLoans() {
        int daysInAdvance = 7;
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(daysInAdvance);

        when(loanRepository.findActiveLoansDueSoon(today, endDate)).thenReturn(Collections.singletonList(testLoan));
        when(userRepository.findById(testBorrower.getUserId())).thenReturn(Optional.of(testBorrower));

        List<LoanNotificationInfo> notifications = loanService.getDueSoonNotifications(daysInAdvance);

        assertNotNull(notifications);
        assertEquals(1, notifications.size());
        LoanNotificationInfo info = notifications.get(0);
        assertEquals(testLoan.getLoanId(), info.getLoanId());
        assertEquals(testBorrower.getUserId(), info.getUserIdToNotify());
        assertEquals(testBorrower.getEmail(), info.getUserEmailToNotify());
        assertEquals(BigDecimal.valueOf(testLoan.getAmount()), info.getAmountDue());
        assertEquals(testLoan.getDueDate(), info.getDueDate());
        assertEquals(LoanNotificationInfo.NotificationType.DUE_SOON, info.getNotificationType());

        verify(loanRepository).findActiveLoansDueSoon(today, endDate);
        verify(userRepository).findById(testBorrower.getUserId());
    }

    @Test
    void getDueSoonNotifications_shouldReturnEmptyListWhenNoLoansDueSoon() {
        int daysInAdvance = 7;
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(daysInAdvance);

        when(loanRepository.findActiveLoansDueSoon(today, endDate)).thenReturn(Collections.emptyList());

        List<LoanNotificationInfo> notifications = loanService.getDueSoonNotifications(daysInAdvance);

        assertNotNull(notifications);
        assertTrue(notifications.isEmpty());
        verify(loanRepository).findActiveLoansDueSoon(today, endDate);
        verify(userRepository, never()).findById(any());
    }

    @Test
    void getOverdueNotifications_shouldReturnNotificationsForOverdueLoans() {
        testLoan.setDueDate(LocalDate.now().minusDays(1)); // Make it overdue
        LocalDate today = LocalDate.now();

        when(loanRepository.findActiveOverdueLoans(today)).thenReturn(Collections.singletonList(testLoan));
        when(userRepository.findById(testBorrower.getUserId())).thenReturn(Optional.of(testBorrower));

        List<LoanNotificationInfo> notifications = loanService.getOverdueNotifications();

        assertNotNull(notifications);
        assertEquals(1, notifications.size());
        LoanNotificationInfo info = notifications.get(0);
        assertEquals(testLoan.getLoanId(), info.getLoanId());
        // ... (similar assertions as in getDueSoonNotifications_shouldReturnNotificationsForDueSoonLoans)
        assertEquals(testBorrower.getEmail(), info.getUserEmailToNotify());
        assertEquals(LoanNotificationInfo.NotificationType.OVERDUE, info.getNotificationType());


        verify(loanRepository).findActiveOverdueLoans(today);
        verify(userRepository).findById(testBorrower.getUserId());
    }

    @Test
    void getOverdueNotifications_shouldReturnEmptyListWhenNoLoansOverdue() {
        LocalDate today = LocalDate.now();
        when(loanRepository.findActiveOverdueLoans(today)).thenReturn(Collections.emptyList());

        List<LoanNotificationInfo> notifications = loanService.getOverdueNotifications();

        assertNotNull(notifications);
        assertTrue(notifications.isEmpty());
        verify(loanRepository).findActiveOverdueLoans(today);
        verify(userRepository, never()).findById(any());
    }
     @Test
    void getDueSoonNotifications_handlesUserNotFound() {
        int daysInAdvance = 7;
        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusDays(daysInAdvance);

        when(loanRepository.findActiveLoansDueSoon(today, endDate)).thenReturn(Collections.singletonList(testLoan));
        when(userRepository.findById(testBorrower.getUserId())).thenReturn(Optional.empty()); // User not found

        List<LoanNotificationInfo> notifications = loanService.getDueSoonNotifications(daysInAdvance);

        assertNotNull(notifications);
        assertEquals(1, notifications.size());
        LoanNotificationInfo info = notifications.get(0);
        assertNull(info.getUserEmailToNotify()); // Email should be null

        verify(userRepository).findById(testBorrower.getUserId());
    }
}
