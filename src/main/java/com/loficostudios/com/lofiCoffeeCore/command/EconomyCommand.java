package com.loficostudios.com.lofiCoffeeCore.command;

import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;
import com.loficostudios.com.lofiCoffeeCore.command.base.Command;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.utils.Economy;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public class EconomyCommand extends Command {

    @Override
    protected String getIdentifier() {
        return "economy";
    }


    @Override
    public void register() {
        new CommandTree("economy")
                .withPermission(getPermission())
                .withAliases("eco")
                .then(new LiteralArgument("reset")
                        .executesPlayer((sender, args) -> {
                            for (User user : LofiCoffeeCore.getInstance().getUserManager().getLoadedUsers()) {
//                                sender.sendMessage("Reset " + Bukkit.getOfflinePlayer(user.getUniqueId()).getName());
                                Economy.setMoney(user, 0);
                            }
                        }))
                .then(new LiteralArgument("add")
                        .then(new DoubleArgument("amount")
                                .then(new PlayerArgument("player").setOptional(true)
                                    .executesPlayer((sender, args) -> {
                                        modify(sender, args, Economy::setMoney, "Added money", "Added money to player");
                                    }))))
                .then(new LiteralArgument("set")
                        .then(new DoubleArgument("amount")
                                .then(new PlayerArgument("player").setOptional(true)
                                        .executesPlayer((sender, args) -> {
                                            modify(sender, args, Economy::setMoney, "Set money", "Set money to player");
                                        }))))
                .then(new LiteralArgument("remove")
                        .then(new DoubleArgument("amount")
                                .then(new PlayerArgument("player").setOptional(true)
                                        .executesPlayer((sender, args) -> {
                                            modify(sender, args, Economy::subtractMoney, "remove money", "remove money to player");
                                        }))))
                .register();
    }

    private void modify(Player sender, CommandArguments args, BiConsumer<User, Double> onCommand, String selfMessage, String otherMessage) {
        Player target = (Player) args.get("player");
        Double amount = (Double) args.get("amount");

        if (target != null) {
            User user = LofiCoffeeCore.getInstance().getUserManager().getUser(target);
            onCommand.accept(user, amount);
            target.sendMessage(otherMessage);
            if (!target.equals(sender)) {
                sender.sendMessage(selfMessage);
            }
            return;
        }
        User user = LofiCoffeeCore.getInstance().getUserManager().getUser(sender);
        onCommand.accept(user, amount);
        sender.sendMessage(selfMessage);
    }

}
