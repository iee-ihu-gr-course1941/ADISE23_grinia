package com.example.ludo.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity(name="PAWN_POSITION")
@IdClass(PawnId.class)
public class Pawn {

    @Id
    @Column(name = "USERNAME")
    private String username;

    @Id
    @Column(name = "PAWN_ID")
    @Enumerated(EnumType.STRING)
    private Pawns pawnName;

    @Column(name = "X")
    private int x;

    @Column(name = "Y")
    private int y;

    public Pawn() {
        // Default constructor
    }

    public Pawn(Pawns pawnName, String username) {
        this(pawnName, -1, username);
    }

    public Pawn(Pawns pawnName, int x, String username) {
        this.pawnName = pawnName;
        this.username = username;
        this.x = x;
        switch (pawnName){
            case Pawns.R1, Pawns.B1:
                this.y = 0;
                break;
            case Pawns.R2, Pawns.B2:
                this.y = 1;
                break;
            case Pawns.R3, Pawns.B3:
                this.y = 2;
                break;
            case Pawns.R4, Pawns.B4:
                this.y = 3;
                break;
            default:
                this.y = -1;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Pawns getPawnName() {
        return pawnName;
    }

    public void setPawnName(Pawns pawnId) {
        this.pawnName = pawnId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pawn pawn = (Pawn) o;
        return pawnName == pawn.pawnName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pawnName);
    }
}
