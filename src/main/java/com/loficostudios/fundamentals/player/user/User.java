package com.loficostudios.fundamentals.player.user;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.events.status.GodStatusChangedEvent;
import com.loficostudios.fundamentals.events.status.MuteStatusChangedEvent;
import com.loficostudios.fundamentals.exceptions.AFKModuleNotEnabledException;
import com.loficostudios.fundamentals.modules.afk.AFKStatusChangedEvent;
import com.loficostudios.fundamentals.modules.afk.AfkChangeReason;
import com.loficostudios.fundamentals.player.PlayerDoesNotExistException;
import com.loficostudios.fundamentals.player.PlayerExtension;
import com.loficostudios.fundamentals.player.tpa.TeleportRequestManager;
import com.loficostudios.fundamentals.utils.ColorUtils;
import com.loficostudios.melodyapi.file.impl.YamlFile;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings({"LombokSetterMayBeUsed", "LombokGetterMayBeUsed"})
public class User extends PlayerExtension {

    private final YamlFile playerFile;
    private final FileConfiguration config;

    private boolean muted;

    private String displayName;

    private boolean godModeEnabled;
    private boolean afk;

    private boolean magnetEnabled;

    private double money;


    @Getter
    @ApiStatus.Experimental
    private final TeleportRequestManager requestManager;

    private boolean flyEnabled;


    public User(OfflinePlayer player) throws PlayerDoesNotExistException {
        super(null, player.getUniqueId());

        this.playerFile = new YamlFile("players/" + uuid + ".yml", FundamentalsPlugin.getInstance());
        this.config = playerFile.getConfig();
        this.money = config.getDouble("money");
        this.muted = config.getBoolean("muted");
        this.flyEnabled = config.getBoolean("flying");



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
        config.set("flying", flyEnabled);
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
        player().displayName(ColorUtils.deserialize(displayName));
    }

    public boolean isFlyEnabled() {
        return this.flyEnabled;
    }
    public void setFlyEnabled(boolean value) {
        this.flyEnabled = value;
        player().setAllowFlight(value);
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
        if (!FundamentalsPlugin.getInstance().isAfkModuleEnabled()) {
            throw new AFKModuleNotEnabledException("AFK Module is not enabled!");
        }
        return afk;
    }
    public void setAfk(boolean value, AfkChangeReason reason) {
        if (!FundamentalsPlugin.getInstance().isAfkModuleEnabled()) {
            throw new AFKModuleNotEnabledException("AFK Module is not enabled!");
        }
        boolean wasAfk = afk;
        afk = value;

        if (wasAfk != value) {
            Bukkit.getServer().getPluginManager().callEvent(new AFKStatusChangedEvent(this, value, reason));
        }
    }
    public void setAfk(boolean value) {
        if (!FundamentalsPlugin.getInstance().isAfkModuleEnabled()) {
            throw new AFKModuleNotEnabledException("AFK Module is not enabled!");
        }
        boolean wasAfk = afk;
        afk = value;
        if (wasAfk != value) {
            Bukkit.getServer().getPluginManager().callEvent(new AFKStatusChangedEvent(this, value, AfkChangeReason.UNKNOWN));
        }
    }
    public boolean isMagnetEnabled() {
        return this.magnetEnabled;
    }
    public void setMagnetEnabled(boolean value) {
        this.magnetEnabled = value;
    }
}
