package com.example.ludo;

import com.example.ludo.exceptions.LudoException;
import jakarta.websocket.server.PathParam;
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
    public String resetGame() {
        // TODO
        // board.initializeValues();
        return "All values have been restored to default " + Emojis.SUNGLASSES.getFace();
    }

    @PostMapping("/play/{username}")
    public String initUser(@PathVariable String username) {
        try {
            board.setUsers(username);
            return username + ", welcome to LUDO! " + Emojis.EXCITED.getFace();
        } catch (LudoException e) {
            return e.getMessage();
        }
    }

    // TODO should this be a get request?
    @PostMapping("/play/{username}/roll")
    public String rollDie(@PathVariable String username) {

        try {
            int result = board.roll(username);
            return "Die value: " + result + " " + Emojis.EXCITED.getFace();
        } catch (LudoException e) {
            return e.getMessage();
        }

    }

    @PostMapping("/play/{username}/move/{pawn}")
    public String move(@PathVariable String username, @PathVariable String pawn) {

        try {
            board.move(pawn, username);
            return "Board new state";
        } catch(LudoException e) {
            return e.getMessage();
        }

    }
}
