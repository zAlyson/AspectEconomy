package com.alysonsantos.aspect.commands.cash;


import com.alysonsantos.aspect.api.EconomyApi;
import com.alysonsantos.aspect.api.EconomyProvider;
import com.alysonsantos.aspect.manager.EconomyManager;
import com.alysonsantos.aspect.models.Account;
import com.alysonsantos.aspect.util.Formats;
import com.alysonsantos.aspect.util.command.Execution;
import com.alysonsantos.aspect.util.command.annotations.Command;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class CommandCash {

    private final EconomyApi economyApi = EconomyProvider.get();

    @Command(
            name = "cash"
    )
    public void money(Execution execution) {

        if ((execution.argsCount() != 0)) {
            String userName = execution.getArg(0);

            final Account account = economyApi.getAccount(userName);

            if (account == null) {
                execution.sendMessage("§cEste jogador não existe ou está offline.");
                return;
            }

            execution.sendMessage(
                    "\n " +
                            "§a Cash do jogador " + userName + ": §f∞" + Formats.apply(account.getCash()) + "§a." +
                            "\n "
            );
        }

        Player player = execution.getPlayer();
        final Account account = economyApi.getAccount(player.getName());

        execution.sendMessage(
                "\n " +
                        "§e Seus cash: §f∞" + Formats.apply(account.getCash()) + "§e." +
                        "\n "
        );

    }
}
