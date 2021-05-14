package nl.guuslieben.sudoku;

import java.util.Scanner;

import nl.guuslieben.sudoku.killer.Killer;
import nl.guuslieben.sudoku.regular.Sudoku;

public class SudokuMain {

    private static final boolean killer = true;
    private static final boolean avans = true;

    private static final String file = "Sudoku/src/main/resources/" + (killer ? "killer" + (avans ? "_avans" : "") : "regular") + ".txt";

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        SudokuPrinter<AbstractSudoku<?>> printer = new SudokuPrinter<>();

        if (killer) killer(printer, scan);
        else regular(printer, scan);

        scan.close();
        System.exit(0);
    }

    private static void killer(SudokuPrinter<AbstractSudoku<?>> printer, Scanner scan) {
        try {
            Killer killer = Killer.create(file);
            usage();
            printer.target(killer);
            gui(killer, printer, scan);
        }
        catch (Exception e) {
            System.err.println("Error creating Sudoku");
            e.printStackTrace(System.err);
            System.exit(0);
        }
    }

    private static void regular(SudokuPrinter<AbstractSudoku<?>> printer, Scanner scan) {
        try {
            Sudoku sudoku = Sudoku.create(file);
            usage();
            printer.target(sudoku);
            gui(sudoku, printer, scan);
        }
        catch (Exception e) {
            System.err.println("Error creating Sudoku " + e.getMessage());
            System.exit(0);
        }
    }

    private static void usage() {
        System.out.println(
                "s : solve Sudoku. Prints true is solved or false otherwise"
        );
        System.out.println("p : print Sudoku");
        System.out.println("x : exit");
    }

    private static void gui(AbstractSudoku<?> sudoku, SudokuPrinter<?> printer, Scanner scan) {
        try {
            while (true) {
                System.out.print("$ ");
                String argument = scan.next();
                switch (argument) {
                    case "s":
                        long start = System.currentTimeMillis();
                        sudoku.solve();
                        long end = System.currentTimeMillis();
                        System.out.println("Solved: " + sudoku.complete() + " (took " + (end-start) + "ms)");
                        break;
                    case "p":
                        printer.print(System.out);
                        break;
                    case "x":
                        return;
                }
            }
        }
        catch (Exception e) {
            System.err.println("Error processing Sudoku : " + e.getMessage());
            System.exit(0);
        }
    }
}
