package com.loficostudios.fundamentals.modules.warp;

import com.loficostudios.fundamentals.FundamentalsPlugin;
import com.loficostudios.melodyapi.gui.MelodyGui;

import java.util.Arrays;

public class WarpGUI extends MelodyGui {

    public WarpGUI() {
        super(40, "Warps");

        WarpManager warpManager = FundamentalsPlugin.getInstance().getWarpManager();

        int index = 0;
        for (Warp warp : Arrays.stream(warpManager.getIds()).map(warpManager::getWarp).toArray(Warp[]::new)) {
            setSlot(index, warp.getIcon());
            index++;
        }
    }
}
