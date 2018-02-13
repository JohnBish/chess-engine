package com.company;

public class Knight extends Piece{

    Knight(boolean w, int x, int y) {
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
        int deltaX = x - xPos;
        int deltaY = y - yPos;
        if (x > Chess.board.length - 1 ||
                x < 0 ||
                y > Chess.board[0].length - 1 ||
                y < 0) return false; //Checks if piece is out of bounds
        //Checks if destination is already occupied by another piece of same colour
        return (Chess.board[x][y] == null ||
                Chess.board[x][y].isWhite != this.isWhite) &&
                ((Math.abs(deltaX) == 2 && Math.abs(deltaY) == 1) ||
                        (Math.abs(deltaX) == 1 && Math.abs(deltaY) == 2)); //Standard Knight move
    }
}
