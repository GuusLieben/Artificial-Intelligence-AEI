package nl.guuslieben.sudoku.killer;

import java.util.ArrayList;
import java.util.List;

public class Cage {

    private List<KillerCell> cells;
    private int total;

    public Cage() {
        this.cells = new ArrayList<>();
    }

    public List<KillerCell> cells() {
        return this.cells;
    }

    public void cells(List<KillerCell> cells) {
        this.cells = cells;
    }

    public int total() {
        return this.total;
    }

    public void total(int total) {
        this.total = total;
    }
}
