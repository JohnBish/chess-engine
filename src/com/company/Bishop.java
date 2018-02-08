package com.company;

public class Bishop extends Piece{
    public Bishop(boolean w, int x, int y) {
        super(w, x, y);
    }

    public String toString() {
        if(isWhite) {
            return "\u2657 ";
        } else {
            return "\u265d ";
        }
    }

    @Override
    public boolean checkValidMove(int x, int y) {
        int deltaX = x - xPos;
        int deltaY = y - xPos;
        if(x > Main.board.length - 1 || y > Main.board[0].length - 1) return false; //Checks if piece is out of bounds
        if(Main.board[x][y] != null && Main.board[x][y].isWhite == this.isWhite) {
            return false; //Checks if destination is already occupied by another piece of same colour
        }
        if(deltaX == 0 && deltaY == 0) return false;
        if(Math.abs(deltaY) == Math.abs(deltaX)) { //If move is diagonal
            for(int i=1; i<Math.abs(deltaY); i++) {
                if(Main.board[xPos + i * (deltaX / Math.abs(deltaX))][yPos + i * (deltaY / Math.abs(deltaY))] != null) {
                    return false; //Checks if any piece is in the way of Bishop's path
                }
            }
            return true;
        }
    return false;
    }
}
