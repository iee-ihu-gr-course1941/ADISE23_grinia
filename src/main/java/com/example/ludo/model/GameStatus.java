package com.example.ludo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "GAME_STATUS")
public class GameStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "game_status_seq")
    @SequenceGenerator(name = "game_status_seq", sequenceName = "game_status_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "PLAYER1")
    private String player1;

    @Column(name = "PLAYER2")
    private String player2;

    @Column(name = "PLAYERS_TURN")
    private String playersTurn;

    @Column(name = "SHOULD_ROLL")
    private boolean shouldRoll;

    @Column(name = "SHOULD_MOVE")
    private boolean shouldMove;

    @Column(name = "GAME_OVER")
    private boolean gameOver;

    @Column(name = "WINNER")
    private String winner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getPlayersTurn() {
        return playersTurn;
    }

    public void setPlayersTurn(String playersTurn) {
        this.playersTurn = playersTurn;
    }

    public boolean isShouldRoll() {
        return shouldRoll;
    }

    public void setShouldRoll(boolean shouldRoll) {
        this.shouldRoll = shouldRoll;
    }

    public boolean isShouldMove() {
        return shouldMove;
    }

    public void setShouldMove(boolean shouldMove) {
        this.shouldMove = shouldMove;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "GameStatus{" +
                "playersTurn='" + playersTurn + '\'' +
                ", shouldRoll=" + shouldRoll +
                ", shouldMove=" + shouldMove +
                ", gameOver=" + gameOver +
                ", winner='" + winner + '\'' +
                '}';
    }
}
