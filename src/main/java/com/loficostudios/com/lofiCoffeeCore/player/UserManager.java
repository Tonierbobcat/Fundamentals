package com.loficostudios.com.lofiCoffeeCore.player;

import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;
import com.loficostudios.com.lofiCoffeeCore.player.user.User;
import com.loficostudios.com.lofiCoffeeCore.utils.FileUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserManager {
    private final LofiCoffeeCore plugin;
    private final HashMap<UUID, User> onlineUsers = new HashMap<>();

    private final ConcurrentHashMap<UUID, User> loadedUsers = new ConcurrentHashMap<>();

    public UserManager(LofiCoffeeCore plugin) {
        this.plugin = plugin;
        this.loadUsers();
    }

    private void loadUsers() {
        final Logger lgr = plugin.getLogger();
        final BukkitScheduler scheduler = plugin.getServer().getScheduler();

        scheduler.runTaskAsynchronously(plugin, () -> {
            File folder = new File(plugin.getDataFolder(), "players");
            HashSet<UUID> uuids = new HashSet<>();
            if (folder.exists() && folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files == null)
                    return;
                for (File file : files) {
                    if (file.isDirectory())
                        continue;
                    String fileName = file.getName();
                    String uuidString = FileUtils.removeExtension(fileName);

                    try {
                        UUID uuid = UUID.fromString(uuidString);
                        uuids.add(uuid);
                    } catch (final IllegalArgumentException ignore) {
                        plugin.getLogger().log(Level.WARNING, "Invalid UUID found in file: " + fileName);
                    }
                }
            }

            scheduler.runTask(plugin, () -> {
                for (UUID uuid : uuids) {
                    if (uuid == null)
                        continue;
                    OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
                    User user;
                    try {
                        user = new User(player);
                    } catch (Exception e) {
                        lgr.log(Level.SEVERE, "Could not load profile because " + e.getMessage());
                        return;
                    }
                    loadedUsers.put(uuid, user);
                    lgr.log(Level.INFO, "Loaded user: " + player.getName());
                }
            });
        });
    }

    public boolean hasProfile(OfflinePlayer player) {
        return loadedUsers.containsKey(player.getUniqueId());
    }

    private void saveAll() {
        onlineUsers.values().forEach(User::save);
    }

    public User getUser(OfflinePlayer player) {
        if (player == null)
            return null;
        return loadedUsers.get(player.getUniqueId());
    }


    public @NotNull User createUser(OfflinePlayer player) throws UserAlreadyLoadedException {
        if (hasProfile(player))
            throw new UserAlreadyLoadedException("User already has account");
        return new User(player);
    }

    public void handleJoin(Player player) {
        UUID uuid = player.getUniqueId();
        Logger lgr = plugin.getLogger();
        User user = null;
        if (hasProfile(player)) {
            user = loadedUsers.get(uuid);
            onlineUsers.put(uuid, user);

        }
        else {
            try {
                user = createUser(player);
                user.setPlayer(player);
            } catch (Exception e) {
                lgr.log(Level.SEVERE, "Could not create new user. " + e.getMessage());
                player.kick(Component.text("Your User Profile is invalid please contact an admin to resolve this issue").color(TextColor.color(255, 0,0)));
                return;
            }

            onlineUsers.put(uuid, user);
            try {
                if (!loadedUsers.containsKey(uuid))
                    loadedUsers.put(uuid, user);
                else
                    throw new UserAlreadyLoadedException("Player's user profile is already loaded");
            } catch (UserAlreadyLoadedException e) {
                lgr.log(Level.WARNING, e.getMessage());
            }
        }

        // WE Set the player to flying if they are in survival to prevent them from falling to their death
        //logic statement is to prevent exploitation.
        if (player.isOp() || player.hasPermission("lcs.fly")) { //todo replace magic string
            var gm = player.getGameMode();
            if (gm.equals(GameMode.SURVIVAL) || gm.equals(GameMode.ADVENTURE)) {
                boolean wasFlying = user.isFlyEnabled();
                if (wasFlying) {
                    player.setAllowFlight(true);
                    player.setFlying(true);
                }
            }
        }

        user.update(player);
        user.updateInGameDisplay();
        user.save();
    }

    public void handleLeave(Player player) {
        User user = onlineUsers.get(player.getUniqueId());
        user.save();
        UUID uuid = player.getUniqueId();
        onlineUsers.remove(uuid);
    }

    public Collection<User> getLoadedUsers() {
        return Collections.unmodifiableCollection(this.loadedUsers.values());
    }

    public Collection<User> getOnlineUsers() {
        return Collections.unmodifiableCollection(this.onlineUsers.values());
    }
}
