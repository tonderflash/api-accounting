package com.pluralsight.api.controller;

import com.pluralsight.api.dto.response.LedgerResponse;
import com.pluralsight.api.service.LedgerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ledger")
public class LedgerController {

    private final LedgerService ledgerService;

    @Autowired
    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    /**
     * Get the complete ledger with all transactions
     */
    @GetMapping
    public ResponseEntity<LedgerResponse> getLedger() {
        return ResponseEntity.ok(ledgerService.getLedger());
    }

    /**
     * Get ledger with only deposits
     */
    @GetMapping("/deposits")
    public ResponseEntity<LedgerResponse> getDepositsLedger() {
        return ResponseEntity.ok(ledgerService.getDepositsLedger());
    }

    /**
     * Get ledger with only payments
     */
    @GetMapping("/payments")
    public ResponseEntity<LedgerResponse> getPaymentsLedger() {
        return ResponseEntity.ok(ledgerService.getPaymentsLedger());
    }
}
