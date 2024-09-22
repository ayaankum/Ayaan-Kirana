package com.assignment.kirana.model;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class TransactionRequest {

    @Getter
    private String transactionId;
    private String type;
    @Getter
    private double amount;
    private double convertedAmount;
    private String currency;
    private String description;
    private LocalDateTime now;
    @Setter
    private String status;

    public TransactionRequest() {}

    public TransactionRequest(String transactionId, String type, double amount, String currency, String description,LocalDateTime now, double convertedAmount) {
        this.transactionId = transactionId;
        this.type = type;
        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this.now = now;
        this.convertedAmount= convertedAmount;
    }


    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public void setTimestamp(LocalDateTime now) {
        this.now = now;
    }
    public LocalDateTime getTimestamp(){
        return now;
    }
    public String getStatus() {
        return status;
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(double convertedAmount) {
        this.convertedAmount = convertedAmount;
    }
}

