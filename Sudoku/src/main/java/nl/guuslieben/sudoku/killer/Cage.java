package nl.guuslieben.sudoku.killer;

import java.util.ArrayList;
import java.util.List;

public class Cage {

    private List<KillerCell> cells;
    private int total;

    public Cage() {
        this.cells = new ArrayList<>();
    }

    public List<KillerCell> getCells() {
        return this.cells;
    }

    public void setCells(List<KillerCell> cells) {
        this.cells = cells;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
