package com.example.ludo.exceptions;

public class PawnInHomeException extends LudoException{
    public PawnInHomeException() {
        super("Pawn is already in home");
    }
}
