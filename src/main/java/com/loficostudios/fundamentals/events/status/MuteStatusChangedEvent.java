package com.loficostudios.fundamentals.events.status;

import com.loficostudios.fundamentals.player.user.User;

public class MuteStatusChangedEvent extends StatusChangedEvent {
    public MuteStatusChangedEvent(User user, boolean value) {
        super(user, value);
    }
}
