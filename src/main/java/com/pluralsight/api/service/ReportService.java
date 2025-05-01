package com.pluralsight.api.service;

import com.pluralsight.api.dto.response.LedgerResponse;
import com.pluralsight.api.dto.response.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final TransactionService transactionService;

    @Autowired
    public ReportService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * Get month-to-date report
     */
    public LedgerResponse getMonthToDateReport() {
        LocalDateTime startOfMonth = LocalDateTime.of(
            LocalDate.now().withDayOfMonth(1),
            LocalTime.MIN
        );
        
        return generateReport(startOfMonth, LocalDateTime.now());
    }

    /**
     * Get previous month report
     */
    public LedgerResponse getPreviousMonthReport() {
        LocalDate previousMonth = LocalDate.now().minusMonths(1);
        
        LocalDateTime startOfMonth = LocalDateTime.of(
            previousMonth.withDayOfMonth(1),
            LocalTime.MIN
        );
        
        LocalDateTime endOfMonth = LocalDateTime.of(
            previousMonth.with(TemporalAdjusters.lastDayOfMonth()),
            LocalTime.MAX
        );
        
        return generateReport(startOfMonth, endOfMonth);
    }

    /**
     * Get year-to-date report
     */
    public LedgerResponse getYearToDateReport() {
        LocalDateTime startOfYear = LocalDateTime.of(
            LocalDate.now().withDayOfYear(1),
            LocalTime.MIN
        );
        
        return generateReport(startOfYear, LocalDateTime.now());
    }

    /**
     * Get previous year report
     */
    public LedgerResponse getPreviousYearReport() {
        int previousYear = LocalDate.now().getYear() - 1;
        
        LocalDateTime startOfYear = LocalDateTime.of(
            LocalDate.of(previousYear, 1, 1),
            LocalTime.MIN
        );
        
        LocalDateTime endOfYear = LocalDateTime.of(
            LocalDate.of(previousYear, 12, 31),
            LocalTime.MAX
        );
        
        return generateReport(startOfYear, endOfYear);
    }

    /**
     * Get report for a specific vendor
     */
    public LedgerResponse getVendorReport(String vendorName) {
        List<TransactionResponse> allTransactions = transactionService.getAllTransactions();
        
        List<TransactionResponse> vendorTransactions = allTransactions.stream()
            .filter(t -> vendorName.equalsIgnoreCase(t.getVendor()))
            .collect(Collectors.toList());
            
        double totalDeposits = vendorTransactions.stream()
            .filter(t -> "DEPOSIT".equals(t.getType()))
            .mapToDouble(TransactionResponse::getAmount)
            .sum();
            
        double totalPayments = vendorTransactions.stream()
            .filter(t -> "PAYMENT".equals(t.getType()))
            .mapToDouble(TransactionResponse::getAmount)
            .sum();
        
        return LedgerResponse.builder()
            .transactions(vendorTransactions)
            .totalDeposits(totalDeposits)
            .totalPayments(totalPayments)
            .balance(totalDeposits - totalPayments)
            .count(vendorTransactions.size())
            .build();
    }

    /**
     * Generate a report for a specific date range
     */
    private LedgerResponse generateReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<TransactionResponse> allTransactions = transactionService.getAllTransactions();
        
        List<TransactionResponse> filteredTransactions = allTransactions.stream()
            .filter(t -> !t.getDate().isBefore(startDate) && !t.getDate().isAfter(endDate))
            .collect(Collectors.toList());
            
        double totalDeposits = filteredTransactions.stream()
            .filter(t -> "DEPOSIT".equals(t.getType()))
            .mapToDouble(TransactionResponse::getAmount)
            .sum();
            
        double totalPayments = filteredTransactions.stream()
            .filter(t -> "PAYMENT".equals(t.getType()))
            .mapToDouble(TransactionResponse::getAmount)
            .sum();
        
        return LedgerResponse.builder()
            .transactions(filteredTransactions)
            .totalDeposits(totalDeposits)
            .totalPayments(totalPayments)
            .balance(totalDeposits - totalPayments)
            .count(filteredTransactions.size())
            .build();
    }
}
