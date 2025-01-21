package com.loficostudios.com.lofiCoffeeCore.command.teleport;

import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.command.base.AbstractUserManagementCommand;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.player.UserManager;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

public class TeleportRequestCommand extends AbstractUserManagementCommand {

    public TeleportRequestCommand(UserManager userManager) {
        super(userManager);
    }


    @Override
    public void register() {
        new CommandAPICommand(getIdentifier())
                .withAliases("tpr")
                .withPermission(getPermission())
                .withArguments(new PlayerArgument("player"))
                .executesPlayer((sender, args) -> {

                    Player target = (Player) args.get("player");

                    if (target == null) {
                        sender.sendMessage(Messages.INVALID_PLAYER);
                        return;
                    }
                    if (target.getUniqueId().equals(sender.getUniqueId())) {
                        sender.sendMessage(Messages.CANNOT_TPR_SELF);
                        return;
                    }

                    onCommand(userManager.getUser(sender), userManager.getUser(target));
                }).register();
    }

    @Override
    protected void onCommand(User sender, User target) {
        Component info = Component.text()
                .append(Component.text(sender.getDisplayName() + " has requested to teleport to you"))
                .append(Component.text("\n"))
                .build();

        Component accpet = Component.text(" [Accept]")
                .clickEvent(ClickEvent.runCommand("/tpaccept " + sender.getPlayer().getName()))
                .color(TextColor.color(0, 255, 0))
                .decoration(TextDecoration.BOLD, true);
        Component deny = Component.text(" [Deny]")
                .clickEvent(ClickEvent.runCommand("/tpdeny " + sender.getPlayer().getName()))
                .color(TextColor.color(255, 0, 0))
                .decoration(TextDecoration.BOLD, true);

        target.getRequestManager().createRequest(sender);

        target.sendMessage(Component.text().append(info).append(accpet).append(deny).build());
    }

    @Override
    protected String getIdentifier() {
        return "tprequest";
    }
}
