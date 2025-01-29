package com.loficostudios.fundamentals.command.vanilla;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.Messages;
import com.loficostudios.fundamentals.command.FundamentalsCommand;
import com.loficostudios.fundamentals.command.base.Command;
import com.loficostudios.fundamentals.utils.Common;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.IntegerArgument;
import dev.jorel.commandapi.arguments.ItemStackArgument;
import dev.jorel.commandapi.arguments.PlayerArgument;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveCommand extends FundamentalsCommand {

    public GiveCommand(FundamentalsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void register() {
        CommandAPI.unregister("give");

        new CommandAPICommand(getIdentifier())
                .withPermission(getPermission())
                .withArguments(new PlayerArgument("player"))
                .withArguments(new ItemStackArgument("item"))
                .withOptionalArguments(new IntegerArgument("amount"))
                .executesPlayer((sender, args) -> {

                    Player target = (Player) args.get("player");
                    ItemStack item = (ItemStack) args.get("item");
                    Integer amount = (Integer) args.get("amount");
                    if (target == null) {
                        Common.sendMessage(sender, Messages.INVALID_PLAYER);
                        return;
                    }

                    if (item == null) {
                        Common.sendMessage(sender, Messages.INVALID_MATERIAL);
                        return;
                    }

                    item.setAmount(amount == null || amount < 1
                            ? 1
                            : amount);

                    target.getInventory().addItem(item);
                    Common.sendMessage(sender, Messages.GAVE_ITEM
                            .replace("{amount}","" + item.getAmount())
                            .replace("{item}", item.getType().name())
                            .replace("{name}", target.getName()));

                }).register();
    }

    @Override
    protected String getIdentifier() {
        return "give";
    }
}
