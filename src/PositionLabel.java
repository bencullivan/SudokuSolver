import javax.swing.*;

/**
 * Subclass of JLabel
 * Stores row and column instance variables to keep track of the location of the label in the sudoku grid
 * @author Ben Cullivan
 */
public class PositionLabel extends JLabel {

    /**
     * instance variables to hold the position of this label in the sudoku grid
     */
    private final int row;
    private final int column;

    public PositionLabel(int row, int column) {
        super();
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }
}
