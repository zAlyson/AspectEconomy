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

    private final Cache<String, Account> profileMap = Caffeine.newBuilder()
            .maximumSize(10_000)
            .build();

    private final EconomyPlugin plugin;

    public void process(Player player) {

        final UUID uuid = player.getUniqueId();
        final Account account = profileMap.getIfPresent(player.getName());

        if (account == null) {
            plugin.getRepository().async().find(uuid, found -> {

                if (found != null) {
                    profileMap.put(player.getName(), found);
                } else {
                    create(player);
                }

            });
        }

    }

    private void create(Player player) {
        final UUID uuid = player.getUniqueId();

        final Account account = new Account(
                uuid,
                player.getName(),
                0.0,
                0.0,
                0.0
        );

        plugin.getRepository().async().insert(uuid, account, rows -> {
            if (rows > 0)
                profileMap.put(player.getName(), account);
        });

    }

    public void removeAndUpdate(Player player) {
        final Account ifPresent = getProfileMap().getIfPresent(player.getName());

        if (ifPresent != null)
            plugin.getRepository().async().update(ifPresent.getUuid(), ifPresent);

        getProfileMap().asMap().remove(ifPresent.getName());
    }
}