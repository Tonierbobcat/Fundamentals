package com.loficostudios.com.lofiCoffeeCore.modules.afk.command;

import com.loficostudios.com.lofiCoffeeCore.command.base.Command;
import com.loficostudios.com.lofiCoffeeCore.modules.afk.AfkChangeReason;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.player.UserManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.entity.Player;

public class AFKCommand extends Command {

    private final UserManager userManager;

    public AFKCommand(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public void register() {
        new CommandAPICommand("afk")
                .withPermission(getPermission())
                .withOptionalArguments(new PlayerArgument("player"))
                .executesPlayer(this::afk).register();
    }

    @Override
    protected String getIdentifier() {
        return "afk";
    }

    private void afk(Player sender, CommandArguments args) {
        Player target = (Player) args.get("player");


        if (target != null) {
            User user = userManager.getUser(target);
            user.setAfk(!user.isAfk(), AfkChangeReason.COMMAND);
            return;
        }

        User user = userManager.getUser(sender);
        user.setAfk(!user.isAfk(), AfkChangeReason.COMMAND);
    }
}
