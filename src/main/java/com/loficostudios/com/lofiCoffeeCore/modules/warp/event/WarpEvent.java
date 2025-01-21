package com.loficostudios.com.lofiCoffeeCore.modules.warp.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class WarpEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    private Player player;
    @Getter
    private Location from;
    @Getter
    private Location to;

    @Getter
    @Setter
    private boolean cancelled;

    public WarpEvent(Player player, Location from, Location to) {
        this.player = player;
        this.from = from;
        this.to = to;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}