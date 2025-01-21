package com.loficostudios.com.lofiCoffeeCore.modules.warp;

import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;
import com.loficostudios.com.lofiCoffeeCore.api.file.impl.YamlFile;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;

public class WarpManager {

    private final HashMap<String, Warp> warps = new HashMap<>();

    private final YamlFile file;
    private final FileConfiguration config;

    private final LofiCoffeeCore plugin;

    public WarpManager(LofiCoffeeCore plugin) {
        file = new YamlFile("warps.yml", plugin);
        config = file.getConfig();
        this.plugin = plugin;
        load();
    }

    public void load() {
        warps.clear();  // Clear existing warps

        if (config.contains("warps")) {
            for (String id : config.getConfigurationSection("warps").getKeys(false)) {
                try {
                    Map<String, Object> warpData = config.getConfigurationSection("warps." + id).getValues(false);

                    String worldName = (String) warpData.get("world");
                    if (worldName == null) {
                        throw new IllegalArgumentException("Missing world for warp: " + id);
                    }

                    double x = (double) warpData.get("x");
                    double y = (double) warpData.get("y");
                    double z = (double) warpData.get("z");

                    double pitch = (double)  warpData.get("pitch");
                    double yaw = (double) warpData.get("yaw");

                    Location location = new Location(Bukkit.getWorld(worldName), x, y, z, (float) yaw, (float) pitch);

                    if (location.getWorld() == null) {
                        System.out.println("World is null");
                    }

//                    Location location = getLocationFromConfigSection(config.getConfigurationSection())

                    String displayName = (String) warpData.get("displayName");
                    if (displayName == null) {
                        displayName = id;
                    }

                    String iconMaterialName = (String) warpData.get("iconMaterial");
                    Material iconMaterial = Material.getMaterial(iconMaterialName);
                    if (iconMaterial == null) {
                        iconMaterial = Material.ENDER_EYE;
                    }

                    Warp warp = new Warp(location, id, displayName, iconMaterial);
                    warps.put(id, warp);

                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Could not load [" + id + "]. " + e.getMessage() + " " + Arrays.toString(e.getStackTrace()));
                }
            }
        }
    }

    private void save() {
        // Clear the current "warps" section in the configuration
        config.set("warps", null);

        // Iterate through each warp and serialize it into the configuration
        for (Warp warp : warps.values()) {
            String warpId = warp.getId();

            // Create a map to store the warp data
            Map<String, Object> warpData = new LinkedHashMap<>();

            // Serialize the Location
            Location location = warp.getLocation();
            if (location != null) {
                warpData.put("world", location.getWorld().getName());
                warpData.put("x", location.getX());
                warpData.put("y", location.getY());
                warpData.put("z", location.getZ());
                warpData.put("pitch", location.getPitch());
                warpData.put("yaw", location.getYaw());
            }

//            warpData.put("location", serializeLocation(warp.getLocation()));


            // Serialize the display name
            warpData.put("displayName", warp.getDisplayName());

            // Serialize the icon material
            warpData.put("iconMaterial", warp.getIcon().getItem().getType().name());

            // Save the warp data under the warp ID
            config.set("warps." + warpId, warpData);
        }

        // Save the configuration to the file
        file.save();
    }

    Map<String, Object> serializeLocation(Location location) {
        Map<String, Object> objects = new LinkedHashMap<>();
        if (location != null) {
            objects.put("world", location.getWorld().getName());
            objects.put("x", location.getX());
            objects.put("y", location.getY());
            objects.put("z", location.getZ());
            objects.put("pitch", (double) location.getPitch());
            objects.put("yaw", (double) location.getYaw());
        }
        return objects;
    }

    private Location getLocationFromConfigSection(ConfigurationSection section) {
        Map<String, Object> warpData = section.getValues(false);

        String worldName = (String) warpData.get("world");
        if (worldName == null) {
            throw new IllegalArgumentException("Missing world");
        }

        double x = (double) warpData.get("x");
        double y = (double) warpData.get("y");
        double z = (double) warpData.get("z");

        double pitch = (double)  warpData.get("pitch");
        double yaw = (double) warpData.get("yaw");

        return new Location(Bukkit.getWorld(worldName),
                x,
                y,
                z,
                (float) yaw,
                (float) pitch);
    }

    public String[] getIds() {
        return  warps.values()
                .stream()
                .map(Warp::getId)
                .toArray(String[]::new);
    }

    public @Nullable Warp getWarp(String id) {
        return warps.get(id);
    }

    public WarpModifyResult addWarp(Warp warp) {
        if (warps.containsKey(warp.getId())) {
            return WarpModifyResult.WARP_ALREADY_EXISTS;
        }
        warps.put(warp.getId(), warp);
        save();
        return WarpModifyResult.SUCCESS;
    }

    public WarpTeleportResult teleportPlayer(Player player, String id) {
        Warp warp = getWarp(id);

        if (warp == null) {
            return WarpTeleportResult.DOES_NOT_EXIST;
        }

        return warp.teleport(player);
    }

    public WarpModifyResult editDisplayName(String id, String name) {
        Warp warp = getWarp(id);

        if (warp == null) {
            return WarpModifyResult.WARP_NOT_FOUND;
        }

        warp.setDisplayName(name);
        save();
        return WarpModifyResult.SUCCESS;
    }

    public WarpModifyResult editIconMaterial(String id, Material material) {
        Warp warp = getWarp(id);

        if (warp == null) {
            return WarpModifyResult.WARP_NOT_FOUND;
        }

        warp.setIconMaterial(material);
        save();
        return WarpModifyResult.SUCCESS;
    }

    public WarpModifyResult editLocation(String id, Location location) {
        Warp warp = getWarp(id);

        if (warp == null) {
            return WarpModifyResult.WARP_NOT_FOUND;
        }

        warp.setLocation(location);
        save();
        return WarpModifyResult.SUCCESS;
    }
}
