package com.pluralsight.api.dto.response;

import java.util.List;

public class LedgerResponse {
    
    private List<TransactionResponse> transactions;
    private double totalDeposits;
    private double totalPayments;
    private double balance;
    private int count;
    
    public LedgerResponse() {
    }
    
    public LedgerResponse(List<TransactionResponse> transactions, double totalDeposits, 
                         double totalPayments, double balance, int count) {
        this.transactions = transactions;
        this.totalDeposits = totalDeposits;
        this.totalPayments = totalPayments;
        this.balance = balance;
        this.count = count;
    }
    
    // Getters y setters
    public List<TransactionResponse> getTransactions() {
        return transactions;
    }
    
    public void setTransactions(List<TransactionResponse> transactions) {
        this.transactions = transactions;
    }
    
    public double getTotalDeposits() {
        return totalDeposits;
    }
    
    public void setTotalDeposits(double totalDeposits) {
        this.totalDeposits = totalDeposits;
    }
    
    public double getTotalPayments() {
        return totalPayments;
    }
    
    public void setTotalPayments(double totalPayments) {
        this.totalPayments = totalPayments;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    // Método estático que reemplaza al patrón Builder
    public static LedgerResponseBuilder builder() {
        return new LedgerResponseBuilder();
    }
    
    // Clase interna Builder
    public static class LedgerResponseBuilder {
        private List<TransactionResponse> transactions;
        private double totalDeposits;
        private double totalPayments;
        private double balance;
        private int count;
        
        public LedgerResponseBuilder transactions(List<TransactionResponse> transactions) {
            this.transactions = transactions;
            return this;
        }
        
        public LedgerResponseBuilder totalDeposits(double totalDeposits) {
            this.totalDeposits = totalDeposits;
            return this;
        }
        
        public LedgerResponseBuilder totalPayments(double totalPayments) {
            this.totalPayments = totalPayments;
            return this;
        }
        
        public LedgerResponseBuilder balance(double balance) {
            this.balance = balance;
            return this;
        }
        
        public LedgerResponseBuilder count(int count) {
            this.count = count;
            return this;
        }
        
        public LedgerResponse build() {
            return new LedgerResponse(transactions, totalDeposits, totalPayments, balance, count);
        }
    }
}
