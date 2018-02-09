package com.company;

public class Pawn extends Piece{

    Pawn(boolean w, int x, int y) { super(w, x, y);
        passantable = false;
    }

    public String toString() {
        if(isWhite) {
            return "\u2659 ";
        } else {
            return "\u265f ";
        }
    }

    @Override
    public boolean checkValidMove(int x, int y) {
        int unSignedDeltaX;
        int unSignedDeltaY;
        if(isWhite) {
            unSignedDeltaX = x - this.xPos;
            unSignedDeltaY = y - this.yPos;
        } else {
            unSignedDeltaX = this.xPos - x;
            unSignedDeltaY = this.yPos - y;
        }
        if (x > Main.board.length - 1 ||
                x < 0 ||
                y > Main.board[0].length - 1 ||
                y < 0) return false; //Checks if piece is out of bounds
        if(unSignedDeltaY == 1 && Math.abs(unSignedDeltaX) == 1) { //Pawn can take a piece of the opposite colour diagonally
            if(Main.board[x][y] != null && Main.board[x][y].isWhite != this.isWhite) { //Checks if destination is occupied by piece of opposite colour
                return true;
            } else if(Main.board[x][y - (isWhite ? 1:-1)] instanceof Pawn &&
                    Main.board[x][y - (isWhite ? 1:-1)].passantable) {
                Main.board[x][y - (isWhite ? 1:-1)] = null;
                return true;
            }
        }
        if(Main.board[x][y] != null) {
            return false; //Checks if destination is already occupied by another piece
        }
        if(!hasMoved) {
            if(unSignedDeltaY == 2 && unSignedDeltaX == 0) {
                if (Main.board[x][yPos + (y - this.yPos) / 2] == null) {
                    passantable = true;
                    return true;
                }
            }
        }
        if(unSignedDeltaY == 1 && unSignedDeltaX == 0) { //Standard Pawn move
            passantable = false;
            return true;
        }
        return false;
    }
}
