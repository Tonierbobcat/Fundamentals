package com.loficostudios.com.lofiCoffeeCore.command;

import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.command.base.Command;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.player.UserManager;
import com.loficostudios.com.lofiCoffeeCore.utils.ColorUtils;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;

public class GodCommand extends Command {

    private final UserManager userManager;

    public GodCommand(UserManager userManager) {
        this.userManager = userManager;
    }
    @Override
    protected String getIdentifier() {
        return "god";
    }
    @Override
    public void register() {
        new CommandAPICommand("god")
                .withPermission(getPermission())
                .withOptionalArguments(new PlayerArgument("player"))
                .executesPlayer((sender, args) -> {
                    Player target = (Player) args.get("player");
                    if (target != null) {
                        toggleGodMode(target);
                        return;
                    }
                    toggleGodMode(sender);
                }).register();
    }

    private void toggleGodMode(Player player) {
        User user = userManager.getUser(player);
        boolean enabled = !user.isGodModeEnabled();
        user.setGodModeEnabled(enabled);
        if (enabled) {
            player.sendMessage(ColorUtils.deserialize(Messages.GOD_ENABLED));
        }
        else
            player.sendMessage(ColorUtils.deserialize(Messages.GOD_DISABLED));

    }
}
