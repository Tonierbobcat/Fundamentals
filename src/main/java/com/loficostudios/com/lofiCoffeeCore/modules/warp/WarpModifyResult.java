package com.loficostudios.com.lofiCoffeeCore.modules.warp;

import com.loficostudios.com.lofiCoffeeCore.Messages;
import lombok.Getter;

public enum WarpModifyResult {
    SUCCESS(Messages.WARP_EDITED_SUCCESSFULLY),
    WARP_NOT_FOUND(Messages.WARP_DOES_NOT_EXIST),
    WARP_ALREADY_EXISTS(Messages.WARP_ALREADY_EXISTS);

    @Getter
    private final String message;

    WarpModifyResult(String message) {
        this.message = message;
    }
}
