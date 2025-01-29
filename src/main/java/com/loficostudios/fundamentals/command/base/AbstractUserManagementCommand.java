package com.loficostudios.fundamentals.command.base;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.Messages;
import com.loficostudios.fundamentals.command.FundamentalsCommand;
import com.loficostudios.fundamentals.player.user.User;
import com.loficostudios.fundamentals.player.UserManager;
import com.loficostudios.fundamentals.utils.Common;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;

public abstract class AbstractUserManagementCommand extends FundamentalsCommand {

    protected final UserManager userManager;

    public AbstractUserManagementCommand(FundamentalsPlugin plugin, UserManager userManager) {
        super(plugin);
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
                        Common.sendMessage(sender, Messages.INVALID_PLAYER);
                        return;
                    }


                    onCommand(userManager.getUser(sender), userManager.getUser(target));
                }).register();
    }



    protected abstract void onCommand(User sender, User target);
}
