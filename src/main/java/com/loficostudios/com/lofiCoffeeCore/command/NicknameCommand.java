package com.loficostudios.com.lofiCoffeeCore.command;

import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.command.base.Command;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.player.UserManager;
import com.loficostudios.com.lofiCoffeeCore.utils.ColorUtils;
import com.loficostudios.com.lofiCoffeeCore.utils.Common;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.arguments.StringArgument;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class NicknameCommand extends Command {


    private final UserManager userManager;

    public NicknameCommand(UserManager userManager) {
        this.userManager = userManager;
    }


    @Override
    public void register() {

        new CommandAPICommand("nickname")
                .withPermission(getPermission())
                .withAliases("nick")
                .withOptionalArguments(new StringArgument("text"))
                .withOptionalArguments(new PlayerArgument("player").withPermission(getPermission() + ".others"))
                .executesPlayer((sender, args) -> {

                    String text = (String) args.get("text");
                    Player target = (Player) args.get("player");

                    if (target != null) {
                        changeDisplayName(target, text);
                        return;
                    }
                    changeDisplayName(sender, text);

                }).register();
    }

    @Override
    protected String getIdentifier() {
        return "nickname";
    }

    private void changeDisplayName(Player player, String text) {
        User user = userManager.getUser(player);
        String name = text != null ? text : player.getName();
        user.setDisplayName(name);

        Common.sendMessage(player, Messages.DISPLAYNAME_CHANGED
                .replace("{name}", name));
    }
}
