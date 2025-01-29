package com.loficostudios.fundamentals.player;

public class PlayerDoesNotExistException extends RuntimeException {
    public PlayerDoesNotExistException(String message) {
        super(message);
    }
}
