package com.pluralsight.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class PaymentRequest {

    @NotBlank(message = "Description is required")
    private String description;
    
    @NotBlank(message = "Vendor is required")
    private String vendor;
    
    @Positive(message = "Amount must be positive")
    private double amount;

    // Constructor sin argumentos requerido para deserialización de JSON
    public PaymentRequest() {
    }

    // Constructor con todos los campos
    public PaymentRequest(String description, String vendor, double amount) {
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    // Getters y setters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
