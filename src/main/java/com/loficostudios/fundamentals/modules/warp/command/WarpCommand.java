package com.loficostudios.fundamentals.modules.warp.command;

import com.loficostudios.fundamentals.command.base.Command;
import com.loficostudios.fundamentals.modules.warp.WarpGUI;
import com.loficostudios.fundamentals.modules.warp.WarpManager;
import com.loficostudios.fundamentals.modules.warp.WarpTeleportResult;
import com.loficostudios.fundamentals.utils.Common;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;

public class WarpCommand extends Command {

    private final WarpManager warpManager;

    public WarpCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    protected String getIdentifier() {
        return "warp";
    }

    @Override
    public void register() {
        new CommandAPICommand("warp")
                .withPermission(getPermission())
                .withOptionalArguments(new StringArgument("id").replaceSuggestions(ArgumentSuggestions.strings(info -> warpManager.getIds())))
                .executesPlayer((sender, args)-> {
                    var id = (String) args.get("id");

                    if (id == null) {
                        new WarpGUI().open(sender);
                        return;
                    }
                    var warp = warpManager.getWarp(id);
                    if (warp == null) {
                        Common.sendMessage(sender, WarpTeleportResult.DOES_NOT_EXIST.getMessage()
                                .replace("{id}", id));
                        return;
                    }
                    var result = warpManager.teleportPlayer(sender, warp.getId());
                    Common.sendMessage(sender, result.getMessage()
                            .replace("{id}", id)
                            .replace("{name}", warp.getDisplayName()));
                }).register();
    }
}
