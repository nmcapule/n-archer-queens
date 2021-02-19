package nq.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import nq.solver.utils.QueensCollisionsTracker;

/**
 * Implements a gradient-based heuristic solver for the n-queens problem.
 * 
 * Based on: https://fizyka.umk.pl/~milosz/AlgIILab/10.1.1.57.4685.pdf
 */
public class GradientHeuristicSolver {
    int boardSize;
    boolean enable3QueensInLineCheck = false;
    List<Integer> solution;

    QueensCollisionsTracker collisionsTracker;

    public GradientHeuristicSolver(int boardSize, boolean enable3QueensInLineCheck) {
        this.boardSize = boardSize;
        this.enable3QueensInLineCheck = enable3QueensInLineCheck;

        // Init solution.
        this.solution = new ArrayList<Integer>(Collections.nCopies(this.boardSize, 0));
        for (int i = 0; i < this.boardSize; i++) {
            solution.set(i, i);
        }

        this.collisionsTracker = new QueensCollisionsTracker(boardSize, enable3QueensInLineCheck);
        this.collisionsTracker.recalculateAttacksTables(this.solution);
    }

    private Pair<Integer, Integer> positionOf(int index) {
        return Pair.of(this.solution.get(index), index);
    }

    private void swap(int qaIndex, int qbIndex) {
        // Clean the attack lookup table affected entries first.
        this.collisionsTracker.recordDiagCollision(this.positionOf(qaIndex), -1);
        this.collisionsTracker.recordDiagCollision(this.positionOf(qbIndex), -1);

        // Swap in the solution.
        int tmp = this.solution.get(qaIndex);
        this.solution.set(qaIndex, this.solution.get(qbIndex));
        this.solution.set(qbIndex, tmp);

        // Recalculate the attacks lookup table for the affected entries.
        this.collisionsTracker.recordDiagCollision(this.positionOf(qaIndex), +1);
        this.collisionsTracker.recordDiagCollision(this.positionOf(qbIndex), +1);
    }

    public List<Integer> solve() {
        while (true) {
            Collections.shuffle(this.solution);
            this.collisionsTracker.recalculateAttacksTables(this.solution);

            for (int i = 0; i < this.boardSize - 1; i++) {
                for (int j = i + 1; j < this.boardSize; j++) {

                    // Check if need to swap = if one of the queens are in conflict.
                    var qa = Pair.of(this.solution.get(i), i);
                    var qb = Pair.of(this.solution.get(j), j);
                    int beforeSwapAttacks = this.collisionsTracker.countDiagAttacksAgainst(qa)
                            + this.collisionsTracker.countDiagAttacksAgainst(qb)
                            + this.collisionsTracker.countLineAttacksAgainst(this.solution, qa)
                            + this.collisionsTracker.countLineAttacksAgainst(this.solution, qb);
                    if (beforeSwapAttacks == 0) {
                        continue;
                    }

                    // Try swapping and see if that reduces the number of attack conflicts.
                    this.swap(i, j);

                    qa = Pair.of(this.solution.get(i), i);
                    qb = Pair.of(this.solution.get(j), j);
                    int afterSwapAttacks = this.collisionsTracker.countDiagAttacksAgainst(qa)
                            + this.collisionsTracker.countDiagAttacksAgainst(qb)
                            + this.collisionsTracker.countLineAttacksAgainst(this.solution, qa)
                            + this.collisionsTracker.countLineAttacksAgainst(this.solution, qb);

                    // If the swap made it worse, revert.
                    if (afterSwapAttacks > beforeSwapAttacks) {
                        this.swap(i, j);
                        continue;
                    }
                }
            }

            if (this.collisionsTracker.countCollisions(this.solution) == 0) {
                break;
            }
        }

        return solution;
    }
}
