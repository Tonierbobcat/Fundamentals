package com.loficostudios.com.lofiCoffeeCore.player.user;

import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;
import com.loficostudios.com.lofiCoffeeCore.events.status.GodStatusChangedEvent;
import com.loficostudios.com.lofiCoffeeCore.events.status.MuteStatusChangedEvent;
import com.loficostudios.com.lofiCoffeeCore.exceptions.AFKModuleNotEnabledException;
import com.loficostudios.com.lofiCoffeeCore.api.file.impl.YamlFile;
import com.loficostudios.com.lofiCoffeeCore.modules.afk.AFKStatusChangedEvent;
import com.loficostudios.com.lofiCoffeeCore.modules.afk.AfkChangeReason;
import com.loficostudios.com.lofiCoffeeCore.player.PlayerDoesNotExistException;
import com.loficostudios.com.lofiCoffeeCore.player.PlayerExtension;
import com.loficostudios.com.lofiCoffeeCore.player.tpa.TeleportRequestManager;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

@SuppressWarnings({"LombokSetterMayBeUsed", "LombokGetterMayBeUsed"})
public class User extends PlayerExtension {

    private final YamlFile playerFile;
    private final FileConfiguration config;

    private boolean muted;

    private String displayName;

    private boolean godModeEnabled;
    private boolean afk;

    private double money;
    @Getter
    private final TeleportRequestManager requestManager;

    public User(OfflinePlayer player) throws PlayerDoesNotExistException {
        super(null, player.getUniqueId());

        this.playerFile = new YamlFile("players/" + uuid + ".yml", LofiCoffeeCore.getInstance());
        this.config = playerFile.getConfig();
        this.money = config.getDouble("money");
        this.muted = config.getBoolean("muted");

        String displayNameFromConfig = config.getString("displayName");

        this.displayName = displayNameFromConfig == null || displayNameFromConfig.isEmpty()
                ? player.getName()
                : displayNameFromConfig;
        this.requestManager = new TeleportRequestManager(this);
        save();
    }

    public void save() {
        config.set("uuid", uuid.toString());
        config.set("money", money);
        config.set("muted", muted);
        config.set("displayName", displayName);

//        config.set("displayname", displayName);
//
        playerFile.save();
    }

    public void update(Player player) {
        setPlayer(player);
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void updateInGameDisplay() {
        getPlayer().displayName(Component.text(displayName));
    }

    public boolean isMuted() {
        return this.muted;
    }
    public void setMuted(boolean value) {
        boolean wasMuted = muted;
        this.muted = value;

        if (value != wasMuted) {
            Bukkit.getPluginManager().callEvent(new MuteStatusChangedEvent(this, value));
        }
    }
    public void setDisplayName(String text) {
        displayName = text;
        updateInGameDisplay();
        save();
    }
    public boolean isGodModeEnabled() {
        return godModeEnabled;
    }
    public void setGodModeEnabled(boolean value) {
        boolean wasGod = godModeEnabled;
        godModeEnabled = value;

        if (value != wasGod) {
            Bukkit.getServer().getPluginManager().callEvent(new GodStatusChangedEvent(this, value));
        }
    }
    public double getMoney() {
        return this.money;
    }
    public void setMoney(double value) {
        this.money = value;
        save();
    }
    public boolean isAfk() {
        if (!LofiCoffeeCore.getInstance().isAfkModuleEnabled()) {
            throw new AFKModuleNotEnabledException("AFK Module is not enabled!");
        }
        return afk;
    }
    public void setAfk(boolean value, AfkChangeReason reason) {
        if (!LofiCoffeeCore.getInstance().isAfkModuleEnabled()) {
            throw new AFKModuleNotEnabledException("AFK Module is not enabled!");
        }
        boolean wasAfk = afk;
        afk = value;

        if (wasAfk != value) {
            Bukkit.getServer().getPluginManager().callEvent(new AFKStatusChangedEvent(this, value, reason));
        }
    }
    public void setAfk(boolean value) {
        if (!LofiCoffeeCore.getInstance().isAfkModuleEnabled()) {
            throw new AFKModuleNotEnabledException("AFK Module is not enabled!");
        }
        boolean wasAfk = afk;
        afk = value;
        if (wasAfk != value) {
            Bukkit.getServer().getPluginManager().callEvent(new AFKStatusChangedEvent(this, value, AfkChangeReason.UNKNOWN));
        }
    }
}
