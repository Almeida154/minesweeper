package br.com.davoid.minesweeper;

import br.com.davoid.minesweeper.model.Board;
import br.com.davoid.minesweeper.view.BoardConsole;

public class App {
    public static void main(String[] args) {
        Board board = new Board(6, 6, 2);
        BoardConsole bc = new BoardConsole(board);
    }
}
