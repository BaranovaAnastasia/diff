package comparison;

import html.DiffPage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Provides functionality for comparing texts from two files and saving the result as HTML page.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class TextsComparator {
    /**
     * Path to the file with old text.
     */
    private final Path pathToOld;
    /**
     * Path to the file with new text.
     */
    private final Path pathToNew;

    /**
     * Found diff.
     */
    private Difference[][] diff;

    /**
     * Constructor creates a new comparator for comparing text from files at passed paths.
     *
     * @param pathToOld Path to the file with old text.
     * @param pathToNew Path to the file with new text.
     * @throws NullPointerException  If any of the passed paths is null.
     * @throws FileNotFoundException If file at any of the passed paths does not exist.
     */
    public TextsComparator(Path pathToOld, Path pathToNew) throws FileNotFoundException {
        Objects.requireNonNull(pathToOld, "Path to the input file cannot be null. Path to the old file was null.");
        Objects.requireNonNull(pathToOld, "Path to the input file cannot be null. Path to the new file was null.");

        if (!Files.exists(pathToOld)) {
            throw new FileNotFoundException("File " + pathToOld + " does not exist.");
        }
        if (!Files.exists(pathToNew)) {
            throw new FileNotFoundException("File " + pathToNew + " does not exist.");
        }

        this.pathToOld = pathToOld;
        this.pathToNew = pathToNew;
    }

    /**
     * Compares texts from files line by line. Note that empty or blank lines are ignored.
     *
     * @return Array of two arrays. First one contains lines and their markers showing
     * changes in the old file compared to the new one. Second array consists of lines and markers
     * showing changes in the new file compared to the old one.
     * Order of lines and markers in arrays correspond the order of lines in initial file.
     * @throws IOException If I/O errors occurs while reading input files.
     */
    public Difference[][] getDiff() throws IOException {
        if (diff != null) {
            return diff;
        }

        List<String> textOne = Utils.readLines(pathToOld);
        List<String> textTwo = Utils.readLines(pathToNew);

        diff = new DiffAlgorithm(textOne, textTwo).getDiff();
        return diff;
    }

    /**
     * Saves found differences as an HTML page.
     *
     * @param path Path to the output file.
     * @throws NullPointerException If passed path is null.
     * @throws IOException          If I/O errors occurs while reading input files or writing result.
     */
    public void saveDifferenceAsHTML(Path path) throws IOException {
        Objects.requireNonNull(path, "Path was null.");

        if (diff == null) {
            getDiff();
        }


        DiffPage diffPage = DiffPage.getBuilder()
                .addOld(diff[0])
                .addNew(diff[1])
                .build();

        diffPage.save(path);
    }
}
