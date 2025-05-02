package com.pluralsight.api.controller;

import com.pluralsight.api.dto.request.DepositRequest;
import com.pluralsight.api.dto.request.PaymentRequest;
import com.pluralsight.api.dto.response.TransactionResponse;
import com.pluralsight.api.service.TransactionService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Create a new deposit
     */
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> addDeposit(@Valid @RequestBody DepositRequest request) {
        TransactionResponse transaction = transactionService.addDeposit(request);
        return ResponseEntity.ok(transaction);
    }

    /**
     * Create a new payment
     */
    @PostMapping("/payment")
    public ResponseEntity<TransactionResponse> makePayment(@Valid @RequestBody PaymentRequest request) {
        TransactionResponse transaction = transactionService.makePayment(request);
        return ResponseEntity.ok(transaction);
    }

    /**
     * Get all transactions
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    /**
     * Get only deposits
     */
    @GetMapping("/deposits")
    public ResponseEntity<List<TransactionResponse>> getDeposits() {
        return ResponseEntity.ok(transactionService.getDeposits());
    }

    /**
     * Get only payments
     */
    @GetMapping("/payments")
    public ResponseEntity<List<TransactionResponse>> getPayments() {
        return ResponseEntity.ok(transactionService.getPayments());
    }
}
