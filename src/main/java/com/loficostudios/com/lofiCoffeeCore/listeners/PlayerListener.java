package com.loficostudios.com.lofiCoffeeCore.listeners;


import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;
import com.loficostudios.com.lofiCoffeeCore.api.events.PlayerKillEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final LofiCoffeeCore plugin;

    public PlayerListener(LofiCoffeeCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        
        plugin.getUserManager().handleJoin(player);
    }

    @EventHandler
    private void onQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        plugin.getUserManager().handleLeave(player);
    }

    @EventHandler
    private void onPlayerDeath(final PlayerDeathEvent e) {
        Player victim = e.getPlayer();
        if (victim.getKiller() instanceof Player killer) {
            Bukkit.getPluginManager().callEvent(new PlayerKillEvent(victim, killer));
        }
    }
}
