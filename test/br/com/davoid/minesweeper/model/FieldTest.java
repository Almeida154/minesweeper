package br.com.davoid.minesweeper.model;

import br.com.davoid.minesweeper.exception.ExplosionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {
    private Field field;

    @BeforeEach
    void instanceField() {
        this.field = new Field(3, 3);
    }

    @Test
    void addValidNeighbors() {
        Field neighbor1 = new Field(2, 3); // Cross
        assertTrue(this.field.addNeighbor(neighbor1));

        Field neighbor2 = new Field(2, 2); // Diagonal
        assertTrue(this.field.addNeighbor(neighbor2));

        Field neighbor3 = new Field(1, 1); // Invalid because it is out of range
        assertFalse(this.field.addNeighbor(neighbor3));
    }

    @Test
    void toggleFieldCheck() {
        assertFalse(this.field.isChecked());

        this.field.toggleCheck();
        assertTrue(this.field.isChecked());

        this.field.toggleCheck();
        assertFalse(this.field.isChecked());
    }

    @Test
    void openField() {
        assertTrue(this.field.open());
        assertFalse(this.field.open()); // Can't open twice
    }

    @Test
    void openCheckedField() {
        this.field.toggleCheck();
        assertFalse(this.field.open());
    }

    @Test
    void openArmedField() {
        this.field.setArmed(true);
        assertThrows(ExplosionException.class, () -> {
            this.field.open();
        });
    }

    @Test
    void openFieldWithNeighbors() {
        Field someNeighbor = new Field(3, 4);
        Field neighborOfMyNeighbor = new Field(3, 5);
        someNeighbor.addNeighbor(neighborOfMyNeighbor);
        this.field.addNeighbor(someNeighbor);
        this.field.open();
        assertTrue(this.field.isOpened() && someNeighbor.isOpened() && neighborOfMyNeighbor.isOpened());
    }

    @Test
    void checkNeighborhoodBombs() {
        Field someNeighbor = new Field(3, 4);
        someNeighbor.setArmed(true);
        this.field.addNeighbor(someNeighbor);
        Long bombs = this.field.getNeighborhoodBombs();
        assertTrue(bombs == 1);
    }

    @Test
    void checkFieldReveled() {
        assertFalse(this.field.isDone());
        this.field.open();
        assertTrue(this.field.isDone());
    }

    @Test
    void checkFieldProtected() {
        assertFalse(this.field.isDone());
        this.field.setArmed(true);
        this.field.toggleCheck();
        assertTrue(this.field.isDone());
    }
}
