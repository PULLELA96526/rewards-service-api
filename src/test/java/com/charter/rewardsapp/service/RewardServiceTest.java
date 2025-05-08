package com.charter.rewardsapp.service;

import com.charter.rewardsapp.dto.RewardSummary;
import com.charter.rewardsapp.dto.TransactionRequest;
import com.charter.rewardsapp.entity.Transaction;
import com.charter.rewardsapp.util.TestDataUtil;
import com.charter.rewardsapp.exception.ResourceNotFoundException;
import com.charter.rewardsapp.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RewardServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private RewardService rewardService;

    @Test
    void calculatePoints_shouldReturnCorrectPoints() {
        assertThat(rewardService.calculatePoints(120.0)).isEqualTo(90);
        assertThat(rewardService.calculatePoints(80.0)).isEqualTo(30);
        assertThat(rewardService.calculatePoints(150.0)).isEqualTo(150);
        assertThat(rewardService.calculatePoints(45.0)).isEqualTo(0);
        assertThat(rewardService.calculatePoints(null)).isEqualTo(0);
        assertThat(rewardService.calculatePoints(-10.0)).isEqualTo(0);
    }

    @Test
    void getCustomerRewardSummary_shouldReturnCorrectSummary() {
        LocalDate now = LocalDate.now();
        List<Transaction> transactions = Arrays.asList(
                new Transaction(1L, "Rajkumar", 120.0, LocalDate.now().minusDays(15), 90),
                new Transaction(2L, "Suresh", 130.0, LocalDate.now().minusDays(15), 100),
                new Transaction(3L, "Naresh", 50.0, LocalDate.now().minusDays(15), 70)
        );

        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(
                anyLong(), any(LocalDate.class), any(LocalDate.class))
        ).thenReturn(transactions);

        RewardSummary summary = rewardService.getCustomerRewardSummary(101L);

        assertThat(summary.getCustomerId()).isEqualTo(101L);
        assertThat(summary.getCustomerName()).isEqualTo("Rajkumar");
        assertThat(summary.getTotalPoints()).isEqualTo(260);
        assertThat(summary.getMonthlyPoints()).hasSize(1);
    }

    @Test
    void getCustomerRewardSummary_whenNoTransactions_shouldThrowException() {

        when(transactionRepository.findByCustomerIdAndTransactionDateBetween(
                anyLong(), any(LocalDate.class), any(LocalDate.class))
        ).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () -> {
            rewardService.getCustomerRewardSummary(999L);
        });
    }

    @Test
    void createTransaction_shouldSaveWithCalculatedPoints() {
        TransactionRequest request = TestDataUtil.createValidTransactionRequest();
        Transaction savedTransaction = new Transaction(
                request.getCustomerId(),
                request.getCustomerName(),
                request.getAmount(),
                request.getTransactionDate(),
                90
        );

        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);
        Transaction result = rewardService.createTransaction(request);

        assertThat(result.getPoints()).isEqualTo(90);
        verify(transactionRepository, times(1)).save(any(Transaction.class));


    }
    @Test
    void calculatePoints_withNegativeAmount_shouldReturnZero() {
        assertThat(rewardService.calculatePoints(-50.0)).isEqualTo(0);
    }

    @Test
    void calculatePoints_withNullAmount_shouldReturnZero() {
        assertThat(rewardService.calculatePoints(null)).isEqualTo(0);
    }

    @Test
    void createTransaction_withNullRequest_shouldThrowException() {
        assertThrows(NullPointerException.class, () -> {
            rewardService.createTransaction(null);
        });
    }
}