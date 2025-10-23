package com.poseidon.tradingapp.exceptions;

/**
 * Exception levée lorsqu'une règle demandée n'existe pas en base.
 */
public class RuleNotFoundException extends RuntimeException{
    public RuleNotFoundException(String message){
        super(message);
    }
}
