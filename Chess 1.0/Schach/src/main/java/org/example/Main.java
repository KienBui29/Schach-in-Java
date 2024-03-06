package org.example;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        JFrame jframe = new JFrame();
        jframe.getContentPane().setBackground(new Color(110, 110, 110));
        jframe.setLayout(new GridBagLayout());
        jframe.setMinimumSize(new Dimension(800, 800));
        jframe.setLocationRelativeTo(null);

        Board board = new Board();
        jframe.add(board);

        jframe.setVisible(true);

    }
}