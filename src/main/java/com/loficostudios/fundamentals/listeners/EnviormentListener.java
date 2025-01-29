package com.loficostudios.fundamentals.listeners;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.api.events.OreBreakEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EnviormentListener implements Listener {

    private final FundamentalsPlugin plugin;

    private final Material[] ore = new Material[] {
            Material.NETHER_QUARTZ_ORE,
            Material.COPPER_ORE,
            Material.DEEPSLATE_COPPER_ORE,
            Material.COAL_ORE,
            Material.DEEPSLATE_COAL_ORE,
            Material.IRON_ORE,
            Material.DEEPSLATE_IRON_ORE,
            Material.GOLD_ORE,
            Material.DEEPSLATE_GOLD_ORE,
            Material.NETHER_GOLD_ORE,
            Material.EMERALD_ORE,
            Material.DEEPSLATE_EMERALD_ORE,
            Material.DIAMOND_ORE,
            Material.DEEPSLATE_DIAMOND_ORE,
    };

    public EnviormentListener(FundamentalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onBlockBreak(BlockBreakEvent e) {
        final Player player = e.getPlayer();
        final Block block = e.getBlock();

        final ItemStack tool = player.getInventory().getItemInMainHand();

        if (isOre(block)) {
            e.setDropItems(false);

            final OreBreakEvent oreBreakEvent = new OreBreakEvent(block, player,
                    new ArrayList<>(block.getDrops(tool, player)));
            Bukkit.getServer().getPluginManager().callEvent(oreBreakEvent);

            if (oreBreakEvent.isCancelled()) {
                e.setCancelled(true);
                return;
            }

            Collection<ItemStack> drops = oreBreakEvent.getDrops();
            if (drops.isEmpty())
                return;

            List<Item> items = new ArrayList<>();
            if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
                for (ItemStack item : drops) {
                    items.add(player.getWorld().dropItemNaturally(block.getLocation(), item));
                }
            }
            BlockDropItemEvent blockDropItemEvent = new BlockDropItemEvent(block,
                    block.getState(),
                    player, items);
            Bukkit.getPluginManager().callEvent(blockDropItemEvent);
            if (blockDropItemEvent.isCancelled()) {
                for (Item item : items) {
                    item.remove();
                }
            }
        }
    }
//    private void on(DropItem)
    private boolean isOre(Block block) {
        Material type = block.getType();
        for (Material material : ore) {
            if (type.equals(material))
                return true;
        }
        return false;
    }


}
