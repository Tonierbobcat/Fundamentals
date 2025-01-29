package com.loficostudios.fundamentals.command.teleport;

import com.loficostudios.fundamentals.Messages;
import com.loficostudios.fundamentals.command.base.AbstractUserManagementCommand;
import com.loficostudios.fundamentals.player.user.User;
import com.loficostudios.fundamentals.player.UserManager;
import com.loficostudios.fundamentals.utils.ColorUtils;
import com.loficostudios.fundamentals.utils.Common;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
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

        Component accept = createButton(
                " &a[Accept]",
                ClickEvent.runCommand("/tpaccept " + sender.player().getName()));
        Component deny = createButton(
                " &c[Deny]",
                ClickEvent.runCommand("/tpdeny " + sender.player().getName()));

        target.getRequestManager().createRequest(sender);

        Common.sendMessage(target, Component.text().append(info).append(accept).append(deny).build());
    }

    private Component createButton(String label, ClickEvent e) {
        Component text = ColorUtils.deserialize(label);
        return Component.text()
                .append(text).clickEvent(e)
                .decoration(TextDecoration.BOLD, true).build();
    }

    @Override
    protected String getIdentifier() {
        return "tprequest";
    }
}
