# Sudoku Solver

A Sudoku application built using Java that implements recursive backtracking to solve a Sudoku. Includes a GUI built using 
Swing that can be used to do a Sudoku and also includes a visual demonstration of the backtracking algorithm in action. 
The non GUI backtracking algorithm is written recursivly and the GUI backtracking algorithm is written iteratively. 

### Running Sudoku Solver

In order to run Sudoku Solver download the source code and open it in an IDE of your choice (I recommend 
[Intellij](https://www.jetbrains.com/idea/)). If you want to run it strictly for the purpose of testing the algorithm and do
not want to use the GUI, run src/SudokuSolver. If you want to use the GUI, run src/SudokuGame.

### Controls

Click on a spot on the sudoku in order to activate it. Then type a number to mark the spot with that number. 
If you want to enter that number to the grid, press enter. To delete a number, click on the spot in the grid where it is 
located and press backspace (delete on mac). To clear the grid, click "clear". At any point if you want to see the solution, click "solve" and witness the backtracking algorithm in progress. 
