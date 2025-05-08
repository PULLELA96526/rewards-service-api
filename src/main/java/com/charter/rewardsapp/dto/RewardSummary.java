package com.charter.rewardsapp.dto;

import java.util.Map;

public class RewardSummary {
    private Long customerId;
    private String customerName;
    private Map<String, Integer> monthlyPoints;
    private Integer totalPoints;

    public RewardSummary(Long customerId, String customerName,
                         Map<String, Integer> monthlyPoints, Integer totalPoints) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.monthlyPoints = monthlyPoints;
        this.totalPoints = totalPoints;
    }

    public Long getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public Map<String, Integer> getMonthlyPoints() { return monthlyPoints; }
    public Integer getTotalPoints() { return totalPoints; }
}
