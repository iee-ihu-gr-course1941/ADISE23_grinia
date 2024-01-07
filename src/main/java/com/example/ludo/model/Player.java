package com.example.ludo.model;

import com.example.ludo.BoardV2;
import com.example.ludo.db.services.PawnPositionService;
import com.example.ludo.exceptions.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class Player {

    protected static final int BOARD_ROWS = 46;
//    protected static final int BOARD_ROWS = 27;
    protected static final int BOARD_COLUMNS = 4;
    protected static final int SIX_DIE = 6;

    protected String username;
    protected Pawn[][] board;
    protected Pawn[] base;
    protected boolean shouldRoll;
    protected boolean turn;
    protected Color color;
    protected int dieValue;
    protected Pawn pawnToMove;

    // keeps the x coordinates of the player's pawn at any time
    protected int[] pawnXs;

    private String pawnsInHome;

    private final int start;
    private final int home;

    // final square before entering the finishing line
    private final int boardFinalSquare;

    private final BoardV2 masterBoard;

    public Player(String username, Color color, int start, int home, int finishLine, BoardV2 masterBoard) {
        this.username = username;
        this.color = color;
        this.start = start;
        this.home = home;
        this.boardFinalSquare = finishLine - 1;
        this.masterBoard = masterBoard;
        this.pawnsInHome = "";

        board = new Pawn[BOARD_ROWS][BOARD_COLUMNS];

        for (int i = 0; i < BOARD_ROWS; i++) {
            Arrays.fill(board[i], new Pawn(Pawns.NONE, username));
        }

        base = new Pawn[BOARD_COLUMNS];

        // place player's pawns on their base
        for(int i=0; i<BOARD_COLUMNS; i++) {
            base[i] = new Pawn(Pawns.getColorI(color, i), username);

            // persist in the database each pawn's position
            masterBoard.saveBoard(base[i]);
        }

        pawnXs = new int[BOARD_COLUMNS];
        Arrays.fill(pawnXs, -1);

    }

    public boolean move(String pawnVal, Player otherPlayer) throws LudoException {

        // the player has no available moves
        if(!hasMoves()) {
            turn = false;
            otherPlayer.setShouldRoll(true);

            // update database
            masterBoard.getGameStatus().setPlayersTurn(otherPlayer.getUsername());
            masterBoard.getGameStatus().setShouldRoll(true);
            masterBoard.getGameStatus().setShouldMove(false);
            masterBoard.updateGameStatus();


            throw new NoAvailableMovesException();
        }

        // finds the pawn to be moved in the board and sets it to the field pawnToMove
        findInBoard(pawnVal);

        // if pawn has reached home
        if(pawnToMove.getX() == home) {
            // it can't go further
            throw new PawnInHomeException();
        }

        // if pawn hasn't left the base
        if (pawnToMove.getX() == -1) {
            // check if steps is a 6 and place the pawn to the start
            if(dieValue == SIX_DIE) {
                pawnToMove.setX(start);
                board[pawnToMove.getX()][pawnToMove.getY()] = pawnToMove;
                pawnXs[pawnToMove.getY()] = pawnToMove.getX();
                turn = false;
                shouldRoll = true;

                // update pawn position in the database
                masterBoard.saveBoard(pawnToMove);

                // update game status in the database
                masterBoard.getGameStatus().setShouldRoll(true);
                masterBoard.getGameStatus().setShouldMove(false);
                masterBoard.updateGameStatus();

                return true;
            }
            else throw new IllegalMoveException();
        }

        // if pawn is entering finishing line
        if (enteringFinishLine(pawnToMove)) {

            // if pawn is already in finishing line
            if(isInFinishLine(pawnToMove)){

                // if pawn can move in the finishing line
                if(pawnToMove.getX()+dieValue <= home) {
                    movePawn(pawnToMove, dieValue,otherPlayer);
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

    private boolean enteringFinishLine(Pawn pawn) {
        if (color.equals(Color.RED) && pawn.getX() + dieValue > boardFinalSquare) return true;
        if(color.equals(Color.BLUE) && pawn.getX()<boardFinalSquare && pawn.getX()+dieValue>boardFinalSquare) return true;
        return false;
    }

    private boolean isInFinishLine(Pawn pawn) {
        if(color.equals(Color.RED) && pawn.getX() > boardFinalSquare) return true;
        if(color.equals(Color.BLUE) && pawn.getX()>boardFinalSquare && pawn.getX()<home) return true;
        return false;
    }


    private void knock(Player otherPlayer) {
        int x = pawnToMove.getX();

        // see if there are other pawns in the same row
        for (int i = 0; i < BOARD_COLUMNS; i++) {

            // update the other player's pawnXs array
            if(otherPlayer.pawnXs[i] == x && !isInFinishLine(otherPlayer.board[x][i])) {

                otherPlayer.pawnXs[i] = -1;
                // knock it from the other player's board
                otherPlayer.board[x][i].setX(-1);
                // persist in the database the pawn's new position
                masterBoard.saveBoard(otherPlayer.board[x][i]);

                otherPlayer.board[x][i] = new Pawn(Pawns.NONE, username);
            }
        }
    }

    protected void movePawn(Pawn p, int steps, Player otherPlayer) {
        // remove pawn from its previous position by placing a NONE pawn
        board[p.getX()][p.getY()] = new Pawn(Pawns.NONE, username);
        // move pawn by steps
        int newX = p.getX() + steps;
        if(color.equals(Color.BLUE)) {
            newX = newX % (BOARD_ROWS - 1);
        }
        p.setX(newX);
        if(newX == home) {
            pawnsInHome += p.getPawnName().getStringVal() + " ";
        }
        // place pawn on the mainBoard in its new x position
        board[p.getX()][p.getY()] = p;
        // update pawn's x value to the pawnXs array
        pawnXs[p.getY()] = p.getX();

        // persist in the database the pawn's new position
        masterBoard.saveBoard(p);

        turn = false;
        // update game status in the database
        masterBoard.getGameStatus().setShouldMove(false);

        if(dieValue == SIX_DIE) {
            shouldRoll = true;

            // update game status in the database
            masterBoard.getGameStatus().setShouldRoll(true);
        }
        else {
            otherPlayer.setShouldRoll(true);

            // update game status in the database
            masterBoard.getGameStatus().setShouldRoll(true);
            masterBoard.getGameStatus().setPlayersTurn(otherPlayer.getUsername());
        }

        // method call to update game status in the database
        masterBoard.updateGameStatus();
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

        boolean hasMoves = false;

        // checks if the player has any available moves
        for(int i=0; i<BOARD_COLUMNS; i++) {
            if(pawnXs[i] != -1) {
                Pawn p = board[pawnXs[i]][i];
                if(pawnCanMove(p)) {
                    hasMoves = true;
                }

            }
            // if there are pawns in the base and player has rolled a six
            else if(pawnXs[i] == -1 && dieValue == SIX_DIE){
                hasMoves = true;
            }
        }

        if(!hasMoves) {
            turn = false;

            // update game status in the database
            masterBoard.getGameStatus().setShouldMove(false);
            masterBoard.updateGameStatus();

            return false;
        }

        return true;
    }

    public boolean pawnCanMove(Pawn pawn) {
        // if pawn is in base and player hasn't rolled a 6
        if(pawn.getX() == -1 && dieValue != SIX_DIE) return false;

        // if pawn is already in home
        if(pawn.getX() == home) return false;

        // if pawn can't move in the finish line to reach home
        if(color.equals(Color.RED) && pawn.getX() + dieValue > home) return false;
        if(color.equals(Color.BLUE) && (isInFinishLine(pawn) && pawn.getX()+dieValue>home) ||
                (enteringFinishLine(pawn)) && pawn.getX()+dieValue>home) return false;
        return true;
    }

    protected boolean findInBoard(String pawnVal) {

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
        masterBoard.getGameStatus().setShouldRoll(false);

        if(hasMoves()) {
            turn = true;
            masterBoard.getGameStatus().setShouldMove(true);
        }

        masterBoard.updateGameStatus();
        return dieValue;
    }

    public String getAvailableMoves() {
        String moves = "";

        for(int i=0; i<BOARD_COLUMNS; i++) {
            Pawn pawn = new Pawn(Pawns.getColorI(color,i), pawnXs[i], username);
            if(pawnCanMove(pawn)) {
                moves += pawn.getPawnName().getStringVal() + " ";
            }
        }

        return moves;
    }

    public boolean hasWon() {
        for(int x : pawnXs) {
            if(x != home) {
                return false;
            }
        }

        return true;
    }

    public String getUsername() {
        return username;
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

    public Pawn[][] getBoard() {
        return board;
    }

    public int[] getPawnXs() {
        return pawnXs;
    }

    public String getPawnsInHome() {
        return pawnsInHome;
    }

    public int getDieValue() {
        return dieValue;
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
