package com.loficostudios.com.lofiCoffeeCore.experimental;

import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;
import com.loficostudios.com.lofiCoffeeCore.command.base.Command;
import com.loficostudios.com.lofiCoffeeCore.utils.Common;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.LiteralArgument;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class ReloadCommand extends Command {
    @Override
    public void register() {
        new CommandAPICommand(getIdentifier())
                .withPermission(CommandPermission.OP)
                .withArguments(new LiteralArgument("reload"))
                .executesPlayer((sender, args) -> {
                    Common.sendMessage(sender, "&aReloaded " + LofiCoffeeCore.getInstance().reload() + "ms");
                }).register();
    }

    @Override
    protected String getIdentifier() {
        return LofiCoffeeCore.namespace;
    }
}
