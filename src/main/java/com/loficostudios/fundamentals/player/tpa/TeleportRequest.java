package com.loficostudios.fundamentals.player.tpa;

import com.loficostudios.fundamentals.player.user.User;
import org.bukkit.Location;

public record TeleportRequest(User user, Location location, long time) {
    public TeleportRequest(User user, Location location) {
        this(user, location, System.currentTimeMillis());
    }
}
