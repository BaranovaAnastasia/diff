package comparison;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Utils {
    /**
     * Reads all lines from the file at the passed path.
     * Ignores empty and blank lines.
     *
     * @return List of read lines.
     * @throws NullPointerException  If passed path is null.
     * @throws FileNotFoundException If file at the passed path does not exist.
     * @throws IOException           If I/O errors occurs.
     */
    public static List<String> readLines(Path path) throws IOException {
        Objects.requireNonNull(path, "Path cannot be null.");

        if (!Files.exists(path)) {
            throw new FileNotFoundException("File " + path + " does not exist.");
        }

        List<String> lines = new ArrayList<>();

        BufferedReader bufferedReader = new BufferedReader(new FileReader(path.toFile()));
        String str;

        while ((str = bufferedReader.readLine()) != null) {
            if (!str.trim().isEmpty()) {
                lines.add(str.trim());
            }
        }

        bufferedReader.close();

        return lines;
    }

    /**
     * Finds an LCS table for 2 passed arrays.
     *
     * @return LCS table.
     * @throws NullPointerException If any of passed objects is null.
     */
    public static int[][] getLCSTable(Difference[] x, Difference[] y) {
        Objects.requireNonNull(x, "Cannot find LCS for null object.");
        Objects.requireNonNull(y, "Cannot find LCS for null object.");

        int[][] table = new int[x.length + 1][y.length + 1];

        for (int i = 1; i < x.length + 1; ++i) {
            for (int j = 1; j < y.length + 1; ++j) {
                table[i][j] = x[i - 1].getLine().equals(y[j - 1].getLine())
                        ? table[i - 1][j - 1] + 1
                        : Math.max(table[i - 1][j], table[i][j - 1]);
            }
        }
        return table;
    }

    /**
     * Finds an LCS table for 2 passed strings.
     *
     * @return LCS table.
     * @throws NullPointerException If any of passed strings is null.
     */
    public static int[][] getLCSTable(String x, String y) {
        Objects.requireNonNull(x, "Cannot find LCS for null string.");
        Objects.requireNonNull(y, "Cannot find LCS for null string.");

        int[][] table = new int[x.length() + 1][y.length() + 1];

        for (int i = 1; i < x.length() + 1; ++i) {
            for (int j = 1; j < y.length() + 1; ++j) {
                table[i][j] = x.charAt(i - 1) == y.charAt(j - 1)
                        ? table[i - 1][j - 1] + 1
                        : Math.max(table[i - 1][j], table[i][j - 1]);
            }
        }
        return table;
    }
}
