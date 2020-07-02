package com.alysonsantos.aspect.api;

import com.alysonsantos.aspect.EconomyPlugin;
import com.alysonsantos.aspect.manager.AccountManager;
import com.alysonsantos.aspect.manager.AccountQueue;
import com.alysonsantos.aspect.models.Account;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public class EconomyProvider implements EconomyApi {

    private static EconomyProvider instance;

    public static EconomyProvider get() {
        if (instance == null) {
            instance = new EconomyProvider();
        }

        return instance;
    }

    private final AccountManager manager = EconomyPlugin.getPlugin().getAccountManager();
    private final AccountQueue queue = EconomyPlugin.getPlugin().getAccountQueue();

    @Override
    public final Account getAccount(String userName) {
        return manager.getProfileMap().getIfPresent(userName);
    }

    @Override
    public Account getAccount(Player player) {
        return manager.getProfileMap().getIfPresent(player.getName());
    }

    @Override
    public String getMagnata() {
        return EconomyPlugin.getPlugin().getEconomyManager().getTycoon();
    }

    @Override
    public final double getBalance(Player player) {
        return this.getAccount(player.getName()).getBalance();

    }

    @Override
    public final double getBalance(String userName) {
        return this.getAccount(userName).getBalance();
    }

    @Override
    public void set(String userName, double amount) {
        getAccount(userName).setBalance(amount);
        queue.addItem(getAccount(userName));
    }

    @Override
    public void add(String userName, double amount) {
        getAccount(userName).addBalance(amount);
        queue.addItem(getAccount(userName));
    }

    @Override
    public void remove(String userName, double amount) {
        getAccount(userName).removeBalance(amount > getBalance(userName) ? getBalance(userName) : amount);
        queue.addItem(getAccount(userName));
    }

    @Override
    public boolean has(String name, double amount) {
        return getBalance(name) >= amount;
    }

    @Override
    public boolean contains(String userName) {
        return getAccount(userName) != null;
    }
}
