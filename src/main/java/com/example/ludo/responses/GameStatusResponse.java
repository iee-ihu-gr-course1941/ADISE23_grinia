package com.example.ludo.responses;

public class GameStatusResponse extends GameResponse{
    private String status;
    private String username1;
    private String username2;

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public String getStatus() {
        return status;
    }

    public String getUsername1() {
        return username1;
    }

    public String getUsername2() {
        return username2;
    }
}
