package com.loficostudios.fundamentals.command;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.Messages;
import com.loficostudios.fundamentals.command.base.Command;
import com.loficostudios.fundamentals.player.user.User;
import com.loficostudios.fundamentals.utils.Common;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;

public class FlyCommand extends FundamentalsCommand {

    public FlyCommand(FundamentalsPlugin plugin) {
        super(plugin);
    }

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
        User user = plugin.getUserManager().getUser(player);


        user.setFlyEnabled(enabled);

        if (enabled) {
            Common.sendMessage(player, Messages.FLY_ENABLED);
        }
        else {
            Common.sendMessage(player, Messages.FLY_DISABLED);
        }
    }
}
