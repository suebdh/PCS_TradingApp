package com.poseidon.tradingapp.exceptions;

/**
 * Exception levée lorsqu'un point de xx (Rating) est introuvable en base.
 */
public class RatingNotFoundException extends RuntimeException {
    public RatingNotFoundException(String message) { super(message);}
}