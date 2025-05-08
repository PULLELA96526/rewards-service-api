package com.charter.rewardsapp.controller;

import com.charter.rewardsapp.dto.RewardSummary;
import com.charter.rewardsapp.dto.TransactionRequest;
import com.charter.rewardsapp.entity.Transaction;
import com.charter.rewardsapp.service.RewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;


@ExtendWith(MockitoExtension.class)
class RewardsControllerTest {

    @Mock
    private RewardService rewardService;

    @InjectMocks
    private RewardsController rewardsController;

    private TransactionRequest validRequest;
    private TransactionRequest invalidRequest;
    private RewardSummary rewardSummary;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        // Setup test data
        validRequest = new TransactionRequest(1L, "Suresh", 120.0, LocalDate.now());
        invalidRequest = new TransactionRequest(null, null, -50.0, null);

        Map<String, Integer> monthlyPoints = new HashMap<>();
        monthlyPoints.put("JANUARY", 90);
        monthlyPoints.put("FEBRUARY", 120);
        rewardSummary = new RewardSummary(1L, "Suresh", monthlyPoints, 210);

        transaction = new Transaction();
        transaction.setId(1L);
        transaction.setCustomerId(1L);
        transaction.setAmount(Double.valueOf(120.0));
        transaction.setTransactionDate(LocalDate.now());
    }

    // Positive Test Cases for getCustomerRewards
    @Test
    void getCustomerRewards_ValidCustomerId_ReturnsRewardSummary() {

        when(rewardService.getCustomerRewardSummary(anyLong())).thenReturn(rewardSummary);


        ResponseEntity<RewardSummary> response = rewardsController.getCustomerRewards(1L);

       
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getCustomerId());
        assertEquals("Suresh", response.getBody().getCustomerName());
        assertEquals(210, response.getBody().getTotalPoints());
        verify(rewardService, times(1)).getCustomerRewardSummary(1L);
    }

    @Test
    void getCustomerRewards_ValidCustomerId_ReturnsCorrectMonthlyBreakdown() {

        when(rewardService.getCustomerRewardSummary(anyLong())).thenReturn(rewardSummary);


        ResponseEntity<RewardSummary> response = rewardsController.getCustomerRewards(1L);


        assertNotNull(response.getBody().getMonthlyPoints());
        assertEquals(2, response.getBody().getMonthlyPoints().size());
        assertEquals(90, response.getBody().getMonthlyPoints().get("JANUARY"));
        assertEquals(120, response.getBody().getMonthlyPoints().get("FEBRUARY"));
    }

    // Negative Test Cases for getCustomerRewards
    @Test
    void getCustomerRewards_NonExistentCustomerId_ReturnsEmptyData() {

        when(rewardService.getCustomerRewardSummary(anyLong())).thenReturn(
                new RewardSummary(null, null, new HashMap<>(), 0));


        ResponseEntity<RewardSummary> response = rewardsController.getCustomerRewards(999L);


        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody().getCustomerId());
        assertNull(response.getBody().getCustomerName());
        assertTrue(response.getBody().getMonthlyPoints().isEmpty());
        assertEquals(0, response.getBody().getTotalPoints());
    }

    // Positive Test Cases for createTransaction
    @Test
    void createTransaction_ValidRequest_ReturnsCreatedTransaction() {

        when(rewardService.createTransaction(any(TransactionRequest.class))).thenReturn(transaction);


        ResponseEntity<Transaction> response = rewardsController.createTransaction(validRequest);


        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
        assertEquals(1L, response.getBody().getCustomerId());
        assertEquals(Double.valueOf(120.0), response.getBody().getAmount());
        verify(rewardService, times(1)).createTransaction(validRequest);
    }

    @Test
    void createTransaction_ValidRequest_CallsServiceWithCorrectParameters() {

        when(rewardService.createTransaction(any(TransactionRequest.class))).thenReturn(transaction);


        rewardsController.createTransaction(validRequest);


        verify(rewardService).createTransaction(argThat(req ->
                req.getCustomerId().equals(1L) &&
                        req.getCustomerName().equals("Suresh") &&
                        req.getAmount().equals(120.0) &&
                        req.getTransactionDate() != null
        ));
    }

    // Negative Test Cases for createTransaction
    @Test
    void createTransaction_NullRequest_ThrowsException() {

        when(rewardService.createTransaction(isNull())).thenThrow(new IllegalArgumentException("Transaction request cannot be null"));


        assertThrows(IllegalArgumentException.class, () -> {
            rewardsController.createTransaction(null);
        });
    }

    @Test
    void createTransaction_InvalidRequest_NegativeAmount_ThrowsException() {

        when(rewardService.createTransaction(any(TransactionRequest.class)))
                .thenThrow(new IllegalArgumentException("Amount cannot be negative"));


        assertThrows(IllegalArgumentException.class, () -> {
            rewardsController.createTransaction(invalidRequest);
        });
    }

    @Test
    void createTransaction_MissingCustomerId_ThrowsException() {

        TransactionRequest missingIdRequest = new TransactionRequest(null, "Suresh", 100.0, LocalDate.now());
        when(rewardService.createTransaction(any(TransactionRequest.class)))
                .thenThrow(new IllegalArgumentException("Customer ID is required"));


        assertThrows(IllegalArgumentException.class, () -> {
            rewardsController.createTransaction(missingIdRequest);
        });
    }
}
