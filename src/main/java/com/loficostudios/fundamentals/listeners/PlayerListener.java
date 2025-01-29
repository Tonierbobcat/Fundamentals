package com.loficostudios.fundamentals.listeners;


import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.Messages;
import com.loficostudios.fundamentals.player.UserManager;
import com.loficostudios.fundamentals.player.user.User;
import com.loficostudios.fundamentals.utils.Common;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

    private final FundamentalsPlugin plugin;
    private final UserManager userManager;
    public PlayerListener(FundamentalsPlugin plugin) {
        this.plugin = plugin;
        this.userManager = plugin.getUserManager();
    }

    @EventHandler
    private void onJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();

        userManager.handleJoin(player);
    }

    @EventHandler
    private void onQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        userManager.handleLeave(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onChat(final AsyncChatEvent e) {
        Player player = e.getPlayer();
        User user = userManager.getUser(player);
        if (user.isMuted()) {
            Common.sendMessage(user, Messages.MUTED);
            e.setCancelled(true);
        }
    }

//    @EventHandler
//    private void onPlayerDamage(final EntityDamageByEntityEvent e) {
//        if (e.isCancelled())
//            return;
//        if (!(e.getDamager() instanceof Player damager) || !(e.getEntity() instanceof Player victim))
//            return;
//
//        if((victim.getHealth()-e.getDamage()) <= 0) {
//            Player killer = damager;
//            PlayerKillPlayerEvent playerKillEvent = new PlayerKillPlayerEvent(killer, victim);
//
//            Bukkit.getPluginManager().callEvent(playerKillEvent);
//            if (playerKillEvent.isCancelled()) {
//                e.setCancelled(true);
//                return;
//            }
//            killer.sendMessage("onPlayerDamage");
//        }
//    }



    @EventHandler
    private void onGameChange(final PlayerGameModeChangeEvent e) {
        Player player = e.getPlayer();
        User user = userManager.getUser(player);
        if (user.isFlyEnabled() && player.isFlying()) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.setAllowFlight(true);
                player.setFlying(true);
            }, 1);
        }
    }
}
