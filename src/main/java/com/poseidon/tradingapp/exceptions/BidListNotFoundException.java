package com.poseidon.tradingapp.exceptions;

/**
 * Exception levée lorsqu'une offre demandée n'existe pas en base.
 */
public class BidListNotFoundException extends RuntimeException {
    public BidListNotFoundException(String message) {
        super(message);
    }
}
