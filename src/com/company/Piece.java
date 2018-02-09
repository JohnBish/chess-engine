package com.company;

public class Piece {
    public int xPos;
    public int yPos;
    public boolean isWhite;
    public boolean hasMoved;
    public boolean passantable;

    Piece(boolean w, int x, int y) {
        this.isWhite = w;
        this.xPos = x;
        this.yPos = y;
        hasMoved = false;
    }

    public boolean checkValidMove(int x, int y) {
        return false;
    }

    public boolean inCheck(int x, int y) {
        throw new Error("Piece is not a King");
    }

    public boolean inCheckMate() {
        throw new Error("Piece is not a King");
    }
}
