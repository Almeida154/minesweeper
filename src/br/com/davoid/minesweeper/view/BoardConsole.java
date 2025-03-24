package br.com.davoid.minesweeper.view;

import br.com.davoid.minesweeper.exception.ExitException;
import br.com.davoid.minesweeper.exception.ExplosionException;
import br.com.davoid.minesweeper.model.Board;

import java.util.Arrays;
import java.util.Scanner;

public class BoardConsole {
    static private final int OPEN_ACTION = 1;
    static private final int TOGGLE_CHECK_ACTION = 2;
    private Board board;
    private Scanner scanner = new Scanner(System.in);

    public BoardConsole(Board board) {
        this.board = board;
        run();
    }

    private void run() {
        try {
            this.renderHub();
        } catch (ExitException e) {
            System.out.println("Thank you for playing! 👋");
        } finally {
            this.scanner.close();
        }
    }

    private void renderHub() {
        boolean keepPlaying = true;

        while (keepPlaying) {
            this.renderConfig();
            System.out.println("Another game? (Y/n)");
            String answer = this.scanner.nextLine();
            if (answer.equalsIgnoreCase("n")) keepPlaying = false;
        }
    }

    private void renderConfig() {
        try {
            this.renderPlayCycle();
        } catch (ExplosionException e) {
            System.out.println("Game over! 💥");
        }
    }

    private void renderPlayCycle() {
        while (!this.board.isDone()) {
            System.out.println("\n" + this.board);

            int[] coordinates = this.insertCoordinates();
            int action = this.chooseAction();

            if (action == OPEN_ACTION) this.board.openField(coordinates[0], coordinates[1]);
            if (action == TOGGLE_CHECK_ACTION) this.board.toggleFieldCheck(coordinates[0], coordinates[1]);
        }
    }

    private int[] insertCoordinates() {
        String answer = "";

        while (true) {
            System.out.print("Enter two numbers separated by comma (e.g., 1,2): ");
            answer = this.scanner.nextLine().trim();
            if (answer.matches("\\d+,\\d+")) break;
            if (answer.equalsIgnoreCase("Exit")) throw new ExitException();
        }

        return Arrays.stream(answer.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    private int chooseAction() {
        int action = 0;

        while (action != OPEN_ACTION && action != TOGGLE_CHECK_ACTION) {
            System.out.print("Choose an action [" + OPEN_ACTION + " - OPEN | " + TOGGLE_CHECK_ACTION + " - (UN)CHECK]: ");
            String answer = this.scanner.nextLine();
            if (answer.chars().allMatch(Character::isDigit)) action = Integer.parseInt(answer.trim());
        }

        return action;
    }
}
