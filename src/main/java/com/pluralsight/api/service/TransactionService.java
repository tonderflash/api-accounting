package com.pluralsight.api.service;

import com.pluralsight.api.adapter.CoreApplicationAdapter;
import com.pluralsight.api.dto.request.DepositRequest;
import com.pluralsight.api.dto.request.PaymentRequest;
import com.pluralsight.api.dto.response.TransactionResponse;
import com.pluralsight.api.entity.TransactionEntity;
import com.pluralsight.api.util.TransactionMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final CoreApplicationAdapter coreAdapter;
    private final TransactionMapper mapper;

    @Autowired
    public TransactionService(CoreApplicationAdapter coreAdapter, TransactionMapper mapper) {
        this.coreAdapter = coreAdapter;
        this.mapper = mapper;
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
            .map(mapper::toTransactionResponse)
            .toList();
    }

    /**
     * Get only deposit transactions
     * @return list of deposit transactions
     */
    public List<TransactionResponse> getDeposits() {
        return coreAdapter.getAllTransactions()
            .stream()
            .filter(t -> "DEPOSIT".equals(t.getType()))
            .map(mapper::toTransactionResponse)
            .toList();
    }

    /**
     * Get only payment transactions
     * @return list of payment transactions
     */
    public List<TransactionResponse> getPayments() {
        return coreAdapter.getAllTransactions()
            .stream()
            .filter(t -> "PAYMENT".equals(t.getType()))
            .map(mapper::toTransactionResponse)
            .toList();
    }
}
