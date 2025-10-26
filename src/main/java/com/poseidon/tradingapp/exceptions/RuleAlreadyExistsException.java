package com.poseidon.tradingapp.exceptions;

/**
 * Exception levée lorsqu'une règle avec le même nom existe déjà en base.
 */
public class RuleAlreadyExistsException extends RuntimeException{
    public RuleAlreadyExistsException(String message){
        super(message);
    }
}
