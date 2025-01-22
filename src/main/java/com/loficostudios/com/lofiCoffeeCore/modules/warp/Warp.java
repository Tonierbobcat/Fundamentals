package com.loficostudios.com.lofiCoffeeCore.modules.warp;

import com.loficostudios.com.lofiCoffeeCore.api.gui.GuiIcon;
import com.loficostudios.com.lofiCoffeeCore.modules.warp.event.WarpEvent;
import com.loficostudios.com.lofiCoffeeCore.utils.ColorUtils;
import com.loficostudios.com.lofiCoffeeCore.utils.Common;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import static com.loficostudios.com.lofiCoffeeCore.utils.ColorUtils.deserialize;

public class Warp {
    @Getter
    private final String id;

    @Getter
    @Setter
    private Location location;

    @Getter
    @Setter
    private String displayName;

    @Setter
    private Material iconMaterial;

    public Warp(@NotNull Location location, @NotNull String id, @NotNull String displayName, @NotNull Material material) {
        this.id = id;
        this.location = location;
        this.displayName = displayName;
        this.iconMaterial = material;
    }

    public Warp(@NotNull Location location, @NotNull String id,@NotNull  String displayName) {
        this.id = id;
        this.location = location;
        this.displayName = displayName;
        this.iconMaterial = Material.ENDER_EYE;
    }

    public Warp(@NotNull Location location, @NotNull String id) {
        this.id = id;
        this.location = location;
        this.iconMaterial = Material.ENDER_EYE;
        this.displayName = id;
    }

    public WarpTeleportResult teleport(Player player) {
        if (this.location == null || this.location.getWorld() == null) {
            return WarpTeleportResult.LOCATION_INVALID;
        }
        Location from = player.getLocation();

        var e = new WarpEvent(player, from, this.location);
        Bukkit.getServer().getPluginManager().callEvent(e);

        if (e.isCancelled()) {
            return WarpTeleportResult.CANCELLED;
        }

        player.teleport(this.location);
        player.playSound(player.getLocation(), Sound.ITEM_CHORUS_FRUIT_TELEPORT, 1, 1);
        return WarpTeleportResult.SUCCESS;
    }

    public @NotNull GuiIcon getIcon() {
        if (iconMaterial == null) {
            ItemStack fallBackIcon = new ItemStack(Material.BARRIER);
            fallBackIcon.editMeta(meta -> meta.displayName(deserialize(id)));
            return new GuiIcon(fallBackIcon, id);
        }
        ItemStack icon = new ItemStack(iconMaterial);

        String name = displayName != null
                ? displayName
                : id;

        ItemMeta meta = icon.getItemMeta();
        if (meta != null) {
            meta.displayName(deserialize(name).decoration(TextDecoration.ITALIC, false));
            icon.setItemMeta(meta);
        }

        return new GuiIcon(icon, this.id, player -> {
            var result = teleport(player);
            Common.sendMessage(player, result.getMessage()
                    .replace("{id}", this.id)
                    .replace("{name}", this.displayName));
        });
    }
}
