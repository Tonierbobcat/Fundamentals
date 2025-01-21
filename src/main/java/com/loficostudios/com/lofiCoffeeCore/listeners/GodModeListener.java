package com.loficostudios.com.lofiCoffeeCore.listeners;

import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import io.papermc.paper.event.entity.EntityDamageItemEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class GodModeListener implements Listener {

    private final LofiCoffeeCore plugin;

    public GodModeListener(LofiCoffeeCore plugin) {
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
