package com.loficostudios.fundamentals.expansion;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.jetbrains.annotations.NotNull;

public class LofiCoffeeCoreExpansion extends PlaceholderExpansion {
    @NotNull
    @Override
    public String getIdentifier() {
        return FundamentalsPlugin.namespace;
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
