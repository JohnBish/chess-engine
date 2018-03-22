package com.company;

public abstract class Piece {
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

    //Check if move is valid
    public abstract boolean checkValidMove(int x, int y);

    public boolean inCheck(int x, int y) {
        throw new Error("Piece is not a King");
    }

    public boolean inCheckMate() {
        throw new Error("Piece is not a King");
    }
}
