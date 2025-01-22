package com.loficostudios.com.lofiCoffeeCore.api.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerKillPlayerEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    @Getter
    private final Player killer;
    @Getter
    private final Player victim;

    @Getter
    @Setter
    private boolean cancelled;

    public PlayerKillPlayerEvent(Player killer, Player victim) {
        this.killer = killer;
        this.victim = victim;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


}
