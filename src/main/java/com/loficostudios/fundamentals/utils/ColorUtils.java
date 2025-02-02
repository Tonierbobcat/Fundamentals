/**
 * @Author Tonierbobcat
 * @Github https://github.com/Tonierbobcat
 * @version MelodyApi
 */

package com.loficostudios.fundamentals.utils;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

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
