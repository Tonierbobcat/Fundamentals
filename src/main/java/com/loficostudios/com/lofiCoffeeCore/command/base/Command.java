package com.loficostudios.com.lofiCoffeeCore.command.base;

import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;

import java.util.List;

public abstract class Command
{
    public abstract void register();
    protected abstract String getIdentifier();
    protected String getPermission() {
        return LofiCoffeeCore.namespace + "." + getIdentifier();
    }
}
