package com.alysonsantos.aspect.util;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.user.User;
import net.luckperms.api.query.QueryOptions;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Helper {

    private static final LuckPerms api = LuckPermsProvider.get();

    public static void sendActionBarForAll(String message) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {

            PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
            CraftPlayer craftPlayer = (CraftPlayer) onlinePlayer;

            craftPlayer.getHandle().playerConnection.sendPacket(packet);

        }
    }

    public static void sendActionBar(Player player, String message) {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
        CraftPlayer craftPlayer = (CraftPlayer) player;

        craftPlayer.getHandle().playerConnection.sendPacket(packet);
    }

    public static String getPrefix(String userName) {
        final Player player = Bukkit.getPlayer(userName);

        if (player == null)
            return "§f";

        final User user = api.getUserManager().getUser(userName);

        final QueryOptions queryOptions = api.getContextManager().getQueryOptions(player);
        final CachedMetaData metaData = user.getCachedData().getMetaData(queryOptions);

        return metaData.getPrefix().replace("&", "§");
    }
}
