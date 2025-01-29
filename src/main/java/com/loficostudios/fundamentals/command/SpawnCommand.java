package com.loficostudios.fundamentals.command;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.Messages;
import com.loficostudios.fundamentals.command.base.Command;
import com.loficostudios.fundamentals.utils.Common;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.Location;

public class SpawnCommand extends FundamentalsCommand {
    public SpawnCommand(FundamentalsPlugin plugin) {
        super(plugin);
    }

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
                        Common.sendMessage(sender, Messages.NO_SPAWN_TELEPORTING_WORLD_SPAWN);
                    }
                    sender.teleport(spawnPoint != null
                            ? spawnPoint
                            : worldSpawn);
                }).register();
    }
}
