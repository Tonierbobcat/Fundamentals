package com.loficostudios.com.lofiCoffeeCore.events.status;

import com.loficostudios.com.lofiCoffeeCore.player.user.User;

public class GodStatusChangedEvent extends StatusChangedEvent {
    public GodStatusChangedEvent(User user, boolean value) {
        super(user, value);
    }
}
