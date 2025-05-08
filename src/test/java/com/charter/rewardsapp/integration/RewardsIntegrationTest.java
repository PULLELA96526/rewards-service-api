package com.charter.rewardsapp.integration;

import com.charter.rewardsapp.dto.RewardSummary;
import com.charter.rewardsapp.dto.TransactionRequest;
import com.charter.rewardsapp.entity.Transaction;
import com.charter.rewardsapp.util.TestDataUtil;
import com.charter.rewardsapp.RewardsappApplication;
import com.charter.rewardsapp.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = RewardsappApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RewardsIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TransactionRepository transactionRepository;

    @BeforeEach
    void setUp() {

        transactionRepository.deleteAll();
    }

    @Test
    void whenCreateTransaction_thenCalculatePointsAndSave() {
        TransactionRequest request = TestDataUtil.createValidTransactionRequest();
        String url = "http://localhost:" + port + "/api/rewards/transactions";
        ResponseEntity<Transaction> response = restTemplate.postForEntity(url, request, Transaction.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getPoints()).isEqualTo(90);

        List<Transaction> transactions = transactionRepository.findAll();
        assertEquals(1, transactions.size());  // Verify list size
        assertEquals(90, transactions.get(0).getPoints());  // Verify points value
    }

    @Test
    void whenGetCustomerRewards_thenReturnCorrectSummary() {
        LocalDate now = LocalDate.now();
        transactionRepository.saveAll(List.of(
                new Transaction(101L, "Rajkumar", 120.0, now.minusMonths(1), 90),
                new Transaction(101L, "Naresh", 80.0, now.minusDays(15), 30),
                new Transaction(102L, "Ramesh", 150.0, now, 150)
        ));

        String url = "http://localhost:" + port + "/api/rewards/customer/101";
        ResponseEntity<RewardSummary> response = restTemplate.getForEntity(url, RewardSummary.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getCustomerId()).isEqualTo(101L);
        assertThat(response.getBody().getTotalPoints()).isEqualTo(120);
    }
}
