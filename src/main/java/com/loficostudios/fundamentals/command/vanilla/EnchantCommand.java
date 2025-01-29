package com.loficostudios.fundamentals.command.vanilla;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.Messages;
import com.loficostudios.fundamentals.command.FundamentalsCommand;
import com.loficostudios.fundamentals.command.base.Command;
import com.loficostudios.fundamentals.utils.Common;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.*;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class EnchantCommand extends FundamentalsCommand {
    public EnchantCommand(FundamentalsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void register() {
        CommandAPI.unregister("enchant");
        new CommandAPICommand(getIdentifier())
                .withPermission(getPermission())
                .withArguments(new PlayerArgument("player"))
                .withArguments(new EnchantmentArgument("enchant"))
                .withOptionalArguments(new IntegerArgument("level"))
                .executesPlayer((sender, args) -> {

                    Enchantment enchant = (Enchantment) args.get("enchant");
                    Integer level = (Integer) args.get("level");
                    Player target = (Player) args.get("player");
                    if (target == null) {
                        Common.sendMessage(sender, Messages.INVALID_PLAYER);
                        return;
                    }
                    if (enchant == null) {
                        Common.sendMessage(sender, Messages.INVALID_ENCHANTMENT);
                        return;
                    }
                    ItemStack mainHand = target.getInventory().getItemInMainHand();
                    if (mainHand.getType().equals(Material.AIR)) {
                        Common.sendMessage(sender, Messages.CANNOT_ENCHANT_AIR);
                        return;
                    }
                    ItemMeta meta =  mainHand.getItemMeta();
                    if (meta != null) {
                        meta.addEnchant(enchant, level == null || level < 0 ? 1 : level, true);
                    }
                    mainHand.setItemMeta(meta);
                }).register();
    }

    @Override
    protected String getIdentifier() {
        return "enchant";
    }
}
