
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class MinesweeperGame extends JFrame {
    private int rows = 10;
    private int cols = 10;
    private int mineCount = 20;
    private JButton[][] buttons;
    private boolean[][] mines;
    private boolean[][] revealed;
    private int remainingCells;

    public MinesweeperGame() {
        initializeGame();
        setupUI();
    }

    private void initializeGame() {
        buttons = new JButton[rows][cols];
        mines = new boolean[rows][cols];
        revealed = new boolean[rows][cols];
        remainingCells = rows * cols - mineCount;

        // Initialize the game board with buttons
        setLayout(new GridLayout(rows, cols));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setMargin(new Insets(0, 0, 0, 0));
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (!revealed[row][col]) {
                            revealCell(row, col);
                        }
                    }
                });
                add(buttons[i][j]);
            }
        }

        // Randomly place mines on the game board
        placeMines();
    }

    private void setupUI() {
        setTitle("Minesweeper");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void placeMines() {
        Random rand = new Random();
        int minesPlaced = 0;

        while (minesPlaced < mineCount) {
            int randRow = rand.nextInt(rows);
            int randCol = rand.nextInt(cols);

            if (!mines[randRow][randCol]) {
                mines[randRow][randCol] = true;
                minesPlaced++;
            }
        }
    }

    private void revealCell(int row, int col) {
        if (mines[row][col]) {
            // Game over - player clicked on a mine
            buttons[row][col].setText("*");
            gameOver();
        } else {
            int count = countAdjacentMines(row, col);
            if (count == 0) {
                // Recursively reveal neighboring cells if there are no adjacent mines
                buttons[row][col].setText("");
                revealed[row][col] = true;
                remainingCells--;

                // Check and reveal neighbors
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int newRow = row + i;
                        int newCol = col + j;
                        if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && !revealed[newRow][newCol]) {
                            revealCell(newRow, newCol);
                        }
                    }
                }
            } else {
                // Show the count of adjacent mines
                buttons[row][col].setText(Integer.toString(count));
                revealed[row][col] = true;
                remainingCells--;
            }

            // Check for a win
            if (remainingCells == 0) {
                gameWin();
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int newRow = row + i;
                int newCol = col + j;
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && mines[newRow][newCol]) {
                    count++;
                }
            }
        }

        return count;
    }

    private void gameOver() {
        JOptionPane.showMessageDialog(this, "Game Over!");
        resetGame();
    }

    private void gameWin() {
        JOptionPane.showMessageDialog(this, "Congratulations! You won!");
        resetGame();
    }

    private void resetGame() {
        // Reset the game by creating a new instance of the MinesweeperGame class
        this.dispose();
        new MinesweeperGame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MinesweeperGame();
            }
        });
    }
}