package com.loficostudios.com.lofiCoffeeCore.command;

import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.command.base.Command;
import com.loficostudios.com.lofiCoffeeCore.utils.ColorUtils;
import com.loficostudios.com.lofiCoffeeCore.utils.Common;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;

public class FlyCommand extends Command {


    @Override
    protected String getIdentifier() {
        return "fly";
    }

    @Override
    public void register() {
        new CommandAPICommand("fly")
                .withPermission(getPermission())
                .withOptionalArguments(new PlayerArgument("player").withPermission(getPermission() + ".others"))
                .executesPlayer((sender, args) -> {
                    Player target = (Player) args.get("player");
                    if (target != null) {
                        toggleFlight(target);
                        return;
                    }
                        toggleFlight(sender);
                }).register();
    }

    private void toggleFlight(Player player) {
        boolean enabled = !player.getAllowFlight();
        player.setAllowFlight(enabled);
        if (enabled) {
            Common.sendMessage(player, Messages.FLY_ENABLED);
        }
        else {
            Common.sendMessage(player, Messages.FLY_DISABLED);
        }
    }
}
