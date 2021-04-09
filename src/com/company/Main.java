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

        Path outPath = args.length < 3 ? Path.of("diff.html") : Path.of(args[2]);
        tc.saveDifferenceAsHTML(outPath);
        System.out.println("The result is at: " + outPath.toAbsolutePath());
    }

    /**
     * Checks if passed array of string contains at least 2 valid paths to the input files
     * and, if 3rd path is present, it is a path to a .html output file.
     */
    private static boolean checkPaths(String[] paths) {
        if (paths == null || paths.length < 2) {
            System.out.println("Not enough input arguments. " +
                    "Please provide at least 2 paths: to an old text, to a new one." +
                    "Optionally, provide an output .HTML file.");
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
        if (paths.length >= 3 && !paths[2].toLowerCase().endsWith(".html")) {
            System.out.println("Please, provide .HTML file as an output file.");
            return false;
        }

        return true;
    }
}