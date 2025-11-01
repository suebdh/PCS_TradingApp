package com.poseidon.tradingapp.exceptions;

/**
 * Exception levée lorsqu'un Trade est introuvable en base de données.
 */
public class TradeNotFoundException extends RuntimeException {
    public TradeNotFoundException(String message){super(message);
    }
}
