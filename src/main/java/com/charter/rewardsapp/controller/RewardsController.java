package com.charter.rewardsapp.controller;

import com.charter.rewardsapp.dto.RewardSummary;
import com.charter.rewardsapp.dto.TransactionRequest;
import com.charter.rewardsapp.entity.Transaction;
import com.charter.rewardsapp.service.RewardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/rewards")
public class RewardsController {

    @Autowired
    private RewardService rewardService;

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<RewardSummary> getCustomerRewards(@PathVariable Long customerId) {
        return ResponseEntity.ok(rewardService.getCustomerRewardSummary(customerId));
    }

    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransaction(
            @RequestBody TransactionRequest request) {
        return ResponseEntity.ok(rewardService.createTransaction(request));
    }

}
