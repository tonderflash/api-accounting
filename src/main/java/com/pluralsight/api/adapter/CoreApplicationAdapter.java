package com.pluralsight.api.adapter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
    @Value("${accounting.csv-path}")
    private String csvPath;

    /**
     * Transaction model representing data stored in CSV
     */
    public static class Transaction {
        private LocalDateTime date;
        private String description;
        private String vendor;
        private double amount;
        private String type; // "DEPOSIT" or "PAYMENT"

        public Transaction(LocalDateTime date, String description, String vendor, double amount, String type) {
            this.date = date;
            this.description = description;
            this.vendor = vendor;
            this.amount = amount;
            this.type = type;
        }

        public LocalDateTime getDate() { return date; }
        public String getDescription() { return description; }
        public String getVendor() { return vendor; }
        public double getAmount() { return amount; }
        public String getType() { return type; }
    }

    /**
     * Adds a deposit transaction 
     */
    public boolean addDeposit(String description, String vendor, double amount) {
        try {
            // Create a transaction record
            Transaction transaction = new Transaction(
                LocalDateTime.now(),
                description,
                vendor,
                amount,
                "DEPOSIT"
            );
            
            // Write to CSV file
            saveTransaction(transaction);
            
            logger.info("Deposit added successfully: {} from {}", amount, vendor);
            return true;
        } catch (Exception e) {
            logger.error("Error adding deposit: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Adds a payment transaction (stored as negative amount like the console app)
     */
    public boolean makePayment(String description, String vendor, double amount) {
        try {
            // Create a transaction record
            Transaction transaction = new Transaction(
                LocalDateTime.now(),
                description,
                vendor,
                -Math.abs(amount), // ensure negative value to align with console format
                "PAYMENT"
            );
            
            // Write to CSV file
            saveTransaction(transaction);
            
            logger.info("Payment made successfully: {} to {}", amount, vendor);
            return true;
        } catch (Exception e) {
            logger.error("Error making payment: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Saves a transaction to the CSV file
     */
    private synchronized void saveTransaction(Transaction transaction) throws Exception {
        // Use injected csvPath so we always write to the shared ledger file
        try (PrintWriter writer = new PrintWriter(new FileWriter(csvPath, true))) {
            /*
             * We persist using the same pipe-delimited format as the original
             * console application so both apps can read/write seamlessly.
             *   date|time|description|vendor|amount\n
             * NOTE: For payments the amount is negative, for deposits positive.
             */
            writer.printf("%s|%s|%s|%s|%.2f%n",
                transaction.getDate().toLocalDate().format(DATE_ONLY_FORMATTER),
                transaction.getDate().toLocalTime().format(TIME_ONLY_FORMATTER),
                transaction.getDescription(),
                transaction.getVendor(),
                transaction.getAmount());
        }
    }

    /**
     * Loads all transactions from the CSV file
     */
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        
        // Read from the shared CSV file configured in application.properties
        try (BufferedReader reader = new BufferedReader(new FileReader(csvPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip header if present
                if (line.startsWith("date")) {
                    continue;
                }

                // Split on either comma or pipe
                String[] parts = line.split("[|,]");
                if (parts.length != 5) {
                    continue; // malformed line
                }

                try {
                    LocalDateTime dateTime;
                    if (parts[0].contains(" ")) { // format "yyyy-MM-dd HH:mm:ss" (comma CSV)
                        dateTime = LocalDateTime.parse(parts[0] + " " + parts[1], DATE_FORMATTER);
                        // shift indexes by 1 for remaining fields? actually comma format had dateTime combined, so treat separately earlier. Simplify: fallback to old parser not needed
                    }
                } catch (Exception ignore) {}
                // console format: date | time | desc | vendor | amount
                try {
                    LocalDateTime dt = LocalDateTime.of(
                        LocalDateTime.parse(parts[0] + "T00:00:00").toLocalDate(), // quick parse date only
                        LocalTime.parse(parts[1], TIME_ONLY_FORMATTER)
                    );
                    double amt = Double.parseDouble(parts[4]);
                    String type = amt >= 0 ? "DEPOSIT" : "PAYMENT";

                    Transaction transaction = new Transaction(
                        dt,
                        parts[2],
                        parts[3],
                        amt,
                        type);
                    transactions.add(transaction);
                } catch (Exception e) {
                    logger.warn("Skipping malformed transaction line: {}", line);
                }
            }
        } catch (Exception e) {
            logger.error("Error loading transactions: {}", e.getMessage(), e);
        }
        
        return transactions;
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
