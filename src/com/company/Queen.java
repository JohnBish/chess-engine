package com.company;

public class Queen extends Piece{

    Queen(boolean w, int x, int y) {
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
        return new Rook(this.isWhite, this.xPos, this.yPos).checkValidMove(x, y) ||
                new Bishop(this.isWhite, this.xPos, this.yPos).checkValidMove(x, y); //Can move validly as Rook or Bishop
    }
}
