package com.loficostudios.com.lofiCoffeeCore.command;

import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;
import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.command.base.Command;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.utils.Common;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;

public class BalanceCommand extends Command {
    @Override
    protected String getIdentifier() {
        return "balance";
    }
    @Override
    public void register() {
        new CommandAPICommand("balance")
                .withAliases("bal")
                .withPermission(getPermission())
                .withOptionalArguments(new PlayerArgument("player").withPermission(getPermission() + ".others"))
                .executesPlayer((sender, args)-> {
                    Player target = (Player) args.get("player");
                    if (target != null) {
                        User user = LofiCoffeeCore.getInstance().getUserManager().getUser(target);
                        if (!target.equals(sender)) {
                            Common.sendMessage(sender, Messages.BALANCE_OTHERS
                                    .replace("{amount}", "" + user.getMoney())
                                    .replace("{player}", target.getName()));
                            return;
                        }
                        Common.sendMessage(sender, Messages.BALANCE_SELF
                                .replace("{amount}", "" + user.getMoney()));
                        return;
                    }
                    User user = LofiCoffeeCore.getInstance().getUserManager().getUser(sender);
                    Common.sendMessage(sender, Messages.BALANCE_SELF
                            .replace("{amount}", "" + user.getMoney()));
                }).register();
    }


}
