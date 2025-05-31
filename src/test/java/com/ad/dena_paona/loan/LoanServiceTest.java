package com.ad.dena_paona.loan;

import com.ad.dena_paona.entity.Loan;
import com.ad.dena_paona.payload.response.ApiResponse;
import com.ad.dena_paona.repository.LoanRepository;
import com.ad.dena_paona.service.LoanServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {
    private static Logger logger = LoggerFactory.getLogger(LoanServiceTest.class);
    @Mock
    private LoanRepository loanRepository;
    @InjectMocks
    private LoanServiceImpl loanService;
    List<Loan> loanSample = new ArrayList<>();

    @BeforeEach
    void setUp() {
        loanSample = Arrays.asList(
                new Loan(3L,7L, LocalDate.now()),
                new Loan(4L,4L, LocalDate.now())
        );
    }
    @Test
    void testGetDetailPaonaFromBorrower(){
        long borrowerId = 3;
        long userId = 7;
        Pageable pageable = PageRequest.of(0, 20);
        Page<Loan> loanPage = new PageImpl<>(loanSample, pageable, loanSample.size());

        when(loanRepository.getTransactionHistory(borrowerId, userId, pageable)).thenReturn(loanPage);
        ApiResponse result =  loanService.getDetailPaonaFromBorrower(borrowerId, userId);
        logger.debug("result : {}", result.getData());

        assertEquals(loanSample.size(), result.getData().size());
        assertEquals(loanSample, result.getData());
        //verify(loanRepository, times(1)).getTransactionHistory(borrowerId, userId, pageable);
    }
}
