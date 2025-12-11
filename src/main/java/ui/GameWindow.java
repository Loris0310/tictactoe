package ui;


import logic.GameLogic;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class GameWindow extends JFrame {

    private int size;
    private JButton[][] buttons;

    private GameLogic logic;

    private JLabel infoLabel;
    private JLabel turnLabel;
    private JButton replayButton;
    private JButton settingsButton;

    public GameWindow(int size, char playerSymbol) {
        this.size = size;
        this.logic = new GameLogic(size, playerSymbol);

        this.setTitle("Tic Tac Toe " + size + "x" + size);
        this.setSize(600, 650);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        infoLabel = new JLabel(
                "Vous jouez : " + logic.getPlayerSymbol() +
                        "  |  Ordinateur : " + logic.getAiSymbol(),
                JLabel.CENTER
        );
        turnLabel = new JLabel("", JLabel.CENTER);
        topPanel.add(infoLabel);
        topPanel.add(turnLabel);
        this.add(topPanel, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridLayout(size, size));
        buttons = new JButton[size][size];

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                JButton b = new JButton("");
                b.setFont(b.getFont().deriveFont(Font.BOLD, 32f));
                int row = r;
                int col = c;

                b.addActionListener(e -> handleClick(row, col));

                buttons[r][c] = b;
                gridPanel.add(b);
            }
        }

        this.add(gridPanel, BorderLayout.CENTER);


        JPanel bottomPanel = new JPanel();
        replayButton = new JButton("Rejouer");
        settingsButton = new JButton("Changer les paramètres");
        bottomPanel.add(replayButton);
        bottomPanel.add(settingsButton);
        this.add(bottomPanel, BorderLayout.SOUTH);

        replayButton.addActionListener(e -> resetGame());
        settingsButton.addActionListener(e -> {
            dispose();
            new SetupWindow();
        });

        resetGame();
        setVisible(true);
    }

    private void resetGame() {
        logic.reset();

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                buttons[r][c].setText("");
                buttons[r][c].setEnabled(true);
                buttons[r][c].setBackground(null);
            }
        }

        updateTurnLabel();

        if (logic.getAiSymbol() == logic.getCurrentPlayer()) {

            JOptionPane.showMessageDialog(this,
                    "L'ordinateur commence.",
                    "Tic Tac Toe",
                    JOptionPane.INFORMATION_MESSAGE);

            IaPlay();
        }
    }

    private void updateTurnLabel() {
        turnLabel.setText("Tour : " + logic.getCurrentPlayer());
    }

    private void handleClick(int r, int c) {
        if (!logic.isPlayerMove()) return;
        if (!logic.isCellEmpty(r, c)) return;

        logic.playPlayerMove(r, c);
        buttons[r][c].setText(String.valueOf(logic.getPlayerSymbol()));
        buttons[r][c].setEnabled(false);

        if (handleEndOfTurn(logic.getPlayerSymbol())) return;

        IaPlay();
    }

    private void IaPlay() {
        int[] move = logic.playComputerRandomMove();
        if (move != null) {
            int row = move[0];
            int col = move[1];
            buttons[row][col].setText(String.valueOf(logic.getAiSymbol()));
            buttons[row][col].setEnabled(false);

            handleEndOfTurn(logic.getAiSymbol());
        }
    }

    private boolean handleEndOfTurn(char symbolJustPlayed) {

        if (logic.getMovesPlayed() >= logic.getMinMovesToWin()) {
            List<int[]> winningCells = logic.getWinningCells(symbolJustPlayed);
            if (!winningCells.isEmpty()) {
                boolean playerWin = (symbolJustPlayed == logic.getPlayerSymbol());
                Color winColor = playerWin ? Color.GREEN : Color.RED;

                highlightCells(winningCells, winColor);

                int nbCoups = (int) Math.ceil(logic.getMovesPlayed() / 2.0);

                String msg = playerWin
                        ? "Vous avez gagné la partie après " + nbCoups + " coups !"
                        : "Victoire en " + nbCoups + " coups pour l'ORDINATUEUR";


                JOptionPane.showMessageDialog(this, msg);
                turnLabel.setText(msg + " Cliquez sur Rejouer ou Changer les paramètres.");
                disableBoard();
                return true;
            }
        }

        if (logic.isDraw()) {
            colorDrawBoard();
            JOptionPane.showMessageDialog(this, "Match nul!");
            turnLabel.setText("Match nul! Cliquez sur Rejouer ou Changer les paramètres.");
            disableBoard();
            return true;
        }

        updateTurnLabel();
        return false;
    }

    private void disableBoard() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                buttons[r][c].setEnabled(false);
            }
        }
    }

    private void highlightCells(List<int[]> cells, Color color) {
        for (int[] pos : cells) {
            int r = pos[0];
            int c = pos[1];
            buttons[r][c].setBackground(color);
        }
    }

    private void colorDrawBoard() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                buttons[r][c].setBackground(Color.ORANGE);
            }
        }
    }
}
