package br.com.davoid.minesweeper.model;

import br.com.davoid.minesweeper.exception.ExplosionException;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int rowsQty = 24;
    private int columnsQty = 24;
    private int bombsQty = 10;

    private List<Field> fields = new ArrayList<>();

    public Board(int rowsQty, int columnsQty, int bombsQty) {
        this.rowsQty = rowsQty;
        this.columnsQty = columnsQty;
        this.bombsQty = bombsQty;

        this.generateFields();
        this.generateNeighborhood();
        this.sortBombs();
    }

    public void openField(int row, int column) {
        try {
            this.fields.parallelStream()
                    .filter(field -> field.getRow() == row - 1 && field.getColumn() == column - 1)
                    .findFirst()
                    .ifPresent(field -> field.open());
        } catch (ExplosionException e) {
            this.fields.stream().filter(field -> field.isArmed()).forEach(field -> field.setOpened(true));
            throw e;
        }
    }

    public void toggleFieldCheck(int row, int column) {
        this.fields.stream()
                .filter(field -> field.getRow() == row - 1 && field.getColumn() == column - 1)
                .findFirst()
                .ifPresent(field -> field.toggleCheck());
    }


    private void generateFields() {
        for (int row = 0; row < this.rowsQty; row++) {
            for (int column = 0; column < this.rowsQty; column++) {
                this.fields.add(new Field(row, column));
            }
        }
    }

    private void generateNeighborhood() {
        this.fields.stream().forEach(fieldA -> {
            this.fields.stream().forEach(fieldB -> fieldA.addNeighbor(fieldB));
        });
    }

    private void sortBombs() {
        int bombsAlreadyInGame = 0;

        while (bombsAlreadyInGame < this.bombsQty) {
            int random = (int) (Math.random() * this.fields.size());
            this.fields.get(random).setArmed(true);
            bombsAlreadyInGame = (int) this.fields.stream().filter(field -> field.isArmed()).count();
        }
    }

    public boolean isDone() {
        return this.fields.stream().allMatch(field -> field.isDone());
    }

    public void restart() {
        this.fields.forEach(field -> field.restore());
        this.sortBombs();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("    ");

        for (int column = 0; column < this.columnsQty; column++) {
            sb.append(" ");
            sb.append(column + 1);
            sb.append(" ");
        }

        sb.append("\n—-—");

        for (int column = 0; column < this.columnsQty; column++) {
            sb.append("—-—");
        }

        sb.append("\n");

        int i = 0;

        for (int row = 0; row < this.rowsQty; row++) {
            sb.append(row + 1 + " |");
            sb.append(" ");

            for (int column = 0; column < this.rowsQty; column++) {
                sb.append(" ");
                sb.append(this.fields.get(i++));
                sb.append(" ");
            }
            sb.append("\n");
        }

        sb.append("\nEnter 'exit' to leave.\n");

        return sb.toString();
    }
}
