package com.alysonsantos.aspect.commands.money;


import com.alysonsantos.aspect.api.EconomyApi;
import com.alysonsantos.aspect.api.EconomyProvider;
import com.alysonsantos.aspect.manager.EconomyManager;
import com.alysonsantos.aspect.models.Account;
import com.alysonsantos.aspect.util.Formats;
import com.alysonsantos.aspect.util.Helper;
import com.alysonsantos.aspect.util.command.Execution;
import com.alysonsantos.aspect.util.command.annotations.Command;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class CommandMoney {

    private final EconomyApi economyApi = EconomyProvider.get();
    private final EconomyManager economyManager;

    @Command(
            name = "money",
            aliases = {"coins", "coin"}
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
                            "§a Coins do jogador " + userName + ": §f$" + Formats.apply(account.getBalance()) + "§a." +
                            "\n "
            );
        }

        Player player = execution.getPlayer();
        final Account account = economyApi.getAccount(player.getName());

        execution.sendMessage(
                "\n " +
                        "§a Seus coins: §f$" + Formats.apply(account.getBalance()) + "§a." +
                        "\n "
        );

    }

    @Command(
            name = "money.top"
    )
    public void commandTop(Execution execution) {

        execution.sendMessage(
                "\n " +
                        "§a§l  MONEY TOP §7→§f Jogadores com mais coins" +
                        "\n  §7    Atualiza a cada 5 minutos." + "\n "
        );

        int index = 1;
        for (Account account : economyManager.getAccountList()) {
            execution.sendMessage("    §a" + index + "º: " + (index == 1 ? "§2[$] " : "") + Helper.getPrefix(account.getName()) + account.getName() +"§7: $" + Formats.apply(account.getBalance()) + " de coins");

            index++;
        }

        execution.sendMessage("§f");

    }

    @Command(
            name = "money.magnata"
    )
    public void magnata(Execution execution) {

        execution.sendMessage(
                "\n " +
                        "§a§l   TYCOON ATUAL §7§l→§f " + Helper.getPrefix(economyManager.getTycoon()) + economyManager.getTycoon() + "§a!" +
                        "\n "
        );

    }
}
