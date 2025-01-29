package com.loficostudios.fundamentals.events.status;

import com.loficostudios.fundamentals.player.user.User;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public abstract class StatusChangedEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    private final User user;

    @Getter
    private final boolean value;

    public StatusChangedEvent(User user, boolean value) {
        this.user = user;
        this.value = value;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}