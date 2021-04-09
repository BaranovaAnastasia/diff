package com.company;

import comparison.TextsComparator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Main class of texts comparator program.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class Main {
    /**
     * Finds difference between 2 texts and saves result to the output file.
     */
    public static void main(String[] args) throws IOException {
        if (!checkPaths(args)) {
            return;
        }

        TextsComparator tc = new TextsComparator(Path.of(args[0]), Path.of(args[1]));

        tc.saveDifferenceAsHTML(Path.of(args[2]));
    }

    /**
     * Checks if passed array of string contains 3 valid paths to 2 input and one output files.
     */
    private static boolean checkPaths(String[] paths) {
        if (paths == null || paths.length < 3) {
            System.out.println("Not enough input arguments. " +
                    "Please provide 3 paths: to an old text, to a new one, and output .HTML file.");
            return false;
        }

        if (!Files.exists(Path.of(paths[0]))) {
            System.out.println("File at \"" + paths[0] + "\" not found.");
            return false;
        }
        if (!Files.exists(Path.of(paths[1]))) {
            System.out.println("File at \"" + paths[1] + "\" not found.");
            return false;
        }
        if (!paths[2].toLowerCase().endsWith(".html")) {
            System.out.println("Please, provide .HTML file as an output file.");
            return false;
        }

        return true;
    }
}