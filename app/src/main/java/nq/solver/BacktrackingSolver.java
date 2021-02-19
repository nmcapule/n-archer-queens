package nq.solver;

import java.util.List;
import java.util.Stack;

import org.apache.commons.lang3.tuple.Pair;

import nq.solver.utils.QueensCollisionsTracker;
import nq.solver.utils.SolverUtils;

public class BacktrackingSolver {
    int boardSize;
    boolean enable3QueensInLineCheck = false;
    List<Integer> solution;

    QueensCollisionsTracker collisionsTracker;

    public BacktrackingSolver(int boardSize, boolean enable3QueensInLineCheck) {
        this.boardSize = boardSize;
        this.enable3QueensInLineCheck = enable3QueensInLineCheck;

        this.collisionsTracker = new QueensCollisionsTracker(boardSize, enable3QueensInLineCheck);
        this.collisionsTracker.recalculateAttacksTables();
    }

    public List<Integer> solve() {
        var stack = new Stack<Integer>();

        // Initial position of first level / row.
        stack.push(0);
        this.collisionsTracker.recordDiagCollision(Pair.of(0, stack.size() - 1), +1);

        while (true) {
            int top = stack.peek();
            if (this.collisionsTracker.countDiagAttacksAgainst(Pair.of(top, stack.size() - 1)) > 0
                    || this.collisionsTracker.countCrossAttacksAgainst(stack, Pair.of(top, stack.size() - 1)) > 0
                    || this.collisionsTracker.countLineAttacksAgainst(stack, Pair.of(top, stack.size() - 1)) > 0) {
                while (stack.peek() == this.boardSize - 1) {
                    int prev = stack.pop();
                    this.collisionsTracker.recordDiagCollision(Pair.of(prev, stack.size()), -1);
                    if (stack.empty()) {
                        System.out.println("No solution found for N=" + this.boardSize);
                        return new Stack<Integer>();
                    }
                }
                int prev = stack.pop();
                this.collisionsTracker.recordDiagCollision(Pair.of(prev, stack.size()), -1);

                int next = prev + 1;
                stack.push(next);
                this.collisionsTracker.recordDiagCollision(Pair.of(next, stack.size() - 1), +1);
            } else {
                if (stack.size() == this.boardSize) {
                    break;
                }
                int next = 0;
                stack.push(next);
                this.collisionsTracker.recordDiagCollision(Pair.of(next, stack.size() - 1), +1);
            }
        }

        return stack;
    }
}
