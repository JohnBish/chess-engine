package com.company;

public class Knight extends Piece{
    public Knight(boolean w, int x, int y) {
        super(w, x, y);
    }

    public String toString() {
        if(isWhite) {
            return "\u2658 ";
        } else {
            return "\u265e ";
        }
    }

    @Override
    public boolean checkValidMove(int x, int y) {
        int deltaX;
        int deltaY;
        if(isWhite) {
            deltaX = x - this.xPos;
            deltaY = y - this.yPos;
        } else {
            deltaX = this.xPos - x;
            deltaY = this.yPos - y;
        }
        if(x > Main.board.length - 1 || y > Main.board[0].length - 1) return false; //Checks if piece is out of bounds
        if(Main.board[x][y] != null && Main.board[x][y].isWhite == this.isWhite) {
            return false; //Checks if destination is already occupied by another piece of same colour
        }
        if((Math.abs(deltaX) == 2 && deltaY == 1) || (Math.abs(deltaX) == 1 && deltaY == 2)) {
            return true; //Standard Knight move
        }
        return false;
    }
}
