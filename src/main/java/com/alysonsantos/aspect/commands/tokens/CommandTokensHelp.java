package com.alysonsantos.aspect.commands.tokens;

import com.alysonsantos.aspect.api.EconomyApi;
import com.alysonsantos.aspect.api.EconomyProvider;
import com.alysonsantos.aspect.util.command.Execution;
import com.alysonsantos.aspect.util.command.annotations.Command;

public class CommandTokensHelp {

    private final EconomyApi economyApi = EconomyProvider.get();

    @Command(
            name = "tokens.help",
            aliases = "ajuda"
    )
    public void set(Execution execution) {

        execution.sendMessage(
                "\n " +
                        " §d§l   TYCOON TOKENS: "
        );

        if (execution.getPlayer().hasPermission("economy.admin"))
            execution.sendMessage(
                    "\n §7    Comandos administrativos: \n " +
                            "   §7§l→ §f/tokens setar <jogador> <quantia>\n " +
                            "   §7§l→ §f/tokens remover <jogador> <quantia>\n " +
                            "   §7§l→ §f/tokens adicionar <jogador> <quantia>\n "
            );

    }
}
