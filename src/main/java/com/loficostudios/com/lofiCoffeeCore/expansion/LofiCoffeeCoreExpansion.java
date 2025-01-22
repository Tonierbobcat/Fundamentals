package com.loficostudios.com.lofiCoffeeCore.expansion;

import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LofiCoffeeCoreExpansion extends PlaceholderExpansion {
    @NotNull
    @Override
    public String getIdentifier() {
        return LofiCoffeeCore.namespace;
    }

    @NotNull
    @Override
    public String getAuthor() {
        return "Tonierbocat966";
    }

    @NotNull
    @Override
    public String getVersion() {
        return "0.1";
    }
}
