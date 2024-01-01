package com.example.ludo.exceptions;

public class NotAllUsersSetException extends LudoException{
    public NotAllUsersSetException() {
        super("Not all users are set yet.");
    }
}
