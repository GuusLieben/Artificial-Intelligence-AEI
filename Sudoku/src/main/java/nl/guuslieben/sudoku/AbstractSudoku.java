package nl.guuslieben.sudoku;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class AbstractSudoku<E extends AbstractCell> {
    protected static final String FILE_DELIMITER = ",";
    
    private static final int ROW_SIZE = 9;
    private static final int ROW_COUNT = 9;

    protected final List<E> board;
    protected boolean isComplete;
    protected long timeToSolve;
    protected int combosTried;

    protected abstract void populate(String filePath);
    protected boolean optimised;

    public boolean complete() {
        return this.board.stream().allMatch(AbstractCell::valid);
    }

    public AbstractSudoku() {
        this.board = new ArrayList<>(81);
        this.timeToSolve = -1;
        this.isComplete = false;
        this.combosTried = 0;
        SudokuPrinter<AbstractSudoku<?>> printer = new SudokuPrinter<>();
        printer.target(this);
    }
    /**
     * Attempt to solve this Sudoku. Use <code>complete()</code> to check if
     * puzzle was solved.
     * This method attempts to solve the puzzle by trying all possible attempts
     * starting from the top left cell and advancing left to right returning the first
     * correct solution. Therefore there may be other solutions to the puzzle.
     */
    public void solve() {
        if (this.board.isEmpty()) {
            throw new RuntimeException("Must specify puzzle to solve");
        }
        if (this.isComplete) {
            return;
        }
        if (this.optimised) {
            for (E cell : this.board()) {
                if (!cell.isFinal()) {
                    for (int i = 1; i <= 9; i++) {
                        cell.value(i);
                        if (cell.valid()) cell.possible(i);
                        cell.value(0);
                    }
                }
            }
        }

        this.combosTried = 0;
        long start = System.currentTimeMillis();
        this.isComplete = this.optimised ? this.solveOptimised(0) : this.solve(0);
        this.timeToSolve = this.isComplete ? System.currentTimeMillis() - start : -1;
    }
    
    protected boolean solve(int index) {
        // Reached the last square on the board, check if puzzle complete
        if (index == this.board.size()) {
            return this.complete();
        }
        E s = this.board.get(index);
        // Do not alter 'final' values
        if (s.isFinal()) {
            return this.solve(index + 1);
        } else {
            // Attempt each number 1 - 9 for each square
            for (int i = 1; i <= 9; i++) {
                s.value(i);
                this.addAttempt();
                // If this is a valid value move on to the next square
                if (s.valid()) {
                    boolean done = this.solve(index + 1);
                    if (done) {
                        return true;
                    }
                }
            }
            // When all attempts fail for this square, reset and return
            s.value(0);
            return false;
        }
    }


    Random random = new Random();

    public boolean solveOptimised(int index) {
        // Reached the last square on the board, check if puzzle complete
        if (index == this.board().size()) {
            return this.complete();
        }
        E s = this.board().get(index);
        // Do not alter 'final' values
        if (s.isFinal()) {
            return this.solveOptimised(index + 1);
        } else {
            // Attempt each possible number for each square
            for (int i : s.possibles()) {
                if (!this.options(s).contains(i)) continue;

                s.value(i);
                this.addAttempt();
                // If this is a valid value move on to the next square
                if (s.valid()) {
                    boolean done = this.solveOptimised(index + 1);
                    if (done) {
                        return true;
                    }
                }
            }
            // When all attempts fail for this square, reset and return
            s.value(0);
            return false;
        }
    }

    public List<Integer> options(E cell) {
        if (cell.isFinal()) return new ArrayList<>();
        List<Integer> options = new ArrayList<>(Arrays.asList(1,2,3,4,5,6,7,8,9));
        options.removeAll(cell.row().stream().map(AbstractCell::value).collect(Collectors.toList()));
        options.removeAll(cell.column().stream().map(AbstractCell::value).collect(Collectors.toList()));
        return options;
    }

    public int count(List<? extends AbstractCell> cells, int i) {
        return (int) cells.stream().filter(v -> v.value() == i).count();
    }

    public static int rows() {
        return ROW_COUNT;
    }

    public static int rowSize() {
        return ROW_SIZE;
    }
    
    public List<E> board(){
        return Collections.unmodifiableList(this.board);
    }

    public void addAttempt() {
        this.combosTried++;
    }

    protected void optimised(boolean optimised) {
        this.optimised = optimised;
    }
}
