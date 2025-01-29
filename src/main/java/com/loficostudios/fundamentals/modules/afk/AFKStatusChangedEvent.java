package com.loficostudios.fundamentals.modules.afk;

import com.loficostudios.fundamentals.events.status.StatusChangedEvent;
import com.loficostudios.fundamentals.player.user.User;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class AFKStatusChangedEvent extends StatusChangedEvent {
    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    private final AfkChangeReason reason;

    public AFKStatusChangedEvent(User user, boolean value, AfkChangeReason reason) {
        super(user, value);
        this.reason = reason;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
