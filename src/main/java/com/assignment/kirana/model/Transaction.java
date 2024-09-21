package com.assignment.kirana.model;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public class Transaction {

    @Getter
    private Long id;
    private double amount;
    private String currency;
    private String type; // Can be 'CREDIT' or 'DEBIT'
    private Date date;
    @Setter
    private String transactionId;
    @Getter
    private LocalDateTime timestamp;
    @Getter
    private String status;

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

