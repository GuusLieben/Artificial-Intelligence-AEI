package nl.guuslieben.sudoku.regular;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import nl.guuslieben.sudoku.AbstractSudoku;

public class Sudoku extends AbstractSudoku<Cell> {

    protected Sudoku() { }

    public static Sudoku create(String filepath) {
        Sudoku sudoku = new Sudoku();
        sudoku.populate(filepath);
        
        return sudoku;
    }

    protected void populate(String filepath) {
        List<List<Cell>> column = new ArrayList<>(9);
        List<List<Cell>> grid = new ArrayList<>(9);

        Scanner scan;
        try {
            scan = new Scanner(new File(filepath));
        } catch (FileNotFoundException e1) {
            throw new RuntimeException(e1);
        }

        for (int i = 0; i < 9; i++) {
            column.add(new ArrayList<>());
            grid.add(new ArrayList<>());
        }

        int gridIndex;
        for (int x = 0; x < 9; x++) {
            List<Cell> currentRow = new ArrayList<>();
            String[] nextRow = scan.nextLine().split(FILE_DELIMITER);
            for (int y = 0; y < 9; y++) {

                gridIndex = (y / 3) + ((x / 3) * 3);
                List<Cell> currentColumn = column.get(y);
                List<Cell> currentBlock = grid.get(gridIndex);

                int value = 0;
                try {
                    value = Integer.parseInt(nextRow[y]);
                } catch (NumberFormatException ignored) {
                }
                
                // Create a new square to represent this location on the board
                Cell s = new Cell(x, y, value);
                // Keep a pointer to squares local to this (row, local grid,
                // column)
                s.row(currentRow);
                s.column(currentColumn);
                s.block(currentBlock);

                // Add square to local row, column and grid
                currentRow.add(s);
                currentColumn.add(s);
                currentBlock.add(s);

                this.board.add(s);
            }
        }

        scan.close();
    }

    public long timeToSolve() {
        return this.timeToSolve;
    }

    public int attempts() {
        return this.combosTried;
    }


}

