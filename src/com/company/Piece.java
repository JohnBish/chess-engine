package com.company;

public class Piece {
    int xPos;
    int yPos;
    boolean isWhite;
    boolean hasMoved = false;

    Piece(boolean w, int x, int y) {
        this.isWhite = w;
        this.xPos = x;
        this.yPos = y;
    }

    public boolean checkValidMove(int x, int y) {
        return false;
    }

    public boolean inCheck(int x, int y) {
        throw new Error("Piece is not a King");
    }
}
