package nl.guuslieben.sudoku.killer;

import java.util.List;

import nl.guuslieben.sudoku.AbstractCell;
import nl.guuslieben.sudoku.regular.Cell;

public class KillerCell extends Cell {

    private final Cage cage;

    public KillerCell(int x, int y, int value) {
        super(x, y, value);
        this.cage = new Cage();
    }

    public void total(int total) {
        this.cage.total(total);
    }

    public void cage(List<KillerCell> cage) {
        this.cage.cells(cage);
    }

    public Cage cage() {
        return this.cage;
    }

    @Override
    public boolean valid() {
        return this.validCage() && super.valid();
    }

    private boolean validCage() {
        return this.cage.cells().stream().anyMatch(c -> c.value() == 0)
                || this.cage.total() == this.cage.cells().stream().mapToInt(AbstractCell::value).sum();
    }

    @Override
    public boolean isFinal() {
        return false;
    }
}
