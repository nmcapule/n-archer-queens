package nq.solver;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import nq.solver.utils.CollisionsTracker;

public class BacktrackingSolverTest {
    @Test
    void testSolve() {
        for (int i = 4; i < 15; i++) {
            var solver = new BacktrackingSolver(i, false);
            var checker = new CollisionsTracker(i, false);
            var solution = solver.solve();
            int collisions = checker.countAllDiagAndLineCollisions(solution);
            assertTrue(collisions == 0,
                    String.format("Solver generated invlid solution w/ %d conflicts: %s", collisions, solution));
        }
    }

    @Test
    void testSolveWith3QueensInLineCheck() {
        for (int i = 8; i < 15; i++) {
            var solver = new BacktrackingSolver(i, true);
            var checker = new CollisionsTracker(i, true);
            var solution = solver.solve();
            int collisions = checker.countAllDiagAndLineCollisions(solution);
            assertTrue(collisions == 0,
                    String.format("Solver generated invlid solution w/ %d conflicts: %s", collisions, solution));
        }
    }
}
