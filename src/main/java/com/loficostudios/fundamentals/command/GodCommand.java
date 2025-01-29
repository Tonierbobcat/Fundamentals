package com.loficostudios.fundamentals.command;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.Messages;
import com.loficostudios.fundamentals.command.base.Command;
import com.loficostudios.fundamentals.player.user.User;
import com.loficostudios.fundamentals.player.UserManager;
import com.loficostudios.fundamentals.utils.Common;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;

public class GodCommand extends FundamentalsCommand {

    private final UserManager userManager;

    public GodCommand(FundamentalsPlugin plugin, UserManager userManager) {
        super(plugin);
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
            Common.sendMessage(player, Messages.GOD_ENABLED);
        }
        else
            Common.sendMessage(player, Messages.GOD_DISABLED);

    }
}
