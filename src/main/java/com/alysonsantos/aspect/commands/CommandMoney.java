package com.alysonsantos.aspect.commands;


import com.alysonsantos.aspect.manager.AccountManager;
import com.alysonsantos.aspect.models.Account;
import com.alysonsantos.aspect.util.Formats;
import com.alysonsantos.aspect.util.command.Execution;
import com.alysonsantos.aspect.util.command.annotations.Command;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class CommandMoney {

    private final AccountManager accountManager;

    @Command(
            name = "money"
    )
    public void money(Execution execution) {

        Player player = execution.getPlayer();
        final Account account = accountManager.getProfileMap().getIfPresent(player.getUniqueId());

        execution.sendMessage(
                "\n " +
                        "§a Seus coins: §f∞" + Formats.apply(account.getBalance()) + "§a." +
                        "\n "
        );

    }

}
