package br.com.davoid.minesweeper.model;

import br.com.davoid.minesweeper.exception.ExplosionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {
    private Board board;

    @BeforeEach
    void instanceField() {
        this.board = new Board(6, 6, 2);
    }

    @Test
    void openField() {
        this.board.disableBombs();
        Field field = this.board.getField(3, 3);
        this.board.openField(3, 3);
        assertTrue(field.isOpened());
    }

    @Test
    void openAllArmedFields() {
        this.board.disableBombs();
        Field field = this.board.getField(3, 3);
        field.setArmed(true);

        assertThrows(ExplosionException.class, () -> {
            this.board.openField(3, 3);

            assertTrue(this.board.getFields().stream()
                    .filter(f -> f.isArmed())
                    .allMatch(f -> f.isOpened()));
        });
    }

    @Test
    void toggleField() {
        Field field = this.board.getField(3, 3);
        this.board.toggleFieldCheck(3, 3);
        assertTrue(field.isChecked());
    }

    @Test
    void isGameCompleted() {
        this.board.disableBombs();
        Field field = this.board.getField(3, 3);
        this.board.toggleFieldCheck(3, 3);
        this.board.getFields().forEach(f -> f.setOpened(true));
        assertTrue(field.isDone());
    }

    @Test
    public void testToStringWith2x2Board() {
        String expected =
                "     1  2  3  4  5  6 \n" +
                        "—-——-——-——-——-——-——-—\n" +
                        "1 |  ?  ?  ?  ?  ?  ? \n" +
                        "2 |  ?  ?  ?  ?  ?  ? \n" +
                        "3 |  ?  ?  ?  ?  ?  ? \n" +
                        "4 |  ?  ?  ?  ?  ?  ? \n" +
                        "5 |  ?  ?  ?  ?  ?  ? \n" +
                        "6 |  ?  ?  ?  ?  ?  ? \n" +
                        "\nEnter 'exit' to leave.\n";

        assertEquals(expected, this.board.toString());

        this.board.toggleFieldCheck(3,3);

        String expected2 =
                "     1  2  3  4  5  6 \n" +
                        "—-——-——-——-——-——-——-—\n" +
                        "1 |  ?  ?  ?  ?  ?  ? \n" +
                        "2 |  ?  ?  ?  ?  ?  ? \n" +
                        "3 |  ?  ?  ⚑  ?  ?  ? \n" +
                        "4 |  ?  ?  ?  ?  ?  ? \n" +
                        "5 |  ?  ?  ?  ?  ?  ? \n" +
                        "6 |  ?  ?  ?  ?  ?  ? \n" +
                        "\n" +
                        "Enter 'exit' to leave.\n";

        assertEquals(expected2, this.board.toString());
    }

}
