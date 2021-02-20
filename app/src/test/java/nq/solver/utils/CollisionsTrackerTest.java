package nq.solver.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

public class CollisionsTrackerTest {
    static final List<Integer> INVALID_DIAGONAL_BOARD = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7);
    static final List<Integer> INVALID_DIAGONAL_BOARD_SMALL = Arrays.asList(0, 1, 2, 3);
    static final List<Integer> INVALID_REVERSE_BOARD = Arrays.asList(7, 6, 5, 4, 3, 2, 1, 0);
    static final List<Integer> INVALID_RANDOM_BOARD = Arrays.asList(2, 4, 6, 1, 5, 4, 3, 2);
    static final List<Integer> ALMOST_VALID_BOARD = Arrays.asList(5, 3, 0, 7, 4, 1, 6, 2);
    static final List<Integer> VALID_BOARD = Arrays.asList(5, 3, 0, 4, 7, 1, 6, 2);
    static final List<Integer> VALID_BOARD_SMALL = Arrays.asList(2, 0, 3, 1);

    @Test
    void initialization() {
        var positions = Arrays.asList(1, 2, 3, 0);
        var tracker = new CollisionsTracker(4, true, positions);
        assertEquals(tracker.countAllDiagAndLineCollisions(positions), 6);
    }

    @TestFactory
    Stream<DynamicTest> testCountAttacksAgainst() {
        class TestData {
            String name;
            int boardSize;
            boolean enable3QueensInLineCheck;
            List<Integer> positions;
            Pair<Integer, Integer> checkPosition;
            int expectedDiagAttacks;
            int expectedCrossAttacks;
            int expectedLineAttacks;
        }

        var tests = Arrays.asList(new TestData() {
            {
                name = "Check invalid small diagonal board, position (0,0)";
                boardSize = INVALID_DIAGONAL_BOARD_SMALL.size();
                enable3QueensInLineCheck = true;
                positions = INVALID_DIAGONAL_BOARD_SMALL;
                checkPosition = Pair.of(0, 0);
                expectedDiagAttacks = 3;
                expectedCrossAttacks = 0;
                expectedLineAttacks = 2;
            }
        }, new TestData() {
            {
                name = "Check invalid diagonal board, position (1,1), no 3-queens line check";
                boardSize = INVALID_DIAGONAL_BOARD.size();
                positions = INVALID_DIAGONAL_BOARD;
                checkPosition = Pair.of(1, 1);
                expectedDiagAttacks = 7;
                expectedCrossAttacks = 0;
                expectedLineAttacks = 0;
            }
        }, new TestData() {
            {
                name = "Check invalid random board, position (0,0)";
                boardSize = INVALID_RANDOM_BOARD.size();
                enable3QueensInLineCheck = true;
                positions = INVALID_RANDOM_BOARD;
                checkPosition = Pair.of(2, 0);
                expectedDiagAttacks = 0;
                expectedCrossAttacks = 1;
                expectedLineAttacks = 1;
            }
        }, new TestData() {
            {
                name = "Check valid board, position (5,0)";
                boardSize = VALID_BOARD.size();
                enable3QueensInLineCheck = true;
                positions = VALID_BOARD;
                checkPosition = Pair.of(5, 0);
                expectedDiagAttacks = 0;
                expectedCrossAttacks = 0;
                expectedLineAttacks = 0;
            }
        });

        return tests.stream().map(entry -> {
            return dynamicTest(entry.name, () -> {
                var tracker = new CollisionsTracker(entry.boardSize, entry.enable3QueensInLineCheck, entry.positions);
                assertEquals(entry.expectedDiagAttacks, tracker.countDiagAttacksAgainst(entry.checkPosition));
                assertEquals(entry.expectedCrossAttacks,
                        tracker.countCrossAttacksAgainst(entry.positions, entry.checkPosition));
                assertEquals(entry.expectedLineAttacks,
                        tracker.countLineAttacksAgainst(entry.positions, entry.checkPosition));
            });
        });
    }

    @TestFactory
    Stream<DynamicTest> testCountAllDiagAndLineCollisions() {
        class TestData {
            String name;
            int boardSize;
            boolean enable3QueensInLineCheck;
            List<Integer> positions;
            int expected;
        }

        var tests = Arrays.asList(new TestData() {
            {
                name = "Check invalid small diagonal board";
                boardSize = INVALID_DIAGONAL_BOARD_SMALL.size();
                enable3QueensInLineCheck = true;
                positions = INVALID_DIAGONAL_BOARD_SMALL;
                expected = 11;
            }
        }, new TestData() {
            {
                name = "Check invalid diagonal board, no 3-queens line check";
                boardSize = INVALID_DIAGONAL_BOARD.size();
                positions = INVALID_DIAGONAL_BOARD;
                expected = 7;
            }
        }, new TestData() {
            {
                name = "Check invalid reverse diagonal board";
                boardSize = INVALID_REVERSE_BOARD.size();
                enable3QueensInLineCheck = true;
                positions = INVALID_REVERSE_BOARD;
                expected = 55;
            }
        }, new TestData() {
            {
                name = "Check invalid random board";
                boardSize = INVALID_RANDOM_BOARD.size();
                enable3QueensInLineCheck = true;
                positions = INVALID_RANDOM_BOARD;
                expected = 14;
            }
        }, new TestData() {
            {
                name = "Check almost valid board";
                boardSize = ALMOST_VALID_BOARD.size();
                enable3QueensInLineCheck = true;
                positions = ALMOST_VALID_BOARD;
                expected = 4;
            }
        }, new TestData() {
            {
                name = "Check valid board";
                boardSize = VALID_BOARD.size();
                enable3QueensInLineCheck = true;
                positions = VALID_BOARD;
                expected = 0;
            }
        }, new TestData() {
            {
                name = "Check valid small board";
                boardSize = VALID_BOARD_SMALL.size();
                enable3QueensInLineCheck = true;
                positions = VALID_BOARD_SMALL;
                expected = 0;
            }
        });

        return tests.stream().map(entry -> {
            return dynamicTest(entry.name, () -> {
                var tracker = new CollisionsTracker(entry.boardSize, entry.enable3QueensInLineCheck, entry.positions);
                assertEquals(entry.expected, tracker.countAllDiagAndLineCollisions(entry.positions));
            });
        });
    }
}
