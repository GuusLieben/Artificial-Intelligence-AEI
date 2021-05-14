package nl.guuslieben.sudoku;

import java.io.IOException;
import java.util.List;

import nl.guuslieben.sudoku.regular.Cell;

/**
 * Print sudoku to output using Unicode8 characters to represent board, board will 
 * take format of :
 * <pre>
 *  ╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗ 
 *  ║ 1 │ 5 │ 8 ║ 9 │ 2 │ 7 ║ 6 │ 3 │ 4 ║ 
 *  ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢ 
 *  ║ 9 │ 7 │ 6 ║ 1 │ 3 │ 4 ║ 8 │ 5 │ 2 ║ 
 *  ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢ 
 *  ║ 3 │ 2 │ 4 ║ 6 │ 8 │ 5 ║ 7 │ 1 │ 9 ║ 
 *  ╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣ 
 *  ║ 4 │ 6 │ 1 ║ 8 │ 7 │ 9 ║ 3 │ 2 │ 5 ║ 
 *  ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢ 
 *  ║ 5 │ 8 │ 3 ║ 2 │ 4 │ 6 ║ 9 │ 7 │ 1 ║ 
 *  ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢ 
 *  ║ 2 │ 9 │ 7 ║ 3 │ 5 │ 1 ║ 4 │ 8 │ 6 ║ 
 *  ╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣ 
 *  ║ 7 │ 1 │ 2 ║ 4 │ 9 │ 8 ║ 5 │ 6 │ 3 ║ 
 *  ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢ 
 *  ║ 8 │ 3 │ 9 ║ 5 │ 6 │ 2 ║ 1 │ 4 │ 7 ║ 
 *  ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢ 
 *  ║ 6 │ 4 │ 5 ║ 7 │ 1 │ 3 ║ 2 │ 9 │ 8 ║ 
 *  ╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝ 
 *  </pre>
 * @author Matt
 *
 */
public class SudokuPrinter <T extends AbstractSudoku<? extends AbstractCell>> {

    T target;

    public void print(Appendable out) throws IOException {
        this.top(out);
        this.doubleVertical(out);
        //noinspection unchecked
        List<Cell> board = (List<Cell>) this.target.board();
        this.print(String.valueOf(board.get(0).value()), out);
        for (int i = 1; i < board.size(); i++) {
            // New Row
            if (i % AbstractSudoku.rowSize()  == 0) {
                this.doubleVertical(out);
                // Every 3rd row double line
                if (i % (AbstractSudoku.rowSize() * 3) == 0) {
                    this.doubleHorizontal(out);
                } else {
                    this.singleHorizontal(out);
                }
                this.doubleVertical(out);
            } else if (i % 3 == 0) {
                this.doubleVertical(out);
            } else {
                this.singleVertical(out);
            }
            this.print(String.valueOf(board.get(i).value()), out);
        }
        this.doubleVertical(out);
        this.bottom(out);
    }

    private void top(Appendable out) throws IOException {
        this.print(" ╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗ \n", out);
    }

    private void bottom(Appendable out) throws IOException {
        this.print("\n ╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝ \n", out);
    }

    private void singleVertical(Appendable out) throws IOException {
        this.print(" │ ", out);
    }

    private void doubleVertical(Appendable out) throws IOException {
        this.print(" ║ ", out);
    }

    private void doubleHorizontal(Appendable out) throws IOException {
        this.print("\n ╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣ \n", out);
    }
    
    private void singleHorizontal(Appendable out) throws IOException {
        this.print("\n ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢ \n", out);
    }

    private void print(String value, Appendable out) throws IOException {
        out.append(value);
    }

    public void target(T t) {
        this.target = t;
    }

  

}
