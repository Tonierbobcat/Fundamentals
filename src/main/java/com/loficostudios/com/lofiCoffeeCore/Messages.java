package com.loficostudios.com.lofiCoffeeCore;

import com.loficostudios.com.lofiCoffeeCore.annotation.ConfigField;
import com.loficostudios.com.lofiCoffeeCore.api.file.impl.YamlFile;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Field;

public class Messages {
    @ConfigField
    public static String CANNOT_TPR_SELF = "You cannot send a teleport request to yourself";
    @ConfigField
    public static String INVALID_PLAYER = "&cInvalid Player";
    @ConfigField
    public static String HEALED_TARGET = "You've healed {target}";
    @ConfigField
    public static String HEALED_SELF = "You've been healed";
    @ConfigField
    public static String WARP_CREATED = "Created warp with id {id}";
    @ConfigField
    public static String WARP_EDITED_SUCCESSFULLY = "Successfully edited {id}";
    @ConfigField
    public static String WARP_ALREADY_EXISTS = "Warp with id {id} already exists";
    @ConfigField
    public static String WARP_DOES_NOT_EXIST = "Warp with id {id} does not exist";
    @ConfigField
    public static String WARP_TELEPORT = "You have been warped";
    @ConfigField
    public static String FLY_ENABLED = "Fly is now enabled";
    @ConfigField
    public static String FLY_DISABLED = "Fly is now disabled";
    @ConfigField
    public static String GOD_ENABLED = "God mode is now enabled";
    @ConfigField
    public static String GOD_DISABLED = "God mode is now disabled";
    @ConfigField
    public static String NOW_MUTED = "You are now muted";
    @ConfigField
    public static String NO_LONGER_MUTED = "You are no longer muted";
    @ConfigField
    public static String MUTED = "You are muted and cannot send messages!";
    @ConfigField
    public static String AFK_ENABLED = "You are now afk";
    @ConfigField
    public static String AFK_DISABLED = "You are no longer afk";
    @ConfigField
    public static String AFK_INTERRUPTED = "You are no longer afk (Interrupted)";
    @ConfigField
    public static String DISPLAYNAME_CHANGED = "Your nickname has been changed to: {name}";

    @ConfigField
    public static String GAMEMODE_SET_OTHER = "Set Gamemode for {target} to {gamemode}";
    @ConfigField
    public static String GAMEMODE_SET_SELF = "Gamemode set to {gamemode}";

    @ConfigField
    public static String NO_SPAWN_TELEPORTING_WORLD_SPAWN = "No spawnpoint was found. teleport to world spawn...";


    public static void saveConfig() {
        YamlFile file = new YamlFile("messages.yml", LofiCoffeeCore.getInstance());
        FileConfiguration config = file.getConfig();

        for (Field field : Messages.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigField.class)) {
                try {
                    String fieldName = field.getName(); // The name of the field
                    String configPath = "messages." + fieldName; // Example: messages.CANNOT_TPR_SELF

                    if (config.contains(configPath)) {
                        String configValue = config.getString(configPath);

                        field.setAccessible(true);
                        field.set(null, configValue);
                    } else {
                        config.set(configPath, (String) field.get(null));
                    }
                } catch (IllegalAccessException ignore) {
                }
            }
        }
        file.save();
    }

}
