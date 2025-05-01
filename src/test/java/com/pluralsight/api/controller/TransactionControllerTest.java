package com.pluralsight.api.controller;

import com.pluralsight.api.dto.request.DepositRequest;
import com.pluralsight.api.dto.request.PaymentRequest;
import com.pluralsight.api.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testAddDeposit() throws Exception {
        // Arrange
        DepositRequest request = new DepositRequest("Test Deposit", "Vendor A", 100.0);
        when(transactionService.addDeposit(any(DepositRequest.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/transactions/deposit")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Deposit added successfully"))
                .andExpect(jsonPath("$.data.description").value("Test Deposit"))
                .andExpect(jsonPath("$.data.vendor").value("Vendor A"))
                .andExpect(jsonPath("$.data.amount").value(100.0));
    }

    @Test
    public void testMakePayment() throws Exception {
        // Arrange
        PaymentRequest request = new PaymentRequest("Test Payment", "Vendor B", 50.0);
        when(transactionService.makePayment(any(PaymentRequest.class))).thenReturn(true);

        // Act & Assert
        mockMvc.perform(post("/api/transactions/payment")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Payment added successfully"))
                .andExpect(jsonPath("$.data.description").value("Test Payment"))
                .andExpect(jsonPath("$.data.vendor").value("Vendor B"))
                .andExpect(jsonPath("$.data.amount").value(50.0));
    }
}
