package com.loficostudios.com.lofiCoffeeCore.command.base;

import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.player.UserManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;

public abstract class AbstractUserManagementCommand extends Command {

    protected final UserManager userManager;

    public AbstractUserManagementCommand(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public void register() {

        new CommandAPICommand(getIdentifier())
                .withPermission(getPermission())
                .withArguments(new PlayerArgument("player"))
                .executesPlayer((sender, args) -> {

                    Player target = (Player) args.get("player");

                    if (target == null) {
                        sender.sendMessage(Messages.INVALID_PLAYER);
                        return;
                    }


                    onCommand(userManager.getUser(sender), userManager.getUser(target));
                }).register();
    }



    protected abstract void onCommand(User sender, User target);
}
