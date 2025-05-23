package com.charter.rewardsapp.repository;

import com.charter.rewardsapp.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByCustomerIdAndTransactionDateBetween(
            Long customerId,
            LocalDate startDate,
            LocalDate endDate
    );

}
