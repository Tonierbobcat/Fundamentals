package com.loficostudios.com.lofiCoffeeCore.modules.afk;

import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;
import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.player.UserManager;
import com.loficostudios.com.lofiCoffeeCore.utils.ColorUtils;
import com.loficostudios.com.lofiCoffeeCore.utils.Common;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class AFKListener implements Listener {

    private final UserManager userManager;

    public AFKListener(UserManager userManager) {
        this.userManager = userManager;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onAFKChanged(AFKStatusChangedEvent e) {
        User user = e.getUser();
        boolean afk = e.isValue();
        if (!afk) {
            switch (e.getReason()) {
                case COMMAND, UNKNOWN:
                    Common.sendMessage(user, Messages.AFK_DISABLED);
                    break;
                case INTERACT, CHAT, MOVEMENT:
                    Common.sendMessage(user, Messages.AFK_INTERRUPTED);
                    break;
            }
        }
        else {
            Common.sendMessage(user, Messages.AFK_ENABLED);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        User user = userManager.getUser(e.getPlayer());
        if (!user.isAfk())
            return;
        Location oldPos = e.getFrom();
        Location newPos = e.getTo();
        if (oldPos.distance(newPos) > 0.1) {
            user.setAfk(false, AfkChangeReason.MOVEMENT);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        User user = userManager.getUser(e.getPlayer());
        if (!user.isAfk())
            return;

        user.setAfk(false, AfkChangeReason.INTERACT);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onChat(AsyncChatEvent e) {
        Player player = e.getPlayer();
        User user = userManager.getUser(player);
        if (!user.isAfk())
            return;

        new BukkitRunnable() {
            @Override
            public void run() {
                user.setAfk(false, AfkChangeReason.CHAT);
            }
        }.runTask(LofiCoffeeCore.getInstance());
    }

//    @EventHandler(priority = EventPriority.LOWEST)
//    private void onCommand(PlayerCommandPreprocessEvent e) {
//        Player player = e.getPlayer();
//        User user = userManager.getUser(e.getPlayer());
//        if (!user.isAfk())
//            return;
//
//        user.setAfk(false, AfkChangeReason.CHAT);
//    }
}
