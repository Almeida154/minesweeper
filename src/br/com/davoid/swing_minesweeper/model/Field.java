package br.com.davoid.swing_minesweeper.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;

public class Field {
    static private final int CROSS_DISTANCE = 1;
    static private final int DIAGONAL_DISTANCE = 2;
    private final int row;
    private final int column;

    private boolean isOpened = false;
    private boolean isArmed = false;
    private boolean isChecked = false;

    private List<Field> neighbors = new ArrayList<>();
    private Set<BiConsumer<Field, FieldEvent>> observers = new HashSet<>();

    Field(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public void addObserver(BiConsumer<Field, FieldEvent> observer) {
        this.observers.add(observer);
    }

    private void notifyObservers(FieldEvent event) {
        this.observers.forEach(observer -> observer.accept(this, event));
    }

    public boolean addNeighbor(Field neighbor) {
        boolean sameRow = this.getRow() == neighbor.getRow();
        boolean sameColumn = this.getColumn() == neighbor.getColumn();

        int rowDistance = Math.abs(this.getRow() - neighbor.getRow());
        int columnDistance = Math.abs(this.getColumn() - neighbor.getColumn());

        int distance = rowDistance + columnDistance;

        boolean isDiagonal = !sameRow && !sameColumn && distance == DIAGONAL_DISTANCE;
        boolean isCross = (sameRow || sameColumn) && distance == CROSS_DISTANCE;

        if (isDiagonal || isCross) {
            this.getNeighbors().add(neighbor);
            return true;
        }

        return false;
    }

    public void toggleCheck() {
        if (this.isOpened()) return;
        this.setChecked(!this.isChecked());
        if (this.isChecked()) this.notifyObservers(FieldEvent.CHECK);
        else this.notifyObservers(FieldEvent.UNCHECK);
    }

    public boolean open() {
        if (this.isChecked() || this.isOpened()) return false;

        if (this.isArmed()) {
            this.notifyObservers(FieldEvent.EXPLODE);
            return true;
        }

        this.setOpened(true);

        boolean isNeighborsSafe = this.getNeighborhoodBombs() == 0;
        if (isNeighborsSafe) this.getNeighbors().forEach(neighbor -> neighbor.open());

        return true;
    }

    public boolean isDone() {
        boolean wasReveled = this.isOpened() && !this.isArmed();
        boolean wasProtected = this.isChecked() && this.isArmed();
        return wasReveled || wasProtected;
    }

    public long getNeighborhoodBombs() {
        return this.getNeighbors().stream().filter(neighbor -> neighbor.isArmed()).count();
    }

    public void restore() {
        this.setArmed(false);
        this.setChecked(false);
        this.setOpened(false);
    }


    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
        if (opened) this.notifyObservers(FieldEvent.OPEN);
    }

    public boolean isArmed() {
        return isArmed;
    }

    public void setArmed(boolean armed) {
        isArmed = armed;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public List<Field> getNeighbors() {
        return neighbors;
    }
}
