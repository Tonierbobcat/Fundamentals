package com.loficostudios.com.lofiCoffeeCore.command;

import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.command.base.AbstractUserManagementCommand;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.player.UserManager;

public class MuteCommand extends AbstractUserManagementCommand {
    public MuteCommand(UserManager userManager) {
        super(userManager);
    }

    @Override
    protected String getIdentifier() {
        return "mute";
    }

    @Override
    protected void onCommand(User sender, User target) {

        boolean muted = !target.isMuted();

        target.setMuted(muted);

        if (muted) {
            target.getPlayer().sendMessage(Messages.NOW_MUTED);
        }
        else {
            target.getPlayer().sendMessage(Messages.NO_LONGER_MUTED);
        }
    }
}
