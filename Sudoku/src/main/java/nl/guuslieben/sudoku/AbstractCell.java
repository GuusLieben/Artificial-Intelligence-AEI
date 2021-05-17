package nl.guuslieben.sudoku;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nl.guuslieben.sudoku.regular.Cell;

public abstract class AbstractCell {
    protected final int x;
    protected final int y;
    protected int value;
    protected List<? extends AbstractCell> row = new ArrayList<>();
    protected List<? extends AbstractCell> column = new ArrayList<>();
    protected List<? extends AbstractCell> block = new LinkedList<>();
    protected List<Integer> possibles = new ArrayList<>();

    public AbstractCell(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public List<? extends AbstractCell> grid() {
        return this.block;
    }

    public List<? extends AbstractCell> column() {
        return this.column;
    }

    public List<? extends AbstractCell> row() {
        return this.row;
    }

    public  boolean empty() {
        return this.value == 0;
    }
    
    // Some are final so we know that the value should not be changed
    public abstract boolean isFinal();
    
    public void row(List<? extends AbstractCell> row) {
        this.row = row;
    }

    public <E extends AbstractCell> void column(List<E> column) {
        this.column = column;
    }

    public <E extends AbstractCell> void block(List<E> grid) {
        this.block = grid;
    }

    public int value() {
        return this.value;
    }

    public void value(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + this.x +
                ", y=" + this.y +
                ", value=" + this.value +
                '}';
    }

    public abstract boolean valid();

    protected boolean valid(List<? extends AbstractCell> cells) {
        return cells.stream().filter(s -> !this.equals(s))
                .noneMatch(s -> s.value() == this.value);
    }

    protected boolean validBlock() {
       return this.valid(this.block);
    }

    protected boolean validColumn() {
        return this.valid(this.column);
    }

    protected boolean validRow() {
        return this.valid(this.row);
    }
    
    @SuppressWarnings("ClassReferencesSubclass")
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cell) {
            Cell other = (Cell) obj;
            return other.x == this.x && other.y == this.y;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.x;
        hash = 97 * hash + this.y;
        return hash;
    }

    public void possible(int i) {
        this.possibles.add(i);
    }

    public List<Integer> possibles() {
        return this.possibles;
    }
}
