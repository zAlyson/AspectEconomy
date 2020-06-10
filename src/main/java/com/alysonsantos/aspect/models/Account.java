package com.alysonsantos.aspect.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public final class Account {

    private UUID uuid;

    private String name;

    private double balance;

}
