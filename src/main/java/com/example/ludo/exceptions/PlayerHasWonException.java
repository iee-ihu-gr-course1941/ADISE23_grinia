package com.example.ludo.exceptions;

import com.example.ludo.model.Player;

public class PlayerHasWonException extends LudoException{
    public PlayerHasWonException(Player player) {
        super(player.getUsername() + " HAS WON!");
    }
}
