package com.example.ludo;

import com.example.ludo.exceptions.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Player {

    private static final int BOARD_ROWS = 46;
    private static final int BOARD_COLUMNS = 4;
    private static final int HOME_POSITION = BOARD_ROWS - 1;

    // final square before entering the finishing line
    private static final int BOARD_FINAL_SQUARE = 40;
    private static final int SIX_DIE = 6;
    private static final int RED_LAST_COLUMN = 4;
    private static final int BLUE_LAST_COLUMN = 8;
    private String username;
    private Pawn[][] board;
    private Pawn[] base;
    private boolean shouldRoll;
    private boolean turn;
    private Color color;
    private int dieValue;
    private Pawn pawnToMove;

    // keeps the x coordinates of the player's pawn at any time
    private int[] pawnXs;

    public Player(String username, Color color) {
        this.username = username;
        this.color = color;
        board = new Pawn[BOARD_ROWS][BOARD_COLUMNS];

        for (int i = 0; i < BOARD_ROWS; i++) {
            Arrays.fill(board[i], new Pawn(Pawns.NONE));
        }

        base = new Pawn[BOARD_COLUMNS];

        // place player's pawns on their base
        for(int i=0; i<BOARD_COLUMNS; i++) {
            base[i] = new Pawn(Pawns.getColorI(color, i));
        }

        pawnXs = new int[BOARD_COLUMNS];
        Arrays.fill(pawnXs, -1);

    }

    public boolean move(String pawnVal, Player otherPlayer) throws LudoException {

        // the player has no available moves
        if(!hasMoves()) {
            turn = false;
            otherPlayer.setShouldRoll(true);
            throw new NoAvailableMovesException();
        }

        // finds the pawn to be moved in the board and sets it to the field pawnToMove
        findInBoard(pawnVal);

        // if pawn has reached home
        if(pawnToMove.getX() == HOME_POSITION) {
            // it can't go further
            throw new PawnInHomeException();
        }

        // if pawn hasn't left the base
        if (pawnToMove.getX() == -1) {
            // check if steps is a 6 and place the pawn to the start
            if(dieValue == SIX_DIE) {
                pawnToMove.setX(0);
                board[pawnToMove.getX()][pawnToMove.getY()] = pawnToMove;
                pawnXs[pawnToMove.getY()] = pawnToMove.getX();
                turn = false;
                shouldRoll = true;
                // TODO
                //pawnPositionService.savePawn(p);
                return true;
            }
            else throw new IllegalMoveException();
        }

        // if pawn is entering finishing line
        if (pawnToMove.getX() + dieValue > BOARD_FINAL_SQUARE) {

            // if pawn is already in finishing line
            if(pawnToMove.getX() > BOARD_FINAL_SQUARE){

                // if pawn can move in the finishing line
                if(pawnToMove.getX()+dieValue <= HOME_POSITION) {
                    movePawn(pawnToMove, dieValue,otherPlayer);
                }
                else {
                    // can't move
                    // TODO maybe this is unnecessary
                    throw new NoAvailableMovesException();
                }
            }
            else {
                // finishing line is safe for pawns, can't be knocked
                movePawn(pawnToMove, dieValue,otherPlayer);
            }
        }
        else {
            movePawn(pawnToMove, dieValue,otherPlayer);
            // knock other color pawns from the same row
            knock(otherPlayer);
        }

        return true;
    }

    private void knock(Player otherPlayer) {
        int x = pawnToMove.getX();

        // see if there are other pawns in the same row
        for (int i = 0; i < BOARD_COLUMNS; i++) {

            // update the other player's pawnXs array
            if(otherPlayer.pawnXs[i] == x) {
                otherPlayer.pawnXs[i] = -1;
            }

            // knock it from the other player's board
            otherPlayer.board[x][i].setX(-1);
            otherPlayer.board[x][i] = new Pawn(Pawns.NONE);

            // TODO persist
        }
    }

    private void movePawn(Pawn p, int steps, Player otherPlayer) {
        // remove pawn from its previous position by placing a NONE pawn
        board[p.getX()][p.getY()] = new Pawn(Pawns.NONE);
        // move pawn by steps
        p.setX(p.getX()+steps);
        // place pawn on the mainBoard in its new x position
        board[p.getX()][p.getY()] = p;
        // update pawn's x value to the pawnXs array
        pawnXs[p.getY()] = p.getX();

        turn = false;
        if(dieValue == SIX_DIE) {
            shouldRoll = true;
        }
        else {
            otherPlayer.setShouldRoll(true);
        }

        // TODO
        //pawnPositionService.savePawn(p);
    }

    public boolean hasMoves() {
        // if all pawns are in base and dieValue is not 6 there is no available move
        boolean allInBase = true;
        for(int i=0; i<BOARD_COLUMNS; i++) {
            if(pawnXs[i] != -1) {
                allInBase = false;
                break;
            }
        }

        if(allInBase && dieValue != SIX_DIE) {
            return false;
        }

        // checks if the player has any available moves
        for(int i=0; i<BOARD_COLUMNS; i++) {
            if(pawnXs[i] != -1) {
                Pawn p = board[pawnXs[i]][i];
                if(!pawnCanMove(p)) {
                    turn = false;
                    return false;
                }
            }
        }

        return true;
    }

    public boolean pawnCanMove(Pawn pawn) {
        if(pawn.getX() == -1 && dieValue != SIX_DIE) return false;
        if(pawn.getX() + dieValue > HOME_POSITION) return false;
        return true;
    }

    private boolean findInBoard(String pawnVal) {

        if(!Pawns.stringValExists(pawnVal)) {
            System.out.println("Pawn value doesn't exist.");
            return false;
        }

        // search pawn in base
        for(int i=0; i<BOARD_COLUMNS; i++) {
            if(base[i].getPawnName().getStringVal().equals(pawnVal)) {
                pawnToMove = base[i];
                return true;
            }
        }

        int y = Character.getNumericValue(pawnVal.charAt(1)) - 1;
        // if pawn not found in base, search in the board
        for(int i=0; i<BOARD_ROWS; i++) {
            if(board[i][y].getPawnName().getStringVal().equals(pawnVal)) {
                pawnToMove = board[i][y];
                return true;
            }
        }

        return false;
    }

    public int rollDie() throws LudoException{
        if(!shouldRoll) {
            throw new NotYourTurnException();
        }

        // Create an instance of Random
        Random random = new Random();

        // Generate a random number between 1 and 6
        dieValue = random.nextInt(6) + 1;

        shouldRoll = false;
        if(hasMoves()) {
            turn = true;
        }
        else {
            throw new NoAvailableMovesException();
        }
        return dieValue;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isShouldRoll() {
        return shouldRoll;
    }

    public void setShouldRoll(boolean shouldRoll) {
        this.shouldRoll = shouldRoll;
    }

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(username, player.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}
