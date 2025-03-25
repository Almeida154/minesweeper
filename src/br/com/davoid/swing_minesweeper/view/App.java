package br.com.davoid.swing_minesweeper.view;

import br.com.davoid.swing_minesweeper.model.Board;

import javax.swing.*;

public class App extends JFrame {
    App() {
        Board board = new Board(16, 30, 50);
        add(new BoardPanel(board));

        setTitle("David's Minesweeper");
        setSize(690, 428);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new App();
    }
}
