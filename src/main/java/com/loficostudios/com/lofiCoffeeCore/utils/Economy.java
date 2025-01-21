package com.loficostudios.com.lofiCoffeeCore.utils;

import com.loficostudios.com.lofiCoffeeCore.player.user.User;

public class Economy {
    public static void setMoney(User user, double amount) {
        user.setMoney(amount);
    }
    public static void addMoney(User user, double amount) {
        double balance = user.getMoney();
        user.setMoney(balance + amount);
    }
    public static void subtractMoney(User user, double amount) {
        double balance = user.getMoney();
        user.setMoney(balance - amount);
    }
}
