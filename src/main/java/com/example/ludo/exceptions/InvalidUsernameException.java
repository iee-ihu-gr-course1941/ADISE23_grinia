package com.example.ludo.exceptions;

public class InvalidUsernameException extends LudoException{
    public InvalidUsernameException() {
        super("Invalid username");
    }
}
