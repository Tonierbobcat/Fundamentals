package com.loficostudios.fundamentals.api.chat;

import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

public interface ChatProvider {
    public String getJoinMessageRaw();
    public String getJoinMessage(Player player);
    public String getLeaveMessageRaw();
    public String getLeaveMessage(Player player);
    public String getChatFormat();
    public String getPlayerName(Player player);
    public String getPlayerDisplayName(Player player);
}
