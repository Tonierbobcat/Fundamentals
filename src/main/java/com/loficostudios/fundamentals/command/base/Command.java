package com.loficostudios.fundamentals.command.base;

import com.loficostudios.fundamentals.FundamentalsPlugin;

public abstract class Command
{
    public abstract void register();
    protected abstract String getIdentifier();
    protected String getPermission() {
        return FundamentalsPlugin.namespace + "." + getIdentifier();
    }
}
