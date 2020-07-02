package com.alysonsantos.aspect.commands.tokens;

import com.alysonsantos.aspect.api.EconomyApi;
import com.alysonsantos.aspect.api.EconomyProvider;
import com.alysonsantos.aspect.util.Formats;
import com.alysonsantos.aspect.util.command.Execution;
import com.alysonsantos.aspect.util.command.annotations.Command;

public class CommandTokensSet {

    private final EconomyApi economyApi = EconomyProvider.get();

    @Command(
            name = "tokens.set",
            aliases = "setar",
            permission = "economy.tokens.set"
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

        economyApi.getAccount(target).setTokens(amount);

        execution.sendMessage(
                "\n " +
                        "§d§l SUCESSO!" + "\n " +
                        "§f O valor §a$" + Formats.apply(amount) + "§f de tokens foi setado na conta do jogador§a " + target + "§f." +
                        "\n "
        );
    }
}
