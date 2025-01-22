package com.loficostudios.com.lofiCoffeeCore.api.events;

import com.loficostudios.com.lofiCoffeeCore.exceptions.BlockIsNotOreException;
import com.loficostudios.com.lofiCoffeeCore.Ore;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class OreBreakEvent extends BlockEvent implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    @Getter
    private final Player player;

    @Getter
    private final Ore type;

    @Getter
    @Setter
    private boolean cancelled;

    @Getter
    private final Collection<ItemStack> drops;

    public OreBreakEvent(Block block, Player player, Collection<ItemStack> drops) {
        super(block);
        this.block = block;
        this.player = player;
        this.drops = drops;

        switch (block.getType()) {
            case Material.NETHER_QUARTZ_ORE:
                type = Ore.QUARTZ;
                break;
            case Material.COPPER_ORE:
            case Material.DEEPSLATE_COPPER_ORE:
                type = Ore.COPPER;
                break;
            case Material.COAL_ORE:
            case Material.DEEPSLATE_COAL_ORE:
                type = Ore.COAL;
                break;
            case Material.IRON_ORE:
            case Material.DEEPSLATE_IRON_ORE:
                type = Ore.IRON;
                break;
            case Material.GOLD_ORE:
            case Material.DEEPSLATE_GOLD_ORE:
            case Material.NETHER_GOLD_ORE:
                type = Ore.GOLD;
                break;
            case Material.EMERALD_ORE:
            case Material.DEEPSLATE_EMERALD_ORE:
                type = Ore.EMERALD;
                break;
            case Material.DIAMOND_ORE:
            case Material.DEEPSLATE_DIAMOND_ORE:
                type = Ore.DIAMOND;
                break;
            default:
                throw new BlockIsNotOreException("Block is not an ore");
        }
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
