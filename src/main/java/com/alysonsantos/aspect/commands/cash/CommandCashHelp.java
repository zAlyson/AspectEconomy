package com.alysonsantos.aspect.commands.cash;

import com.alysonsantos.aspect.api.EconomyApi;
import com.alysonsantos.aspect.api.EconomyProvider;
import com.alysonsantos.aspect.util.command.Execution;
import com.alysonsantos.aspect.util.command.annotations.Command;

public class CommandCashHelp {

    private final EconomyApi economyApi = EconomyProvider.get();

    @Command(
            name = "cash.help",
            aliases = "ajuda"
    )
    public void set(Execution execution) {

        execution.sendMessage(
                "\n " +
                        " §6§l   TYCOON CASH: "
        );

        if (execution.getPlayer().hasPermission("economy.admin"))
            execution.sendMessage(
                    "\n §7    Comandos administrativos: \n " +
                            "   §7§l→ §f/cash setar <jogador> <quantia>\n " +
                            "   §7§l→ §f/cash remover <jogador> <quantia>\n " +
                            "   §7§l→ §f/cash adicionar <jogador> <quantia>\n "
            );

    }
}
