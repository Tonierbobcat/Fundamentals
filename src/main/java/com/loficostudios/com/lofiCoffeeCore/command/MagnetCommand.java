package com.loficostudios.com.lofiCoffeeCore.command;

import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;
import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.command.base.Command;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.utils.Common;
import dev.jorel.commandapi.CommandAPICommand;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class MagnetCommand extends Command {
    @Override
    public void register() {
        new CommandAPICommand(getIdentifier())
                .withPermission(getPermission())
                .executesPlayer(((player, args) -> {
                    User user = LofiCoffeeCore.getInstance().getUserManager().getUser(player);
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
