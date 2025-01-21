package com.loficostudios.com.lofiCoffeeCore.listeners;

import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;
import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.utils.ColorUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class PlayerChatListener implements Listener {

    private final LofiCoffeeCore plugin;

    public PlayerChatListener(LofiCoffeeCore plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    private void onChat(AsyncChatEvent e) {
        Player player = e.getPlayer();
        User user = plugin.getUserManager().getUser(player);
        if (user.isMuted()) {
            user.sendMessage(ColorUtils.deserialize(Messages.MUTED));
            e.setCancelled(true);
            return;
        }

        boolean hoverEnabled = plugin.getConfig().getBoolean("hover-enabled");
        Component text = ColorUtils.deserialize(user.getDisplayName());
        Component playerNameComponent = text;
        if (hoverEnabled) {
            playerNameComponent = Component.text()
                    .append(text)
                    .hoverEvent(HoverEvent.showText(getProfileText(player)))
                    .build();
        }

        Component format = ColorUtils.deserialize(plugin.getConfig().getString("format"))
                .replaceText(TextReplacementConfig.builder()
                        .matchLiteral("%player%")
                        .replacement(playerNameComponent)
                        .build())
                .replaceText(TextReplacementConfig.builder()
                        .matchLiteral("%message%")
                        .replacement(ColorUtils.deserialize(e.message()))
                        .build());

        e.renderer((sender, displayName, message, viewer) -> format);
    }

    private static final int WIDTH = 20;

    private Component getProfileText(Player player) {
        final FileConfiguration config = plugin.getConfig();

        StringBuilder builder = new StringBuilder();

        List<String> lines = config.getStringList("hover");

        if (!lines.isEmpty()) {
            StringBuilder text = new StringBuilder(lines.getFirst());

            int remaining = WIDTH - text.length();

            text.append(" ".repeat(Math.max(0, remaining)));

            lines.set(0, text.toString());
        }

        for (String line : lines)  {
            if (plugin.isPapiHook()) {
                String text;
                try {
                    text = PlaceholderAPI.setPlaceholders(player, line);
                } catch (Exception ignore) {
                    text = line;
                }

                builder.append(text).append("\n");
            }
            else {
                builder.append(line).append("\n");
            }
        }




        return ColorUtils.deserialize(builder.toString().trim());
    }
}
