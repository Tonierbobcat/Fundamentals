package com.loficostudios.com.lofiCoffeeCore;

import com.loficostudios.com.lofiCoffeeCore.command.*;
import com.loficostudios.com.lofiCoffeeCore.command.base.Command;
import com.loficostudios.com.lofiCoffeeCore.command.teleport.TeleportAcceptCommand;
import com.loficostudios.com.lofiCoffeeCore.command.teleport.TeleportRequestCommand;
import com.loficostudios.com.lofiCoffeeCore.economy.VaultEconomyProvider;
import com.loficostudios.com.lofiCoffeeCore.exceptions.WarpModuleNotEnabledException;
import com.loficostudios.com.lofiCoffeeCore.api.gui.GuiManager;
import com.loficostudios.com.lofiCoffeeCore.api.gui.listener.GuiListener;
import com.loficostudios.com.lofiCoffeeCore.listeners.GodModeListener;
import com.loficostudios.com.lofiCoffeeCore.modules.afk.command.AFKCommand;
import com.loficostudios.com.lofiCoffeeCore.modules.afk.AFKListener;
import com.loficostudios.com.lofiCoffeeCore.listeners.EnviormentListener;
import com.loficostudios.com.lofiCoffeeCore.listeners.PlayerChatListener;
import com.loficostudios.com.lofiCoffeeCore.listeners.PlayerListener;
import com.loficostudios.com.lofiCoffeeCore.modules.warp.command.WarpCommand;
import com.loficostudios.com.lofiCoffeeCore.modules.warp.command.WarpsCommand;
import com.loficostudios.com.lofiCoffeeCore.player.UserManager;
import com.loficostudios.com.lofiCoffeeCore.modules.warp.WarpManager;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.logging.Level;

public final class LofiCoffeeCore extends JavaPlugin {

    public static final String namespace = "lcs";

    @Getter
    private static LofiCoffeeCore Instance;

    private WarpManager warpManager;
    @Getter
    private UserManager userManager;
    @Getter
    private GuiManager guiManager;

    @Getter
    private boolean warpModuleEnabled;

    @Getter
    private boolean afkModuleEnabled;

    public LofiCoffeeCore() {
        Instance = this;
    }

    @Getter
    private boolean papiHook;

    @Override
    public void onLoad() {
        var config = new CommandAPIBukkitConfig(this).setNamespace(namespace);
        CommandAPI.onLoad(config);
        try {
            Class.forName("net.milkbowl.vault.economy.Economy");
            getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new VaultEconomyProvider(this), this, ServicePriority.Normal);
        } catch (final ClassNotFoundException ignored) {
        }
    }

    private void createConfigs() {
        saveDefaultConfig();
        Messages.saveConfig();

        FileConfiguration config = getConfig();
        warpModuleEnabled = config.getBoolean("modules." + "warps");
        afkModuleEnabled = config.getBoolean("modules." + "afk");
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();
        createConfigs();

        papiHook = Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (papiHook) {
            getLogger().log(Level.INFO, "Hooked into PlaceholderAPI");
        }
        else {
            getLogger().log(Level.WARNING, "PlaceholderAPI not installed");
        }

        if (warpModuleEnabled) {
            this.warpManager = new WarpManager(this);
        }
        this.userManager = new UserManager(this);
        this.guiManager = new GuiManager();

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
    }

    private void registerCommands() {
        Arrays.asList(
                new GodCommand(userManager),
                new SpawnCommand(),
                new HealCommand(),
                new GameModeCommand(),
                new NicknameCommand(userManager),
                new EconomyCommand(),
                new GameModeCommand(),
                new BalanceCommand(),
                new MuteCommand(userManager),
                new TeleportAcceptCommand(userManager),
                new TeleportRequestCommand(userManager),
                new FlyCommand()
        ).forEach(Command::register);

        if (afkModuleEnabled) {
            new AFKCommand(this.userManager)
                    .register();
        }

        if (warpModuleEnabled) {
            new WarpCommand(this.warpManager)
                    .register();
            new WarpsCommand(this.warpManager)
                    .register();
        }
    }

    private void registerListeners() {
        Arrays.asList(
                new GodModeListener(this),
                new PlayerListener(this),
                new EnviormentListener(this),
                new PlayerChatListener(this),
                new GuiListener(this.guiManager)
        ).forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, this));

        if (afkModuleEnabled) {
            Bukkit.getServer().getPluginManager()
                    .registerEvents(new AFKListener(userManager), this);
        }
    }

    public WarpManager getWarpManager() {
        if (!warpModuleEnabled) {
            throw new WarpModuleNotEnabledException("Warp Module is not enabled!");
        }
        return this.warpManager;
    }
}
