package com.loficostudios.com.lofiCoffeeCore.api.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerKillEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    private final Player killer;
    @Getter
    private final Player victim;

    public PlayerKillEvent(Player killer, Player victim) {
        this.killer = killer;
        this.victim = victim;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return null;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
