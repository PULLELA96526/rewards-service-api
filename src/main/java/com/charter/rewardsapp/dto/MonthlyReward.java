package com.charter.rewardsapp.dto;

import lombok.Data;

@Data
public class MonthlyReward {
    private Long customerId;
    private String customerName;
    private String month;
    private Integer points;

    public MonthlyReward(Long customerId, String customerName, String month, Integer points) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.month = month;
        this.points = points;
    }

    public Long getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getMonth() { return month; }
    public Integer getPoints() { return points; }
}
