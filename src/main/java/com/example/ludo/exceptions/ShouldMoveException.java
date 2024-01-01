package com.example.ludo.exceptions;

public class ShouldMoveException extends LudoException{
    public ShouldMoveException() {
        super("You should move and not roll.");
    }
}
