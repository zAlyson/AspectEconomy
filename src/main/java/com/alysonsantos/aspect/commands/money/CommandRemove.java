package com.alysonsantos.aspect.commands.money;

import com.alysonsantos.aspect.api.EconomyApi;
import com.alysonsantos.aspect.api.EconomyProvider;
import com.alysonsantos.aspect.util.Formats;
import com.alysonsantos.aspect.util.command.Execution;
import com.alysonsantos.aspect.util.command.annotations.Command;

public class CommandRemove {

    private final EconomyApi economyApi = EconomyProvider.get();

    @Command(
            name = "money.remove",
            aliases = "remover",
            permission = "economy.remove"
    )
    public void set(Execution execution, String target, double amount) {

        if (!economyApi.contains(target)) {
            execution.sendMessage("§cEste jogador não existe.");
            return;
        }

        if (amount < 0) {
            execution.sendMessage("§cEste valor não é válido.");
            return;
        }

        economyApi.remove(target, amount);

        execution.sendMessage(
                "\n " +
                        "§b§l SUCESSO!" + "\n " +
                        "§f O valor §a$" + Formats.apply(amount) + "§f de coins foi removido da conta do jogador." +
                        "\n "
        );
    }
}
