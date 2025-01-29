package com.loficostudios.fundamentals.command;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.Messages;
import com.loficostudios.fundamentals.command.base.Command;
import com.loficostudios.fundamentals.player.UserManager;
import com.loficostudios.fundamentals.player.user.User;
import com.loficostudios.fundamentals.utils.Common;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;

public class BalanceCommand extends FundamentalsCommand {

    private final UserManager userManager;
    public BalanceCommand(FundamentalsPlugin plugin, UserManager userManager) {
        super(plugin);
        this.userManager = userManager;
    }

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
                        User user = userManager.getUser(target);
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
                    User user = userManager.getUser(sender);
                    Common.sendMessage(sender, Messages.BALANCE_SELF
                            .replace("{amount}", "" + user.getMoney()));
                }).register();
    }


}
