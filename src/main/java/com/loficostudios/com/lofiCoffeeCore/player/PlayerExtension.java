package com.loficostudios.com.lofiCoffeeCore.player;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerExtension {

    private Player player;
    protected final UUID uuid;

    public PlayerExtension(Player player, UUID uuid) {
        this.player = player;
        this.uuid = uuid;
    }

    public Player player() {
        return player;
    }

    public UUID getUniqueId() {
        return this.uuid;
    }

    protected void setPlayer(Player player) {
        if (!player.getUniqueId().equals(uuid)) {
            throw new PlayerChecksumException("Player UUID does not match value defined in base!");
        }
        this.player = player;
    }

    public void sendMessage(Component message) {
        player.sendMessage(message);
    }
    public void sendMessage(String message) {
        player.sendMessage(message);
    }
}
