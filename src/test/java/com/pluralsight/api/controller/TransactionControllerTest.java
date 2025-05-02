package com.pluralsight.api.controller;

import com.pluralsight.api.dto.request.DepositRequest;
import com.pluralsight.api.dto.request.PaymentRequest;
import com.pluralsight.api.dto.response.TransactionResponse;
import com.pluralsight.api.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

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
        TransactionResponse response = new TransactionResponse(1L, "Test Deposit", "Vendor A", 100.0, "DEPOSIT", LocalDateTime.now());
        when(transactionService.addDeposit(any(DepositRequest.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/transactions/deposit")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Test Deposit"))
                .andExpect(jsonPath("$.vendor").value("Vendor A"))
                .andExpect(jsonPath("$.amount").value(100.0));
    }

    @Test
    public void testMakePayment() throws Exception {
        // Arrange
        PaymentRequest request = new PaymentRequest("Test Payment", "Vendor B", 50.0);
        TransactionResponse responseP = new TransactionResponse(2L, "Test Payment", "Vendor B", -50.0, "PAYMENT", LocalDateTime.now());
        when(transactionService.makePayment(any(PaymentRequest.class))).thenReturn(responseP);

        // Act & Assert
        mockMvc.perform(post("/api/transactions/payment")
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.description").value("Test Payment"))
                .andExpect(jsonPath("$.vendor").value("Vendor B"))
                .andExpect(jsonPath("$.amount").value(-50.0));
    }
}
