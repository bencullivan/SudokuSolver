import java.util.Arrays;
import java.util.HashSet;

/**
 * A class containing static methods for solving a sudoku puzzle
 * @author Ben Cullivan
 */
public class SudokuSolver {

    /**
     * dimensions
     */
    public static final int N = 9;

    // ** A zero on a sudoku represents an empty spot **

    /**
     * initialize an easy test sudoku
     * https://www.websudoku.com/?level=1&set_id=2220966754
     */
    public static final int[][] EASY_TEST_SUDOKU = {
            {0, 7, 4, 0, 6, 0, 2, 1, 0},
            {0, 2, 6, 5, 1, 3, 0, 9, 0},
            {9, 0, 0, 0, 7, 0, 0, 8, 0},
            {0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 3, 0, 9, 8, 2, 0, 4, 0},
            {0, 0, 0, 0, 0, 7, 0, 0, 0},
            {0, 8, 0, 0, 5, 0, 0, 0, 9},
            {0, 6, 0, 3, 2, 8, 5, 7, 0},
            {0, 5, 2, 0, 9, 0, 4, 6, 0}
    };

    /**
     * initialize a hard test sudoku
     * https://www.websudoku.com/?level=4&set_id=4884744131
     */
    public static final int[][] HARD_TEST_SUDOKU = {
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

    /**
     * Attempts to solve the sudoku by modifying the input sudoku array
     * @param sudoku and array representing the sudoku to be solved
     * @return whether the sudoku was successfully solved
     */
    public static boolean solve(int[][] sudoku, int n) {
        // create a HashGrid to store the numbers of the sudoku for fast lookup times
        HashGrid hGrid = new HashGrid(sudoku, n);

        // start the recursive backtracking algorithm
        return backtrack(sudoku, hGrid, n, 0, 0);
    }

    /**
     * This method implements recursive backtracking in order to solve the sudoku
     * Time Complexity: O(9^(n*n)) where n is the number of rows and columns
     * Space complexity: O(n*n) where n is the number of rows and columns
     * @param sudoku the sudoku to be solved
     * @param hGrid the hash grid that keeps track of the numbers that have been placed
     * @param n the height and width of the sudoku which is nxn
     * @param row the current row number
     * @param column the current column number
     * @return whether the sudoku can be solved given the numbers that are currently in it
     */
    private static boolean backtrack(int[][] sudoku, HashGrid hGrid, int n, int row, int column) {
        // cases where numbers do not need to be checked for this row and column
        if (column >= n) return backtrack(sudoku, hGrid, n, row+1, 0);
        if (row >= n) return true;
        if (sudoku[row][column] != 0) return backtrack(sudoku, hGrid, n, row, column+1);

        // loop over the potential numbers 1-n
        for (int i = 1; i <= n; i++) {
            if (hGrid.isValidPlacement(i, row, column)) {
                // try adding this number to the grid
                hGrid.add(i, row, column);
                sudoku[row][column] = i;

                // check if we can continue with this placement
                // if we can, then return true
                if (backtrack(sudoku, hGrid, n, row, column+1)) return true;
                // if we cannot, then remove this number from the sudoku and grid and try the
                // next number in the next loop iteration
                else {
                    hGrid.remove(i, row, column);
                    sudoku[row][column] = 0;
                }
            }
        }
        return false;
    }

    public static void guiSolve(SudokuGrid sGrid, int[][] sudoku, int n) {

    }

    private static boolean guiBacktrack(SudokuGrid sGrid, int[][] sudoku, HashGrid hGrid, int n, int row, int column) {
        return false;
    }

    /**
     * Uses HashSets to verify that the each row, column, and section contains no duplicates
     * Time Complexity: O(n*n) where n is the number of rows and columns
     * Space Complexity: O(n) where n is the number of rows and columns
     * @param sudoku the sudoku to be verified
     * @param n the number of rows and columns in the sudoku
     * @return whether the solution is valid
     */
    public static boolean verifySudoku(int[][] sudoku, int n) {
        // make sure the board sie is valid
        if (n != 4 && n != 9 && n != 16) return false;

        // initialize the hash sets
        HashSet<Integer> rowSet = new HashSet<>();
        HashSet<Integer> columnSet = new HashSet<>();
        HashSet<Integer> sectionSet = new HashSet<>();

        // check the rows and columns to make sure there are no duplicates
        for (int i = 0; i < n; i++) {
            rowSet.clear();
            columnSet.clear();
            for (int j = 0; j < n; j++) {
                if (rowSet.contains(sudoku[i][j])) return false;
                if (columnSet.contains(sudoku[j][i])) return false;
                rowSet.add(sudoku[i][j]);
                columnSet.add(sudoku[j][i]);
            }
        }

        // check the sections to make sure there are no duplicates
        int root = (int) Math.sqrt(n);
        for (int i = 0; i < root; i++) {
            for (int j = 0; j < root; j++) {
                sectionSet.clear();
                int startA = root * i;
                int startB = root * j;
                for (int a = 0; a < root; a++) {
                    for (int b = 0; b < root; b++) {
                        if (sectionSet.contains(sudoku[startA+a][startB+b])) return false;
                        sectionSet.add(sudoku[startA+a][startB+b]);
                    }
                }
            }
        }

        return true;
    }

    /**
     * Prints the sudoku
     * @param sudoku the sudoku to be printed
     * @param n the number of rows and columns in the sudoku
     */
    public static void displaySudoku(int[][] sudoku, int n) {
        // print the top line of dashes
        for (int i = 0; i < 3 * n + 2; i++) {
            System.out.print('-');
        }
        System.out.print('\n');

        // print each row of the sudoku surrounded by pipes
        for (int i = 0; i < n; i++) {
            System.out.print("| ");
            for (int j = 0; j < n; j++) {
                if (j < n - 1) System.out.print(sudoku[i][j] + "  ");
                else System.out.print(sudoku[i][j] + " |\n");
            }
        }

        // print the bottom line of dashes
        for (int i = 0; i < 3 * n + 2; i++) {
            System.out.print('-');
        }
        System.out.print("\n");
    }

    public static void main(String[] args) {
        // display the initial easy sudoku
        System.out.println("\nEasy test sudoku before solving:");
        displaySudoku(EASY_TEST_SUDOKU, N);

        // create a copy of the easy test sudoku that will be solved
        int[][] easyToSolve = Arrays.stream(EASY_TEST_SUDOKU).map(int[]::clone).toArray(int[][]::new);

        // display the solved easy sudoku (if possible)
        System.out.println("\nEasy test sudoku after solving:");
        if (solve(easyToSolve, N)) displaySudoku(easyToSolve, N);
        else System.out.println("\nThis sudoku cannot be solved.");

        if (verifySudoku(easyToSolve, N)) System.out.println("Solution Verified ✅\n\n");
        else System.out.println("Incorrect Solution ❌\n\n");

        // display the initial hard sudoku
        System.out.println("Hard test sudoku before solving: ");
        displaySudoku(HARD_TEST_SUDOKU, N);

        // create a copy of the hard test sudoku that will be solved
        int[][] hardToSolve = Arrays.stream(HARD_TEST_SUDOKU).map(int[]::clone).toArray(int[][]::new);

        // display the solved hard sudoku (if possible)
        System.out.println("\nHard test sudoku after solving:");
        if (solve(hardToSolve, N)) displaySudoku(hardToSolve, N);
        else System.out.println("\nTHis sudoku cannot be solved.");

        if (verifySudoku(hardToSolve, N)) System.out.println("Solution Verified ✅\n\n");
        else System.out.println("Incorrect Solution ❌\n\n");
    }
}
