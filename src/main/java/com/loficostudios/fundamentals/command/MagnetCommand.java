package com.loficostudios.fundamentals.command;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.Messages;
import com.loficostudios.fundamentals.command.base.Command;
import com.loficostudios.fundamentals.player.user.User;
import com.loficostudios.fundamentals.utils.Common;
import dev.jorel.commandapi.CommandAPICommand;
import org.jetbrains.annotations.ApiStatus;

public class MagnetCommand extends FundamentalsCommand {

    public MagnetCommand(FundamentalsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void register() {
        new CommandAPICommand(getIdentifier())
                .withPermission(getPermission())
                .executesPlayer(((player, args) -> {
                    User user = plugin.getUserManager().getUser(player);
                    toggleMagnet(user);
                })).register();
    }

    private void toggleMagnet(User user) {
        boolean enabled = !user.isMagnetEnabled();
        user.setMagnetEnabled(enabled);
        Common.sendMessage(user, enabled
                ? Messages.MAGNET_ENABLED
                : Messages.MAGNET_DISABLED);
    }

    @Override
    protected String getIdentifier() {
        return "magnet";
    }
}
