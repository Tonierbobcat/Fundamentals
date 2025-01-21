package com.loficostudios.com.lofiCoffeeCore.events.status;

import com.loficostudios.com.lofiCoffeeCore.player.user.User;

public class MuteStatusChangedEvent extends StatusChangedEvent {
    public MuteStatusChangedEvent(User user, boolean value) {
        super(user, value);
    }
}
