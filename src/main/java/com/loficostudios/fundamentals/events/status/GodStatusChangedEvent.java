package com.loficostudios.fundamentals.events.status;

import com.loficostudios.fundamentals.player.user.User;

public class GodStatusChangedEvent extends StatusChangedEvent {
    public GodStatusChangedEvent(User user, boolean value) {
        super(user, value);
    }
}
