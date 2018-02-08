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
        if (x > Main.board.length - 1 ||
                x < Main.board.length - 1 ||
                y > Main.board[0].length - 1 ||
                y < Main.board[0].length - 1) return false; //Checks if piece is out of bounds
        //Checks if destination is already occupied by another piece of same colour
        return (Main.board[x][y] == null ||
                Main.board[x][y].isWhite != this.isWhite) &&
                (deltaX != 0 || deltaY != 0) &&
                Math.abs(deltaX) <= 1 && Math.abs(deltaY) <= 1 && //King can move one square in any direction
                !inCheck(x, y); //Not in check
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

    @Override
    public boolean inCheckMate(int x, int y) {
        for(int dx = -1; dx <= 1; dx++) {
            for(int dy = -1; dy <= 1; dy++) {
                //Magic goes here
            }
        }
        return true;
    }
}
