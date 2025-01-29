package com.loficostudios.fundamentals;

import com.loficostudios.fundamentals.api.chat.ChatProvider;
import com.loficostudios.fundamentals.command.*;
import com.loficostudios.fundamentals.command.base.Command;
import com.loficostudios.fundamentals.command.teleport.TeleportAcceptCommand;
import com.loficostudios.fundamentals.command.teleport.TeleportDenyCommand;
import com.loficostudios.fundamentals.command.teleport.TeleportRequestCommand;
import com.loficostudios.fundamentals.command.vanilla.EnchantCommand;
import com.loficostudios.fundamentals.command.vanilla.GameModeCommand;
import com.loficostudios.fundamentals.command.vanilla.GiveCommand;
import com.loficostudios.fundamentals.economy.VaultEconomyProvider;
import com.loficostudios.fundamentals.exceptions.WarpModuleNotEnabledException;
import com.loficostudios.fundamentals.expansion.LofiCoffeeCoreExpansion;
import com.loficostudios.fundamentals.experimental.IReloadable;
import com.loficostudios.fundamentals.experimental.ReloadCommand;
import com.loficostudios.fundamentals.listeners.GodModeListener;
import com.loficostudios.fundamentals.listeners.MagnetListener;
import com.loficostudios.fundamentals.modules.afk.command.AFKCommand;
import com.loficostudios.fundamentals.modules.afk.AFKListener;
import com.loficostudios.fundamentals.listeners.EnviormentListener;
import com.loficostudios.fundamentals.api.chat.listeners.ChatListener;
import com.loficostudios.fundamentals.listeners.PlayerListener;
import com.loficostudios.fundamentals.modules.warp.command.WarpCommand;
import com.loficostudios.fundamentals.modules.warp.command.WarpsCommand;
import com.loficostudios.fundamentals.player.UserManager;
import com.loficostudios.fundamentals.modules.warp.WarpManager;
import com.loficostudios.fundamentals.utils.database.MongoDBConfiguration;
import com.loficostudios.fundamentals.utils.database.MongoDBUtils;
import com.loficostudios.melodyapi.MelodyPlugin;
import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.logging.Level;

public final class FundamentalsPlugin extends MelodyPlugin<FundamentalsPlugin> {

    public static final String namespace = "lcs";

    @Getter
    private static FundamentalsPlugin Instance;

    private WarpManager warpManager;
    @Getter
    private UserManager userManager;

    @Getter
    private boolean warpModuleEnabled;

    @Getter
    private boolean afkModuleEnabled;

    private final List<IReloadable> reloadTargets = new ArrayList<>();

    public FundamentalsPlugin() {
        Instance = this;
    }

    @Override
    protected void onStart() {
        CommandAPI.onEnable();
        createConfigs();

        boolean placeholderAPIHook = false;
        try {
            Class.forName("me.clip.placeholderapi.PlaceholderAPI");
            placeholderAPIHook = true;
        } catch (ClassNotFoundException ignore) {
        }
        if (placeholderAPIHook) {
            getLogger().log(Level.INFO, "Hooked into PlaceholderAPI");
            new LofiCoffeeCoreExpansion().register();

        }
        else {
            getLogger().log(Level.WARNING, "PlaceholderAPI not installed");
        }

        MongoDBUtils.initialize(new MongoDBConfiguration(
                    "lcs",
                    "localhost"
                ));

        if (warpModuleEnabled) {
            this.warpManager = new WarpManager(this);
        }
        this.userManager = new UserManager(this);

        registerCommands();
        registerListeners();
    }

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
    public void onDisable() {
    }

    private void registerCommands() {
        List<String> enabledCommands = getConfig().getStringList("commands");

        Map<String, Command> commands = new HashMap<>();

        commands.put("god", new GodCommand(userManager));
        commands.put("nickname", new NicknameCommand(userManager));
        commands.put("heal", new HealCommand());
        commands.put("spawn", new SpawnCommand());
        commands.put("gamemode", new GameModeCommand());
        commands.put("give", new GiveCommand());
        commands.put("enchant", new EnchantCommand());
        commands.put("economy", new EconomyCommand());
        commands.put("balance", new BalanceCommand());
        commands.put("reload", new ReloadCommand());
        commands.put("mute", new MuteCommand(userManager));
        commands.put("tpaccept", new TeleportAcceptCommand(userManager));
        commands.put("tpdeny", new TeleportDenyCommand(userManager));
        commands.put("tprequest", new TeleportRequestCommand(userManager));
        commands.put("fly", new FlyCommand());

        commands.put("magnet", new MagnetCommand());

        if (afkModuleEnabled) {
            commands.put("afk", new AFKCommand(this.userManager));
        }

        if (warpModuleEnabled) {
            commands.put("warp", new WarpCommand(this.warpManager));
            commands.put("warps", new WarpsCommand(this.warpManager));
        }

        commands.forEach((name, command) -> {
            if (enabledCommands.contains(name)) {
                try {
                    command.register();
                } catch (Exception e) {
                    getLogger().log(Level.SEVERE, "Could not register '" + name + "' command." + e.getMessage());
                }
            }
        });
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
                new EnviormentListener(this)
        ).forEach(listener -> {
            Bukkit.getServer().getPluginManager().registerEvents(listener, this);
            if (listener instanceof IReloadable) {
                reloadTargets.add((IReloadable) listener);
            }
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
