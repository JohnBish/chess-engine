package com.company;

public class Queen extends Piece{
    public Queen(boolean w, int x, int y) {
        super(w, x, y);
    }

    public String toString() {
        if(isWhite) {
            return "\u2655 ";
        } else {
            return "\u265b ";
        }
    }

    @Override
    public boolean checkValidMove(int x, int y) {
        if(new Rook(this.isWhite, this.xPos, this.yPos).checkValidMove(x, y) ||
                new Bishop(this.isWhite, this.xPos, this.yPos).checkValidMove(x, y)) {
            return true;
        }
        return false;
    }
}
