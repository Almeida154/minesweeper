package br.com.davoid.swing_minesweeper.view;

import br.com.davoid.swing_minesweeper.model.Field;
import br.com.davoid.swing_minesweeper.model.FieldEvent;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.function.BiConsumer;

public class FieldButton extends JButton implements BiConsumer<Field, FieldEvent>, MouseListener {
    private Field field;

    private final Color BG_DEFAULT = new Color(184, 184, 184);
    private final Color BG_CHECK = new Color(8, 179, 247);
    private final Color BG_EXPLODE = new Color(189, 66, 68);
    private final Color COLOR_GREEN = new Color(0, 100, 0);

    public FieldButton(Field field) {
        this.field = field;
        field.addObserver(this);

        addMouseListener(this);

        setOpaque(true);
        setBackground(BG_DEFAULT);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
    }


    private void applyDefaultStyle() {
        setBackground(BG_DEFAULT);
        setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        setText("");
    }

    private void applyOpenStyle() {
        setBorder(BorderFactory.createLineBorder(Color.GRAY));

        if (this.field.isArmed()) {
            setBackground(BG_EXPLODE);
            return;
        }

        setBackground(BG_DEFAULT);

        int neighborhoodBombs = (int) this.field.getNeighborhoodBombs();

        switch (neighborhoodBombs) {
            case 1:
                setForeground(COLOR_GREEN);
                break;
            case 2:
                setForeground(Color.BLUE);
                break;
            case 3:
                setForeground(Color.YELLOW);
                break;
            case 4:
            case 5:
            case 6:
                setForeground(Color.RED);
                break;
            default:
                setForeground(Color.PINK);
        }

        setText(neighborhoodBombs > 0 ? Integer.toString(neighborhoodBombs) : "");
    }

    private void applyCheckStyle() {
        setBackground(BG_CHECK);
        setForeground(Color.BLACK);
        setText("⚑");
    }

    private void applyExplodeStyle() {
        setBackground(BG_EXPLODE);
        setForeground(Color.WHITE);
        setText("☹︎");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            this.field.open();
            return;
        }
        this.field.toggleCheck();
    }

    @Override
    public void accept(Field field, FieldEvent fieldEvent) {
        switch (fieldEvent) {
            case OPEN:
                applyOpenStyle();
                break;
            case CHECK:
                applyCheckStyle();
                break;
            case EXPLODE:
                applyExplodeStyle();
                break;
            default:
                applyDefaultStyle();
        }

        SwingUtilities.invokeLater(() -> {
            repaint();
            revalidate();
        });
    }

    // UNUSED

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
