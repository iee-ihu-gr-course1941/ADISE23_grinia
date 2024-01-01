package com.example.ludo;

import com.example.ludo.exceptions.*;
import org.springframework.stereotype.Component;

@Component
public class BoardV2 {

    private Player p1;
    private Player p2;


    public BoardV2() {
    }

    public boolean move(String pawn, String username) throws LudoException{
        checkSet();

        Player p = getPlayer(username);

        if(!p.isTurn()) {
            throw new NotYourTurnException();
        }

        return p.move(pawn, getOtherPlayer(p));
    }

    public int roll(String username) throws LudoException {
        checkSet();

        Player p = getPlayer(username);

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
        }

        return dieValue;

    }

    private Player getPlayer(String username) throws InvalidUsernameException{
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
        this.p1 = new Player(username, Color.RED);
    }

    public Player getP2() {
        return p2;
    }

    public void setP2(String username) throws InvalidUsernameException{
        if(username.equals(p1.getUsername())) throw new InvalidUsernameException();
        this.p2 = new Player(username, Color.BLUE);
    }

    public void setUsers(String username) throws LudoException{
        if(p1 == null) setP1(username);
        else if(p2 == null) {
            setP2(username);
            p1.setShouldRoll(true);
            p2.setShouldRoll(false);
        }
        else throw new AllUsersSetException();
    }

    private void checkSet() throws NotAllUsersSetException {
        if(p1 == null || p2 == null) throw new NotAllUsersSetException();
    }
}
