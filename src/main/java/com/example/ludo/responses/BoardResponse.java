package com.example.ludo.responses;

import com.example.ludo.BoardV2;
import com.example.ludo.model.Player;

import java.util.Arrays;

public class BoardResponse extends GameResponse{

    private int dieValue;

    private String p1Home;
    private String p2Home;
    private String board[][];

    public BoardResponse() {
    }

    public String[][] getBoard() {
        return board;
    }

    public void setBoard(BoardV2 boardV2) {
        board = new String[BoardV2.BOARD_ROWS][BoardV2.BOARD_COLUMNS];
        for(int i=0; i<BoardV2.BOARD_ROWS; i++) {
            Arrays.fill(board[i], "0");
        }

        Player p1 = boardV2.getP1();

        int y = 0;
        for(int x : p1.getPawnXs()) {
            if(x != -1) {
                board[x][y] = p1.getBoard()[x][y].getPawnName().getStringVal();
            }
            y++;
        }

        Player p2 = boardV2.getP2();

        y = 0;
        for(int x : p2.getPawnXs()) {
            if(x != -1) {
                board[x][y] = p2.getBoard()[x][y].getPawnName().getStringVal();
            }
            y++;
        }
    }

    public void setBoard(String[][] board) {
        this.board = board;
    }

    public int getDieValue() {
        return dieValue;
    }

    public void setDieValue(int dieValue) {
        this.dieValue = dieValue;
    }

    public String getP1Home() {
        return p1Home;
    }

    public void setP1Home(String p1Home) {
        this.p1Home = p1Home;
    }

    public String getP2Home() {
        return p2Home;
    }

    public void setP2Home(String p2Home) {
        this.p2Home = p2Home;
    }
}
