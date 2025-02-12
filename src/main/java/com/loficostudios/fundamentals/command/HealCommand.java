package com.loficostudios.fundamentals.command;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.Messages;
import com.loficostudios.fundamentals.command.base.Command;
import com.loficostudios.fundamentals.utils.Common;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.PlayerArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

public class HealCommand extends FundamentalsCommand {

    public HealCommand(FundamentalsPlugin plugin) {
        super(plugin);
    }

    @Override
    protected String getIdentifier() {
        return "heal";
    }

    @Override
    public void register() {
        new CommandAPICommand("heal")
                .withPermission(getPermission())
                .withOptionalArguments(new PlayerArgument("player").withPermission(getPermission() + ".others"))
                .executesPlayer(this::heal).register();
    }

    private void heal(Player sender, CommandArguments args) {
        Player target = (Player) args.get("player");

        if (target != null) {

            AttributeInstance maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH);

            if (maxHealth == null) {
                return;
            }
            target.setHealth(maxHealth.getValue());
            target.setFoodLevel(20);

            Common.sendMessage(target, Messages.HEALED_SELF);
            Common.sendMessage(sender, Messages.HEALED_TARGET.replace("{target}", target.getName()));
            return;
        }
        AttributeInstance maxHealth = sender.getAttribute(Attribute.GENERIC_MAX_HEALTH);

        if (maxHealth == null) {
            return;
        }

        sender.setHealth(maxHealth.getValue());
        sender.setFoodLevel(20);

        Common.sendMessage(sender, Messages.HEALED_SELF);
    }
}
