package com.loficostudios.com.lofiCoffeeCore;

import com.loficostudios.com.lofiCoffeeCore.annotation.ConfigField;
import com.loficostudios.com.lofiCoffeeCore.api.file.impl.YamlFile;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.awt.*;
import java.lang.reflect.Field;

public class Messages {
    //COLOR PALETTE FROM LIGHT TO DARK AFD2E9 9D96B8 9A7197 886176 7C5869

    @ConfigField
    public static String INVALID_PLAYER = "&#7C5869Invalid Player";

    @ConfigField
    public static String INVALID_TRANSACTION = "&#7C5869Invalid Transaction";
    @ConfigField
    public static String INVALID_MATERIAL = "&#7C5869Invalid Material";
    @ConfigField
    public static String INVALID_ENCHANTMENT = "&#7C5869Invalid Enchantment";

    @ConfigField
    public static String CANNOT_ENCHANT_AIR = "&#886176Cannot enchant air";
    @ConfigField
    public static String HEALED_TARGET = "&#9A7197You’ve healed &#AFD2E9{target}&#9A7197.";
    @ConfigField
    public static String HEALED_SELF = "&#AFD2E9You’ve been healed.";
    @ConfigField
    public static String WARP_CREATED = "&#9D96B8Created warp with ID &#AFD2E9{id}&#9D96B8.";
    @ConfigField
    public static String WARP_EDITED_SUCCESSFULLY = "&#9D96B8Successfully edited warp with ID &#AFD2E9{id}&#9D96B8.";
    @ConfigField
    public static String WARP_ALREADY_EXISTS = "&#7C5869Warp with ID &#AFD2E9{id} &#7C5869already exists.";
    @ConfigField
    public static String WARP_DOES_NOT_EXIST = "&#7C5869Warp with ID &#AFD2E9{id} &#7C5869does not exist.";
    @ConfigField
    public static String WARP_TELEPORT = "&#9A7197You have been warped.";
    @ConfigField
    public static String FLY_ENABLED = "&#AFD2E9Flying is now &#9A7197enabled&#AFD2E9.";
    @ConfigField
    public static String FLY_DISABLED = "&#7C5869Flying is now &#9A7197disabled&#7C5869.";
    @ConfigField
    public static String GOD_ENABLED = "&#AFD2E9God mode is now &#9A7197enabled&#AFD2E9.";
    @ConfigField
    public static String GOD_DISABLED = "&#7C5869God mode is now &#9A7197disabled&#7C5869.";
    @ConfigField
    public static String NOW_MUTED = "&#886176You are now muted.";
    @ConfigField
    public static String NO_LONGER_MUTED = "&#AFD2E9You are no longer muted.";
    @ConfigField
    public static String MUTED = "&#886176You are muted and cannot send messages!";
    @ConfigField
    public static String AFK_ENABLED = "&#9D96B8You are now AFK.";
    @ConfigField
    public static String AFK_DISABLED = "&#AFD2E9You are no longer AFK.";
    @ConfigField
    public static String AFK_INTERRUPTED = "&#7C5869You are no longer AFK &#9A7197(interrupted)&#7C5869.";
    @ConfigField
    public static String DISPLAYNAME_CHANGED = "&#9D96B8Your nickname has been changed to: &#AFD2E9{name}&#9D96B8.";

    @ConfigField
    public static String GAMEMODE_SET_OTHER = "&#9A7197Set gamemode for &#AFD2E9{player} &#9A7197to &#AFD2E9{gamemode}&#9A7197.";
    @ConfigField
    public static String GAMEMODE_SET_SELF = "&#9A7197Your gamemode has been set to &#AFD2E9{gamemode}&#9A7197.";

    @ConfigField
    public static String NO_SPAWN_TELEPORTING_WORLD_SPAWN = "&#7C5869No spawnpoint found. Teleporting to world spawn...";

    //region Teleport Request/Accept Messages
    @ConfigField
    public static String CANNOT_TELEPORT_REQUEST_SELF = "&#886176You cannot send a teleport request to yourself.";
    @ConfigField
    public static String TELEPORT_REQUEST = "&#AFD2E9{player} &#9A7197has requested to teleport to you.";
    @ConfigField
    public static String NO_ONGOING_REQUESTS_SPECIFIC = "&#7C5869You have no ongoing requests from this user.";
    @ConfigField
    public static String NO_ONGOING_REQUESTS = "&#7C5869You have no ongoing requests.";
    @ConfigField
    public static String ACCEPT_TELEPORT_REQUEST = "&#9A7197Accepted &#AFD2E9{player}&#9A7197's teleport request.";
    @ConfigField
    public static String TELEPORT_REQUEST_CREATED = "&#AFD2E9Teleport request sent to &#9A7197{player}&#AFD2E9.";
    @ConfigField
    public static String TELEPORT_REQUEST_DENIED = "&#7C5869Your teleport request to &#9A7197{player} &#7C5869was denied.";
    @ConfigField
    public static String DENY_TELEPORT_REQUEST = "&#7C5869You denied &#9A7197{player}&#7C5869's teleport request.";



    //endregion

    @ConfigField
    public static String GAVE_ITEM = "&#AFD2E9Gave &#9A7197{amount}x &#9D96B8{item} &#AFD2E9to &#9A7197{name}&#AFD2E9.";

    //region MAGNET
    @ConfigField
    public static String MAGNET_ENABLED = "&#AFD2E9Magnet mode is now &#9A7197enabled&#AFD2E9.";
    @ConfigField
    public static String MAGNET_DISABLED = "&#7C5869Magnet mode is now &#9A7197disabled&#7C5869.";
    //endregion

    @ConfigField
    public static String BALANCE_SELF = "&#AFD2E9Your balance: &#9A7197{amount}&#AFD2E9.";
    @Getter
    public static String BALANCE_OTHERS = "&#AFD2E9{player}&#AFD2E9's balance: &#9A7197{amount}&#AFD2E9.";

    @ConfigField
    public static String MONEY_ADD_SELF = "&#AFD2E9Added &#9A7197{amount} &#AFD2E9to your balance.";
    @ConfigField
    public static String MONEY_ADD_OTHER = "&#AFD2E9Added &#9A7197{amount} &#AFD2E9to &#9A7197{player}&#AFD2E9's balance.";
    @ConfigField
    public static String MONEY_SET_SELF = "&#AFD2E9Your balance has been set to &#9A7197{amount}&#AFD2E9.";
    @ConfigField
    public static String MONEY_SET_OTHER = "&#AFD2E9Set &#9A7197{player}&#AFD2E9's balance to &#9A7197{amount}&#AFD2E9.";
    @ConfigField
    public static String MONEY_REMOVE_SELF = "&#7C5869Removed &#9A7197{amount} &#7C5869from your balance.";
    @ConfigField
    public static String MONEY_REMOVE_OTHER = "&#7C5869Removed &#9A7197{amount} &#7C5869from &#9A7197{player}&#7C5869's balance.";


    public static void saveConfig() {
        YamlFile file = new YamlFile("messages.yml", LofiCoffeeCore.getInstance());
        FileConfiguration config = file.getConfig();
        for (Field field : Messages.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigField.class)) {
                try {
                    String fieldName = field.getName();
                    String configPath = "messages." + fieldName;

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
