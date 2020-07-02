package com.alysonsantos.aspect.commands.money;

import com.alysonsantos.aspect.api.EconomyApi;
import com.alysonsantos.aspect.api.EconomyProvider;
import com.alysonsantos.aspect.util.command.Execution;
import com.alysonsantos.aspect.util.command.annotations.Command;

public class CommandHelp {

    private final EconomyApi economyApi = EconomyProvider.get();

    @Command(
            name = "money.help",
            aliases = "ajuda"
    )
    public void set(Execution execution) {

        execution.sendMessage(
                "\n " +
                        " §b§l   TYCOON ECONOMY: " +
                        "\n \n " +
                        "  §7§l→ §f/money §8- §fMostra seus coins. \n " +
                        "   §7§l→ §f/money top §8- §fMostra os jogadores mais ricos. \n " +
                        "   §7§l→ §f/money enviar§8- §fEnviar coins para outro jogador. \n "
        );

        if (execution.getPlayer().hasPermission("money.admin"))
            execution.sendMessage(
                    "\n §7    Comandos administrativos: \n " +
                            "  §7§l→ §f/money setar <jogador> <quantia>\n " +
                            "   §7§l→ §f/money remover <jogador> <quantia>\n " +
                            "   §7§l→ §f/money adicionar <jogador> <quantia>\n "
            );

    }
}
