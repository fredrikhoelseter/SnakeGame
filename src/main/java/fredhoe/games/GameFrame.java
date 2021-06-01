package fredhoe.games;

import javax.swing.JFrame;

public class GameFrame {

    private static final int FRAME_WIDTH = 720;
    private static final int FRAME_HEIGHT = 744;
    private int[] x = new int[30];
    private int[] y = new int[30];
    private int square = 24;

    private JFrame frame;
    private GameController controller;

    public GameFrame() {
        for (int i = 0; i < 30; i++) {
            x[i] = i * square;
            y[i] = i * square;
        }
    }

    public void initialize() {
        frame = new JFrame("Snake Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);

        controller = new GameController();
        frame.add(controller);
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

}
