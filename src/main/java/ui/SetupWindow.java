package ui;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class SetupWindow extends JFrame {

    private static final Integer[] GRID_SIZES = {3, 4, 5};
    private static final String[] PION_VALUES = {"X", "O"};

    private int size;
    private char playerSymbol;

    private JComboBox<Integer> sizeSelector;
    private JComboBox<String> playerSelector;
    private JButton startButton;

    public SetupWindow() {
        this.setTitle("Configuration Tic Tac Toe");
        this.setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridLayout(3, 2, 10, 10));
        setResizable(false);

        this.add(new JLabel("Taille de la grille :"));
        sizeSelector = new JComboBox<>(GRID_SIZES);
        this.add(sizeSelector);

        this.add(new JLabel("Choix du pion :"));
        playerSelector = new JComboBox<>(PION_VALUES);
        this.add(playerSelector);

        startButton = new JButton("Commencer");
        this.add(new JLabel(""));
        this.add(startButton);

        startButton.addActionListener(e -> {
            if(sizeSelector.getSelectedItem() != null){
                size = (int) sizeSelector.getSelectedItem();
            }
            playerSymbol = Objects.requireNonNull(playerSelector.getSelectedItem()).toString().charAt(0);

            new GameWindow(size, playerSymbol);
            dispose();
        });

        setVisible(true);
    }
}
