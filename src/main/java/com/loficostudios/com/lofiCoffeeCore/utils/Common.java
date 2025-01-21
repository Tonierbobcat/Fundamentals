package com.loficostudios.com.lofiCoffeeCore.utils;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;

@Deprecated
public class Common {
    public static void Broadcast(String message) {
        Bukkit.getServer().broadcast(Component.text(message));
    }
    public static void Broadcast(Component message) {
        Bukkit.getServer().broadcast( message);
    }
}
