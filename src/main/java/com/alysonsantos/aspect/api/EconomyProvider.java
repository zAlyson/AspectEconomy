package com.alysonsantos.aspect.api;

import com.alysonsantos.aspect.EconomyPlugin;
import com.alysonsantos.aspect.manager.AccountManager;
import com.alysonsantos.aspect.models.Account;
import org.bukkit.entity.Player;

public class EconomyProvider implements EconomyApi {

    private static EconomyProvider instance;

    public static EconomyProvider get() {
        if (instance == null) {
            instance = new EconomyProvider();
        }

        return instance;
    }

    private final AccountManager manager = EconomyPlugin.getPlugin().getAccountManager();

    @Override
    public final Account getAccount(String userName) {
        return manager.getProfileMap().getIfPresent(userName);
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
    }

    @Override
    public void add(String userName, double amount) {
        getAccount(userName).addBalance(amount);
    }

    @Override
    public void remove(String userName, double amount) {
        getAccount(userName).removeBalance(amount > getBalance(userName) ? 0 : amount);
    }

    @Override
    public boolean contains(String userName) {
        return getAccount(userName) == null;
    }
}
