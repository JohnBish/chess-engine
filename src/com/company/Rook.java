package com.company;

public class Rook extends Piece{

    Rook(boolean w, int x, int y) {
        super(w, x, y);
    }

    public String toString() {
        if(isWhite) {
            return "\u2656 ";
        } else {
            return "\u265c ";
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
        if(deltaY == 0) {
            for(int i=1; i<Math.abs(deltaX); i++) {
                if(Chess.board[xPos + i * (deltaX / Math.abs(deltaX))][yPos] != null) return false;
            }
            return true;
        } else if(deltaX == 0) {
            for(int i=1; i<Math.abs(deltaY); i++) {
                if(Chess.board[xPos][yPos + i * (deltaY / Math.abs(deltaY))] != null) return false;
            }
            return true;
        }
        return false;
    }
}
