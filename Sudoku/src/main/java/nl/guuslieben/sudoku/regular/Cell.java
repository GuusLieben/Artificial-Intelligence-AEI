package nl.guuslieben.sudoku.regular;

import nl.guuslieben.sudoku.AbstractCell;

public class Cell extends AbstractCell implements Comparable<Cell> {

    // If this value was specified when Square was initialized then isFinal is
    // true
    private boolean isFinal;

    public Cell(int x, int y, int value) {
        super(x, y, value);
        this.isFinal = value != 0;
    }

    public boolean set() {
        return this.value != 0;
    }

    @Override
    public boolean isFinal() {
        return this.isFinal;
    }

    public void isFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    @Override
    public boolean valid() {
        return this.validRow() && this.validColumn() && this.validBlock();
    }

    @Override
    public int compareTo(Cell c) {
        int yComp = Integer.compare(this.y, c.y());
        if (yComp == 0) {
            return Integer.compare(this.x, c.x());
        }
        return yComp;
    }

}
