package test;

import comparison.DiffAlgorithm;
import comparison.DifferenceMarker;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiffTest {
    @Test
    void equalTest() {
        List<String> text1 = Arrays.asList("qwerty", "qwerty", "qwerty", "qwerty");
        List<String> text2 = Arrays.asList("qwerty", "qwerty", "qwerty", "qwerty");

        DiffAlgorithm diffAlgorithm = new DiffAlgorithm(text1, text2);
        var result = diffAlgorithm.getDiff();

        assertEquals(text1.size(), result[0].length);
        assertEquals(text2.size(), result[1].length);

        for (int i = 0; i < result[0].length; i++) {
            assertEquals(DifferenceMarker.EQUAL, result[0][i].getMarker());
            assertEquals("qwerty", result[0][i].getLine());
        }

        for (int i = 0; i < result[1].length; i++) {
            assertEquals(DifferenceMarker.EQUAL, result[1][i].getMarker());
            assertEquals("qwerty", result[1][i].getLine());
        }
    }

    @Test
    void deletedTestOne() {
        List<String> text1 = Arrays.asList("qwerty", "qwerty", "qwerty", "qwerty");
        List<String> text2 = Arrays.asList("qwerty", "qwerty", "qwerty");

        DiffAlgorithm diffAlgorithm = new DiffAlgorithm(text1, text2);
        var result = diffAlgorithm.getDiff();

        assertEquals(text1.size(), result[0].length);
        assertEquals(text2.size(), result[1].length);

        assertEquals(DifferenceMarker.DELETED, result[0][0].getMarker());
        assertEquals("qwerty", result[0][0].getLine());

        for (int i = 1; i < result[0].length; i++) {
            assertEquals(DifferenceMarker.EQUAL, result[0][i].getMarker());
            assertEquals("qwerty", result[0][i].getLine());
        }

        for (int i = 0; i < result[1].length; i++) {
            assertEquals(DifferenceMarker.EQUAL, result[1][i].getMarker());
            assertEquals("qwerty", result[1][i].getLine());
        }
    }

    @Test
    void deletedTestTwo() {
        List<String> text1 = Arrays.asList("qwerty", "qwerty", "some line", "qwerty");
        List<String> text2 = Arrays.asList("qwerty", "qwerty", "qwerty");

        DiffAlgorithm diffAlgorithm = new DiffAlgorithm(text1, text2);
        var result = diffAlgorithm.getDiff();

        assertEquals(text1.size(), result[0].length);
        assertEquals(text2.size(), result[1].length);

        for (int i = 0; i < result[0].length; i++) {
            if (result[0][i].getLine().equals("some line")) {
                assertEquals(DifferenceMarker.DELETED, result[0][i].getMarker());
            } else {
                assertEquals(DifferenceMarker.EQUAL, result[0][i].getMarker());
                assertEquals("qwerty", result[0][i].getLine());
            }
        }

        for (int i = 0; i < result[1].length; i++) {
            assertEquals(DifferenceMarker.EQUAL, result[1][i].getMarker());
            assertEquals("qwerty", result[1][i].getLine());
        }
    }

    @Test
    void insertedTestOne() {
        List<String> text1 = Arrays.asList("qwerty", "qwerty", "qwerty");
        List<String> text2 = Arrays.asList("qwerty", "qwerty", "qwerty", "qwerty");

        DiffAlgorithm diffAlgorithm = new DiffAlgorithm(text1, text2);
        var result = diffAlgorithm.getDiff();

        assertEquals(text1.size(), result[0].length);
        assertEquals(text2.size(), result[1].length);

        for (int i = 0; i < result[0].length; i++) {
            assertEquals(DifferenceMarker.EQUAL, result[0][i].getMarker());
            assertEquals("qwerty", result[0][i].getLine());
        }

        assertEquals(DifferenceMarker.INSERTED, result[1][0].getMarker());
        assertEquals("qwerty", result[1][0].getLine());

        for (int i = 1; i < result[1].length; i++) {
            assertEquals(DifferenceMarker.EQUAL, result[1][i].getMarker());
            assertEquals("qwerty", result[1][i].getLine());
        }
    }

    @Test
    void insertedTestTwo() {
        List<String> text1 = Arrays.asList("qwerty", "qwerty", "qwerty");
        List<String> text2 = Arrays.asList("qwerty", "some inserted text", "qwerty", "qwerty");

        DiffAlgorithm diffAlgorithm = new DiffAlgorithm(text1, text2);
        var result = diffAlgorithm.getDiff();

        assertEquals(text1.size(), result[0].length);
        assertEquals(text2.size(), result[1].length);

        for (int i = 0; i < result[0].length; i++) {
            assertEquals(DifferenceMarker.EQUAL, result[0][i].getMarker());
            assertEquals("qwerty", result[0][i].getLine());
        }

        for (int i = 0; i < result[1].length; i++) {
            if (result[1][i].getLine().equals("some inserted text")) {
                assertEquals(DifferenceMarker.INSERTED, result[1][i].getMarker());
            } else {
                assertEquals(DifferenceMarker.EQUAL, result[1][i].getMarker());
                assertEquals("qwerty", result[1][i].getLine());
            }
        }
    }

    @Test
    void deletedInsertedTestOne() {
        List<String> text1 = Arrays.asList("qwerty", "qwerty", "qwerty", "this line is deleted");
        List<String> text2 = Arrays.asList("qwerty", "some inserted text", "qwerty", "qwerty");

        DiffAlgorithm diffAlgorithm = new DiffAlgorithm(text1, text2);
        var result = diffAlgorithm.getDiff();

        assertEquals(text1.size(), result[0].length);
        assertEquals(text2.size(), result[1].length);

        for (int i = 0; i < result[0].length; i++) {
            if (result[0][i].getLine().equals("this line is deleted")) {
                assertEquals(DifferenceMarker.DELETED, result[0][i].getMarker());
            } else {
                assertEquals(DifferenceMarker.EQUAL, result[0][i].getMarker());
                assertEquals("qwerty", result[0][i].getLine());
            }
        }

        for (int i = 0; i < result[1].length; i++) {
            if (result[1][i].getLine().equals("some inserted text")) {
                assertEquals(DifferenceMarker.INSERTED, result[1][i].getMarker());
            } else {
                assertEquals(DifferenceMarker.EQUAL, result[1][i].getMarker());
                assertEquals("qwerty", result[1][i].getLine());
            }
        }
    }

    @Test
    void changedTest() {
        List<String> text1 = Arrays.asList("qwerty", "this line will be changed", "qwerty", "qwerty");
        List<String> text2 = Arrays.asList("qwerty", "this line was changed", "qwerty", "qwerty");

        DiffAlgorithm diffAlgorithm = new DiffAlgorithm(text1, text2);
        var result = diffAlgorithm.getDiff();

        assertEquals(text1.size(), result[0].length);
        assertEquals(text2.size(), result[1].length);

        for (int i = 0; i < result[0].length; i++) {
            if (result[0][i].getLine().equals("this line will be changed")) {
                assertEquals(DifferenceMarker.CHANGED, result[0][i].getMarker());
            } else {
                assertEquals(DifferenceMarker.EQUAL, result[0][i].getMarker());
                assertEquals("qwerty", result[0][i].getLine());
            }
        }

        for (int i = 0; i < result[1].length; i++) {
            if (result[1][i].getLine().equals("this line was changed")) {
                assertEquals(DifferenceMarker.CHANGED, result[1][i].getMarker());
            } else {
                assertEquals(DifferenceMarker.EQUAL, result[1][i].getMarker());
                assertEquals("qwerty", result[1][i].getLine());
            }
        }
    }

    @Test
    void diffTestOne() {
        List<String> text1 = Arrays.asList("some unchanged line", "this line will be changed", "qwerty", "some old line");
        List<String> text2 = Arrays.asList("some new line", "some unchanged line", "this line was changed", "qwerty");

        DiffAlgorithm diffAlgorithm = new DiffAlgorithm(text1, text2);
        var result = diffAlgorithm.getDiff();

        assertEquals(text1.size(), result[0].length);
        assertEquals(text2.size(), result[1].length);

        for (int i = 0; i < result[0].length; i++) {
            if (result[0][i].getLine().equals("this line will be changed")) {
                assertEquals(DifferenceMarker.CHANGED, result[0][i].getMarker());
            } else if (result[0][i].getLine().equals("some old line")) {
                assertEquals(DifferenceMarker.DELETED, result[0][i].getMarker());
            } else {
                assertEquals(DifferenceMarker.EQUAL, result[0][i].getMarker());
            }
        }

        for (int i = 0; i < result[1].length; i++) {
            if (result[1][i].getLine().equals("this line was changed")) {
                assertEquals(DifferenceMarker.CHANGED, result[1][i].getMarker());
            } else if (result[1][i].getLine().equals("some new line")) {
                assertEquals(DifferenceMarker.INSERTED, result[1][i].getMarker());
            } else {
                assertEquals(DifferenceMarker.EQUAL, result[1][i].getMarker());
            }
        }
    }

    @Test
    void diffTestTwo() {
        List<String> text1 = Arrays.asList("some unchanged line", "line to delete", "another unchanged line", "also a line to delete", "qwerty12");
        List<String> text2 = Arrays.asList("some new line", "some unchanged line", "another unchanged line", "qwerty");

        DiffAlgorithm diffAlgorithm = new DiffAlgorithm(text1, text2);
        var result = diffAlgorithm.getDiff();

        assertEquals(text1.size(), result[0].length);
        assertEquals(text2.size(), result[1].length);

        assertEquals(DifferenceMarker.EQUAL, result[0][0].getMarker());
        assertEquals(DifferenceMarker.EQUAL, result[1][1].getMarker());
        assertEquals("some unchanged line", result[0][0].getLine());
        assertEquals("some unchanged line", result[1][1].getLine());

        assertEquals(DifferenceMarker.INSERTED, result[1][0].getMarker());
        assertEquals("some new line", result[1][0].getLine());

        assertEquals(DifferenceMarker.DELETED, result[0][1].getMarker());
        assertEquals("line to delete", result[0][1].getLine());

        assertEquals(DifferenceMarker.EQUAL, result[0][2].getMarker());
        assertEquals(DifferenceMarker.EQUAL, result[1][2].getMarker());
        assertEquals("another unchanged line", result[0][2].getLine());
        assertEquals("another unchanged line", result[1][2].getLine());

        assertEquals(DifferenceMarker.DELETED, result[0][3].getMarker());
        assertEquals("also a line to delete", result[0][3].getLine());

        assertEquals(DifferenceMarker.CHANGED, result[0][4].getMarker());
        assertEquals(DifferenceMarker.CHANGED, result[1][3].getMarker());
        assertEquals("qwerty12", result[0][4].getLine());
        assertEquals("qwerty", result[1][3].getLine());
    }

    @Test
    void diffTestThree() {
        List<String> text1 = Arrays.asList("line to delete", "one", "also a line to delete", "two");
        List<String> text2 = Arrays.asList("some new line", "one", "two");

        DiffAlgorithm diffAlgorithm = new DiffAlgorithm(text1, text2);
        var result = diffAlgorithm.getDiff();

        assertEquals(text1.size(), result[0].length);
        assertEquals(text2.size(), result[1].length);

        assertEquals(DifferenceMarker.DELETED, result[0][0].getMarker());
        assertEquals("line to delete", result[0][0].getLine());

        assertEquals(DifferenceMarker.INSERTED, result[1][0].getMarker());
        assertEquals("some new line", result[1][0].getLine());

        assertEquals(DifferenceMarker.EQUAL, result[0][1].getMarker());
        assertEquals(DifferenceMarker.EQUAL, result[1][1].getMarker());
        assertEquals("one", result[0][1].getLine());
        assertEquals("one", result[1][1].getLine());

        assertEquals(DifferenceMarker.DELETED, result[0][2].getMarker());
        assertEquals("also a line to delete", result[0][2].getLine());

        assertEquals(DifferenceMarker.EQUAL, result[0][3].getMarker());
        assertEquals(DifferenceMarker.EQUAL, result[1][2].getMarker());
        assertEquals("two", result[0][3].getLine());
        assertEquals("two", result[1][2].getLine());
    }
}