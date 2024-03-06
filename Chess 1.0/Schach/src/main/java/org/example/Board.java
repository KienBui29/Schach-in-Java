package org.example;

import pieces.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Board extends JPanel {

    public int titleSize = 85;

    int cols = 8;
    int rows = 8;

    ArrayList<Piece> piecelist = new ArrayList<Piece>();

    public Piece selectedPiece;

    private boolean isWhiteTurn = true; // Variable zur Verfolgung des aktuellen Spielers

    Input input = new Input(this);

    public CheckScanner checkScanner = new CheckScanner(this);

    public int enPassantTile = -1;

    public Board() {
        this.setPreferredSize(new Dimension(cols * titleSize, rows * titleSize));

        this.addMouseListener(input);
        this.addMouseMotionListener(input);

        addPieces();
    }

    // Methode zum Wechseln des aktuellen Spielers
    private void switchTurn() {
        isWhiteTurn = !isWhiteTurn;
    }

    public Piece getPiece(int col, int row) {

        for (Piece piece : piecelist) {
            if(piece.col == col && piece.row == row) {
                return piece;
            }
        }

        return null;
    }

    public void makeMove(Move move) {
        if ((isWhiteTurn && move.piece.isWhite) || (!isWhiteTurn && !move.piece.isWhite)) {

        if (move.piece.name.equals("Pawn")) {
            movePawn(move);
        } else if (move.piece.name.equals("King")) {
            moveKing((move));
        }

            move.piece.col = move.newCol;
            move.piece.row = move.newRow;
            move.piece.xPos = move.newCol * titleSize;
            move.piece.yPos = move.newRow * titleSize;

            move.piece.isFirstMove = false;

            capture(move.capture);

        switchTurn();
}
    }


    private void moveKing(Move move) {

        if (Math.abs(move.piece.col - move.newCol) == 2) {
            Piece rook;
            if (move.piece.col < move.newCol) {
                rook = getPiece(7, move.piece.row);
                rook.col = 5;
            } else {
                rook = getPiece(0, move.piece.row);
                rook.col = 3;
            }
            rook.xPos = rook.col * titleSize;
        }

    }

    private void movePawn(Move move) {

        // en passant
        int colorIndex = move.piece.isWhite ? 1 : -1;

        if (getTileNum(move.newCol, move.newRow) == enPassantTile) {
            move.capture = getPiece(move.newCol, move.newRow + colorIndex);
        }
        if (Math.abs(move.piece.row - move.newRow) == 2) {
            enPassantTile = getTileNum(move.newCol, move.newRow + colorIndex);
        } else {
            enPassantTile = -1;
        }

        // promotions
        colorIndex = move.piece.isWhite ? 0 : 7;
        if (move.newRow == colorIndex) {
            promotePawn(move);
        }
    }

    private void promotePawn(Move move) {
        piecelist.add(new Queen(this, move.newCol, move.newRow, move.piece.isWhite));
        capture(move.piece);
    }

    public void capture(Piece piece) {
        piecelist.remove(piece);
    }


    public boolean isValidMove(Move move) {

        if (sameTeam(move.piece, move.capture)) {
            return false;
        }
        if (!move.piece.isValidMovement(move.newCol, move.newRow)) {
            return false;
        }
        if (move.piece.moveCollidesWithPiece(move.newCol, move.newRow)) {
            return false;
        }
        if (checkScanner.isKingChecked(move)) {
            return false;
        }


        return true;
    }

    public  boolean sameTeam(Piece p1, Piece p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.isWhite == p2.isWhite;
    }

    public int getTileNum(int col, int row) {
        return row * rows + col;
    }

    Piece findKing(boolean isWhite) {
        for (Piece piece : piecelist) {
            if (isWhite == piece.isWhite && piece.name.equals("King")) {
                return piece;
            }
        }
        return null;
    }

    public void addPieces() {

        piecelist.add(new Rook(this, 0, 0, false));
        piecelist.add(new Knight(this, 1, 0, false));
        piecelist.add(new Bishop(this, 2, 0, false));
        piecelist.add(new Queen(this, 3, 0, false));
        piecelist.add(new King(this, 4, 0, false));
        piecelist.add(new Bishop(this, 5, 0, false));
        piecelist.add(new Knight(this, 6, 0, false));
        piecelist.add(new Rook(this, 7, 0, false));

        piecelist.add(new Pawn(this, 0, 1, false));
        piecelist.add(new Pawn(this, 1, 1, false));
        piecelist.add(new Pawn(this, 2, 1, false));
        piecelist.add(new Pawn(this, 3, 1, false));
        piecelist.add(new Pawn(this, 4, 1, false));
        piecelist.add(new Pawn(this, 5, 1, false));
        piecelist.add(new Pawn(this, 6, 1, false));
        piecelist.add(new Pawn(this, 7, 1, false));


        piecelist.add(new Rook(this, 0, 7, true));
        piecelist.add(new Knight(this, 1, 7, true));
        piecelist.add(new Bishop(this, 2, 7, true));
        piecelist.add(new Queen(this, 3, 7, true));
        piecelist.add(new King(this, 4, 7, true));
        piecelist.add(new Bishop(this, 5, 7, true));
        piecelist.add(new Knight(this, 6, 7, true));
        piecelist.add(new Rook(this, 7, 7, true));

        piecelist.add(new Pawn(this, 0, 6, true));
        piecelist.add(new Pawn(this, 1, 6, true));
        piecelist.add(new Pawn(this, 2, 6, true));
        piecelist.add(new Pawn(this, 3, 6, true));
        piecelist.add(new Pawn(this, 4, 6, true));
        piecelist.add(new Pawn(this, 5, 6, true));
        piecelist.add(new Pawn(this, 6, 6, true));
        piecelist.add(new Pawn(this, 7, 6, true));
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // paint Board
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Piece king = findKing(isWhiteTurn);
                // Check if the king is in check and if the current tile is the king's tile
                if (king != null && checkScanner.isKingChecked(new Move(this, king, c, r)) &&
                        king.col == c && king.row == r) {
                    g2d.setColor(new Color(255, 130, 130, 255)); // Set color to red if king is in check
                } else {
                    g2d.setColor((c + r) % 2 == 0 ? new Color(235, 236, 208, 255) : new Color(158, 224, 248, 255));
                }
                g2d.fillRect(c * titleSize, r * titleSize, titleSize, titleSize);
            }
        }

        // paint Highlights
        if (selectedPiece != null) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (isValidMove(new Move(this, selectedPiece, c, r))) {
                        g2d.setColor(new Color(119, 248, 139, 153));
                        g2d.fillRect(c * titleSize, r * titleSize, titleSize, titleSize);
                    }
                }
            }
        }

        // paint Pieces
        for (Piece piece : piecelist) {
            piece.paint(g2d);
        }
    }

}
