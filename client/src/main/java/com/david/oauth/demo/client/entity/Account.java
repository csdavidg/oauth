package com.david.oauth.demo.client.entity;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class Account {

    private long id;
    private BigInteger balance;
    private TransactionType transactionType;
    private LocalDateTime dateTime;

    public Account() {
    }

    public Account(BigInteger balance, TransactionType transactionType, LocalDateTime dateTime) {
        this.balance = balance;
        this.transactionType = transactionType;
        this.dateTime = dateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
