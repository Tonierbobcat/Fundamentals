package com.loficostudios.fundamentals.command.teleport;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.command.base.AbstractUserManagementCommand;
import com.loficostudios.fundamentals.player.user.User;
import com.loficostudios.fundamentals.player.UserManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

public class TeleportDenyCommand extends AbstractUserManagementCommand {

    public TeleportDenyCommand(FundamentalsPlugin plugin, UserManager userManager) {
        super(plugin, userManager);
    }

    @Override
    public void register() {

        new CommandAPICommand(getIdentifier())
                .withAliases("tpd")
                .withPermission(getPermission())
                .withArguments(new PlayerArgument("player").setOptional(true).replaceSuggestions(ArgumentSuggestions.strings(info -> {
                    if (!(info.sender() instanceof Player sender))
                        return null;
                    return userManager.getUser(sender).getRequestManager().getRequests().stream().map(HumanEntity::getName).toArray(String[]::new);
                })))
                .executesPlayer((sender, args) -> {
                    Player target = (Player) args.get("player");

                    onCommand(userManager.getUser(sender), userManager.getUser(target));
                }).register();
    }

    @Override
    protected void onCommand(User sender, User target) {
        if (target != null) {
            sender.getRequestManager().denyRequest(target);
            return;
        }
        sender.getRequestManager().denyRequest();
    }

    @Override
    protected String getIdentifier() {
        return "tpdeny";
    }
}
