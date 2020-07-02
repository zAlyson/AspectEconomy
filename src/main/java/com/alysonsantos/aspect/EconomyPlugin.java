package com.alysonsantos.aspect;


import com.alysonsantos.aspect.api.EconomyProvider;
import com.alysonsantos.aspect.commands.cash.*;
import com.alysonsantos.aspect.commands.money.*;
import com.alysonsantos.aspect.commands.tokens.*;
import com.alysonsantos.aspect.database.connection.MySQLDatabase;
import com.alysonsantos.aspect.database.connection.SQLDatabase;
import com.alysonsantos.aspect.manager.AccountManager;
import com.alysonsantos.aspect.manager.AccountQueue;
import com.alysonsantos.aspect.manager.EconomyManager;
import com.alysonsantos.aspect.repository.AccountRepository;
import com.alysonsantos.aspect.scheduler.BukkitSchedulerAdapter;
import com.alysonsantos.aspect.scheduler.SchedulerAdapter;
import com.alysonsantos.aspect.util.VaultEconomy;
import com.alysonsantos.aspect.util.command.CommandFrame;
import com.google.gson.Gson;
import lombok.Getter;
import me.lucko.helper.Events;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Getter
public class EconomyPlugin extends ExtendedJavaPlugin {

    private AccountRepository repository;
    private AccountQueue accountQueue;

    private final AccountManager accountManager;
    private final EconomyManager economyManager;
    private final SchedulerAdapter schedulerAdapter;

    public EconomyPlugin() {
        this.accountManager = new AccountManager(this);
        this.economyManager = new EconomyManager(this);
        this.schedulerAdapter = new BukkitSchedulerAdapter(this);
    }

    @Override
    protected void enable() {
        initDatabase();
        registerCommands();
        observeLogins();

        economyManager.startScheduler();

        EconomyProvider.get();

        new VaultEconomy();
    }

    private void registerCommands() {
        CommandFrame commandFrame = new CommandFrame(this);

        commandFrame.registerType(OfflinePlayer.class, Bukkit::getOfflinePlayer);
        commandFrame.setUsageMessage("");
        commandFrame.register
                (
                        new CommandMoney(economyManager),
                        new CommandSet(),
                        new CommandAdd(),
                        new CommandPay(),
                        new CommandHelp(),
                        new CommandRemove(),
                        new CommandTokens(),
                        new CommandTokensAdd(),
                        new CommandTokensHelp(),
                        new CommandTokensRemove(),
                        new CommandTokensSet(),
                        new CommandCash(),
                        new CommandCashAdd(),
                        new CommandCashHelp(),
                        new CommandCashRemove(),
                        new CommandCashSet()
                );
    }

    private void initDatabase() {
        SQLDatabase mySQLDatabase = new MySQLDatabase();

        mySQLDatabase.update(
                "CREATE TABLE IF NOT EXISTS economy (" +
                        "uuid VARCHAR(42) NOT NULL PRIMARY KEY, " +
                        "name VARCHAR(16) NOT NULL, " +
                        "balance double,  " +
                        "tokens double, " +
                        "cash double)"
        );

        this.repository = new AccountRepository(mySQLDatabase, new Gson());
        this.accountQueue = new AccountQueue();

        accountQueue.setRemovalAction(account -> repository.update(account.getUuid(), account));
    }

    private void observeLogins() {
        Events.subscribe(PlayerLoginEvent.class, EventPriority.MONITOR)
                .filter(e -> e.getResult() == PlayerLoginEvent.Result.ALLOWED)
                .handler(e -> {
                    accountManager.process(e.getPlayer());
                });

        Events.subscribe(PlayerQuitEvent.class, EventPriority.MONITOR)
                .handler(e -> {
                    accountManager.removeAndUpdate(e.getPlayer());
                });
    }

    public static EconomyPlugin getPlugin() {
        return EconomyPlugin.getPlugin(EconomyPlugin.class);
    }
}