package nl.guuslieben.sudoku;

import nl.guuslieben.sudoku.regular.Cell;

import java.io.IOException;
import java.util.List;

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
public class SudokuPrinter <T extends AbstractSuduko<? extends AbstractCell>> {

    T target;
    /**
     * Print statistics about the sudoku , the time taken to complete and
     * combonations tried to complete
     * 
     * @param out
     *            Output stream
     */
    public void printStatistics(Appendable out) {
    }

    /**
     * Print representation of this Suduko puzzle to Appendable output a each
     * block will be separated by double line, otherwise cells will be separated
     * by single line
     * @throws IOException 
     * 
     */
    public void print(Appendable out) throws IOException {
        this.printTop(out);
        this.printDoubleVerticleLine(out);
        //noinspection unchecked
        List<Cell> board = (List<Cell>) this.target.getBoard();
        this.print(String.valueOf(board.get(0).getValue()), out);
        for (int i = 1; i < board.size(); i++) {
            // New Row
            if (i % AbstractSuduko.getRowSize()  == 0) {
                this.printDoubleVerticleLine(out);
                // Every 3rd row double line
                if (i % (AbstractSuduko.getRowSize() * 3) == 0) {
                    this.printDoubleHorizontalLine(out);
                } else {
                    this.printSingleHorizontalLine(out);
                }
                this.printDoubleVerticleLine(out);
            } else if (i % 3 == 0) {
                this.printDoubleVerticleLine(out);
            } else {
                this.printSingleVerticleLine(out);
            }
            this.print(String.valueOf(board.get(i).getValue()), out);
        }
        this.printDoubleVerticleLine(out);
        this.printBottom(out);
    }

    private void printTop(Appendable out) throws IOException {
        this.print(" ╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗ \n", out);
    }

    private void printBottom(Appendable out) throws IOException {
        this.print("\n ╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝ \n", out);
    }

    private void printSingleVerticleLine(Appendable out) throws IOException {
        this.print(" │ ", out);
    }

    private void printDoubleVerticleLine(Appendable out) throws IOException {
        this.print(" ║ ", out);
    }

    private void printDoubleHorizontalLine(Appendable out) throws IOException {
        this.print("\n ╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣ \n", out);
    }
    
    private void printSingleHorizontalLine(Appendable out) throws IOException {
        this.print("\n ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢ \n", out);
    }

    private void print(String value, Appendable out) throws IOException {
        out.append(value);
    }

    public void setTarget(T t) {
        this.target = t;
    }

  

}
