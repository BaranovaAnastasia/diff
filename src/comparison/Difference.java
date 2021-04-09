package comparison;

import java.util.Objects;

/**
 * Describes a changed line of text. Contains a line and
 * a marker showing what exact kind of change occurred to it.
 *
 * @author <a href="mailto:aabaranova_3@edu.hse.ru">Anastasia Baranova</a>
 * @see DifferenceMarker
 */
public class Difference {
    /**
     * Changed line.
     */
    private final String line;
    /**
     * One of: EQUAL, INSERTED, DELETED, CHANGED.
     */
    private DifferenceMarker marker;

    /**
     * Creates a new instance with passed line.
     *
     * @throws NullPointerException If passed line is null.
     */
    public Difference(String line) {
        Objects.requireNonNull(line, "Line cannot be null.");

        this.line = line;
    }

    /**
     * Returns this changed line.
     */
    public String getLine() {
        return line;
    }

    /**
     * Returns difference marker showing what kind of change occurred to the line.
     */
    public DifferenceMarker getMarker() {
        return marker;
    }

    /**
     * Sets difference marker showing what kind of change occurred to the line.
     */
    public void setMarker(DifferenceMarker marker) {
        this.marker = marker;
    }
}
