package com.charter.rewardsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    private Long customerId;

    private String customerName;

    private Double amount;

    private LocalDate transactionDate;
}
