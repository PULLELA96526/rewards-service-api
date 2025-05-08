package com.charter.rewardsapp.service;

import com.charter.rewardsapp.dto.RewardSummary;
import com.charter.rewardsapp.dto.TransactionRequest;
import com.charter.rewardsapp.entity.Transaction;
import com.charter.rewardsapp.exception.ResourceNotFoundException;
import com.charter.rewardsapp.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RewardService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Integer calculatePoints(Double amount) {
        if (amount == null || amount <= 0) {
            return 0;
        }

        int points = 0;
        double remainingAmount = amount;

        // Calculate points for amount over $100
        if (remainingAmount > 100) {
            points += (int) ((remainingAmount - 100) * 2);
            remainingAmount = 100;
        }

        // Calculate points for amount between $50-$100
        if (remainingAmount > 50) {
            points += (int) (remainingAmount - 50);
        }

        return points;
    }

    public RewardSummary getCustomerRewardSummary(Long customerId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(2);

        List<Transaction> transactions = transactionRepository
                .findByCustomerIdAndTransactionDateBetween(customerId, startDate, endDate);

        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }

        Map<String, Integer> monthlyPoints = new LinkedHashMap<>();
        int totalPoints = 0;

        for (Transaction t : transactions) {
            String month = t.getTransactionDate().getMonth().toString();
            monthlyPoints.merge(month, t.getPoints(), Integer::sum);
            totalPoints += t.getPoints();
        }

        return new RewardSummary(
                customerId,
                transactions.get(0).getCustomerName(),
                monthlyPoints,
                totalPoints
        );
    }

    public Transaction createTransaction(TransactionRequest request) {
        Integer points = calculatePoints(request.getAmount());
        Transaction transaction = new Transaction(
                request.getCustomerId(),
                request.getCustomerName(),
                request.getAmount(),
                request.getTransactionDate(),
                points
        );
        return transactionRepository.save(transaction);
    }
}