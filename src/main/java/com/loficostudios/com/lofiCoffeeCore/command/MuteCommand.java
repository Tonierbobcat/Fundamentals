package com.loficostudios.com.lofiCoffeeCore.command;

import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.command.base.AbstractUserManagementCommand;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.player.UserManager;
import com.loficostudios.com.lofiCoffeeCore.utils.ColorUtils;
import com.loficostudios.com.lofiCoffeeCore.utils.Common;

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
            Common.sendMessage(target, Messages.NOW_MUTED);
        }
        else {
            Common.sendMessage(target, Messages.NO_LONGER_MUTED);
        }
    }
}
