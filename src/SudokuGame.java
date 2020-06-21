import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

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
    private static final int BOTTOM_HEIGHT = 15;
    private static final int N = 9;
    private static final int ROOT = 3;

    /**
     * child panels
     */
    private JPanel mainPanel;
    private SudokuGrid mainGrid;

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
        // add the sudoku grid to the main panel
        mainGrid = new SudokuGrid(N, ROOT, SudokuSolver.EASY_TEST_SUDOKU);
        mainPanel.add(mainGrid);
        // add the bottom panel to the main panel
        initBottomPanel();
        // add the main panel to the this frame
        add(mainPanel);
        // add the key listener
        addKeyListener(createKeyListener());
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
        JPanel bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(WIDTH, BOTTOM_HEIGHT));
        // add the bottom panel to the main panel
        mainPanel.add(bottomPanel);
    }

    /**
     * creates a listener that is invoked when keys are pressed
     * @return the KeyListener object
     */
    private KeyListener createKeyListener() {
        return new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                mainGrid.keyTyped(e);
            }
            @Override
            public void keyPressed(KeyEvent e) {
                mainGrid.keyPressed(e);
            }
            @Override
            public void keyReleased(KeyEvent e) {}
        };
    }

    public static void main(String[] args) {
        // start the game
        SwingUtilities.invokeLater(SudokuGame::new);
    }
}
