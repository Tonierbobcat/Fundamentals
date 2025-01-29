package com.loficostudios.fundamentals;

import com.loficostudios.fundamentals.api.chat.ChatProvider;
import com.loficostudios.fundamentals.experimental.IReloadable;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LCSChatProvider implements ChatProvider, IReloadable {

    private final FundamentalsPlugin plugin;
    private FileConfiguration config;

    public LCSChatProvider(FundamentalsPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @Override
    public String getJoinMessageRaw() {
        return config.getString("join-message");
    }

    @Override
    public @NotNull String getJoinMessage(Player player) {
        return getJoinMessageRaw()
                .replace("{player}", player.getName());
    }

    @Override
    public String getLeaveMessageRaw() {
        return config.getString("leave-message");
    }

    @Override
    public @NotNull String getLeaveMessage(Player player) {
        return getLeaveMessageRaw().replace("{player}", player.getName());
    }

    @Override
    public @NotNull String getChatFormat() {
        return config.getString("format");
    }

    @Override
    public @NotNull List<String> getHoverLines() {
        return config.getStringList("hover");
    }

    @Override
    public boolean isHoverMessageEnabled() {
        return config.getBoolean("hover-enabled");
    }

    @Override
    public ClickEvent getClickEvent(Player player) {
        return ClickEvent.suggestCommand("/msg " + player.getName());
    }

    @Override
    public void reload() {
        this.config = plugin.getConfig();
    }
}
