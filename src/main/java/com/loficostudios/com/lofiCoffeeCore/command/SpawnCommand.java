package com.loficostudios.com.lofiCoffeeCore.command;

import com.loficostudios.com.lofiCoffeeCore.Messages;
import com.loficostudios.com.lofiCoffeeCore.command.base.Command;
import dev.jorel.commandapi.CommandAPICommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;

public class SpawnCommand extends Command {
    @Override
    protected String getIdentifier() {
        return "spawn";
    }
    @Override
    public void register() {
        new CommandAPICommand("spawn")
                .withPermission(getPermission())
                .executesPlayer((sender, args) -> {
                    Location worldSpawn = sender.getWorld().getSpawnLocation();
                    Location spawnPoint = sender.getBedSpawnLocation();

                    if (spawnPoint == null) {
                        sender.sendMessage(Component.text(Messages.NO_SPAWN_TELEPORTING_WORLD_SPAWN));
                    }
                    sender.teleport(spawnPoint != null
                            ? spawnPoint
                            : worldSpawn);
                }).register();
    }
}
