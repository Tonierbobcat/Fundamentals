package com.loficostudios.com.lofiCoffeeCore.api.chat;

import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

@ApiStatus.Experimental
public interface ChatProvider {
    public String getJoinMessageRaw();
    public String getJoinMessage(Player player);
    public String getLeaveMessageRaw();
    public String getLeaveMessage(Player player);
    public String getChatFormat();
    public List<String> getHoverLines();
    public boolean isHoverMessageEnabled();
    public ClickEvent getClickEvent(Player player);
}
