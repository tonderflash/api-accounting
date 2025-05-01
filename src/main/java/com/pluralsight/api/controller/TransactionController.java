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
    public ResponseEntity<Map<String, Object>> addDeposit(@Valid @RequestBody DepositRequest request) {
        boolean success = transactionService.addDeposit(request);
        
        if (success) {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Deposit added successfully",
                "data", request
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to add deposit"
            ));
        }
    }

    /**
     * Create a new payment
     */
    @PostMapping("/payment")
    public ResponseEntity<Map<String, Object>> makePayment(@Valid @RequestBody PaymentRequest request) {
        boolean success = transactionService.makePayment(request);
        
        if (success) {
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Payment added successfully",
                "data", request
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "message", "Failed to add payment"
            ));
        }
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
