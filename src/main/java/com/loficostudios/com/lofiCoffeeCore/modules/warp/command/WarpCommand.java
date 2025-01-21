package com.loficostudios.com.lofiCoffeeCore.modules.warp.command;

import com.loficostudios.com.lofiCoffeeCore.command.base.Command;
import com.loficostudios.com.lofiCoffeeCore.modules.warp.WarpGUI;
import com.loficostudios.com.lofiCoffeeCore.modules.warp.WarpManager;
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

                    var result = warpManager.teleportPlayer(sender, id);
                    sender.sendMessage(result.getMessage().replace("{id}", id));
                }).register();
    }
}
