package com.company;

public class Bishop extends Piece{

    Bishop(boolean w, int x, int y) {
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
        int deltaY = y - yPos;
        if (x > Chess.board.length - 1 ||
                x < 0 ||
                y > Chess.board[0].length - 1 ||
                y < 0) return false; //Checks if piece is out of bounds
        if(Chess.board[x][y] != null && Chess.board[x][y].isWhite == this.isWhite) {
            return false; //Checks if destination is already occupied by another piece of same colour
        }
        if(deltaX == 0 && deltaY == 0) return false;
        if(Math.abs(deltaY) == Math.abs(deltaX)) { //If move is diagonal
            for(int i=1; i<Math.abs(deltaY); i++) {
                if(Chess.board[xPos + i * (deltaX / Math.abs(deltaX))][yPos + i * (deltaY / Math.abs(deltaY))] != null) {
                    return false; //Checks if any piece is in the way of Bishop's path
                }
            }
            return true;
        }
    return false;
    }
}
