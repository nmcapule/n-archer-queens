package nq.solver;

import java.util.List;

/** A generic interface for an n-queens solver. */
public interface Solver {

    /**
     * Returns a solution for the n-queens problem. The solution is a list of queen positions
     * per row in the board.
     * 
     * For example, this array: [2, 0, 3, 1] is equivalent to:
     * 
     *  ```
     *  . . x .
     *  x . . .
     *  . . . x
     *  . x . .
     *  ```
     * 
     * @return A list of queen positions for each of the row.
     */
    public List<Integer> solve();
}
