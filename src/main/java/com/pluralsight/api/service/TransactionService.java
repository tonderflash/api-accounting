package com.pluralsight.api.service;

import com.pluralsight.api.adapter.CoreApplicationAdapter;
import com.pluralsight.api.dto.request.DepositRequest;
import com.pluralsight.api.dto.request.PaymentRequest;
import com.pluralsight.api.dto.response.TransactionResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final CoreApplicationAdapter coreAdapter;

    @Autowired
    public TransactionService(CoreApplicationAdapter coreAdapter) {
        this.coreAdapter = coreAdapter;
    }

    /**
     * Add a new deposit transaction
     * @param request the deposit details
     * @return true if successful
     */
    public boolean addDeposit(DepositRequest request) {
        return coreAdapter.addDeposit(
            request.getDescription(),
            request.getVendor(),
            request.getAmount()
        );
    }

    /**
     * Add a new payment transaction
     * @param request the payment details
     * @return true if successful
     */
    public boolean makePayment(PaymentRequest request) {
        return coreAdapter.makePayment(
            request.getDescription(),
            request.getVendor(),
            request.getAmount()
        );
    }

    /**
     * Get all transactions
     * @return list of all transactions
     */
    public List<TransactionResponse> getAllTransactions() {
        return coreAdapter.getAllTransactions()
            .stream()
            .map(this::mapToTransactionResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get only deposit transactions
     * @return list of deposit transactions
     */
    public List<TransactionResponse> getDeposits() {
        return coreAdapter.getAllTransactions()
            .stream()
            .filter(t -> "DEPOSIT".equals(t.getType()))
            .map(this::mapToTransactionResponse)
            .collect(Collectors.toList());
    }

    /**
     * Get only payment transactions
     * @return list of payment transactions
     */
    public List<TransactionResponse> getPayments() {
        return coreAdapter.getAllTransactions()
            .stream()
            .filter(t -> "PAYMENT".equals(t.getType()))
            .map(this::mapToTransactionResponse)
            .collect(Collectors.toList());
    }

    /**
     * Map adapter transaction to response DTO
     */
    private TransactionResponse mapToTransactionResponse(CoreApplicationAdapter.Transaction transaction) {
        return TransactionResponse.builder()
            .description(transaction.getDescription())
            .vendor(transaction.getVendor())
            .amount(transaction.getAmount())
            .type(transaction.getType())
            .date(transaction.getDate())
            .build();
    }
}
