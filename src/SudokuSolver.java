

/**
 * A class containing static methods for solving a sudoku puzzle
 * @author Ben Cullivan
 */
public class SudokuSolver {

    /**
     * Attempts to solve the sudoku by modifying the input sudoku array
     * @param sudoku and array representing the sudoku to be solved
     * @return whether the sudoku was successfully solved
     */
    public static boolean solve(int[][] sudoku, int n) {
        // create a Grid to store the numbers of the sudoku for fast lookup times
        Grid grid = new Grid(sudoku, n);

        return false;
    }

    public static void main(String[] args) {

        // A zero on a sudoku represents an empty spot

        // initialize an easy test sudoku
        // https://www.websudoku.com/?level=1&set_id=2220966754
        int[][] easyTestSudoku = {
                {0, 7, 4, 0, 6, 0, 2, 1, 0},
                {0, 3, 6, 5, 1, 3, 0, 9, 0},
                {9, 0, 0, 0, 7, 0, 0, 8, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0},
                {0, 3, 0, 9, 8, 2, 0, 4, 0},
                {0, 0, 0, 0, 0, 7, 0, 0, 0},
                {0, 8, 0, 0, 5, 0, 0, 0, 9},
                {0, 6, 0, 3, 2, 8, 5, 7, 0},
                {0, 5, 2, 0, 9, 0, 4, 6, 0}
        };

        // initialize a hard test sudoku
        // https://www.websudoku.com/?level=4&set_id=4884744131
        int[][] hardTestSudoku = {
                {6, 0, 0, 0, 0, 0, 0, 3, 0},
                {3, 0, 0, 1, 0, 0, 4, 0, 0},
                {0, 2, 5, 0, 0, 3, 0, 6, 0},
                {0, 0, 0, 9, 0, 2, 0, 0, 0},
                {1, 0, 7, 0, 0, 0, 6, 0, 4},
                {0, 0, 0, 7, 0, 1, 0, 0, 0},
                {0, 6, 0, 3, 0, 0, 8, 1, 0},
                {0, 0, 8, 0, 0, 7, 0, 0, 6},
                {0, 4, 0, 0, 0, 0, 0, 0, 5}
        };

    }
}
