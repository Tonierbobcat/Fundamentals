package com.loficostudios.fundamentals.player;

public class UserAlreadyLoadedException extends RuntimeException {
    public UserAlreadyLoadedException(String message) {
        super(message);
    }
}
