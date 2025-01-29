package com.loficostudios.fundamentals.utils;

import org.bukkit.Bukkit;

import java.util.logging.Level;

public class Debug {
    public static void logError(String message) {
        send(Level.SEVERE, message);

    }

    public static void log(String message) {
        send(Level.INFO, message);
    }

    public static void logWarning(String message) {

        send(Level.WARNING, message);
    }

    private static void send(Level level, String message) {
        Bukkit.getLogger().log(level, "[Fundamentals] " + message);
    }
}
