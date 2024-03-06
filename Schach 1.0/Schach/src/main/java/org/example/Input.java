package org.example;

import pieces.Piece;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Input extends MouseAdapter {

    private Board board;
    private boolean isWhiteTurn = true;

    public Input(Board board) {
        this.board = board;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int col = e.getX() / board.titleSize;
        int row = e.getY() / board.titleSize;

        Piece pieceXY = board.getPiece(col, row);

        if (pieceXY != null) {
            if ((isWhiteTurn && pieceXY.isWhite) || (!isWhiteTurn && !pieceXY.isWhite)) {
                board.selectedPiece = pieceXY;
            }
        }

        board.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (board.selectedPiece != null) {
            board.selectedPiece.xPos = e.getX() - board.titleSize / 2;
            board.selectedPiece.yPos = e.getY() - board.titleSize / 2;
            board.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int col = e.getX() / board.titleSize;
        int row = e.getY() / board.titleSize;

        if (board.selectedPiece != null) {
            Move move = new Move(board, board.selectedPiece, col, row);

            if (board.isValidMove(move)) {
                board.makeMove(move);
                isWhiteTurn = !isWhiteTurn;
            } else {
                board.selectedPiece.xPos = board.selectedPiece.col * board.titleSize;
                board.selectedPiece.yPos = board.selectedPiece.row * board.titleSize;
            }
        }

        board.selectedPiece = null;
        board.repaint();
    }
}

