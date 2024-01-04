package com.example.ludo;

import com.example.ludo.exceptions.LudoException;
import com.example.ludo.model.Player;
import com.example.ludo.responses.BoardResponse;
import com.example.ludo.responses.GameResponse;
import com.example.ludo.responses.GameStatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ludo")
public class LudoControllerV2 {

    private final BoardV2 board;

    @Autowired
    public LudoControllerV2(BoardV2 board) {
        this.board = board;
    }

    @GetMapping("/rules")
    public String gameRules() {
        // TODO
        return "Game rules";
    }

    @PostMapping("/reset")
    public GameResponse resetGame() {
        GameResponse response = new GameResponse();
        board.resetGame();
        response.setMessage("All values have been restored to default " + Emojis.SUNGLASSES.getFace());
        return response;
    }

    @PostMapping("/play/{username}")
    public GameResponse initUser(@PathVariable String username) {
        GameResponse response = new GameResponse();
        try {
            board.setUsers(username);
            response.setMessage(username + ", welcome to LUDO! " + Emojis.EXCITED.getFace());
        } catch (LudoException e) {
            response.setMessage(e.getMessage());
        }
        return response;
    }

    @PutMapping("/play/{username}/roll")
    public GameResponse rollDie(@PathVariable String username) {

        try {
            int dieValue = board.roll(username);
            BoardResponse response = new BoardResponse();
            response.setBoard(board);
            response.setDieValue(dieValue);
            Player p = board.getPlayerByUsername(username);
            response.setMessage("Available moves: " + p.getAvailableMoves());
            return response;
        } catch (LudoException e) {
            GameResponse response = new GameResponse();
            response.setMessage(e.getMessage());
            return  response;
        }
    }

    @PutMapping("/play/{username}/move/{pawn}")
    public GameResponse move(@PathVariable String username, @PathVariable String pawn) {

        try {
            board.move(pawn, username);
            BoardResponse response = new BoardResponse();
            response.setBoard(board);
            response.setMessage("Successfully moved!");
            response.setP1Home(board.getP1Home());
            response.setP2Home(board.getP2Home());
            response.setDieValue(-1);
            return response;
        } catch(LudoException e) {
            GameResponse response = new GameResponse();
            response.setMessage(e.getMessage());
            return response;
        }
    }

    @GetMapping("/play/{username}/moves")
    public GameResponse getAvailableMoves(@PathVariable String username) {

        try {
            Player p = board.getPlayerByUsername(username);
            BoardResponse response = new BoardResponse();
            response.setMessage("Available moves: " + p.getAvailableMoves());
            response.setBoard(board);
            response.setDieValue(p.getDieValue());
            response.setP1Home(board.getP1Home());
            response.setP2Home(board.getP2Home());
            return  response;
        } catch (Exception e) {
            GameResponse response = new GameResponse();
            response.setMessage(e.getMessage());
            return  response;
        }
    }

    @GetMapping("/status")
    public GameStatusResponse getGameStatus() {
        GameStatusResponse response = new GameStatusResponse();
        response.setStatus(board.getStatus());

        if(board.getP1() != null)
            response.setUsername1(board.getP1().getUsername());
        if(board.getP2() != null)
            response.setUsername2(board.getP2().getUsername());

        return response;
    }
}
