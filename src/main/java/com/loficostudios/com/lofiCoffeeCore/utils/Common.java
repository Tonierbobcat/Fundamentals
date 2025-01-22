package com.loficostudios.com.lofiCoffeeCore.utils;

import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextDecoration;
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
