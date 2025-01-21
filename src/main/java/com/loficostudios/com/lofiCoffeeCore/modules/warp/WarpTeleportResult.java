package com.loficostudios.com.lofiCoffeeCore.modules.warp;

import com.loficostudios.com.lofiCoffeeCore.Messages;
import lombok.Getter;

public enum WarpTeleportResult {
    SUCCESS(Messages.WARP_TELEPORT),
    CANCELLED("Warp cancelled"), //TODO: CHANGE THIS
    LOCATION_INVALID("Location is invalid"),
    DOES_NOT_EXIST(Messages.WARP_DOES_NOT_EXIST);

    @Getter
    private final String message;

    WarpTeleportResult(String message) {
        this.message = message;
    }
}
