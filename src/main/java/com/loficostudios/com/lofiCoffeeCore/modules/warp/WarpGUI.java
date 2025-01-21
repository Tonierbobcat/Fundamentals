package com.loficostudios.com.lofiCoffeeCore.modules.warp;

import com.loficostudios.com.lofiCoffeeCore.LofiCoffeeCore;
import com.loficostudios.com.lofiCoffeeCore.api.gui.MelodyGui;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class WarpGUI extends MelodyGui {

    public WarpGUI() {

        WarpManager warpManager = LofiCoffeeCore.getInstance().getWarpManager();

        int index = 0;
        for (Warp warp : Arrays.stream(warpManager.getIds()).map(warpManager::getWarp).toArray(Warp[]::new)) {
            setSlot(index, warp.getIcon());
            index++;
        }
    }


    @Override
    protected @NotNull Integer getSize() {
        return 40;
    }

    @Override
    protected @Nullable String getTitle() {
        return "Warps";
    }
}
