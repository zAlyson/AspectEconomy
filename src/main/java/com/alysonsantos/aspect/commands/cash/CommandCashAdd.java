package com.alysonsantos.aspect.commands.cash;

import com.alysonsantos.aspect.api.EconomyApi;
import com.alysonsantos.aspect.api.EconomyProvider;
import com.alysonsantos.aspect.util.Formats;
import com.alysonsantos.aspect.util.command.Execution;
import com.alysonsantos.aspect.util.command.annotations.Command;

public class CommandCashAdd {

    private final EconomyApi economyApi = EconomyProvider.get();

    @Command(
            name = "cash.add",
            aliases = "adicionar",
            permission = "economy.cash.add"
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

        economyApi.getAccount(target).addCash(amount);

        execution.sendMessage(
                "\n " +
                        "§6§l SUCESSO!" + "\n " +
                        "§f O valor §e$" + Formats.apply(amount) + "§f de cash foi adicionado na conta do jogador §e" + target + "§f." +
                        "\n "
        );
    }
}
