package com.alysonsantos.aspect.api;

import com.alysonsantos.aspect.models.Account;
import org.bukkit.entity.Player;

import java.util.UUID;

public interface EconomyApi {

    Account getAccount(String userName);

    Account getAccount(Player player);

    String getMagnata();

    double getBalance(Player player);

    double getBalance(String userName);

    void set(String userName, double amount);

    void add(String userName, double amount);

    void remove(String userName, double amount);

    boolean contains(String name);

    boolean has(String name, double amount);

}
