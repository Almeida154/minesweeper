package br.com.davoid.swing_minesweeper.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Board implements BiConsumer<Field, FieldEvent> {
    private final int rowsQty;
    private final int columnsQty;
    private final int bombsQty;

    private List<Field> fields = new ArrayList<>();
    private Set<Consumer<Boolean>> observers = new HashSet<>();

    public Board(int rowsQty, int columnsQty, int bombsQty) {
        this.rowsQty = rowsQty;
        this.columnsQty = columnsQty;
        this.bombsQty = bombsQty;

        this.generateFields();
        this.generateNeighborhood();
        this.sortBombs();
    }

    public void addObserver(Consumer<Boolean> observer) {
        this.observers.add(observer);
    }

    private void notifyObservers(Boolean result) {
        this.observers.forEach(observer -> observer.accept(result));
    }

    public void openField(int row, int column) {
        this.fields.parallelStream()
                .filter(field -> field.getRow() == row - 1 && field.getColumn() == column - 1)
                .findFirst()
                .ifPresent(field -> field.open());
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
                Field field = new Field(row, column);
                field.addObserver(this);
                this.fields.add(field);
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

    public void disableBombs() {
        this.fields.stream().forEach(field -> field.setArmed(false));
    }

    public List<Field> getFields() {
        return fields;
    }

    public Field getField(int row, int column) {
        return this.getFields().stream()
                .filter(f -> f.getRow() == row - 1 && f.getColumn() == column - 1)
                .findFirst()
                .get();
    }

    public void showBombs() {
        this.fields.stream()
                .filter(field -> field.isArmed())
                .filter(field -> !field.isChecked())
                .forEach(field -> field.setOpened(true));
    }

    @Override
    public void accept(Field field, FieldEvent fieldEvent) {
        if (fieldEvent == FieldEvent.EXPLODE) {
            this.showBombs();
            this.notifyObservers(false);
        } else if (this.isDone()) {
            this.notifyObservers(true);
        }
    }

    public int getRowsQty() {
        return rowsQty;
    }

    public int getColumnsQty() {
        return columnsQty;
    }
}
