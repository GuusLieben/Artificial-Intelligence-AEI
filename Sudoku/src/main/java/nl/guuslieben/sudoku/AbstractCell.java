package nl.guuslieben.sudoku;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import nl.guuslieben.sudoku.regular.Cell;

public abstract class AbstractCell {
    // x, y coordinate of square, 0,0 is top left
    protected int x, y, value;
    // Local row
    protected List<? extends AbstractCell> row = new ArrayList<>();
    // Local column
    protected List<? extends AbstractCell> column = new ArrayList<>();
    // Local grid
    protected List<? extends AbstractCell> block = new LinkedList<>();

    public AbstractCell(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public List<? extends AbstractCell> getGrid() {
        return this.block;
    }

    public List<? extends AbstractCell> getColumn() {
        return this.column;
    }

    public List<? extends AbstractCell> getRow() {
        return this.row;
    }

    public  boolean isEmpty() {
        return this.value == 0;
    }
    
    // Some are final so we know that the value should not be changed
    public abstract boolean isFinal();
    
    public void setRow(List<? extends AbstractCell> row) {
        this.row = row;
    }

    public <E extends AbstractCell> void setColumn(List<E> column) {
        this.column = column;
    }

    public <E extends AbstractCell> void setBlock(List<E> grid) {
        this.block = grid;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                ", value=" + value +
                '}';
    }

    public abstract boolean isValid();

    protected boolean valid(List<? extends AbstractCell> cells) {
        return cells.stream().filter(s -> !this.equals(s))
                .noneMatch(s -> s.getValue() == this.value);
    }

    protected boolean isBlockValid() {
       return this.valid(this.block);
    }

    protected boolean isColumnValid() {
        return this.valid(this.column);
    }

    protected boolean isRowValid() {
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
}
