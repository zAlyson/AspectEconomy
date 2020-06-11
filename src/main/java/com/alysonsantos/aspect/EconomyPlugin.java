package com.alysonsantos.aspect;


import com.alysonsantos.aspect.api.EconomyApi;
import com.alysonsantos.aspect.api.EconomyProvider;
import com.alysonsantos.aspect.commands.CommandMoney;
import com.alysonsantos.aspect.database.connection.MySQLDatabase;
import com.alysonsantos.aspect.database.connection.SQLDatabase;
import com.alysonsantos.aspect.manager.AccountManager;
import com.alysonsantos.aspect.manager.EconomyManager;
import com.alysonsantos.aspect.repository.AccountRepository;
import com.alysonsantos.aspect.scheduler.BukkitSchedulerAdapter;
import com.alysonsantos.aspect.scheduler.SchedulerAdapter;
import com.alysonsantos.aspect.util.command.CommandFrame;
import com.google.gson.Gson;
import lombok.Getter;
import me.lucko.helper.Events;
import me.lucko.helper.internal.HelperImplementationPlugin;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import me.lucko.helper.sql.DatabaseCredentials;
import me.lucko.helper.sql.Sql;
import me.lucko.helper.sql.SqlProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerLoginEvent;

@HelperImplementationPlugin
@Getter
public class EconomyPlugin extends ExtendedJavaPlugin {

    private AccountRepository repository;

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

        loadProvider();
        registerCommands();
        observeLogins();

        economyManager.startScheduler();

    }

    private void registerCommands() {
        CommandFrame commandFrame = new CommandFrame(this);

        commandFrame.registerType(OfflinePlayer.class, Bukkit::getOfflinePlayer);
        commandFrame.register(new CommandMoney(accountManager, economyManager));
    }

    private void loadProvider() {

        YamlConfiguration config = loadConfig("config.yml");

        SqlProvider sqlProvider = getService(SqlProvider.class);
        Sql sql = sqlProvider.getSql(DatabaseCredentials.fromConfig(config));

        SQLDatabase mySQLDatabase = new MySQLDatabase(sql, true);

        mySQLDatabase.update(
                "CREATE TABLE IF NOT EXISTS economy (" +
                        "uuid VARCHAR(42) NOT NULL PRIMARY KEY, " +
                        "name VARCHAR(16) NOT NULL, " +
                        "balance double)"
        );

        this.repository = new AccountRepository(mySQLDatabase, new Gson());

    }

    private void observeLogins() {
        Events.subscribe(PlayerLoginEvent.class, EventPriority.MONITOR)
                .filter(e -> e.getResult() == PlayerLoginEvent.Result.ALLOWED)
                .handler(e -> {
                    accountManager.process(e.getPlayer());
                });
    }

    public static EconomyPlugin getPlugin() {
        return EconomyPlugin.getPlugin(EconomyPlugin.class);
    }

}