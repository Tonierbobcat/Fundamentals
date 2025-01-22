package com.loficostudios.com.lofiCoffeeCore;

import com.loficostudios.com.lofiCoffeeCore.api.chat.ChatProvider;
import com.loficostudios.com.lofiCoffeeCore.command.*;
import com.loficostudios.com.lofiCoffeeCore.command.base.Command;
import com.loficostudios.com.lofiCoffeeCore.command.teleport.TeleportAcceptCommand;
import com.loficostudios.com.lofiCoffeeCore.command.teleport.TeleportRequestCommand;
import com.loficostudios.com.lofiCoffeeCore.command.vanilla.EnchantCommand;
import com.loficostudios.com.lofiCoffeeCore.command.vanilla.GameModeCommand;
import com.loficostudios.com.lofiCoffeeCore.command.vanilla.GiveCommand;
import com.loficostudios.com.lofiCoffeeCore.economy.VaultEconomyProvider;
import com.loficostudios.com.lofiCoffeeCore.exceptions.WarpModuleNotEnabledException;
import com.loficostudios.com.lofiCoffeeCore.api.gui.GuiManager;
import com.loficostudios.com.lofiCoffeeCore.api.gui.listeners.GuiListener;
import com.loficostudios.com.lofiCoffeeCore.experimental.*;
import com.loficostudios.com.lofiCoffeeCore.listeners.GodModeListener;
import com.loficostudios.com.lofiCoffeeCore.modules.afk.command.AFKCommand;
import com.loficostudios.com.lofiCoffeeCore.modules.afk.AFKListener;
import com.loficostudios.com.lofiCoffeeCore.listeners.EnviormentListener;
import com.loficostudios.com.lofiCoffeeCore.api.chat.listeners.ChatListener;
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
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    private final List<IReloadable> reloadTargets = new ArrayList<>();

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
        new MagnetCommand().register(); //TODO

        Arrays.asList(
                new GodCommand(userManager),
                new SpawnCommand(),
                new GiveCommand(),
                new EnchantCommand(),
                new HealCommand(),
                new GameModeCommand(),
                new NicknameCommand(userManager),
                new EconomyCommand(),
                new GameModeCommand(),
                new BalanceCommand(),
                new ReloadCommand(),
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
        //region CHAT_PROVIDER && CHAT_LISTENER
        ServicesManager servicesManager = getServer().getServicesManager();
        RegisteredServiceProvider<ChatProvider> rsp = servicesManager.getRegistration(ChatProvider.class);
        var chatProviderImpl = rsp != null
                ? rsp.getProvider()
                : null;
        if (chatProviderImpl == null) {
            chatProviderImpl = new LCSChatProvider(this);
            reloadTargets.add((IReloadable) chatProviderImpl);
        }
        else {
            if (chatProviderImpl instanceof IReloadable)
                this.reloadTargets.add((IReloadable) chatProviderImpl);
        }
        getServer().getPluginManager().registerEvents(new ChatListener(chatProviderImpl), this);
        //endregion

        Arrays.asList(
                new MagnetListener(this),
                new GodModeListener(this),
                new PlayerListener(this),
                new EnviormentListener(this),
                new GuiListener(this.guiManager)
        ).forEach(listener -> {
            if (listener instanceof IReloadable) {
                reloadTargets.add((IReloadable) listener);
            }
            Bukkit.getServer().getPluginManager().registerEvents(listener, this);
        });

        if (afkModuleEnabled) {
            Bukkit.getServer().getPluginManager()
                    .registerEvents(new AFKListener(userManager), this);
        }
    }

    @ApiStatus.Experimental
    public long reload() {
        long startTimeMillis = System.currentTimeMillis();
        this.reloadConfig();

        Messages.saveConfig();

        for (IReloadable clazz : reloadTargets) {
            clazz.reload();
        }

        return System.currentTimeMillis() - startTimeMillis;
    }

    public WarpManager getWarpManager() {
        if (!warpModuleEnabled) {
            throw new WarpModuleNotEnabledException("Warp Module is not enabled!");
        }
        return this.warpManager;
    }
}
