package nq.solver.utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class CollisionsTracker {
    int boardSize;

    /** 
     * Tracks the number of queens attacking a top-left to bottom-right diagonal (\). The indices
     * are labeled from left to right. For example, with N=4:
     * 
     *  ```
     *  3 4 5 6 
     *  2 \ . \ .
     *  1 . \ . \
     *  0 \ . \ .
     *    . \ . \
     *  ```
     */
    List<Integer> attacksDiagPositive;
    /** 
     * Tracks the number of queens attacking a top-right to bottom-left diagonal (/). The indices
     * are labeled from left to right. For example, with N=4:
     * 
     *  ```
     *    0 1 2 3
     *  . / . / 4
     *  / . / . 5
     *  . / . / 6
     *  / . / .
     *  ```
     */
    List<Integer> attacksDiagNegative;

    boolean enable3QueensInLineCheck;

    public CollisionsTracker(int boardSize, boolean enable3QueensInLineCheck, List<Integer> positions) {
        this.boardSize = boardSize;
        this.enable3QueensInLineCheck = enable3QueensInLineCheck;

        // Initialize diagonal attacks lookup arrays.
        this.attacksDiagPositive = new ArrayList<Integer>(Collections.nCopies(this.boardSize * 2 - 1, 0));
        this.attacksDiagNegative = new ArrayList<Integer>(Collections.nCopies(this.boardSize * 2 - 1, 0));

        // Initialize attack tables.
        this.recalculateAttacksTables(positions);
    }

    public CollisionsTracker(int boardSize, boolean enable3QueensInLineCheck) {
        this(boardSize, enable3QueensInLineCheck, new ArrayList<Integer>());
    }

    /** Given a list of positions, recalculate the diagonal attack lookup tables. */
    public void recalculateAttacksTables(List<Integer> positions) {
        Collections.fill(this.attacksDiagPositive, 0);
        Collections.fill(this.attacksDiagNegative, 0);

        for (int i = 0; i < positions.size(); i++) {
            int y = i;
            int x = positions.get(i);

            var pair = diagonalIndices(Pair.of(x, y));

            this.attacksDiagPositive.set(pair.getLeft(), this.attacksDiagPositive.get(pair.getLeft()) + 1);
            this.attacksDiagNegative.set(pair.getRight(), this.attacksDiagNegative.get(pair.getRight()) + 1);
        }
    }

    /** Given a position, modify the number of diagonal attacks on both sides in that position. */
    public void recordDiagCollision(Pair<Integer, Integer> position, int d) {
        var pair = diagonalIndices(position);
        this.attacksDiagPositive.set(pair.getLeft(), this.attacksDiagPositive.get(pair.getLeft()) + d);
        this.attacksDiagNegative.set(pair.getRight(), this.attacksDiagNegative.get(pair.getRight()) + d);
    }

    /** Count the number of diagonal queen attacks on the given position. */
    public int countDiagAttacksAgainst(Pair<Integer, Integer> position) {
        var pair = diagonalIndices(position);

        return (this.attacksDiagPositive.get(pair.getLeft()) - 1) + (this.attacksDiagNegative.get(pair.getRight()) - 1);
    }

    /** Count the number of crosswise queen attacks on the given position. */
    public int countCrossAttacksAgainst(List<Integer> solution, Pair<Integer, Integer> position) {
        int attacks = 0;

        for (int i = 0; i < solution.size(); i++) {
            if (solution.get(i) == position.getLeft() && i == position.getRight()) {
                continue;
            }

            if (solution.get(i) == position.getLeft()) {
                attacks += 1;
            }
            if (i == position.getRight()) {
                attacks += 1;
            }
        }

        return attacks;
    }

    /** Count the number of 3-queens that are on the same straight line from the given position. */
    public int countLineAttacksAgainst(List<Integer> solution, Pair<Integer, Integer> position) {
        if (!this.enable3QueensInLineCheck) {
            return 0;
        }

        int attacks = 0;

        var df = new DecimalFormat();
        df.setMaximumFractionDigits(4);

        var lineAttacksTable = new HashSet<String>();
        for (int i = 0; i < solution.size(); i++) {
            if (i == position.getRight()) {
                continue;
            }

            int oy = i;
            int ox = solution.get(i);

            var hash = df.format(Utils.slope(position, Pair.of(ox, oy)));
            if (lineAttacksTable.contains(hash)) {
                attacks += 1;
            }
            lineAttacksTable.add(hash);
        }

        return attacks;
    }

    /** Given a solution, count the number of diagonal and 3-queens-in-a-line collisions. */
    public int countAllDiagAndLineCollisions(List<Integer> solution) {
        int collisions = 0;

        for (int i = 0; i < this.boardSize * 2 - 1; i++) {
            int negative = this.attacksDiagNegative.get(i);
            if (negative > 1) {
                collisions += negative - 1;
            }
            int positive = this.attacksDiagPositive.get(i);
            if (positive > 1) {
                collisions += positive - 1;
            }
        }

        for (int i = 0; i < this.boardSize; i++) {
            collisions += this.countLineAttacksAgainst(solution, Pair.of(solution.get(i), i));
        }

        return collisions;
    }

    /**
     * Given a position pair (x, y), find its indices in the positive and negative diagonal
     * conflicts lookup table.
     * 
     * @return a pair of integers where left is positive, right is negative diagonal index
     */
    private Pair<Integer, Integer> diagonalIndices(Pair<Integer, Integer> pos) {
        int x = pos.getLeft();
        int y = pos.getRight();

        int lrIndex = x - y + this.boardSize - 1;
        int rlIndex = x + y;

        return Pair.of(lrIndex, rlIndex);
    }
}
