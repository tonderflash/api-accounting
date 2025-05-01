package com.pluralsight.api.service;

import com.pluralsight.api.adapter.CoreApplicationAdapter;
import com.pluralsight.api.dto.response.LedgerResponse;
import com.pluralsight.api.dto.response.TransactionResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LedgerService {

    private final TransactionService transactionService;

    @Autowired
    public LedgerService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Get complete ledger with transactions and totals
     * @return ledger with all transactions and summary
     */
    public LedgerResponse getLedger() {
        List<TransactionResponse> transactions = transactionService.getAllTransactions();
        
        double totalDeposits = transactions.stream()
            .filter(t -> "DEPOSIT".equals(t.getType()))
            .mapToDouble(TransactionResponse::getAmount)
            .sum();
            
        double totalPayments = transactions.stream()
            .filter(t -> "PAYMENT".equals(t.getType()))
            .mapToDouble(TransactionResponse::getAmount)
            .sum();
            
        return LedgerResponse.builder()
            .transactions(transactions)
            .totalDeposits(totalDeposits)
            .totalPayments(totalPayments)
            .balance(totalDeposits - totalPayments)
            .count(transactions.size())
            .build();
    }

    /**
     * Get ledger with only deposits
     * @return ledger with deposit transactions and summary
     */
    public LedgerResponse getDepositsLedger() {
        List<TransactionResponse> transactions = transactionService.getDeposits();
        
        double totalDeposits = transactions.stream()
            .mapToDouble(TransactionResponse::getAmount)
            .sum();
            
        return LedgerResponse.builder()
            .transactions(transactions)
            .totalDeposits(totalDeposits)
            .totalPayments(0)
            .balance(totalDeposits)
            .count(transactions.size())
            .build();
    }

    /**
     * Get ledger with only payments
     * @return ledger with payment transactions and summary
     */
    public LedgerResponse getPaymentsLedger() {
        List<TransactionResponse> transactions = transactionService.getPayments();
        
        double totalPayments = transactions.stream()
            .mapToDouble(TransactionResponse::getAmount)
            .sum();
            
        return LedgerResponse.builder()
            .transactions(transactions)
            .totalDeposits(0)
            .totalPayments(totalPayments)
            .balance(-totalPayments)
            .count(transactions.size())
            .build();
    }
}
