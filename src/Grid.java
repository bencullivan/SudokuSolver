import java.util.ArrayList;
import java.util.HashSet;

/**
 * A class to store information of a sudoku grid in HashSets in order to utilize constant lookup time.
 */
public class Grid {

    /**
     * ArrayLists of the hashsets that will represent each row, column, and section
     */
    private final ArrayList<HashSet<Integer>> rowSets;
    private final ArrayList<HashSet<Integer>> columnSets;
    private final ArrayList<HashSet<Integer>> sectionSets;

    public Grid(int[][] sudoku, int n) {

        // make sure that the arguments passed in are in compliance with valid sudoku format
        if (n == 0 || sudoku.length == 0 || sudoku[0].length == 0 || sudoku.length != sudoku[0].length) {
            throw new IllegalArgumentException("The sudoku must have an equal number of rows and columns that " +
                    "is greater than 0.");
        }

        // initialize all of the array lists
        rowSets = new ArrayList<>(n);
        columnSets = new ArrayList<>(n);
        sectionSets = new ArrayList<>(n);

        // initialize all of the hash sets
        for (int i = 0; i < n; i++) {
            rowSets.add(new HashSet<>());
            columnSets.add(new HashSet<>());
            sectionSets.add(new HashSet<>());
        }

        // add all of the starting numbers of the sudoku to the hashset
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (sudoku[i][j] != 0) {
                    add(sudoku[i][j], i, j);
                }
            }
        }
    }

    /**
     * Adds a number to the hash set grid that stores the numbers of the sudoku
     * @param num the number to be added
     * @param row the row of the sudoku where the number will be added
     * @param column the column of the sudoku where the number will be added
     */
    public void add(int num, int row, int column) {
        // add the number to the corresponding row set
        rowSets.get(row).add(num);

        // add the number to the corresponding column set
        columnSets.get(column).add(num);

        // add the number to the corresponding section set
        sectionSets.get(toSectionNum(row, column)).add(num);
    }

    /**
     * Removes a number from the hash set grid
     * @param num the number to be removed
     * @param row the row of the sudoku where the number is
     * @param column the column of the sudoku where the number is
     */
    public void remove(int num, int row, int column) {
        // remove the number from its row set
        rowSets.get(row).remove(num);

        // remove the number from its column set
        columnSets.get(column).remove(num);

        // remove the number from its section set
        sectionSets.get(toSectionNum(row, column)).remove(num);
    }

    /**
     * @param num the number that may be placed
     * @param row the row number in the sudoku
     * @param column the column number in the sudoku
     * @return Whether this number can be placed at this location in accordance with Sudoku rules
     */
    public boolean isValidPlacement(int num, int row, int column) {
        return !rowSets.get(row).contains(num) && !columnSets.get(column).contains(num)
                && !sectionSets.get(toSectionNum(row, column)).contains(num);
    }

    /**
     * Converts a row number and a column number to the number of the section they correspond to
     * @param row the row number
     * @param column the column number
     * @return the section number
     */
    private int toSectionNum(int row, int column) {
        return row / 3 + 3 * (column / 3);
    }

    public static void main(String[] args) {
        Grid g = new Grid(new int[][] {{1, 2, 3}, {1, 2, 3}, {1, 2, 3}}, 3);
        boolean b = g.isValidPlacement(1, 0, 0);
        System.out.println(b);
    }
}
