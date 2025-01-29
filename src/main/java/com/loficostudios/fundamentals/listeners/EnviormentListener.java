package com.loficostudios.fundamentals.listeners;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import org.bukkit.event.Listener;

public class EnviormentListener implements Listener {

    private final FundamentalsPlugin plugin;

    public EnviormentListener(FundamentalsPlugin plugin) {
        this.plugin = plugin;
    }

//    @EventHandler(priority = EventPriority.LOWEST)
//    private void onBlockBreak(BlockBreakEvent e) {
//        final Player player = e.getPlayer();
//        final Block block = e.getBlock();
//
//        final ItemStack tool = player.getInventory().getItemInMainHand();
//
//        if (isOre(block)) {
//            e.setDropItems(false);
//
//            final OreBreakEvent oreBreakEvent = new OreBreakEvent(block, player,
//                    new ArrayList<>(block.getDrops(tool, player)));
//            Bukkit.getServer().getPluginManager().callEvent(oreBreakEvent);
//
//            if (oreBreakEvent.isCancelled()) {
//                e.setCancelled(true);
//                return;
//            }
//
//            Collection<ItemStack> drops = oreBreakEvent.getDrops();
//            if (drops.isEmpty())
//                return;
//
//            List<Item> items = new ArrayList<>();
//            if (player.getGameMode().equals(GameMode.SURVIVAL) || player.getGameMode().equals(GameMode.ADVENTURE)) {
//                for (ItemStack item : drops) {
//                    items.add(player.getWorld().dropItemNaturally(block.getLocation(), item));
//                }
//            }
//            BlockDropItemEvent blockDropItemEvent = new BlockDropItemEvent(block,
//                    block.getState(),
//                    player, items);
//            Bukkit.getPluginManager().callEvent(blockDropItemEvent);
//            if (blockDropItemEvent.isCancelled()) {
//                for (Item item : items) {
//                    item.remove();
//                }
//            }
//        }
//    }
////    private void on(DropItem)
//    private boolean isOre(Block block) {
//        Material type = block.getType();
//        for (Material material : ore) {
//            if (type.equals(material))
//                return true;
//        }
//        return false;
//    }


}
