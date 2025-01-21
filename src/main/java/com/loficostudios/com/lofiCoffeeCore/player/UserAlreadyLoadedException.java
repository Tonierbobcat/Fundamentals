package com.loficostudios.com.lofiCoffeeCore.player;

public class UserAlreadyLoadedException extends RuntimeException {
    public UserAlreadyLoadedException(String message) {
        super(message);
    }
}
