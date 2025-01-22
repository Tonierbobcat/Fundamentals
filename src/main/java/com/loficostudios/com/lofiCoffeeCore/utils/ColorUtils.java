/**
 * @Author Tonierbobcat
 * @Github https://github.com/Tonierbobcat
 * @version MelodyApi
 */

package com.loficostudios.com.lofiCoffeeCore.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ColorUtils {
//	public static Component deserialize(Component input) {
//		String rawMessage = LegacyComponentSerializer.legacyAmpersand().serialize(input);
//		return LegacyComponentSerializer.legacyAmpersand().deserialize(rawMessage);
//	}
	public static Component deserialize(String input) {
		String rawMessage = LegacyComponentSerializer.legacyAmpersand().serialize(Component.text(input));
		return LegacyComponentSerializer.legacyAmpersand().deserialize(rawMessage);
	}
//	public static Component
}
