package com.loficostudios.com.lofiCoffeeCore.command;

import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;
import com.loficostudios.com.lofiCoffeeCore.command.base.Command;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
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
                .withPermission(getPermission())
                .withOptionalArguments(new PlayerArgument("player").withPermission(getPermission() + ".others"))
                .executesPlayer((sender, args)-> {
                    Player target = (Player) args.get("player");
                    if (target != null) {
                        User user = LofiCoffeeCore.getInstance().getUserManager().getUser(target);
                        sender.sendMessage("Balance: " + user.getMoney());
                        return;
                    }
                    User user = LofiCoffeeCore.getInstance().getUserManager().getUser(sender);
                    sender.sendMessage("Balance: " + user.getMoney());
                }).register();
    }


}
