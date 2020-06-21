import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Subclass of JFrame
 * Sets up the window so that the user can play sudoku
 */
public class SudokuGame extends JFrame {

    /**
     * private constants
     */
    private static final int WIDTH = 500;
    private static final int HEIGHT = 530;
    private static final int BOTTOM_HEIGHT = 30;
    private static final int N = 9;
    private static final int ROOT = 3;
    private static final String STRIKES_STRING = "Strikes: ";
    private static final Font BUTTON_FONT = new Font("SansSerif", Font.PLAIN, 15);
    private static final Font LABEL_FONT = new Font("SansSerif", Font.BOLD, 15);

    /**
     * children
     */
    private JPanel mainPanel;
    private SudokuGrid mainGrid;

    /**
     * the label that shows how many strikes the user has
     */
    private JLabel guessLabel;

    /**
     * the current number of strikes
     */
    private int numStrikes = 0;

    public SudokuGame() {
        super("Sudoku");
        initWindow();
    }

    /**
     * Initializes the window for playing a sudoku game
     */
    private void initWindow() {
        // set up the main panel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        // add a sudoku grid to the main panel
        mainGrid = new SudokuGrid(this, N, ROOT, SudokuSolver.EASY_TEST_SUDOKU);
        mainPanel.add(mainGrid);
        // add the bottom panel to the main panel
        initBottomPanel();
        // add the main panel to the this frame
        add(mainPanel);
        // set the size location and make the frame visible
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * sets up the bottom panel of the window
     */
    private void initBottomPanel() {
        // the bottom panel
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
        bottomPanel.setPreferredSize(new Dimension(WIDTH, BOTTOM_HEIGHT));

        // set up the solve button
        initSolveButton(bottomPanel);

        // set up the clear button
        initClearButton(bottomPanel);

        // set up the guess label
        initGuessLabel(bottomPanel);

        // add the bottom panel to the main panel
        mainPanel.add(bottomPanel);
    }

    private void initSolveButton(JPanel bottomPanel) {
        // create the solve button
        JButton solve = new JButton("Solve");
        solve.setFont(BUTTON_FONT);
        solve.setVerticalAlignment(SwingConstants.CENTER);
        solve.setHorizontalAlignment(SwingConstants.CENTER);
        solve.setFocusPainted(false);
        solve.addActionListener(createSolveListener());

        // add the solve button to the bottom panel
        bottomPanel.add(solve).setLocation(0, 0);
    }

    private void initClearButton(JPanel bottomPanel) {
        // create the clear button
        JButton clear = new JButton("Clear");
        clear.setFont(BUTTON_FONT);
        clear.setVerticalAlignment(SwingConstants.CENTER);
        clear.setHorizontalAlignment(SwingConstants.CENTER);
        clear.setFocusPainted(false);
        clear.addActionListener(createClearListener());

        // add the clear button to the bottom panel
        bottomPanel.add(clear).setLocation(0, 1);
    }

    private void initGuessLabel(JPanel bottomPanel) {
        // the text displaying wrong guesses
        guessLabel = new JLabel(STRIKES_STRING + numStrikes);
        guessLabel.setFont(LABEL_FONT);
        guessLabel.setHorizontalAlignment(SwingConstants.CENTER);
        guessLabel.setVerticalAlignment(SwingConstants.CENTER);

        // add the text to the bottom panel
        bottomPanel.add(guessLabel).setLocation(0, 2);
    }

    private ActionListener createSolveListener() {
        return e -> SudokuSolver.guiSolve(mainGrid,
                Arrays.stream(mainGrid.getUnsolved()).map(int[]::clone).toArray(int[][]::new), N);
    }

    private ActionListener createClearListener() {
        return e -> {
            // clear the sudoku grid and reset the strikes
            mainGrid.clearGrid();
            numStrikes = 0;
            guessLabel.setText(STRIKES_STRING + numStrikes);
        };
    }

    public void addStrike() {
        // if there have already been 5, clear the board and show the solve
        if (numStrikes >= 5) {
            numStrikes = 0;
            guessLabel.setText(STRIKES_STRING + numStrikes);
            SudokuSolver.guiSolve(mainGrid,
                    Arrays.stream(mainGrid.getUnsolved()).map(int[]::clone).toArray(int[][]::new), N);
        } else {
            numStrikes++;
            guessLabel.setText(STRIKES_STRING + numStrikes);
        }
    }

    public static void main(String[] args) {
        // start the game
        SwingUtilities.invokeLater(SudokuGame::new);
    }
}
