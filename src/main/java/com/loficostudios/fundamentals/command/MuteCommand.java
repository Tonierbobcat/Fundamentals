package com.loficostudios.fundamentals.command;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.Messages;
import com.loficostudios.fundamentals.command.base.AbstractUserManagementCommand;
import com.loficostudios.fundamentals.player.user.User;
import com.loficostudios.fundamentals.player.UserManager;
import com.loficostudios.fundamentals.utils.Common;

public class MuteCommand extends AbstractUserManagementCommand {
    public MuteCommand(FundamentalsPlugin plugin, UserManager userManager) {
        super(plugin, userManager);
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
