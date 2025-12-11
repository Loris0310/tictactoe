package logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameLogic {

    private int size;
    private char[][] board;

    private char playerSymbol;
    private char aiSymbol;
    private char currentPlayer;

    private int movesPlayed;
    private Random random = new Random();

    public GameLogic(int size, char playerSymbol) {
        this.size = size;
        this.playerSymbol = playerSymbol;
        this.aiSymbol = (playerSymbol == 'X') ? 'O' : 'X';
        this.board = new char[size][size];
    }

    public void reset() {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                board[r][c] = '\0';
            }
        }
        movesPlayed = 0;
        currentPlayer = 'X';
    }

    public char getPlayerSymbol() {
        return playerSymbol;
    }

    public char getAiSymbol() {
        return aiSymbol;
    }

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public int getMovesPlayed() {
        return movesPlayed;
    }

    public int getMinMovesToWin() {
        return 2 * size - 1;
    }

    public boolean isPlayerMove() {
        return currentPlayer == playerSymbol;
    }

    public boolean isCellEmpty(int r, int c) {
        return board[r][c] == '\0';
    }

    public void playPlayerMove(int r, int c) {
        if (!isPlayerMove()) return;
        if (!isCellEmpty(r, c)) return;

        board[r][c] = playerSymbol;
        movesPlayed++;
        currentPlayer = aiSymbol;
    }

    public int[] playComputerRandomMove() {
        if (currentPlayer != aiSymbol) return null;

        List<int[]> emptyCells = new ArrayList<>();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board[r][c] == '\0') {
                    emptyCells.add(new int[]{r, c});
                }
            }
        }

        if (emptyCells.isEmpty()) return null;

        int[] choice = emptyCells.get(random.nextInt(emptyCells.size()));
        int r = choice[0];
        int c = choice[1];

        board[r][c] = aiSymbol;
        movesPlayed++;
        currentPlayer = playerSymbol;

        return choice;
    }

    public boolean isDraw() {
        return movesPlayed == size * size;
    }

    public List<int[]> getWinningCells(char symbol) {
        List<int[]> cells = new ArrayList<>();

        // Lignes
        for (int r = 0; r < size; r++) {
            boolean ok = true;
            for (int c = 0; c < size; c++) {
                if (board[r][c] != symbol) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                for (int c = 0; c < size; c++) {
                    cells.add(new int[]{r, c});
                }
                return cells;
            }
        }

        // Colonnes
        for (int c = 0; c < size; c++) {
            boolean ok = true;
            for (int r = 0; r < size; r++) {
                if (board[r][c] != symbol) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                for (int r = 0; r < size; r++) {
                    cells.add(new int[]{r, c});
                }
                return cells;
            }
        }

        // Diagonale principale
        boolean okDiag1 = true;
        for (int i = 0; i < size; i++) {
            if (board[i][i] != symbol) {
                okDiag1 = false;
                break;
            }
        }
        if (okDiag1) {
            for (int i = 0; i < size; i++) {
                cells.add(new int[]{i, i});
            }
            return cells;
        }

        // Diagonale secondaire
        boolean okDiag2 = true;
        for (int i = 0; i < size; i++) {
            if (board[i][size - 1 - i] != symbol) {
                okDiag2 = false;
                break;
            }
        }
        if (okDiag2) {
            for (int i = 0; i < size; i++) {
                cells.add(new int[]{i, size - 1 - i});
            }
            return cells;
        }

        return cells;
    }
}
