package com.example.ludo.exceptions;

public class NoAvailableMovesException extends LudoException{

    public NoAvailableMovesException() {
        super("There are no available moves.");
    }
}
