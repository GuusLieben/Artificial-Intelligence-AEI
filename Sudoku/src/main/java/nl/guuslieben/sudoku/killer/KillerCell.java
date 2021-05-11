package nl.guuslieben.sudoku.killer;

import java.util.List;

import nl.guuslieben.sudoku.AbstractCell;
import nl.guuslieben.sudoku.regular.Cell;

class KillerCell extends Cell {

    private final Cage cage;

    public KillerCell(int x, int y, int value) {
        super(x, y, value);
        this.cage = new Cage();
    }

    public void setTotal(int total) {
        this.cage.setTotal(total);
    }

    public void setCage(List<KillerCell> cage) {
        this.cage.setCells(cage);
    }

    @Override
    public boolean isValid() {
        return this.isCageValid() && super.isValid();
    }

    private boolean isCageValid() {
        return this.cage.getCells().stream().anyMatch(c -> c.getValue() == 0)
                || this.cage.getTotal() == this.cage.getCells().stream().mapToInt(AbstractCell::getValue).sum();
    }

    @Override
    public boolean isFinal() {
        return false;
    }


}
