package nq.solver;

import java.util.List;
import java.util.Stack;

import org.apache.commons.lang3.tuple.Pair;

import nq.solver.utils.CollisionsTracker;

public class BacktrackingSolver implements Solver {
    int boardSize;
    boolean enable3QueensInLineCheck = false;
    List<Integer> solution;

    CollisionsTracker collisionsTracker;

    public BacktrackingSolver(int boardSize, boolean enable3QueensInLineCheck) {
        this.boardSize = boardSize;
        this.enable3QueensInLineCheck = enable3QueensInLineCheck;

        this.collisionsTracker = new CollisionsTracker(boardSize, enable3QueensInLineCheck);
    }

    public List<Integer> solve() {
        var stack = new Stack<Integer>();

        // Initial position of first level / row.
        stack.push(0);
        this.collisionsTracker.recordDiagCollision(Pair.of(0, stack.size() - 1), +1);

        while (true) {
            // Check first if the queen at the latest/lowest row is in conflict with another.
            int top = stack.peek();
            int attacksAgainstTop = this.collisionsTracker.countDiagAttacksAgainst(Pair.of(top, stack.size() - 1))
                    + this.collisionsTracker.countCrossAttacksAgainst(stack, Pair.of(top, stack.size() - 1))
                    + this.collisionsTracker.countLineAttacksAgainst(stack, Pair.of(top, stack.size() - 1));

            if (attacksAgainstTop > 0) {
                // Pop until we can find a row where we can move the queen to the next column.
                while (stack.peek() == this.boardSize - 1) {
                    int prev = stack.peek();
                    this.collisionsTracker.recordDiagCollision(Pair.of(prev, stack.size() - 1), -1);
                    stack.pop();
                    if (stack.empty()) {
                        System.out.println("No solution found for N=" + this.boardSize);
                        return new Stack<Integer>();
                    }
                }

                // Move the lowest queen in the board to the next position.
                int prev = stack.peek();
                this.collisionsTracker.recordDiagCollision(Pair.of(prev, stack.size() - 1), -1);
                stack.pop();
                int next = prev + 1;
                stack.push(next);
                this.collisionsTracker.recordDiagCollision(Pair.of(next, stack.size() - 1), +1);
            } else {
                // Termination condition. If the latest placed queen is valid and at the end of the
                // board, then we found our solution.
                if (stack.size() == this.boardSize) {
                    break;
                }

                // Else, we push to the next row below and explore this branch.
                int next = 0;
                stack.push(next);
                this.collisionsTracker.recordDiagCollision(Pair.of(next, stack.size() - 1), +1);
            }
        }

        return stack;
    }
}
