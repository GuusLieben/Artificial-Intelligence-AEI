package nl.guuslieben.sudoku;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

import nl.guuslieben.sudoku.killer.Killer;
import nl.guuslieben.sudoku.regular.Sudoku;

public class SudokuMain {

    public static void main(String[] args) throws IOException {
        final File lookup = new File("Sudoku/src/main/resources");
        final NumberFormat format = NumberFormat.getInstance();
        format.setGroupingUsed(true);

        for (File file : lookup.listFiles()) {
            if (file.isDirectory()) continue;

            Pair<Long, Double> notOptimised = solve(file, false);
            Pair<Long, Double> optimised = solve(file, true);
            System.out.println("Results:");
            System.out.println("- Not optimised: " + notOptimised.first/1000 + "s " + notOptimised. first%1000 + "ms and " + format.format(notOptimised.second.intValue()) + " attempts");
            System.out.println("- Optimised: " + optimised. first/1000 + "s " + optimised. first%1000 + "ms and " + format.format(optimised.second.intValue()) + " attempts");
            System.out.println("- Improvement: " + round(-(((optimised.second - notOptimised.second) / notOptimised.second) * 100)) + "%\n\n");
        }

        System.exit(0);
    }

    private static Pair<Long, Double> solve(File file, boolean optimised) throws IOException {
        AbstractSudoku<?> sudoku = file.getName().contains("killer")
                ? Killer.create(file, optimised)
                : Sudoku.create(file, optimised);
        System.out.println("Attempting to solve " + ((optimised) ? "with" : "without") + " optimisation: " + file.getName() + " ... ");
        sudoku.solve();
        if (optimised) {
            SudokuPrinter<AbstractSudoku<?>> printer = new SudokuPrinter<>();
            printer.target(sudoku);
            printer.print(System.out);
        }
        return new Pair<>(sudoku.timeToSolve, (double) sudoku.combosTried);
    }

    public static double round(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            return value;
        }

        BigDecimal decimal = BigDecimal.valueOf(value);
        decimal = decimal.setScale(2, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }
}
