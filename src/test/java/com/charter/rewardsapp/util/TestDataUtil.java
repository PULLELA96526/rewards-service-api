package com.charter.rewardsapp.util;

import com.charter.rewardsapp.dto.TransactionRequest;
import com.charter.rewardsapp.entity.Transaction;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class TestDataUtil {

    public static List<Transaction> createTestTransactions() {
        return Arrays.asList(
                new Transaction(1L, "Rajkumar", 120.0, LocalDate.now().minusDays(15), 90),
                new Transaction(2L, "Suresh", 130.0, LocalDate.now().minusDays(10), 120),
                new Transaction(3L, "Ramesh", 140.0, LocalDate.now().minusDays(5), 90),
                new Transaction(4L, "Naresh", 150.0, LocalDate.now().minusDays(17), 90),
                new Transaction(5L, "Somesh", 160.0, LocalDate.now().minusDays(20), 90)
        );
    }

    public static TransactionRequest createValidTransactionRequest() {
        return new TransactionRequest(101L, "Rajkumar", 120.0, LocalDate.now());
    }

    public static TransactionRequest createInvalidTransactionRequest() {
        return new TransactionRequest(null, "", -50.0, LocalDate.now().plusDays(1));
    }
}
