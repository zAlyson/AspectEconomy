package com.alysonsantos.aspect.manager;

import com.alysonsantos.aspect.EconomyPlugin;
import com.alysonsantos.aspect.models.Account;
import com.alysonsantos.aspect.util.Formats;
import com.alysonsantos.aspect.util.Helper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.lucko.helper.Schedulers;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class EconomyManager {

    private final EconomyPlugin plugin;
    private final List<Account> accountList = new ArrayList<>();

    private String tycoon = "";

    public void startScheduler() {

        Schedulers.builder()
                .async()
                .every(10, TimeUnit.SECONDS)
                .run(this::loadMoneyTop);

    }

    public void loadMoneyTop() {

        final AccountManager accountManager = plugin.getAccountManager();

        final long init = System.currentTimeMillis();

        final Collection<Account> values = accountManager.getProfileMap().asMap().values();

        if (values.isEmpty())
            return;

        accountList.clear();
        accountList.addAll(
                values.stream()
                        .limit(10)
                        .sorted(Comparator.comparing(Account::getBalance).reversed())
                        .collect(Collectors.toList())
        );

        checkNewTycoon(accountList.get(0));

        final long end = System.currentTimeMillis();

        plugin.getLogger().info("Money top carregado em " + (end - init) + "ms.");

    }

    private void checkNewTycoon(final Account found) {

        if (found.getName().equals(tycoon)) {
            Helper.sendActionBarForAll("§a§lTYCOON §7→§f O jogador §d" + tycoon + "§f continua sendo o magnata!");
            return;
        }

        Bukkit.broadcastMessage(
                "\n " +
                        "§a§l   NEW TYCOON!" + "\n " +
                        "   §7→§fO jogador §d" + found.getName() + "§f é novo magnata, com §7$" + Formats.apply(found.getBalance()) + " coins§f." + "\n "
        );

        tycoon = found.getName();

    }
}
