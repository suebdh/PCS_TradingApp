package com.poseidon.tradingapp.exceptions;

/**
 * Exception levée lorsqu'un point de courbe (CurvePoint) est introuvable en base.
 */
public class CurvePointNotFoundException extends RuntimeException {

    public CurvePointNotFoundException(String message) {
        super(message);
    }
}
