package com.company;

import java.util.ArrayList;

public class King extends Piece{
    private ArrayList<Piece> threatening;

    King(boolean w, int x, int y) {
        super(w, x, y);
        threatening = new ArrayList<>();
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
        if (x > Chess.board.length - 1 ||
                x < 0 ||
                y > Chess.board[0].length - 1 ||
                y < 0) return false; //Checks if piece is out of bounds
        //Checks if destination is already occupied by another piece of same colour
        if (Chess.board[x][y] == null ||
                Chess.board[x][y].isWhite != this.isWhite) if (deltaX != 0 || deltaY != 0)
            if (Math.abs(deltaX) <= 1) {
                if (Math.abs(deltaY) <= 1) { //Standard King move
                    return !inCheck(x, y); //Can't move into check
                }
            } else if(Math.abs(deltaX) == 2 && deltaY == 0) {
                boolean validCastle = !hasMoved &&  //Checks if castle is valid
                        Chess.board[xPos + deltaX / Math.abs(deltaX)][yPos] == null &&
                        !inCheck(xPos + deltaX / Math.abs(deltaX), yPos) &&
                        Chess.board[xPos + 2 * (deltaX / Math.abs(deltaX))][yPos] == null &&
                        !inCheck(xPos + 2 * (deltaX / Math.abs(deltaX)), yPos) &&
                        (Chess.board[xPos + 3 * (deltaX / Math.abs(deltaX))][yPos] == null) ||
                        (Chess.board[xPos + 3 * (deltaX / Math.abs(deltaX))][yPos] instanceof Rook) &&
                                Chess.board[(deltaX > 0) ? 7:0][yPos] instanceof Rook &&
                                !(Chess.board[(deltaX > 0) ? 7:0][yPos].hasMoved);
                if(validCastle) {
                    Chess.board[(deltaX > 0) ? 5:3][yPos] = Chess.board[(deltaX > 0) ? 7:0][yPos];
                    Chess.board[(deltaX > 0) ? 7:0][yPos] = null;
                    GUI.promptLabel.append(Chess.COLOURS.get(isWhite) + " castles!");
                    return true; //Castles
                }
            }
        return false;
    }

    //Checks if specified coordinates are threatened
    @Override
    public boolean inCheck(int x, int y) {
        threatening.clear();
        for(int i = Chess.board.length - 1; i >= 0; i--) {
            for(int j = 0; j< Chess.board[0].length; j++) {
                if(Chess.board[j][i] != null && Chess.board[j][i].isWhite != this.isWhite) {
                    if(Chess.board[j][i].checkValidMove(x, y)) {
                        threatening.add(Chess.board[j][i]);
                    }
                }
            }
        }
        return !threatening.isEmpty();
    }

    @Override
    public boolean inCheckMate() {
        //Checks if the king can get out of check
        for(int dx = -1; dx <= 1; dx++) {
            for(int dy = -1; dy <= 1; dy++) {
                if(this.checkValidMove(xPos + dx, yPos + dy)) return false;
            }
        }
        if(threatening.size() > 1) {
            return true;
        } else {
            //Makes a list of moves that would put the king out of check
            ArrayList<int[]> savingMoves = new ArrayList<>();
            int deltaX = xPos - threatening.get(0).xPos;
            int deltaY = yPos - threatening.get(0).yPos;
            savingMoves.add(new int[] {threatening.get(0).xPos, threatening.get(0).yPos});
            if(Math.abs(deltaX) == Math.abs(deltaY)) { //If piece is Bishop or Queen
                for(int i=1; i<Math.abs(deltaX); i++) {
                    savingMoves.add(new int[] {threatening.get(0).xPos + i * (deltaX / Math.abs(deltaX)),
                            threatening.get(0).yPos + i * (deltaY / Math.abs(deltaY))});
                }
            } else if(deltaX == 0 || deltaY == 0) { //If piece is Rook or Queen
                for(int i=1; i<(Math.abs(deltaX) + Math.abs(deltaY)); i++) {
                    if(deltaX == 0) {
                        savingMoves.add(new int[] {threatening.get(0).xPos,
                                threatening.get(0).yPos + i * (deltaY / Math.abs(deltaY))});
                    } else {
                        savingMoves.add(new int[] {threatening.get(0).xPos + i * (deltaX / Math.abs(deltaX)),
                                threatening.get(0).yPos});
                    }
                }
            }

            //Checks if any of the saving moves are possible with any piece of correct colour
            for (int[] savingMove : savingMoves) {
                for (int i = Chess.board.length - 1; i >= 0; i--) {
                    for (int j = 0; j < Chess.board[0].length; j++) {
                        if (Chess.board[j][i] != null && Chess.board[j][i].isWhite == this.isWhite) {
                            if (Chess.board[j][i].checkValidMove(savingMove[0], savingMove[1])) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
