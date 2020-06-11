package com.alysonsantos.aspect.commands;


import com.alysonsantos.aspect.manager.AccountManager;
import com.alysonsantos.aspect.manager.EconomyManager;
import com.alysonsantos.aspect.models.Account;
import com.alysonsantos.aspect.util.Formats;
import com.alysonsantos.aspect.util.command.Execution;
import com.alysonsantos.aspect.util.command.annotations.Command;
import lombok.AllArgsConstructor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryMode;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.Normalizer;

@AllArgsConstructor
public class CommandMoney {

    private final AccountManager accountManager;
    private final EconomyManager economyManager;

    @Command(
            name = "money",
            aliases = {"coins", "coin"}
    )
    public void money(Execution execution) {

        if ((execution.argsCount() != 0)) {
            String userName = execution.getArg(0);

            final Account account = accountManager.getProfileMap().getIfPresent(userName);

            if (account == null) {
                execution.sendMessage("§cEste jogador não existe ou está offline.");
                return;
            }

            execution.sendMessage(
                    "\n " +
                            "§a Coins do jogador " + userName + ": §f∞" + Formats.apply(account.getBalance()) + "§a." +
                            "\n "
            );
        }

        Player player = execution.getPlayer();
        final Account account = accountManager.getProfileMap().getIfPresent(player.getName());

        execution.sendMessage(
                "\n " +
                        "§a Seus coins: §f∞" + Formats.apply(account.getBalance()) + "§a." +
                        "\n "
        );

    }

    private final LuckPerms api = LuckPermsProvider.get();
    ;

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
            execution.sendMessage("    §a" + index + "º: " + (index == 1 ? "§2[$]" : " ") + getPrefix(account.getName()) + account.getName() +"§7: $" + Formats.apply(account.getBalance()) + " de coins");

            index++;
        }

        execution.sendMessage("§f");

    }

    public final String getPrefix(String userName) {
        final Player player = Bukkit.getPlayer(userName);
        final User user = api.getUserManager().getUser(userName);

        final QueryOptions queryOptions = api.getContextManager().getQueryOptions(player);
        final CachedMetaData metaData = user.getCachedData().getMetaData(queryOptions);

        return metaData.getPrefix().replace("&", "§");
    }

}
