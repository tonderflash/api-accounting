package com.pluralsight.api.util;

import com.pluralsight.api.dto.response.TransactionResponse;
import com.pluralsight.api.entity.TransactionEntity;

import org.springframework.stereotype.Component;

/**
 * Utility class to map between core application entities and API DTOs
 */
@Component
public class TransactionMapper {

    /**
     * Maps from core Transaction to TransactionResponse DTO
     */
    public TransactionResponse toTransactionResponse(TransactionEntity transaction) {
        return TransactionResponse.builder()
            .description(transaction.getDescription())
            .vendor(transaction.getVendor())
            .amount(transaction.getAmount())
            .type(transaction.getType())
            .date(transaction.getDate())
            .build();
    }
}
