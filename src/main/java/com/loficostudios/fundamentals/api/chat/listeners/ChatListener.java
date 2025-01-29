package com.loficostudios.fundamentals.api.chat.listeners;

import com.loficostudios.fundamentals.api.chat.ChatProvider;
import com.loficostudios.fundamentals.utils.ColorUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;
import java.util.regex.Pattern;

public class ChatListener implements Listener {
    private static final int WIDTH = 20;
    private final ChatProvider provider;
    private final boolean placeholderAPIHook;
    public ChatListener(ChatProvider provider) {
        boolean placeholderAPIHook;
        this.provider = provider;
        try {
            Class.forName("me.clip.placeholderapi");
            placeholderAPIHook = true;
        } catch (ClassNotFoundException e) {
            placeholderAPIHook = false;
        }
        this.placeholderAPIHook = placeholderAPIHook;
    }

    @EventHandler
    private void onJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        var message = ColorUtils.deserialize(provider.getJoinMessageRaw()).replaceText(builder -> builder
                .match(Pattern.quote("{player}"))
                .replacement(getPlayerNameComponent(player, false)));
        e.joinMessage(message);
    }

    @EventHandler
    private void onQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        var message = ColorUtils.deserialize(provider.getLeaveMessageRaw()).replaceText(builder -> builder
                .match(Pattern.quote("{player}"))
                .replacement(getPlayerNameComponent(player, false)));
        e.quitMessage(message);
    }

    @EventHandler
    private void onChat(AsyncChatEvent e) {
        if (e.isCancelled())
            return;
        Player player = e.getPlayer();
        
        String original = PlainTextComponentSerializer.plainText().serialize(e.message());

        Component format = ColorUtils.deserialize(provider.getChatFormat())
                .replaceText(TextReplacementConfig.builder()
                        .matchLiteral("%player%")
                        .replacement(getPlayerNameComponent(player, true))
                        .build())
                .replaceText(TextReplacementConfig.builder()
                        .matchLiteral("%message%")
                        .replacement(ColorUtils.deserialize(original))
                        .build());

        e.renderer((sender, displayName, message, viewer
        ) -> format);
    }



    private Component getProfileText(Player player) {
        StringBuilder builder = new StringBuilder();

        List<String> lines = provider.getHoverLines();

        if (!lines.isEmpty()) {
            StringBuilder text = new StringBuilder(lines.getFirst());

            int remaining = WIDTH - text.length();

            text.append(" ".repeat(Math.max(0, remaining)));

            lines.set(0, text.toString());
        }

        for (String line : lines)  {
            if (placeholderAPIHook) {
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

    private Component getPlayerNameComponent(Player player, boolean displayName) {
        Component name = displayName
                ? player.displayName()
                : Component.text(player.getName());
        Component finalComponent = name;
        if (provider.isHoverMessageEnabled()) {
            TextComponent.Builder builder = Component.text()
                    .append(name)
                    .hoverEvent(HoverEvent.showText(getProfileText(player)));

            ClickEvent onClick = provider.getClickEvent(player);
            if (onClick != null) {
                builder.clickEvent(onClick);
            }
            finalComponent = builder.build();
        }

        return finalComponent;
    }
}
