package com.company;

import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Main {
    private static final Map<String, Class<? extends Piece>> PIECES = new HashMap<String, Class<? extends Piece>>() {{
        put("K", King.class);
        put("Q", Queen.class);
        put("R", Rook.class);
        put("N", Knight.class);
        put("B", Bishop.class);
        put("P", Pawn.class);
    }};
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
    public static final Map<Boolean, String> COLOURS = new HashMap<Boolean, String>() {{
        put(true, "White");
        put(false, "Black");
    }};
    private static final int[] BOARD_DIMENSIONS = new int[] {8, 8};
    private static final String ANSI_BLACK_BACKGROUND = "\u001B[47m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static Piece[][] board;
    private static Scanner sc = new Scanner(System.in);
    private static int[] kingCoords = {4, 0, 4, 7}; //Keeps coordinates of Kings. White's coords are first

    public static void main(String[] args) {
        board = initBoard();
        System.out.println("Welcome to Chess. At any time, type e to exit");
        mainLoop();
    }

    private static Piece[][] initBoard() {
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

    private static void mainLoop() {
        boolean turn = true; //White's turn is true, black's turn is false
        boolean inCheck = false;
        renderBoard(true);
        while (true) {
            if (board[kingCoords[turn ? 0 : 2]][kingCoords[turn ? 1 : 3]].inCheck(kingCoords[turn ? 0 : 2], kingCoords[turn ? 1 : 3])) {
                inCheck = true;
                if (board[kingCoords[turn ? 0 : 2]][kingCoords[turn ? 1 : 3]].inCheckMate()) {
                    System.out.println("Checkmate! " + COLOURS.get(!turn) + " wins.");
                    return;
                }
                System.out.println("Check!");
            }
            System.out.println(COLOURS.get(turn) + "'s turn:");
            String rawMove = sc.nextLine(); //Gets next move in algebraic notation
            if (rawMove.equals("e")) {
                System.out.println("Thanks for playing");
                return;
            }
            rawMove = stripRawMove(turn, rawMove);
            int[] pieceCoords = parseCoords(rawMove); //Returns piece coords
            rawMove = stripLeadingCoord(rawMove);
            int[] moveCoords = parseCoords(rawMove); //Returns move location coords
            System.out.print("\n");
            if (board[pieceCoords[0]][pieceCoords[1]] != null &&
                    board[pieceCoords[0]][pieceCoords[1]].isWhite == turn &&
                    board[pieceCoords[0]][pieceCoords[1]].checkValidMove(moveCoords[0], moveCoords[1]) &&
                    checkValidPlay(inCheck, turn, moveCoords[0], moveCoords[1])) {
                board[pieceCoords[0]][pieceCoords[1]].xPos = moveCoords[0];
                board[pieceCoords[0]][pieceCoords[1]].yPos = moveCoords[1];
                board[moveCoords[0]][moveCoords[1]] = board[pieceCoords[0]][pieceCoords[1]];
                board[moveCoords[0]][moveCoords[1]].hasMoved = true;
                board[pieceCoords[0]][pieceCoords[1]] = null;
                if (board[moveCoords[0]][moveCoords[1]] instanceof King) {
                    kingCoords[turn ? 0 : 2] = moveCoords[0];
                    kingCoords[turn ? 1 : 3] = moveCoords[1];
                }
                turn = !turn;
                renderBoard(turn);
            } else {
                System.out.println("Invalid move");
            }
        }
    }

    private static void renderBoard(boolean t) {
        System.out.println("  a b c d e f g h");
        for (int i = t ? board.length - 1 : 0; t ? i >= 0 : i < board.length; i += t ? -1 : 1) {
            System.out.print(ANSI_RESET);
            System.out.print(i + 1 + " ");
            for (int j = 0; j < board[0].length; j++) {
                if((i + j) % 2 == 0) {
                    System.out.print(ANSI_BLACK_BACKGROUND);
                } else {
                    System.out.print(ANSI_RESET);
                }
                if (board[j][i] != null) {
                    System.out.print(board[j][i]);
                } else {
                    System.out.print("  ");
                }
            }
            System.out.print(ANSI_RESET);
            System.out.print(" " + (i + 1));
            System.out.print("\n");
        }
        System.out.println("  a b c d e f g h");
    }

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

    private static String stripRawMove(boolean c, String m) {
        m = m.replaceAll("\\s", "");
        m = m.replaceAll("x", "");
        m = m.replaceAll("\\+", "");
        if(m.length() == 1) {
            return null;
        } else if(m.equals("O-O")) {
            m = "e" + (c ? 1:8) + "g" + (c ? 1:8); //Kingside castle
        } else if(m.equals("O-O-O")) {
            m = "e" + (c ? 1:8) + "c" + (c ? 1:8); //Queenside castle
        }
        int isPawn;
        try {
            isPawn = Integer.parseInt(m.substring(1, 2));
        } catch (NumberFormatException e) {
            isPawn = 0;
        }
        if (isPawn == 0)
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
                                System.out.println("Move is possible with more than one " + PIECES.get(m.substring(0, 1)).getSimpleName());
                                return null;
                            }
                            numInstance = i;
                        }
                    }
                    m = m.substring(1);
                    m = NUMBERS.get(instances.get(numInstance).xPos) + (instances.get(numInstance).yPos + 1) + m;
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

    private static boolean checkValidPlay(boolean c, boolean t, int x, int y) {
        if (c) {
            Piece temp = board[x][y]; //Stores piece at move location to replace later
            board[x][y] = new Piece(t, x, y);
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