package com.pluralsight.api.adapter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pluralsight.api.entity.TransactionEntity;
import com.pluralsight.api.repository.TransactionRepository;

/**
 * Adapter to interface with the core accounting application
 * without modifying its code.
 */
@Component
public class CoreApplicationAdapter {

    private static final Logger logger = LoggerFactory.getLogger(CoreApplicationAdapter.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // Separate formatters to match console application output
    private static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_ONLY_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Path of the shared CSV file. Injected from application.properties to
     * allow the API and the original console application to work over the
     * same ledger data without hard-coding paths.
     */
    private final TransactionRepository transactionRepository;

    @Autowired
    public CoreApplicationAdapter(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Transaction model ahora gestionado por JPA (TransactionEntity)
     */

    /**
     * Adds a deposit transaction and returns the saved entity with ID
     */
    public TransactionEntity addDeposit(String description, String vendor, double amount) {
        try {
            TransactionEntity transaction = new TransactionEntity();
            transaction.setDate(LocalDateTime.now());
            transaction.setDescription(description);
            transaction.setVendor(vendor);
            transaction.setAmount(amount);
            transaction.setType("DEPOSIT");
            TransactionEntity savedTransaction = transactionRepository.save(transaction);
            logger.info("Deposit added successfully: {} from {}, ID: {}", amount, vendor, savedTransaction.getId());
            return savedTransaction;
        } catch (Exception e) {
            logger.error("Error adding deposit: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to add deposit", e);
        }
    }

    /**
     * Adds a payment transaction (stored as negative amount) and returns the saved entity with ID
     */
    public TransactionEntity makePayment(String description, String vendor, double amount) {
        try {
            TransactionEntity transaction = new TransactionEntity();
            transaction.setDate(LocalDateTime.now());
            transaction.setDescription(description);
            transaction.setVendor(vendor);
            transaction.setAmount(-Math.abs(amount)); // ensure negative value
            transaction.setType("PAYMENT");
            TransactionEntity savedTransaction = transactionRepository.save(transaction);
            logger.info("Payment made successfully: {} to {}, ID: {}", amount, vendor, savedTransaction.getId());
            return savedTransaction;
        } catch (Exception e) {
            logger.error("Error making payment: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to make payment", e);
        }
    }



    /**
     * Loads all transactions from the database
     */
    public List<TransactionEntity> getAllTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Helper class to simulate Scanner input for integration with original app
     * if needed in the future
     */
    private static class MockScanner {
        private final Iterator<String> inputIterator;

        public MockScanner(List<String> inputs) {
            this.inputIterator = inputs.iterator();
        }

        public String nextLine() {
            return inputIterator.hasNext() ? inputIterator.next() : "";
        }
        
        // Add other Scanner methods as needed for your implementation
    }
}
