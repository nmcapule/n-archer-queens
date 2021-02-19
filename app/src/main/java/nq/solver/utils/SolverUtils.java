package nq.solver.utils;

import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class SolverUtils {
    public static double slope(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
        double dx = a.getLeft() - b.getLeft();
        double dy = a.getRight() - b.getRight();

        return dx / dy;
    }

    public static Pair<Integer, Integer> diagonalIndices(Pair<Integer, Integer> pos, int boardSize) {
        int x = pos.getLeft();
        int y = pos.getRight();

        int lrIndex = x - y + boardSize - 1;
        int rlIndex = x + y;

        return Pair.of(lrIndex, rlIndex);
    }

    public static void printChessboard(List<Integer> positions, int boardSize) {
        for (int i = 0; i < boardSize; i++) {
            int x = -1;
            if (i < positions.size()) {
                x = positions.get(i);
            }

            for (int j = 0; j < boardSize; j++) {
                if (j == x) {
                    System.out.print(" X");
                } else {
                    System.out.print(" .");
                }
            }
            System.out.println();
        }
    }
}
