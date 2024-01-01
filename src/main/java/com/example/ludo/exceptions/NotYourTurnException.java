package com.example.ludo.exceptions;

public class NotYourTurnException extends LudoException{
    public NotYourTurnException() {
        super("It is not your turn");
    }
}
