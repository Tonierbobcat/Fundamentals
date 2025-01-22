package com.loficostudios.com.lofiCoffeeCore.command.teleport;

import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.command.base.AbstractUserManagementCommand;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.player.UserManager;
import com.loficostudios.com.lofiCoffeeCore.utils.ColorUtils;
import com.loficostudios.com.lofiCoffeeCore.utils.Common;
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
                        Common.sendMessage(sender, Messages.INVALID_PLAYER);
                        return;
                    }
                    if (target.getUniqueId().equals(sender.getUniqueId())) {
                        Common.sendMessage(sender, Messages.CANNOT_TELEPORT_REQUEST_SELF);
                        return;
                    }

                    onCommand(userManager.getUser(sender), userManager.getUser(target));
                }).register();
    }

    @Override
    protected void onCommand(User sender, User target) {

        Component message = ColorUtils.deserialize(Messages.TELEPORT_REQUEST
                .replace("{player}", sender.getDisplayName()));

        Component info = Component.text()
                .append(message)
                .append(Component.text("\n"))
                .build();

        Component accept = Component.text(" [Accept]")
                .clickEvent(ClickEvent.runCommand("/tpaccept " + sender.getPlayer().getName()))
                .color(TextColor.color(0, 255, 0))
                .decoration(TextDecoration.BOLD, true);
        Component deny = Component.text(" [Deny]")
                .clickEvent(ClickEvent.runCommand("/tpdeny " + sender.getPlayer().getName()))
                .color(TextColor.color(255, 0, 0))
                .decoration(TextDecoration.BOLD, true);

        target.getRequestManager().createRequest(sender);

        Common.sendMessage(target, Component.text().append(info).append(accept).append(deny).build());
    }

    @Override
    protected String getIdentifier() {
        return "tprequest";
    }
}
