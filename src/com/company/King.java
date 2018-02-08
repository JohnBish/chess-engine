package com.company;

public class King extends Piece{
    King(boolean w, int x, int y) {
        super(w, x, y);
    }

    public String toString() {
        if(isWhite) {
            return "\u2654 ";
        } else {
            return "\u265a ";
        }
    }

    @Override
    public boolean checkValidMove(int x, int y) {
        int deltaX = x - xPos;
        int deltaY = y - yPos;
        if (x > Main.board.length - 1 || y > Main.board[0].length - 1) return false; //Checks if piece is out of bounds
        if (Main.board[x][y] != null && Main.board[x][y].isWhite == this.isWhite) {
            return false; //Checks if destination is already occupied by another piece of same colour
        }
        if(deltaX == 0 && deltaY == 0) return false;
        if(Math.abs(deltaX) <= 1 && Math.abs(deltaY) <= 1) {
            return !inCheck(x, y);
        }
        return false;
    }

    @Override
    public boolean inCheck(int x, int y) {
        for(int i=Main.board.length - 1; i >= 0; i--) {
            for(int j=0; j<Main.board[0].length; j++) {
                if(Main.board[j][i] != null && Main.board[j][i].isWhite != this.isWhite) {
                    if(Main.board[j][i].checkValidMove(x, y)) {
                        return true; //Checks if King is in check
                    }
                }
            }
        }
        return false;
    }
}
