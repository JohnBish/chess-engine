package com.company;

import javax.swing.*;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * U1A1Q5
 * @author Jack Bishop
 * ICS 4UI
 * Ms Harris
 * 13 February 2018
 */

//Pawn promotion is not implemented yet.

/*Printing to the console has been commented out and replaced
* with outputting to GUI, but may be reintroduced in the
* future. The renderBoard() method will still output to the
* console if called without the JLabel parameter.*/

public class Chess {
    //Declare constants and variables

    //Converts piece representation characters to appropriate class
    private static final Map<String, Class<? extends Piece>> PIECES = new HashMap<String, Class<? extends Piece>>() {{
        put("K", King.class);
        put("Q", Queen.class);
        put("R", Rook.class);
        put("N", Knight.class);
        put("B", Bishop.class);
        put("P", Pawn.class);
    }};

    //Converts letters to numbers
    private static final Map<String, Integer> LETTERS = new HashMap<String, Integer>() {{
        put("a", 0);
        put("b", 1);
        put("c", 2);
        put("d", 3);
        put("e", 4);
        put("f", 5);
        put("g", 6);
        put("h", 7);
    }};

    //Converts numbers to letters
    private static final Map<Integer, String> NUMBERS = new HashMap<Integer, String>() {{
        put(0, "a");
        put(1, "b");
        put(2, "c");
        put(3, "d");
        put(4, "e");
        put(5, "f");
        put(6, "g");
        put(7, "h");
    }};

    //Converts boolean to string representation of turn
    public static final Map<Boolean, String> COLOURS = new HashMap<Boolean, String>() {{
        put(true, "White");
        put(false, "Black");
    }};
    private static final int[] BOARD_DIMENSIONS = new int[] {8, 8};

    public static Piece[][] board;
    //private static Scanner sc = new Scanner(System.in);
    /*Keeps coordinates of Kings to check is player is in check.
    White's coordinates are first*/
    private static int[] kingCoords = {4, 0, 4, 7};
    public static boolean turn = true; //White's turn is true, black's turn is false;
    private static boolean inCheck;
    private static GUI gui;

    public static void main(String[] args) {
        gui = new GUI(); //Initializes GUI
    }

    public static Piece[][] initBoard() {
        Piece[][] b = new Piece[BOARD_DIMENSIONS[0]][BOARD_DIMENSIONS[1]];
        for (int i = 0; i < b.length; i++) {
            b[i][1] = new Pawn(true, i, 1);
            b[i][6] = new Pawn(false, i, 6);
        }
        b[0][0] = new Rook(true, 0, 0);
        b[7][0] = new Rook(true, 7, 0);
        b[0][7] = new Rook(false, 0, 7);
        b[7][7] = new Rook(false, 7, 7);
        b[1][0] = new Knight(true, 1, 0);
        b[6][0] = new Knight(true, 6, 0);
        b[1][7] = new Knight(false, 1, 7);
        b[6][7] = new Knight(false, 6, 7);
        b[2][0] = new Bishop(true, 2, 0);
        b[5][0] = new Bishop(true, 5, 0);
        b[2][7] = new Bishop(false, 2, 7);
        b[5][7] = new Bishop(false, 5, 7);
        b[4][0] = new King(true, 4, 0);
        b[3][0] = new Queen(true, 3, 0);
        b[4][7] = new King(false, 4, 7);
        b[3][7] = new Queen(false, 3, 7);
        return b;
    }

    public static void move(String rawMove) { //rawMove is next move in algebraic notation
        GUI.promptLabel.append(rawMove + "\n");
        rawMove = stripRawMove(turn, rawMove);
        int[] pieceCoords = parseCoords(rawMove); //Returns piece coordinates
        rawMove = stripLeadingCoord(rawMove);
        int[] moveCoords = parseCoords(rawMove); //Returns move location coordinates
        //Makes sure move entered is valid and moves piece
        movePiece(pieceCoords, moveCoords);
        //Checks for check
        checkIfInCheck();
        GUI.promptLabel.append(COLOURS.get(turn) + "'s turn" + "\n");
    }

    private static void checkIfInCheck() {
        if (board[kingCoords[turn ? 0 : 2]][kingCoords[turn ? 1 : 3]].inCheck(kingCoords[turn ? 0 : 2], kingCoords[turn ? 1 : 3])) {
            inCheck = true;
            //Checks for checkmate
            if (board[kingCoords[turn ? 0 : 2]][kingCoords[turn ? 1 : 3]].inCheckMate()) {
                GUI.promptLabel.append("Checkmate! " + COLOURS.get(!turn) + " wins." + "\n\n");
                GUI.playButton.setVisible(true);
                return;
            }
            GUI.promptLabel.append("Check!\n");
        }
    }

    private static void movePiece(int[] p, int[] m) {
        if (board[p[0]][p[1]] != null &&
                board[p[0]][p[1]].isWhite == turn &&
                board[p[0]][p[1]].checkValidMove(m[0], m[1]) &&
                checkValidPlay(inCheck, turn, m[0], m[1])) {
            //Moves piece
            board[p[0]][p[1]].xPos = m[0];
            board[p[0]][p[1]].yPos = m[1];
            board[m[0]][m[1]] = board[p[0]][p[1]];
            board[m[0]][m[1]].hasMoved = true;
            board[p[0]][p[1]] = null;
            if (board[m[0]][m[1]] instanceof King) {
                //Updates king's coordinates
                kingCoords[turn ? 0 : 2] = m[0];
                kingCoords[turn ? 1 : 3] = m[1];
            }
            turn = !turn;
            renderBoard(turn, GUI.outputLabel);
        } else {
            GUI.promptLabel.append("Invalid move" + "\n");
        }
    }

    //Outputs current state of board
    public static void renderBoard(boolean t, JLabel l) {
        l.setText("<html>" + (t ? "<pre>  a b c d e f g h</pre>" : "<pre>  h g f e d c b a</pre>"));
        for (int i = t ? board.length - 1 : 0; t ? i >= 0 : i < board.length; i += t ? -1 : 1) {
            l.setText(l.getText() + (i + 1) + " ");
            for (int j = t ? 0 : board.length - 1; t ? j < board.length : j >= 0; j += t ? 1 : -1) {
                if (board[j][i] != null) {
                    l.setText(l.getText() + board[j][i]);
                } else {
                    l.setText(l.getText() + "- ");
                }
            }
            l.setText(l.getText() + " " + (i + 1));
            l.setText(l.getText() + "<br>");
        }
        l.setText(l.getText() + (t ? "<pre>  a b c d e f g h</pre>" : "<pre>  h g f e d c b a</pre>"));
    }

    //Converts letters to numbers in input string
    private static int[] parseCoords(String m) {
        if (m == null) {
            return new int[]{0, 0};
        }
        int row;
        try {
            row = Integer.parseInt(m.substring(1, 2)) - 1;
        } catch (NumberFormatException e) {
            return new int[]{0, 0};
        }
        if (LETTERS.containsKey(m.substring(0, 1))) {
            return new int[]{LETTERS.get(m.substring(0, 1)), row};
        }
        return new int[]{0, 0};
    }

    //Strips whitespace from move and handles special cases
    private static String stripRawMove(boolean c, String m) {
        m = m.replaceAll("\\s", "");
        m = m.replaceAll("x", "");
        m = m.replaceAll("\\+", "");
        if(m.length() == 1) {
            return null;
        }
        switch(m) {
            case "":
                return null;
            case "O-O":
            case "0-0":
                m = "e" + (c ? 1:8) + "g" + (c ? 1:8); //Kingside castle
                break;
            case "O-O-O":
            case "0-0-0":
                m = "e" + (c ? 1:8) + "c" + (c ? 1:8); //Queenside castle
                break;
        }

        try {
            Integer.parseInt(m.substring(0, 1));
            return null; //Input must not start with number
        } catch(NumberFormatException e1) {
            try {
                Integer.parseInt(m.substring(1, 2));
                //2 character  pawn move abbreviation
                if(m.length() == 2) {
                    for(int i = c ? 1 : -1; c ? i < 3 : i > -3; i += c ? 1:-1) {
                        if(board[LETTERS.get(m.substring(0, 1))][Integer.parseInt(m.substring(1, 2)) - i - 1] != null &&
                                board[LETTERS.get(m.substring(0, 1))][Integer.parseInt(m.substring(1, 2)) - i - 1].checkValidMove(
                                        LETTERS.get(m.substring(0, 1)), Integer.parseInt(m.substring(1, 2)) - 1)) {
                            return m.substring(0, 1) + (Integer.parseInt(m.substring(1, 2)) - i) + m;
                        }
                    }
                    return null;
                }
            } catch (NumberFormatException e) {
                if(m.length() == 2) return null; //2-char move is not possible without pawn abbreviation
                switch (m.substring(0, 1)) {
                    case "K":
                    case "Q":
                    case "R":
                    case "N":
                    case "B":
                    case "P":
                        ArrayList<Piece> instances = findPiecesByClass(c, PIECES.get(m.substring(0, 1)));
                        m = m.toLowerCase();
                        int[] pieceCoords = parseCoords(m.substring(1));
                        int numInstance = -1;
                        for (int i = 0; i < instances.size(); i++) {
                            if (instances.get(i).checkValidMove(pieceCoords[0], pieceCoords[1])) {
                                //Looks for a valid move with pieces of the specified type and colour
                                if (numInstance != -1) {
                                    GUI.promptLabel.append("Move is possible with more than one " +
                                            PIECES.get(m.substring(0, 1)).getSimpleName() + "\n");
                                    return null;
                                }
                                numInstance = i;
                            }
                        }
                        if(numInstance == -1) return null;
                        m = m.substring(1);
                        m = NUMBERS.get(instances.get(numInstance).xPos) + (instances.get(numInstance).yPos + 1) + m;
                    default:
                        if(m.length() == 3) {
                            return null;
                        }
                }
            }
        }
        return m;
    }

    private static String stripLeadingCoord(String m) {
        if (m == null) return null;
        m = m.substring(2);
        if (m.length() > 2) {
            m = m.substring(1);
        }
        return m;
    }

    //Returns locations of all pieces of the specified class and colour
    private static ArrayList<Piece> findPiecesByClass(boolean c, Class<? extends Piece> p) {
        ArrayList<Piece> instances = new ArrayList<>();
        for (int i = board.length - 1; i >= 0; i--) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[j][i] != null &&
                        p.isInstance(board[j][i]) &&
                        board[j][i].isWhite == c) {
                    instances.add(board[j][i]);
                }
            }
        }
        return instances;
    }

    //Makes sure player is not causing king to be in check
    private static boolean checkValidPlay(boolean c, boolean t, int x, int y) {
        if (c) {
            Piece temp = board[x][y]; //Stores piece at move location to replace later
            board[x][y] = new Pawn(t, x, y);
            if (board[kingCoords[t ? 0 : 2]][kingCoords[t ? 1 : 3]].inCheck(kingCoords[t ? 0 : 2], kingCoords[t ? 1 : 3])) {
                board[x][y] = temp;
                return false;
            }
            board[x][y] = temp;
            return true;
        } else {
            return true;
        }
    }
}