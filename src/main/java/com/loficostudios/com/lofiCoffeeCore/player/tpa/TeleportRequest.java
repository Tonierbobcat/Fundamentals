package com.loficostudios.com.lofiCoffeeCore.player.tpa;

import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import org.bukkit.Location;

import java.util.UUID;

public record TeleportRequest(User user, Location location, long time) {
    public TeleportRequest(User user, Location location) {
        this(user, location, System.currentTimeMillis());
    }
}
