package com.loficostudios.fundamentals.utils;

import com.loficostudios.fundamentals.player.user.User;
import org.jetbrains.annotations.NotNull;

public class Economy {
    private static double minMoney = 0;
    private static double maxMoney = 999999999;
    public static void setMoney(@NotNull User user, double amount) {
//        double bal = user.getMoney();
        if (amount < minMoney)
            throw new IllegalArgumentException("Amount less then min");
        if (amount > maxMoney)
            throw new IllegalArgumentException("Amount greater then max");
        user.setMoney(amount);
    }
    public static void addMoney(User user, double amount) {
        double balance = user.getMoney();
        setMoney(user, balance + amount);
    }
    public static void subtractMoney(User user, double amount) {
        double balance = user.getMoney();

        setMoney(user, balance - amount);
    }
    public static double getMoney(User user) {
        return user.getMoney();
    }
    public static boolean has(User user, double amount) {
//        Common.sendMessage(user, "Has Amount: " + (user.getMoney() >= amount));
        return getMoney(user) >= amount;
    }
}
