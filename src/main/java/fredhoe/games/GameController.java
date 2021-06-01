package fredhoe.games;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameController extends JPanel implements ActionListener, KeyListener, ImageObserver {
    private int xPos;
    private int yPos;
    private int xHeadPos;
    private int yHeadPos;
    private int velX;
    private int velY;

    private int square;
    private char direction;
    private boolean eatApple;
    private boolean openMenu;
    private int score;
    private int highScore;
    private int bodyCount;

    private Point2D position;
    private Point2D applePos;
    private ArrayList<Point2D> bodyPositions;
    private ArrayList<Point2D> allEmptyPositions;
    private static final ArrayList<Point2D> allPositions = new ArrayList<>();

    private Timer timer;
    private GameFrame gameFrame;

    public GameController () {
        highScore = 0;
        for (yPos = 0; yPos <= 29; yPos++) {
            for (xPos = 0; xPos <= 29; xPos++) {
                xPos = xPos * 24;
                yPos = yPos * 24;
                allPositions.add(new Point2D.Double(xPos, yPos));
            }
        }
        gameFrame = new GameFrame();
        this.square = gameFrame.getSquare();
        timer = new Timer(50, this);
        start();

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (openMenu) {
            try {
                Image image = ImageIO.read(this.getClass().getResourceAsStream("/PAUSED.png"));
                g.drawImage(image, -150, 100, this);
            } catch (IOException ioException) {
                System.out.println("Something went wrong faggot!");
            }
        } else {
            this.setBackground(Color.BLACK);
            for (int i = 0; i < bodyPositions.size(); i++) {
                g.setColor(Color.GREEN);
                g.fillRect((int) bodyPositions.get(i).getX(), (int) bodyPositions.get(i).getY(), square, square);
            }
            if (eatApple) {
                bodyCount += 3;
                locateNewApple();
                eatApple = false;
            }

            g.setColor(Color.RED);
            g.fillRect((int) applePos.getX(), (int) applePos.getY(), square, square);
        }

        g.setColor(Color.CYAN);
        if (score > highScore) {
            highScore = score;
        }
        g.drawString("High Score: " + highScore, 600, 24);
        g.setColor(Color.CYAN);
        g.drawString("Score; " + score, 600, 48);

        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!openMenu) {
            xHeadPos += velX;
            yHeadPos += velY;

            if (xHeadPos < 0) {
                xHeadPos = 29;
            }
            if (xHeadPos > 29) {
                xHeadPos = 0;
            }

            if (yHeadPos < 0) {
                yHeadPos = 29;
            }
            if (yHeadPos > 29) {
                yHeadPos = 0;
            }

            xPos = gameFrame.getX()[xHeadPos];
            yPos = gameFrame.getY()[yHeadPos];

            position = new Point2D.Double(xPos, yPos);

            if (position.getX() == applePos.getX() && position.getY() == applePos.getY()) {
                eatApple = true;
                score += 1;
            }

            if (bodyPositions.contains(position)) {
                start();
            } else {
                bodyPositions.add(position);
                allEmptyPositions.remove(position);
                if (bodyPositions.size() > bodyCount) {
                    allEmptyPositions.add(bodyPositions.get(0));
                    bodyPositions.remove(0);
                }
            }
        }

        repaint();
    }

    public void start() {
        openMenu = false;
        allEmptyPositions = allPositions;
        score = 0;
        eatApple = true;
        xHeadPos = 13;
        yHeadPos = 13;
        direction = 'R';
        bodyCount = 3;
        this.xPos = gameFrame.getX()[xHeadPos];
        this.yPos = gameFrame.getY()[yHeadPos];

        bodyPositions = new ArrayList<>();
        right();
        position = new Point2D.Double(xPos, yPos);
        bodyPositions.add(position);
        allEmptyPositions.remove(position);
    }

    public void locateNewApple() {
        Random random = new Random();
        applePos = allEmptyPositions.get(random.nextInt(allEmptyPositions.size()));
    }

    public void right() {
        velX = 1;
        velY = 0;
    }

    public void left() {
        velX = -1;
        velY = 0;
    }

    public void up() {
        velX = 0;
        velY = -1;
    }

    public void down() {
        velX = 0;
        velY = 1;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_UP && (direction == 'R' || direction == 'L')) {
            up();
            direction = 'U';
        }
        if (keyCode == KeyEvent.VK_DOWN && (direction == 'R' || direction == 'L')) {
            down();
            direction = 'D';
        }
        if (keyCode == KeyEvent.VK_RIGHT && (direction == 'U' || direction == 'D')) {
            right();
            direction = 'R';
        }
        if (keyCode == KeyEvent.VK_LEFT && (direction == 'U' || direction == 'D')) {
            left();
            direction = 'L';
        }
        if (keyCode == KeyEvent.VK_ESCAPE && openMenu == false) {
            openMenu = true;
        } else if (keyCode == KeyEvent.VK_ESCAPE && openMenu == true) {
            openMenu = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}
}
