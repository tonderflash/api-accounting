package com.pluralsight.api.controller;

import com.pluralsight.api.dto.response.LedgerResponse;
import com.pluralsight.api.service.ReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * Get month-to-date report
     */
    @GetMapping("/month-to-date")
    public ResponseEntity<LedgerResponse> getMonthToDateReport() {
        return ResponseEntity.ok(reportService.getMonthToDateReport());
    }

    /**
     * Get previous month report
     */
    @GetMapping("/previous-month")
    public ResponseEntity<LedgerResponse> getPreviousMonthReport() {
        return ResponseEntity.ok(reportService.getPreviousMonthReport());
    }

    /**
     * Get year-to-date report
     */
    @GetMapping("/year-to-date")
    public ResponseEntity<LedgerResponse> getYearToDateReport() {
        return ResponseEntity.ok(reportService.getYearToDateReport());
    }

    /**
     * Get previous year report
     */
    @GetMapping("/previous-year")
    public ResponseEntity<LedgerResponse> getPreviousYearReport() {
        return ResponseEntity.ok(reportService.getPreviousYearReport());
    }

    /**
     * Get report for a specific vendor
     */
    @GetMapping("/vendor/{vendorName}")
    public ResponseEntity<LedgerResponse> getVendorReport(@PathVariable String vendorName) {
        return ResponseEntity.ok(reportService.getVendorReport(vendorName));
    }
}
