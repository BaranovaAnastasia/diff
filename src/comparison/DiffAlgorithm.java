package comparison;

import java.util.List;
import java.util.Objects;

/**
 * Provides functionality for comparing 2 texts line by line.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 */
public class DiffAlgorithm {
    /**
     * Lines of old text and changes occurred to them.
     */
    private final Difference[] oldLines;
    /**
     * Lines of new text and changes occurred to them.
     */
    private final Difference[] newLines;

    /**
     * Position in texts while the algorithm is running.
     */
    private final DiffPosition position;

    /**
     * Creates a new DiffAlgorithm instance to compare passed texts.
     *
     * @param oldLines Lines of an old text.
     * @param newLines Lines of a new text.
     * @throws NullPointerException If any of the passed texts is null.
     */
    public DiffAlgorithm(List<String> oldLines, List<String> newLines) {
        Objects.requireNonNull(oldLines, "Cannot work with null list. oldLines was null.");
        Objects.requireNonNull(newLines, "Cannot work with null list. newLines was null.");

        this.oldLines = new Difference[oldLines.size()];
        this.newLines = new Difference[newLines.size()];

        for (int i = 0; i < oldLines.size(); i++) {
            this.oldLines[i] = new Difference(oldLines.get(i));
        }
        for (int i = 0; i < newLines.size(); i++) {
            this.newLines[i] = new Difference(newLines.get(i));
        }

        this.position = new DiffPosition();
    }

    /**
     * Finds a difference between texts, comparing them line by line.
     *
     * @return 2 arrays with lines of an old and a new texts (respectively) with changes occurred to them.
     */
    public Difference[][] getDiff() {
        var LCSTable = Utils.getLCSTable(oldLines, newLines);
        findEqual(oldLines.length, newLines.length, LCSTable);
        findNotEqual();

        return new Difference[][]{oldLines, newLines};
    }

    /**
     * Recursively finds all the unchanged lines of texts and sets EQUAL markers to those lines.
     */
    private void findEqual(int i, int j, int[][] LCSTable) {
        if (LCSTable[i][j] == 0) {
            return;
        }
        if (oldLines[i - 1].getLine().equals(newLines[j - 1].getLine())) {
            oldLines[i - 1].setMarker(DifferenceMarker.EQUAL);
            newLines[j - 1].setMarker(DifferenceMarker.EQUAL);
            findEqual(i - 1, j - 1, LCSTable);
            return;
        }
        if (LCSTable[i - 1][j] < LCSTable[i][j - 1]) {
            findEqual(i, j - 1, LCSTable);
            return;
        }
        findEqual(i - 1, j, LCSTable);
    }

    /**
     * Goes through all the changed lines deciding if they were inserted, deleted or changed.
     */
    private void findNotEqual() {
        for (int i = 0; i < oldLines.length; i++) {
            if (oldLines[i].getMarker() != null) {
                if (oldLines[i].getMarker() == DifferenceMarker.EQUAL) {
                    position.moveEPO();
                }
                continue;
            }

            position.findNextSPN();
            findMatch(i);

            // If changed line was not found, line is deleted.
            if (oldLines[i].getMarker() == null) {
                oldLines[i].setMarker(DifferenceMarker.DELETED);
            }
        }

        fillRemainingNew();
    }

    /**
     * For i-th line in an old text tries to find a line in a new text that can be
     * the same line but changed.
     *
     * @param i Index in an old text.
     */
    private void findMatch(int i) {
        for (int j = position.startPositionNew; j < newLines.length; j++) {
            // If the line equals to some other line in an old text, it (or any line after)
            // cannot be the changed version of an old i-th line.
            if (newLines[j].getMarker() == DifferenceMarker.EQUAL) {
                position.moveEPN();
                position.startPositionNew = j + 1;
                break;
            }

            // If the length of the longest common subsequence is at least 2/3 of each line (old and new)
            // lines can be considered as changed.
            int LCSLength = Utils
                    .getLCSTable(oldLines[i].getLine(), newLines[j].getLine())
                    [oldLines[i].getLine().length()][newLines[j].getLine().length()];
            if (LCSLength >= oldLines[i].getLine().length() / 1.5 &&
                    LCSLength >= newLines[j].getLine().length() / 1.5) {
                oldLines[i].setMarker(DifferenceMarker.CHANGED);
                newLines[j].setMarker(DifferenceMarker.CHANGED);
                position.startPositionNew = j + 1;
                break;
            }
        }
    }

    /**
     * Sets all the new lines that were not classified as EQUAL or CHANGED to INSERTED.
     */
    private void fillRemainingNew() {
        for (Difference newLine : newLines) {
            if (newLine.getMarker() != null) {
                continue;
            }
            newLine.setMarker(DifferenceMarker.INSERTED);
        }
    }


    /**
     * Describes a position in texts when finding diff.
     *
     * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
     */
    private class DiffPosition {
        /**
         * Amount of EQUALs met in an old text.
         */
        private int equalPositionOld = 0;
        /**
         * Amount of EQUALs met in a new text.
         */
        private int equalPositionNew = 0;

        /**
         * Position in a new text starting from which to search for a match.
         */
        private int startPositionNew = 0;

        /**
         * Increases the amount of EQUALs met in an old text.
         */
        public void moveEPO() {
            ++equalPositionOld;
        }

        /**
         * Increases the amount of EQUALs met in a new text.
         */
        public void moveEPN() {
            ++equalPositionNew;
        }

        /**
         * <p>Finds the next position in a new text starting from which to search for a match.</p>
         * <p>The next position is the position where marker is not yet set and
         * the amount of EQUALs met in a new text is not less the the amount of EQUALs met in an old one.</p>
         */
        public void findNextSPN() {
            while (startPositionNew < DiffAlgorithm.this.newLines.length &&
                    (DiffAlgorithm.this.newLines[startPositionNew].getMarker() != null ||
                            equalPositionNew < equalPositionOld)) {
                if (DiffAlgorithm.this.newLines[startPositionNew].getMarker() == DifferenceMarker.EQUAL) {
                    ++equalPositionNew;
                }
                ++startPositionNew;
            }
        }
    }
}
