package com.alysonsantos.aspect.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@AllArgsConstructor
@Data
public final class Account {

    private UUID uuid;

    private String name;

    private double balance;

    private double tokens;

    private double cash;

    public void addBalance(double amount) {
        this.balance = this.balance + amount;
    }

    public void removeBalance(double amount) {
        this.balance = balance - amount;
    }

    public void addTokens(double amount) {
        this.tokens = this.tokens + amount;
    }

    public void removeTokens(double amount) {
        this.tokens = Math.min(amount, tokens);
    }

    public void addCash(double amount) {
        this.cash = this.cash + amount;
    }

    public void removeCash(double amount) {
        this.cash = Math.min(amount, cash);
    }
}
