package br.com.davoid.swing_minesweeper.view;

import br.com.davoid.swing_minesweeper.model.Board;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {
    BoardPanel(Board board) {
        setLayout(new GridLayout(board.getRowsQty(), board.getColumnsQty()));
        board.getFields().forEach(field -> add(new FieldButton(field)));

        board.addObserver(hasWon -> {
            SwingUtilities.invokeLater(() -> {
                if (hasWon) {
                    JOptionPane.showMessageDialog(this, "Win!");
                } else {
                    JOptionPane.showMessageDialog(this, "Game over!");
                }

                board.restart();
            });
        });
    }
}
