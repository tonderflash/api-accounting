package com.pluralsight.api.dto.response;

import java.time.LocalDateTime;

public class TransactionResponse {
    
    private Long id;
    private String description;
    private String vendor;
    private double amount;
    private String type; // DEPOSIT or PAYMENT
    private LocalDateTime date;

    public TransactionResponse() {
    }

    public TransactionResponse(Long id, String description, String vendor, double amount, String type, LocalDateTime date) {
        this.id = id;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
        this.type = type;
        this.date = date;
    }

    // Getter y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    // Método estático que reemplaza al patrón Builder
    public static TransactionResponseBuilder builder() {
        return new TransactionResponseBuilder();
    }

    // Clase interna para implementar el patrón Builder
    public static class TransactionResponseBuilder {
        private Long id;
        private String description;
        private String vendor;
        private double amount;
        private String type;
        private LocalDateTime date;

        public TransactionResponseBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public TransactionResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TransactionResponseBuilder vendor(String vendor) {
            this.vendor = vendor;
            return this;
        }

        public TransactionResponseBuilder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public TransactionResponseBuilder type(String type) {
            this.type = type;
            return this;
        }

        public TransactionResponseBuilder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public TransactionResponse build() {
            return new TransactionResponse(id, description, vendor, amount, type, date);
        }
    }
}
