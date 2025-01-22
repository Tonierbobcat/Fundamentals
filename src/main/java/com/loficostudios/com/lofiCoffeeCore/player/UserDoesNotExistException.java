package com.loficostudios.com.lofiCoffeeCore.player;

public class UserDoesNotExistException extends RuntimeException {
    public UserDoesNotExistException(String message) {
        super(message);
    }
}
