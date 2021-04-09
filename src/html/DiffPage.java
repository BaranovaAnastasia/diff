package html;

import comparison.Difference;
import comparison.DifferenceMarker;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>Describes an HTML page displaying the difference between two texts.</p>
 * <p>Texts and changes are presented as a table where each cell contains
 * a line of text on a colored background (green if a line was added,
 * gray if it was deleted, and blue if it was changed).</p>
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class DiffPage {
    /**
     * URL of template HTML file.
     */
    private final InputStream templateHTML = DiffPage.class.getClassLoader().getResourceAsStream("diff.html");

    /**
     * Result text of HTML file.
     */
    private String HTML;

    /**
     * Saves HTML page to the file ar the passed path.
     *
     * @param path Path to the output file.
     * @throws NullPointerException If path is null.
     */
    public void save(Path path) {
        Objects.requireNonNull(path, "Path cannot be null.");

        try {
            Files.writeString(path, HTML);
        } catch (IOException e) {
            System.out.println("Cannot save the page.");
        }
    }

    /**
     * Private constructor to prevent instance creation without using Builder.
     */
    private DiffPage() {
    }

    /**
     * Returns a new builder of a page.
     */
    public static Builder getBuilder() {
        return new DiffPage().new Builder();
    }


    /**
     * Builder of a HTML page that displays the difference between two texts.
     *
     * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
     */
    public class Builder {
        /**
         * Private constructor to prevent direct builder creation.
         */
        private Builder() {
        }

        /**
         * Array of markers showing changes in an old text.
         */
        private DifferenceMarker[] oldMarkers;
        /**
         * Array of markers showing changes in a new text.
         */
        private DifferenceMarker[] newMarkers;

        /**
         * HTML-formatted strings containing old text lines inside td tags
         * with specified class based on what kind of changes occurred to the line.
         */
        private List<String> oldHTML;
        /**
         * HTML-formatted strings containing new text lines inside td tags
         * with specified class based on what kind of changes occurred to the line.
         */
        private List<String> newHTML;

        /**
         * HTML-formatted string containing all lines of both texts presented as a table cells.
         *
         * @see #oldHTML
         * @see #newHTML
         */
        private String HTMLTable;

        /**
         * Flag shows if old text has already been added to the page.
         */
        private boolean oldSet = false;
        /**
         * Flag shows if new text has already been added to the page.
         */
        private boolean newSet = false;

        /**
         * Adds lines of the text to the HTMl page.
         *
         * @param text  Lines of the text with changes occurred to them.
         * @param isOld True, if an old text should be added, false, if a new one.
         */
        private void setText(Difference[] text, boolean isOld) {
            var lines = isOld ? oldHTML : newHTML;
            var markers = isOld ? oldMarkers : newMarkers;

            for (int i = 0; i < text.length; i++) {
                String cls = text[i].getMarker() == DifferenceMarker.DELETED
                        ? "class = \"deleted\""
                        : text[i].getMarker() == DifferenceMarker.CHANGED
                        ? "class = \"changed\""
                        : text[i].getMarker() == DifferenceMarker.INSERTED
                        ? "class = \"inserted\""
                        : "";
                lines.add("<td " + cls + ">" + text[i].getLine() + "</td>\n");

                markers[i] = text[i].getMarker();
            }
        }

        /**
         * Adds lines of an old text to the HTMl page.
         *
         * @param text Lines of the text with changes occurred to them.
         * @throws NullPointerException If text is null.
         */
        public Builder addOld(Difference[] text) {
            Objects.requireNonNull(text);

            oldHTML = new ArrayList<>();
            oldMarkers = new DifferenceMarker[text.length];

            setText(text, true);
            oldSet = true;

            return this;
        }

        /**
         * Adds lines of a new text to the HTMl page.
         *
         * @param text Lines of the text with changes occurred to them.
         * @throws NullPointerException If text is null.
         */
        public Builder addNew(Difference[] text) {
            Objects.requireNonNull(text);

            newHTML = new ArrayList<>();
            newMarkers = new DifferenceMarker[text.length];

            setText(text, false);
            newSet = true;

            return this;
        }

        /**
         * Shows if lines in the passed positions in an old and a new text can be at the same row of the table.
         */
        private boolean canAddBoth(int oldIndex, int newIndex) {
            return newIndex < newMarkers.length && oldIndex < oldMarkers.length &&
                    (newMarkers[newIndex] == DifferenceMarker.CHANGED && oldMarkers[oldIndex] == DifferenceMarker.CHANGED
                            || newMarkers[newIndex] == DifferenceMarker.EQUAL && oldMarkers[oldIndex] == DifferenceMarker.EQUAL
                            || newMarkers[newIndex] == DifferenceMarker.INSERTED && oldMarkers[oldIndex] == DifferenceMarker.DELETED);
        }

        /**
         * Goes through all the lines and markers to build a result
         * table with both texts, an old and a new ones.
         *
         * @see #HTMLTable
         */
        private void assembleTable() {
            StringBuilder result = new StringBuilder();

            int oldIndex = 0;
            int newIndex = 0;

            while (oldIndex < oldMarkers.length || newIndex < newMarkers.length) {
                // Adding inserted lines of the new text.
                while (newIndex < newMarkers.length && newMarkers[newIndex] == DifferenceMarker.INSERTED) {
                    result.append("<tr>");
                    if (oldIndex < oldMarkers.length && oldMarkers[oldIndex] == DifferenceMarker.DELETED) {
                        result.append(oldHTML.get(oldIndex));
                        ++oldIndex;
                    } else {
                        result.append("<td></td>");
                    }
                    result.append(newHTML.get(newIndex)).append("</tr>\n");
                    ++newIndex;
                }

                // Adding deleted lines of the old text.
                while (oldIndex < oldMarkers.length && oldMarkers[oldIndex] == DifferenceMarker.DELETED) {
                    result.append("<tr>").append(oldHTML.get(oldIndex));
                    if (newIndex < newMarkers.length && newMarkers[newIndex] == DifferenceMarker.DELETED) {
                        result.append(newHTML.get(oldIndex));
                        ++newIndex;
                    } else {
                        result.append("<td></td>");
                    }
                    result.append("</tr>\n");
                    ++oldIndex;
                }

                // Adding lines of both texts to the same row.
                while (canAddBoth(oldIndex, newIndex)) {
                    result.append("<tr>").append(oldHTML.get(oldIndex)).append(newHTML.get(newIndex)).append("</tr>");
                    ++oldIndex;
                    ++newIndex;
                }
            }

            HTMLTable = result.toString();
        }

        /**
         * Creates a result HTML page using template HTML and created table with texts.
         *
         * @return HTML-formatted text of the result HTML page.
         * @throws IOException          If an I/O error occurs.
         * @throws NullPointerException If template file is not found.
         */
        private String buildHTML() throws IOException {
            if (templateHTML == null) {
                throw new NullPointerException("Cannot find template page.");
            }

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(templateHTML, StandardCharsets.UTF_8));
            String str;

            StringBuilder pageBuilder = new StringBuilder();

            while ((str = bufferedReader.readLine()) != null) {
                if (str.contains("${tableDataHere}")) {
                    pageBuilder.append(HTMLTable).append("\n");
                } else {
                    pageBuilder.append(str).append("\n");
                }
            }

            bufferedReader.close();

            return pageBuilder.toString();
        }

        /**
         * Builds a page.
         *
         * @return Built HTML page.
         * @see DiffPage
         */
        public DiffPage build() throws IOException {
            if (!oldSet || !newSet) {
                return null;
            }
            assembleTable();
            HTML = buildHTML();
            return DiffPage.this;
        }
    }
}
