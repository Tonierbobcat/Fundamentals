package com.loficostudios.fundamentals.listeners;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.experimental.IReloadable;
import com.loficostudios.fundamentals.player.UserManager;
import com.loficostudios.fundamentals.player.user.User;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;

public class MagnetListener implements Listener, IReloadable {

    private final FundamentalsPlugin plugin;
    private final UserManager userManager;

    private Sound pickupSound;
    private float pickUpVolume;
    private float pickUpPitch;
    private boolean magnetAlwaysEnabled;

    private boolean magnetExp;
    private boolean magnetMobDrops;
    private boolean magnetBlockDrops;

    public MagnetListener(FundamentalsPlugin plugin) {
        this.userManager = plugin.getUserManager();
        this.plugin = plugin;

        this.reload();
    }

    public void reload() {
        FileConfiguration config = plugin.getConfig();
        this.magnetAlwaysEnabled = config.getBoolean("magnet-always-enabled");
        String name = config.getString("magnet-pickup.sound");
        try {
            this.pickupSound = Sound.valueOf(name);
        } catch (IllegalArgumentException ignore) {
            plugin.getLogger().log(Level.SEVERE, "Invalid Sound");
        }
        this.pickUpVolume = (float) config.getDouble("magnet-pickup.volume");
        this.pickUpPitch = (float) config.getDouble("magnet-pickup.pitch");
        magnetExp = config.getBoolean("magnet-exp");
        magnetBlockDrops = config.getBoolean("magnet-block-drops");
        magnetMobDrops = config.getBoolean("magnet-mob-drops");
    }

    @EventHandler
    private void onBlockDropItem(BlockDropItemEvent e) {
        if (e.isCancelled() || !magnetBlockDrops)
            return;
        Player player = e.getPlayer();
        GameMode current = player.getGameMode();
        if (!current.equals(GameMode.SURVIVAL) && !current.equals(GameMode.ADVENTURE))
            return;
        User user = userManager.getUser(player);
        if (!magnetAlwaysEnabled) {
            if (!user.isMagnetEnabled())
                return;
        }
        Block block = e.getBlock();

        if (magnetBlockDrops) {
            e.setCancelled(true);
            List<Item> drops = e.getItems();

            if (drops.isEmpty())
                return;
            for (Item drop : drops) {
                player.getInventory().addItem(drop.getItemStack());
            }
            player.playSound(block.getLocation(), pickupSound, pickUpVolume, pickUpPitch);
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent e) {
        if (e.isCancelled() || !magnetExp || !e.isDropItems())
            return;
        Player player = e.getPlayer();
        GameMode current = player.getGameMode();
        if (!current.equals(GameMode.SURVIVAL) && !current.equals(GameMode.ADVENTURE))
            return;

        User user = userManager.getUser(player);
        if (!magnetAlwaysEnabled) {
            if (!user.isMagnetEnabled())
                return;
        }

        int exp = e.getExpToDrop();
        if (exp < 1)
            return;
        e.setExpToDrop(0);
        player.giveExp(exp, true);
        player.playSound(e.getBlock().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

    }

//    @EventHandler
//    private void onOreBreak(OreBreakEvent e) {
//        Collection<ItemStack> drops = e.getDrops();
//
//        if (e.getType().equals(Ore.DIAMOND)) {
//            e.setCancelled(true);
//        }
//
//        drops.clear();
//        drops.add(new ItemStack(Material.ENDER_EYE, 32));
//    }

    @EventHandler
    private void onMobDeath(EntityDeathEvent e) {
        if (e.isCancelled() || !magnetMobDrops)
            return;
        Player player = e.getEntity().getKiller();
        if (player == null)
            return;
        User user = userManager.getUser(player);
        if (!magnetAlwaysEnabled) {
            if (!user.isMagnetEnabled())
                return;
        }

        if (magnetMobDrops) {
            Collection<ItemStack> drops = new ArrayList<>(e.getDrops());
            if (!drops.isEmpty()) {
                e.getDrops().clear();

                for (ItemStack drop : drops) {
                    player.getInventory().addItem(drop);
                }
                player.playSound(e.getEntity().getLocation(), pickupSound, pickUpVolume, pickUpPitch);
            }
        }
        if (magnetExp) {
            int exp = e.getDroppedExp();
            if (exp < 1)
                return;
            e.setDroppedExp(0);
            player.giveExp(exp, true);
            player.playSound(e.getEntity().getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
    }
}
