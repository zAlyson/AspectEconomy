package com.alysonsantos.aspect.commands.money;

import com.alysonsantos.aspect.api.EconomyApi;
import com.alysonsantos.aspect.api.EconomyProvider;
import com.alysonsantos.aspect.util.Formats;
import com.alysonsantos.aspect.util.command.Execution;
import com.alysonsantos.aspect.util.command.annotations.Command;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandPay {

    private final EconomyApi economyApi = EconomyProvider.get();

    @Command(
            name = "money.pay",
            aliases = "enviar",
            permission = "economy.add"
    )
    public void pay(Execution execution, String target, double amount) {

        if (target.equalsIgnoreCase(execution.getPlayer().getName())) {
            execution.sendMessage("§cNão é possível fazer isso...");
            return;
        }

        if (!economyApi.contains(target)) {
            execution.sendMessage("§cEste jogador não existe.");
            return;
        }

        if (amount <= 0) {
            execution.sendMessage("§cEste valor não é válido.");
            return;
        }

        if (amount > economyApi.getBalance(execution.getPlayer())) {
            execution.sendMessage("§cVocê não possui essa quantia de coins.");
            return;
        }

        economyApi.add(target, amount);
        economyApi.remove(execution.getPlayer().getName(), amount);

        execution.sendMessage(
                "\n " +
                        "§b§l SUCESSO!" + "\n " +
                        "§f O valor §a$" + Formats.apply(amount) + "§f de coins foi enviado para a conta do jogador." +
                        "\n "
        );

        Player player = Bukkit.getPlayer(target);
        player.sendMessage(
                "\n " +
                        "§b§l   TYCOON ECONOMY: " + "\n " +
                        "§f   Você recebeu §a$" + Formats.apply(amount) + "§f de coins do jogador " + execution.getPlayer().getName() + "§f!" +
                        "\n "
        );
    }
}
