package fredhoe.games;

import javax.swing.JFrame;
import java.awt.*;

public class GameFrame {

    private final int squaresX = 30;
    private final int squaresY = 30;
    private final int square = 24;

    private final int FRAME_WIDTH = squaresX * square;
    private final int FRAME_HEIGHT = squaresY * square + square;
    private int[] x = new int[squaresX];
    private int[] y = new int[squaresY];


    private JFrame frame;
    private GameController controller;

    public GameFrame() {
        for (int i = 0; i < squaresX; i++) {
            x[i] = i * square;

        }
        for (int i = 0; i < squaresY; i++) {
            y[i] = i * square;
        }
    }

    public void initialize() {
        frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);

        controller = new GameController();
        frame.add(controller, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public int[] getX() {
        return x;
    }

    public int[] getY() {
        return y;
    }

    public int getSquare() {
        return square;
    }

    public int getSquaresX() {
        return squaresX;
    }

    public int getSquaresY() {
        return squaresY;
    }

    public int getFRAME_WIDTH() {
        return FRAME_WIDTH;
    }

    public int getFRAME_HEIGHT() {
        return FRAME_HEIGHT;
    }

 }
