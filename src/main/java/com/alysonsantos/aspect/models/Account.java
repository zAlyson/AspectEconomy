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

    public void addBalance(double amount) {
        this.balance = this.balance + amount;
    }

    public void removeBalance(double amount) {
        this.balance = balance - amount;
    }
}
