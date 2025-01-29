package com.loficostudios.fundamentals.listeners;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.player.user.User;
import io.papermc.paper.event.entity.EntityDamageItemEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class GodModeListener implements Listener {

    private final FundamentalsPlugin plugin;

    public GodModeListener(FundamentalsPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;
        User user = plugin.getUserManager().getUser(player);
        if (!user.isGodModeEnabled())
            return;
        e.setCancelled(true);
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;
        User user = plugin.getUserManager().getUser(player);
        if (!user.isGodModeEnabled())
            return;
        e.setCancelled(true);
    }

    @EventHandler
    private void onEntityDamageByBlock(EntityDamageByBlockEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;
        User user = plugin.getUserManager().getUser(player);
        if (!user.isGodModeEnabled())
            return;
        e.setCancelled(true);
    }

    @EventHandler
    private void onEntityDamageItem(EntityDamageItemEvent e) {
        if (!(e.getEntity() instanceof Player player))
            return;
        User user = plugin.getUserManager().getUser(player);
        if (!user.isGodModeEnabled())
            return;
        e.setCancelled(true);
    }

}
