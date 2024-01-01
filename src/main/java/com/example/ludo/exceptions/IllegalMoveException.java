package com.example.ludo.exceptions;

public class IllegalMoveException extends LudoException{
    public IllegalMoveException() {
        super("Illegal move");
    }
}
