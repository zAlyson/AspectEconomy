package com.alysonsantos.aspect.manager;

import com.alysonsantos.aspect.EconomyPlugin;
import com.alysonsantos.aspect.models.Account;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;


@RequiredArgsConstructor
@Getter
public final class AccountManager {

    private final Cache<UUID, Account> profileMap = Caffeine.newBuilder()
            .maximumSize(10_000)
            .build();

    private final EconomyPlugin plugin;

    public void process(Player player) {

        final UUID uuid = player.getUniqueId();
        final Account account = profileMap.getIfPresent(uuid);

        if (account == null) {
            plugin.getRepository().async().find(uuid, found -> {

                if (found != null)
                    profileMap.put(uuid, found);
                else
                    create(player);

            });
        }

    }

    private void create(Player player) {
        final UUID uuid = player.getUniqueId();

        Account account = new Account(
                uuid,
                player.getName(),
                0.0
        );

        plugin.getRepository().async().insert(uuid, account, rows -> {
            if (rows > 0)
                profileMap.put(uuid, account);
        });

    }
}