import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Stack;

/**
 * Subclass of JPanel
 * Stores and displays sub panels and labels for each number in a sudoku
 * @author Ben Cullivan
 */
public class SudokuGrid extends JPanel {

    /**
     * private constants
     */
    private final int N;
    private final int ROOT;
    private static final Color SELECTED_COLOR = new Color(255, 0, 0);
    private static final Font MAIN_FONT = new Font("SansSerif", Font.BOLD, 35);
    private static final Font SECONDARY_FONT = new Font("SansSerif", Font.BOLD, 15);
    private static final int DELAY = 2;
    private static final Color BACKGROUND_COLOR = new Color(238, 238, 238);

    private final PositionLabel[][] sudoku;
    private final int[][] unsolved;
    private final int[][] solved;
    private final HashSet<Character> numChars = new HashSet<>();
    private final SudokuGame parent;

    private PositionLabel selected;
    private Stack<StackItem> stack;
    private Timer timer;
    private ArrayList<int[]> unfilled;
    private int index;
    private HashGrid hGrid;
    private int[][] duplicate;

    public SudokuGrid(SudokuGame parent, int n, int root, int[][] unsolved) {
        // initialize this panel with a grid layout
        super(new GridLayout(root, root));

        // set the parent
        this.parent = parent;

        // initialize constants
        N = n;
        ROOT = root;

        // initialize the sudoku array
        sudoku = new PositionLabel[n][n];

        // add num chars to the hash set
        for (int i = 1; i <= N; i++) numChars.add(Character.forDigit(i, 10));

        // set the unsolved sudoku
        this.unsolved = unsolved;

        // solve the sudoku and set the solved sudoku
        int[][] solved = Arrays.stream(unsolved).map(int[]::clone).toArray(int[][]::new);
        if (!SudokuSolver.solve(solved, N)) throw new IllegalArgumentException("Unsolved sudoku not solvable.");
        this.solved = solved;

        // set the border of this label
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

        // initialize the grid
        initGrid();
    }

    /**
     * Initializes the sudoku grid
     */
    public void initGrid() {
        for (int i = 0; i < ROOT; i++) {
            for (int j = 0; j < ROOT; j++) {
                // create a sub-section which will be another JPanel
                JPanel section = new JPanel(new GridLayout(ROOT, ROOT));
                section.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
                int startA = i * ROOT;
                int startB = j * ROOT;
                for (int a = 0; a < ROOT; a++) {
                    for (int b = 0; b < ROOT; b++) {
                        // create a label
                        PositionLabel label = new PositionLabel(startA + a, startB + b);
                        label.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                        label.setOpaque(true);

                        // if this position contains one of the provided numbers, add it to the grid
                        if (unsolved[startA + a][startB + b] != 0) {
                            initNonSelectableLabel(label, String.valueOf(unsolved[startA + a][startB + b]));
                        } else {
                            initSelectableLabel(label);
                        }

                        // add the label to the sudoku grid
                        sudoku[startA + a][startB + b] = label;
                        // add the label to the section panel
                        section.add(label).setLocation(a, b);
                    }
                }
                // add the section to this panel
                add(section).setLocation(i, j);
            }
        }
    }

    /**
     * removes all numbers that have been input by the user
     */
    public void clearGrid() {
        // stop the timer if it is running
        if (timer != null && timer.isRunning()) timer.stop();
        // loop over the grid and clear all of the numbers that are not in the unsolved sudoku
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (unsolved[i][j] == 0) {
                    sudoku[i][j].setBackground(BACKGROUND_COLOR);
                    sudoku[i][j].setText("");
                    sudoku[i][j].setFont(SECONDARY_FONT);
                    sudoku[i][j].setHorizontalAlignment(SwingConstants.RIGHT);
                    sudoku[i][j].setVerticalAlignment(SwingConstants.TOP);
                }
            }
        }
    }

    /**
     * removes all numbers that have been input by the user and sets the font to the main font
     */
    private void clearGridSetFont() {
        // loop over the grid and set the font of all the numbers that are not in the unsolved sudoku
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (unsolved[i][j] == 0) {
                    sudoku[i][j].setBackground(BACKGROUND_COLOR);
                    sudoku[i][j].setText("");
                    sudoku[i][j].setFont(MAIN_FONT);
                    sudoku[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                    sudoku[i][j].setVerticalAlignment(SwingConstants.CENTER);
                }
            }
        }
    }

    /**
     * Initializes each label that contains a number in the unsolved sudoku
     *
     * @param label the label being initialized
     * @param text  the number to be placed on the label
     */
    private void initNonSelectableLabel(PositionLabel label, String text) {
        label.setText(text);
        label.setHorizontalAlignment(SwingUtilities.CENTER);
        label.setVerticalAlignment(SwingUtilities.CENTER);
        label.setFont(MAIN_FONT);
        label.setEnabled(false);
    }

    /**
     * initializes each label that does not contain a number in the unsolved sudoku
     *
     * @param label the label being initialized
     */
    private void initSelectableLabel(PositionLabel label) {
        label.setHorizontalAlignment(SwingUtilities.RIGHT);
        label.setVerticalAlignment(SwingUtilities.TOP);
        label.setFont(SECONDARY_FONT);
        label.setFocusable(true);
        label.setOpaque(true);
        label.addMouseListener(createMouseListener());
        label.addKeyListener(createKeyListener());
    }

    /**
     * initializes the array list of positions where the unsolved sudoku is not filled
     */
    private void initUnfilled() {
        unfilled = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (unsolved[i][j] == 0) unfilled.add(new int[]{i, j});
            }
        }
    }

    /**
     * @return a mouse listener that is called when the user presses on a square of the sudoku
     */
    private MouseListener createMouseListener() {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // do nothing
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // if there is an element that is already selected, restore its border to the normal border
                if (selected != null) selected.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
                // set that this label is now selected
                selected = (PositionLabel) e.getComponent();
                // set the border of this label to show that it is selected
                selected.setBorder(BorderFactory.createLineBorder(SELECTED_COLOR, 1));
                // request focus so that this label can receive keyboard events
                selected.requestFocus();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // do nothing
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // do nothing
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // do nothing
            }
        };
    }

    /**
     * @return a key listener that is called when the user types a key (only if the user has selected a sudoku square)
     */
    private KeyListener createKeyListener() {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                System.out.println("typed");
                // if they typed a number, set the text of this label
                if (!e.isActionKey() && selected != null && numChars.contains(e.getKeyChar())) {
                    selected.setText(String.valueOf(e.getKeyChar()));
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("pressed");
                if (e.getKeyCode() == KeyEvent.VK_ENTER && selected != null && !selected.getText().equals("")) {
                    // get the number that is on the label
                    int guess = Integer.parseInt(selected.getText());
                    // if the user is correct, set this number
                    if (guess == solved[selected.getRow()][selected.getColumn()]) {
                        selected.setHorizontalAlignment(SwingUtilities.CENTER);
                        selected.setVerticalAlignment(SwingUtilities.CENTER);
                        selected.setFont(MAIN_FONT);
                    }
                    // if the user is wrong, add a strike and clear the number
                    else {
                        parent.addStrike();
                        selected.setText("");
                    }
                }
                // if the user pressed the backspace key (the delete key on mac)
                else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE && selected != null) {
                    selected.setText("");
                    selected.setHorizontalAlignment(SwingUtilities.RIGHT);
                    selected.setVerticalAlignment(SwingUtilities.TOP);
                    selected.setFont(SECONDARY_FONT);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // do nothing
            }
        };
    }

    /**
     * Attempts to solve the sudoku by modifying the unsolved array
     */
    public void solve() {
        // create a duplicate of the unsolved array
        duplicate = Arrays.stream(unsolved).map(int[]::clone).toArray(int[][]::new);
        // clear the grid
        clearGridSetFont();
        // create a HashGrid to store the numbers of the sudoku for constant lookup times
        hGrid = new HashGrid(duplicate, N);
        // create the stack, save the unfilled positions, and start the timer
        initUnfilled();
        stack = new Stack<>();
        stack.push(new StackItem(0, unfilled.get(0)[0], unfilled.get(0)[1]));
        index = 0;
        timer = new Timer(DELAY, e -> backtrack());
        timer.start();
    }

    public void backtrack() {
        // if the stack is empty or we have solved the sudoku, stop the timer
        if (stack.empty() || index >= unfilled.size()) {
            timer.stop();
            return;
        }
        // get the top item
        StackItem current = stack.peek();
        int row = current.getRow();
        int column = current.getColumn();

        // if the this number has been added, remove it from the grid before trying to add this one
        if (current.getAdded()) {
            duplicate[row][column] = 0;
            hGrid.remove(current.getNum(), row, column);
            sudoku[row][column].setText("");
            sudoku[row][column].setBackground(BACKGROUND_COLOR);
            current.setAdded(false);
        }

        // increment this number
        current.setNum(current.getNum() + 1);

        // if the current's number is greater than 9, backtrack
        if (current.getNum() > 9) {
            sudoku[row][column].setText("");
            sudoku[row][column].setBackground(BACKGROUND_COLOR);
            index--;
            stack.pop();
            return;
        }

        // set the current number on the gui
        sudoku[row][column].setText(String.valueOf(current.getNum()));
        // set the color to show it is solving
        sudoku[row][column].setBackground(Color.GREEN);

        // set this number on the grid
        if (hGrid.isValidPlacement(current.getNum(), row, column)) {
            duplicate[row][column] = current.getNum();
            hGrid.add(current.getNum(), row, column);
            current.setAdded(true);
            index++;
            if (index >= unfilled.size()) {
                timer.stop();
                return;
            }
            stack.push(new StackItem(0, unfilled.get(index)[0], unfilled.get(index)[1]));
        }
    }
}
