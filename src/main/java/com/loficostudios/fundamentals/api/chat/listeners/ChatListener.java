package com.loficostudios.fundamentals.api.chat.listeners;

import com.loficostudios.fundamentals.api.chat.ChatProvider;
import com.loficostudios.fundamentals.utils.ColorUtils;
import com.loficostudios.fundamentals.utils.Debug;
import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ChatListener implements Listener {
    private static final char HEX_SYMBOL;
    private static final Map<String, String> formatMap;

//    private static final int WIDTH = 20;
    private final ChatProvider provider;
    private final boolean placeholderAPIHook;
    private final MiniMessage mm;
    public ChatListener(ChatProvider provider) {
        mm = MiniMessage.miniMessage();
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
        var raw = provider.getJoinMessageRaw();
        var name = provider.getPlayerName(player);

        if (placeholderAPIHook) {
            raw = PlaceholderAPI.setPlaceholders(player, raw);
            name = PlaceholderAPI.setPlaceholders(player, name);
        }
        if (raw.contains("§")) {
            raw = serializeLegacy(raw);

        }
        if (name.contains("§")) {
            name = serializeLegacy(name);
        }

        try {
            e.joinMessage(mm.deserialize(raw
                    .replace("{player}", name)));
        } catch (Exception ex) {
            Debug.logWarning("Could not send serialized join-message. " + ex.getMessage());
            e.joinMessage(Component.text(raw
                    .replace("{player}", name)
            ));
        }
    }

    @EventHandler
    private void onQuit(final PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        var raw = provider.getLeaveMessageRaw();
        var name = provider.getPlayerName(player);

        if (placeholderAPIHook) {
            raw = PlaceholderAPI.setPlaceholders(player, raw);
            name = PlaceholderAPI.setPlaceholders(player, name);
        }
        if (raw.contains("§")) {
            raw = serializeLegacy(raw);
        }
        if (name.contains("§")) {
            name = serializeLegacy(name);
        }

        try {
            e.quitMessage(mm.deserialize(raw
                    .replace("{player}", name)));
        } catch (Exception ex) {
            Debug.logWarning("Could not send serialized quit-message. " + ex.getMessage());
            e.quitMessage(Component.text(raw
                    .replace("{player}", name)
            ));
        }
    }

    @EventHandler
    private void onChat(AsyncChatEvent e) {
        if (e.isCancelled())
            return;
        Player player = e.getPlayer();
        
        String message = PlainTextComponentSerializer.plainText().serialize(e.message());

        var format = provider.getChatFormat();
        var name = provider.getPlayerDisplayName(player);

        if (placeholderAPIHook) {
            message = PlaceholderAPI.setPlaceholders(player, message);
            format = PlaceholderAPI.setPlaceholders(player, format);
            name = PlaceholderAPI.setPlaceholders(player, name);
        }
        if (message.contains("§")) {
            message = serializeLegacy(message);
        }
        if (format.contains("§")){
            format = serializeLegacy(format);
        }
        if (name.contains("§")) {
            name = serializeLegacy(name);
        }

        format = format
                .replace("{player}", name)
                .replace("{message}", message);

        try {
            String result = format;
            e.renderer((
                    sender,
                    displayName,
                    eve,
                    viewer
            ) -> mm.deserialize(result));
        } catch (Exception ignore) {
        }
    }


    public static String serializeLegacy(String message) {
        var result = new StringBuilder();
        int length = message.length();

        for (int characterIndex = 0; characterIndex < length; characterIndex++) {
            char c = message.charAt(characterIndex);

            // Check for formatting code
            if (c == '§') {
                // Handle hex color codes
                if (characterIndex + 1 < length && message.charAt(characterIndex + 1) == HEX_SYMBOL && characterIndex + 13 <= length) {

                    //minimessage formats hex codes like this <#rrggbb>
                    // Extract the 6 hex digits
                    var hexColor = new StringBuilder("#");
                    for (int j = 2; j < 14; j += 2) {
                        hexColor.append(message.charAt(characterIndex + j));
                    }
                    result.append("<color:").append(hexColor).append(">");

                    // Skip over the entire hex sequence
                    // §x§r§r§g§g§b§b 14 - 1 = 13

                    characterIndex += 13;

                } else if (characterIndex + 1 < length) {
                    // Handle single-character format codes
                    // get the character after the
                    String formatCode = message.substring(characterIndex, characterIndex + 2); //gets the string with § + colorCode

                    String colorFormat = formatMap.get(formatCode);

                    if (colorFormat != null) {
                        result.append(colorFormat);
                        characterIndex++; // Skip the next character since it's part of the code
                    } else {
                        result.append(formatCode); // If no match, keep original
                    }
                }
            } else {
                // Append normal characters
                result.append(c);
            }
        }

        return result.toString();
    }


//    private Component getProfileText(Player player) {
//        StringBuilder builder = new StringBuilder();
//
//        List<String> lines = provider.getHoverLines();
//
//        if (!lines.isEmpty()) {
//            StringBuilder text = new StringBuilder(lines.get(0));
//
//            int remaining = WIDTH - text.length();
//
//            text.append(" ".repeat(Math.max(0, remaining)));
//
//            lines.set(0, text.toString());
//        }
//
//        for (String line : lines)  {
//            if (placeholderAPIHook) {
//                String text;
//                try {
//                    text = PlaceholderAPI.setPlaceholders(player, line);
//                } catch (Exception ignore) {
//                    text = line;
//                }
//
//                builder.append(text).append("\n");
//            }
//            else {
//                builder.append(line).append("\n");
//            }
//        }
//
//        return ColorUtils.deserialize(builder.toString().trim());
//    }
//
//    private Component getPlayerNameComponent(Player player, boolean displayName) {
//        Component name = displayName
//                ? player.displayName()
//                : Component.text(player.getName());
//        Component finalComponent = name;
//        if (provider.isHoverMessageEnabled()) {
//            TextComponent.Builder builder = Component.text()
//                    .append(name)
//                    .hoverEvent(HoverEvent.showText(getProfileText(player)));
//
//            ClickEvent onClick = provider.getClickEvent(player);
//            if (onClick != null) {
//                builder.clickEvent(onClick);
//            }
//            finalComponent = builder.build();
//        }
//
//        return finalComponent;
//    }

    static {
        formatMap = new HashMap<>();
        HEX_SYMBOL = 'x';
        formatMap.put("§0", "<black>");
        formatMap.put("§1", "<dark_blue>");
        formatMap.put("§2", "<dark_green>");
        formatMap.put("§3", "<dark_aqua>");
        formatMap.put("§4", "<dark_red>");
        formatMap.put("§5", "<dark_purple>");
        formatMap.put("§6", "<gold>");
        formatMap.put("§7", "<gray>");
        formatMap.put("§8", "<dark_gray>");
        formatMap.put("§9", "<blue>");
        formatMap.put("§a", "<green>");
        formatMap.put("§b", "<aqua>");
        formatMap.put("§c", "<red>");
        formatMap.put("§d", "<light_purple>");
        formatMap.put("§e", "<yellow>");
        formatMap.put("§f", "<white>");
        formatMap.put("§l", "<bold>");
        formatMap.put("§o", "<italic>");
        formatMap.put("§n", "<underline>");
        formatMap.put("§m", "<strikethrough>");
        formatMap.put("§k", "<obfuscated>");
        formatMap.put("§r", "<reset>");
    }
}
