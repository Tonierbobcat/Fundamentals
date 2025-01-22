package com.loficostudios.com.lofiCoffeeCore.player.tpa;

import org.bukkit.Location;

import java.util.UUID;

public record TeleportRequest(UUID uuid, Location location, long time) {
    public TeleportRequest(UUID uuid, Location location) {
        this(uuid, location, System.currentTimeMillis());
    }
}
