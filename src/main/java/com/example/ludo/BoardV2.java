package com.example.ludo;

import com.example.ludo.db.services.GameStatusService;
import com.example.ludo.db.services.PawnPositionService;
import com.example.ludo.exceptions.*;
import com.example.ludo.model.Color;
import com.example.ludo.model.GameStatus;
import com.example.ludo.model.Pawn;
import com.example.ludo.model.Player;
import org.springframework.stereotype.Component;

@Component
public class BoardV2 {

    public final static int BOARD_ROWS = 46;
    public final static int BOARD_COLUMNS = 8;

    private final PawnPositionService pawnPositionService;
    private final GameStatusService gameStatusService;

    private Player p1;
    private Player p2;

    private GameStatus gameStatus;


    public BoardV2(PawnPositionService pawnPositionService, GameStatusService gameStatusService) {
        this.pawnPositionService = pawnPositionService;
        this.gameStatusService = gameStatusService;

        // remove all previous values from tables in the database
        this.pawnPositionService.deleteAll();
        this.gameStatusService.deleteData();

        gameStatus = new GameStatus();
    }

    public void resetGame() {
        p1 = null;
        p2 = null;
        // remove all previous values from tables in the database
        this.pawnPositionService.deleteAll();
        this.gameStatusService.deleteData();
    }

    public boolean move(String pawn, String username) throws LudoException{
        checkSet();

        Player p = getPlayerByUsername(username);

        if(!p.isTurn()) {
            throw new NotYourTurnException();
        }

        if(p.hasWon()) {
            // update game status in the database
            gameStatus.setGameOver(true);
            gameStatus.setWinner(p.getUsername());
            gameStatusService.saveGameStatus(gameStatus);

            throw new PlayerHasWonException(p);
        }

        return p.move(pawn, getOtherPlayer(p));
    }

    public int roll(String username) throws LudoException {
        checkSet();

        Player p = getPlayerByUsername(username);

        if(!p.isShouldRoll() ) {
            if(!p.isTurn()) {
                throw new NotYourTurnException();
            }
            else {
                throw new ShouldMoveException();
            }
        }

        int dieValue = p.rollDie();
        if(!p.hasMoves()) {
            Player otherPlayer = getOtherPlayer(p);
            otherPlayer.setShouldRoll(true);

            // update game status in the database
            gameStatus.setPlayersTurn(otherPlayer.getUsername());
            gameStatus.setShouldRoll(true);
            gameStatusService.saveGameStatus(gameStatus);
        }

        return dieValue;
    }

    public Player getPlayerByUsername(String username) throws InvalidUsernameException{
        if(p1.getUsername().equals(username)) return p1;
        else if (p2.getUsername().equals(username)) return p2;
        else throw new InvalidUsernameException();
    }

    private Player getOtherPlayer(Player player) {
        if(player.equals(p1)) return p2;
        else return p1;
    }

    public Player getP1() {
        return p1;
    }

    public void setP1(String username) {
        this.p1 = new Player(username, Color.RED, 0, 45, 41, this);
        // this is for 27 rows board (testing)
//        this.p1 = new Player(username, Color.RED, 0, 26, 24, this);
        p1.setShouldRoll(true);

        // update game status in the database
        gameStatus.setPlayer1(username);
        gameStatus.setPlayersTurn(username);
        gameStatus.setShouldRoll(true);
        gameStatus.setShouldMove(false);
        this.gameStatusService.saveGameStatus(gameStatus);
    }

    public Player getP2() {
        return p2;
    }

    public void setP2(String username) throws InvalidUsernameException{
        if(username.equals(p1.getUsername())) throw new InvalidUsernameException();

        this.p2 = new Player(username, Color.BLUE, 20, 19, 15, this);
        // this is for 27 rows board (testing)
//        this.p2 = new Player(username, Color.BLUE, 12, 11, 9, this);
        p2.setShouldRoll(false);

        // update game status in the database
        gameStatus.setPlayer2(username);
        this.gameStatusService.saveGameStatus(gameStatus);
    }

    public void setUsers(String username) throws LudoException{
        if(p1 == null) setP1(username);
        else if(p2 == null) {
            setP2(username);
        }
        else throw new AllUsersSetException();
    }

    private void checkSet() throws NotAllUsersSetException {
        if(p1 == null || p2 == null) throw new NotAllUsersSetException();
    }

    public String getStatus() {
        try {
            checkSet();
        } catch (NotAllUsersSetException e) {
            return "WAITING FOR PLAYERS";
        }

        if(p1.hasWon()) {
            return "GAME OVER:" + p1.getUsername() + " IS THE WINNER";
        }
        else if(p2.hasWon()) {
            return "GAME OVER:" + p2.getUsername() + " IS THE WINNER";
        }

        return gameStatus.toString();
    }

    public void saveBoard(Pawn pawn) {
        pawnPositionService.savePawn(pawn);
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void updateGameStatus() {
        gameStatusService.saveGameStatus(gameStatus);
    }

    public String getP1Home() {
        return p1.getPawnsInHome();
    }

    public String getP2Home() {
        return p2.getPawnsInHome();
    }
}
