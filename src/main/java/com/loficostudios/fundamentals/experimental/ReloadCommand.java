package com.loficostudios.fundamentals.experimental;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.command.FundamentalsCommand;
import com.loficostudios.fundamentals.command.base.Command;
import com.loficostudios.fundamentals.utils.Common;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.CommandPermission;
import dev.jorel.commandapi.arguments.LiteralArgument;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class ReloadCommand extends FundamentalsCommand {

    public ReloadCommand(FundamentalsPlugin plugin) {
        super(plugin);
    }

    @Override
    public void register() {
        new CommandAPICommand(getIdentifier())
                .withPermission(CommandPermission.OP)
                .withArguments(new LiteralArgument("reload"))
                .executesPlayer((sender, args) -> {
                    Common.sendMessage(sender, "&aReloaded " + plugin.reload() + "ms");
                }).register();
    }

    @Override
    protected String getIdentifier() {
        return FundamentalsPlugin.namespace;
    }
}
