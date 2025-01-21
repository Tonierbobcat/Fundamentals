package com.loficostudios.com.lofiCoffeeCore.player;

public class PlayerDoesNotExistException extends RuntimeException {
    public PlayerDoesNotExistException(String message) {
        super(message);
    }
}
