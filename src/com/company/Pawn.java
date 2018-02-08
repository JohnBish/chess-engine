package com.company;

import com.company.Piece;

public class Pawn extends Piece{
    public Pawn(boolean w, int x, int y) { super(w, x, y); }

    public String toString() {
        if(isWhite) {
            return "\u2659 ";
        } else {
            return "\u265f ";
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
        if(Main.board[x][y] != null && Main.board[x][y].isWhite != this.isWhite) { //Checks if destination is occupied by piece of opposite colour
            if(deltaY == 1 && Math.abs(deltaX) == 1) {
                return true; //Pawn can take a piece of the opposite colour diagonally
            }
        }
        if(Main.board[x][y] != null) {
            return false; //Checks if destination is already occupied by another piece
        }
        if(!hasMoved) {
            if(deltaY == 2 && deltaX == 0) {
                if(Main.board[x][yPos + (y - this.yPos) / 2] != null) {
                    return false;
                }
                return true; //Pawn can move 2 squares if it hasn't moved yet
            }
        }
        return deltaY == 1 && deltaX == 0; //Standard Pawn move
    }
}
