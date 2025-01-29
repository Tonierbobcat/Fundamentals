package com.loficostudios.fundamentals.command;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.fundamentals.command.base.Command;

public abstract class FundamentalsCommand extends Command {
    protected final FundamentalsPlugin plugin;
    public FundamentalsCommand(FundamentalsPlugin plugin) {
        this.plugin = plugin;
    }
}
