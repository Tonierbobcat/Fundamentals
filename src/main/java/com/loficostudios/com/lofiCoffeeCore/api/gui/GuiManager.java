/**
 * @Author Tonierbobcat
 * @Github https://github.com/Tonierbobcat
 * @version MelodyApi
 */

package com.loficostudios.com.lofiCoffeeCore.api.gui;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiManager {

    private final Map<UUID, MelodyGui> playerData = new HashMap<>();

    public MelodyGui getGui(@NotNull Player player) {
        return this.playerData.get(player.getUniqueId());
    }

    public void setGui(@NotNull Player player, @NotNull MelodyGui gui) {
        this.playerData.put(player.getUniqueId(), gui);
    }
}