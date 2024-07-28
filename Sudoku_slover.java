import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Sudoku_slover extends JFrame {
    private JLabel[][] gridLabels = new JLabel[9][9];
    private JPanel gridPanel = new JPanel(new GridLayout(9, 9));
    private JPanel buttonPanel = new JPanel(new GridLayout(5, 2));
    private JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
    private JPanel controlPanel = new JPanel(new BorderLayout());
    private JLabel titleLabel = new JLabel("Sudoku Solver", SwingConstants.CENTER);

    public Sudoku_slover() {
        setTitle("Sudoku Solver");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        buttonPanel.setBackground(new Color(121, 171, 252));
        mainPanel.setBackground(new Color(121, 171, 252));
        initGridPanel();
        initButtonPanel();

        mainPanel.add(gridPanel, BorderLayout.CENTER);
        controlPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(controlPanel, BorderLayout.EAST);
        add(mainPanel);

        setVisible(true);
    }

    private void initGridPanel() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JLabel label = new JLabel("", SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                label.setOpaque(true);
                label.setBackground(Color.WHITE);
                label.setFont(new Font("Arial", Font.PLAIN, 24));
                gridLabels[row][col] = label;
                gridPanel.add(label);
                label.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        selectLabel(evt);
                    }
                });
            }
        }
    }

    private void initButtonPanel() {
        for (int i = 1; i <= 9; i++) {
            JButton button = new JButton(String.valueOf(i));
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    insertNumber(e);
                }
            });
            button.setFocusable(false);
            buttonPanel.add(button);
        }

        JButton zeroButton = new JButton("0");
        zeroButton.setFocusable(false);
        zeroButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertNumber(e);
            }
        });
        buttonPanel.add(zeroButton);

        JButton backButton = new JButton("Back");
        backButton.setFocusable(false);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backAction(e);
            }
        });
        buttonPanel.add(backButton);

        JButton clearButton = new JButton("All Clear");
        clearButton.setFocusable(false);
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearGrid();
            }
        });
        buttonPanel.add(clearButton);

        JButton solveButton = new JButton("Solve");
        solveButton.setFocusable(false);
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                solveSudoku();
            }
        });
        controlPanel.add(solveButton, BorderLayout.SOUTH);

        JButton tryAgainButton = new JButton("Try Again");
        tryAgainButton.setFocusable(false);
        tryAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetGrid();
            }
        });
        controlPanel.add(tryAgainButton, BorderLayout.NORTH);

        JButton rulesButton = new JButton("Rules");
        rulesButton.setFocusable(false);
        rulesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRules();
            }
        });
        controlPanel.add(rulesButton, BorderLayout.NORTH);
    }

    private JLabel selectedLabel;

    private void selectLabel(java.awt.event.MouseEvent evt) {
        selectedLabel = (JLabel) evt.getSource();
        resetLabelBackgrounds();
        selectedLabel.setBackground(Color.YELLOW);
    }

    private void insertNumber(ActionEvent e) {
        if (selectedLabel != null) {
            JButton button = (JButton) e.getSource();
            selectedLabel.setText(button.getText());
            selectedLabel.setBackground(Color.WHITE);
            selectedLabel = null;
        }
    }

    private void backAction(ActionEvent e) {
        if (selectedLabel != null) {
            selectedLabel.setText("");
            selectedLabel.setBackground(Color.WHITE);
            selectedLabel = null;
        }
    }

    private void clearGrid() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                gridLabels[row][col].setText("");
                gridLabels[row][col].setBackground(Color.WHITE);
            }
        }
    }

    private void resetGrid() {
        clearGrid();
    }

    private void resetLabelBackgrounds() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                gridLabels[row][col].setBackground(Color.WHITE);
            }
        }
    }

    private void solveSudoku() {
        try {
            int[][] board = new int[9][9];
            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    String text = gridLabels[row][col].getText();
                    if (!text.isEmpty()) {
                        int number = Integer.parseInt(text);
                        if (number < 0 || number > 9) {
                            throw new NumberFormatException("Invalid number");
                        }
                        board[row][col] = number;
                    } else {
                        board[row][col] = 0;
                    }
                }
            }

            if (isValidInitialBoard(board)) {
                if (solveSudoku(board)) {
                    for (int row = 0; row < 9; row++) {
                        for (int col = 0; col < 9; col++) {
                            gridLabels[row][col].setText(String.valueOf(board[row][col]));
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "No solution exists");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid initial board setup");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers between 0 and 9");
        }
    }

    private boolean isValidInitialBoard(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] != 0) {
                    int num = board[row][col];
                    board[row][col] = 0; 
                    if (!isValid(board, row, col, num)) {
                        return false;
                    }
                    board[row][col] = num; 
                }
            }
        }
        return true;
    }

    private boolean solveSudoku(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board)) {
                                return true;
                            }
                            board[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        for (int x = 0; x < 9; x++) {
            if (board[row][x] == num || board[x][col] == num ||
                    board[(row / 3) * 3 + x / 3][(col / 3) * 3 + x % 3] == num) {
                return false;
            }
        }
        return true;
    }

    private void showRules() {
        String rules = "Sudoku Rules:\n\n" +
                "1. Each row must contain the numbers 1-9 without repetition.\n" +
                "2. Each column must contain the numbers 1-9 without repetition.\n" +
                "3. Each of the nine 3x3 sub-grids must contain the numbers 1-9 without repetition.\n" +
                "4. The puzzle starts with a partially filled grid and the objective is to fill in the grid\n" +
                "   with the remaining numbers.\n" +
                "5. No guessing is required; every puzzle has a unique solution that can be solved\n" +
                "   using logical reasoning alone.";
        JOptionPane.showMessageDialog(this, rules, "Sudoku Rules", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new Sudoku_slover();
    }
}
