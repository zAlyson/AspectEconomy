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
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Getter
public class EconomyManager {

    private final EconomyPlugin plugin;
    private final List<Account> accountList = new ArrayList<>();

    private String tycoon = "";

    public void startScheduler() {

        Schedulers.builder()
                .async()
                .every(5, TimeUnit.MINUTES)
                .run(this::loadMoneyTop);

    }

    public void loadMoneyTop() {
        accountList.clear();
        accountList.addAll(plugin.getRepository().getAll());

        checkNewTycoon(accountList.get(0));
    }

    private void checkNewTycoon(final Account found) {

        if (found.getName().equals(tycoon)) {
            Helper.sendActionBarForAll("§a§lTYCOON §7§l→§f O jogador §d" + Helper.getPrefix(tycoon) + tycoon + "§f continua sendo o magnata!");
            return;
        }

        Bukkit.broadcastMessage(
                "\n " +
                        "§a§l   NEW TYCOON!" + "\n " +
                        "   §7➡§f O jogador §d" + Helper.getPrefix(found.getName()) + found.getName() + "§f é novo magnata, com §7$" + Formats.apply(found.getBalance()) + " coins§f." + "\n "
        );

        tycoon = found.getName();

    }
}
