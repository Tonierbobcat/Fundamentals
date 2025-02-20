package com.loficostudios.fundamentals.utils;

import com.loficostudios.fundamentals.player.user.User;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@UtilityClass
public class Common {
    public static void broadcast(String message) {
        Bukkit.getServer().broadcast(Component.text(message));
    }
    public static void broadcast(Component message) {
        Bukkit.getServer().broadcast( message);
    }
    public static void sendMessage(Player player, Component message) {
        player.sendMessage(message);
    }
    public static void sendMessage(Player player, String message) {
        player.sendMessage(ColorUtils.deserialize(message));
    }
    public static void sendMessage(User user, String message) {
        user.sendMessage(ColorUtils.deserialize(message));
    }
    public static void sendMessage(User user, Component message) {
        user.sendMessage(message);
    }

}
