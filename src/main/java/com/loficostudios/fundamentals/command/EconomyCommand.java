package com.loficostudios.fundamentals.command;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.Messages;
import com.loficostudios.fundamentals.command.base.Command;
import com.loficostudios.fundamentals.player.UserManager;
import com.loficostudios.fundamentals.player.user.User;
import com.loficostudios.fundamentals.utils.Common;
import com.loficostudios.fundamentals.utils.Economy;
import dev.jorel.commandapi.CommandTree;
import dev.jorel.commandapi.arguments.DoubleArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

public class EconomyCommand extends FundamentalsCommand {

    private final UserManager userManager;

    public EconomyCommand(FundamentalsPlugin plugin, UserManager userManager) {
        super(plugin);
        this.userManager = userManager;
    }

    private enum BalanceModifyResult {
        SUCCESS("Success"),
        INVALID_TRANSACTION(Messages.INVALID_TRANSACTION),
        FAILURE("Failure");
        @Getter
        private final String message;
        BalanceModifyResult(String message) {
            this.message = message;
        }
    }

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
                            for (User user : userManager.getLoadedUsers()) {
//                                sender.sendMessage("Reset " + Bukkit.getOfflinePlayer(user.getUniqueId()).getName());
                                Economy.setMoney(user, 0);
                            }
                        }))
                .then(new LiteralArgument("add")
                        .then(new PlayerArgument("player")
                                .then(new DoubleArgument("amount")
                                    .executesPlayer((sender, args) -> {
                                        modify(sender, args, this::add, Messages.MONEY_ADD_SELF, Messages.MONEY_ADD_OTHER);
                                    }))))
                .then(new LiteralArgument("set")
                        .then(new PlayerArgument("player")
                                .then(new DoubleArgument("amount")
                                        .executesPlayer((sender, args) -> {
                                            modify(sender, args, this::set, Messages.MONEY_SET_SELF, Messages.MONEY_SET_OTHER);
                                        }))))
                .then(new LiteralArgument("remove")
                        .then(new PlayerArgument("player")
                                .then(new DoubleArgument("amount")
                                        .executesPlayer((sender, args) -> {
                                            modify(sender, args, this::remove, Messages.MONEY_REMOVE_SELF, Messages.MONEY_REMOVE_OTHER);
                                        })))).register();
    }

    private BalanceModifyResult set(User user, double amount) {
        try {
            Economy.setMoney(user, amount);
            return BalanceModifyResult.SUCCESS;
        } catch (IllegalArgumentException e) {
            return BalanceModifyResult.INVALID_TRANSACTION;
        }
        catch (Exception e) {
            return BalanceModifyResult.FAILURE;
        }
    }

    private BalanceModifyResult remove(User user, double amount) {
        try {
            Economy.subtractMoney(user, amount);
            return BalanceModifyResult.SUCCESS;
        } catch (IllegalArgumentException e) {
            return BalanceModifyResult.INVALID_TRANSACTION;
        }
        catch (Exception e) {
            return BalanceModifyResult.FAILURE;
        }
    }

    private BalanceModifyResult add(User user, double amount) {
        try {
            Economy.addMoney(user, amount);
            return BalanceModifyResult.SUCCESS;
        } catch (IllegalArgumentException e) {
            return BalanceModifyResult.INVALID_TRANSACTION;
        }
        catch (Exception e) {
            return BalanceModifyResult.FAILURE;
        }
    }

    private void modify(Player sender, CommandArguments args, BiFunction<User, Double, BalanceModifyResult> onCommand, String selfMessage, String otherMessage) {
        Player target = (Player) args.get("player");
        Double amount = (Double) args.get("amount");

        if (target != null) {

            var result = onCommand.apply(userManager.getUser(target), amount);
            if (!result.equals(BalanceModifyResult.SUCCESS)) {
                Common.sendMessage(sender, result.getMessage()
                        .replace("{amount}", "" + amount));
                return;
            }

            Common.sendMessage(target, selfMessage
                    .replace("{amount}", "" + amount));
            if (!target.equals(sender)) {
                Common.sendMessage(sender, otherMessage
                                .replace("{amount}", "" + amount)
                                .replace("{player}", target.getName()));
            }

            return;
        }
        User user = userManager.getUser(sender);
        var result = onCommand.apply(user, amount);
        if (!result.equals(BalanceModifyResult.SUCCESS)) {
            Common.sendMessage(sender, result.getMessage()
                    .replace("{amount}", "" + amount));
            return;
        }
        Common.sendMessage(sender, selfMessage
                .replace("{amount}", "" + amount));
    }

}
