package com.poseidon.tradingapp.exceptions;

/**
 * Exception levée lorsqu'un utilisateur est introuvable en base de données.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException (String message) {
        super(message);
    }
}
