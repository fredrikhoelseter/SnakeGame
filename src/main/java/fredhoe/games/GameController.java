package fredhoe.games;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameController extends JPanel implements ActionListener, KeyListener, ImageObserver, MouseListener {
    private int xPos;
    private int yPos;
    private int xHeadPos;
    private int yHeadPos;
    private int velX;
    private int velY;
    private int startDelay = 50;
    private final int speedIncrease = 0;
    private final int speedBoost = 0;
    private final int startBodyCount = 6;
    private int bodyIncrease = 3;
    private int delay;

    private int square;
    private char direction;
    private boolean eatApple;
    private boolean openMenu;
    private int score;
    private int highScore;
    private int bodyCount;
    private Image image;

    private Point2D position;
    private Point2D applePos;
    private ArrayList<Point2D> bodyPositions;
    private ArrayList<Point2D> allEmptyPositions;
    private static final ArrayList<Point2D> allPositions = new ArrayList<>();
    private int plusButtonCoordinateX;
    private int minusButtonCoordinateX;
    private int speedButtonsCoordinateY;
    private int buttonSideLength = 30;

    private Timer timer;
    private GameFrame gameFrame;

    public GameController () {
        gameFrame = new GameFrame();
        this.square = gameFrame.getSquare();
        plusButtonCoordinateX = getMiddle(gameFrame.getSquaresX())*square - 12;
        minusButtonCoordinateX = plusButtonCoordinateX + 35;
        speedButtonsCoordinateY = 166;

        highScore = 0;

        for (yPos = 0; yPos <= gameFrame.getSquaresY()-1; yPos++) {
            for (xPos = 0; xPos <= gameFrame.getSquaresX()-1; xPos++) {
                xPos = xPos * square;
                yPos = yPos * square;
                allPositions.add(new Point2D.Double(xPos, yPos));
            }
        }

        timer = new Timer(startDelay, this);
        start();

        addKeyListener(this);
        addMouseListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        this.setBackground(Color.BLACK);
    }

    public void start() {
        bodyCount = startBodyCount;
        delay = startDelay;
        timer.setDelay(delay);
        openMenu = false;
        allEmptyPositions = allPositions;
        score = 0;
        eatApple = true;
        xHeadPos = getMiddle(gameFrame.getSquaresX());
        yHeadPos = getMiddle(gameFrame.getSquaresY());
        direction = 'R';
        this.xPos = gameFrame.getX()[xHeadPos];
        this.yPos = gameFrame.getY()[yHeadPos];

        bodyPositions = new ArrayList<>();
        right();
        position = new Point2D.Double(xPos, yPos);
        bodyPositions.add(position);
        allEmptyPositions.remove(position);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.CYAN);
        if (score > highScore) {
            highScore = score;
        }
        g.drawString("High Score: " + highScore, gameFrame.getFRAME_WIDTH()-120, 24);
        g.setColor(Color.CYAN);
        g.drawString("Score: " + score, gameFrame.getFRAME_WIDTH()-120, 48);

        if (openMenu) {
            drawMenu(g);
        } else {
            drawSnake(g);
            drawApple(g);
        }
        timer.start();
    }

    private void drawSnake(Graphics g) {
        for (int i = 0; i < bodyPositions.size(); i++) {
            g.setColor(Color.GREEN);
            g.fillRect((int) bodyPositions.get(i).getX(), (int) bodyPositions.get(i).getY(), square, square);
        }
    }

    private void drawApple(Graphics g) {
        if (eatApple) {
            locateNewApple();
            eatApple = false;
        }

        g.setColor(Color.RED);
        g.fillRect((int) applePos.getX(), (int) applePos.getY(), square, square);
    }

    private void drawMenu(Graphics g) {
        try {
            image = ImageIO.read(this.getClass().getResourceAsStream("/PAUSED.png"));
            g.drawImage(image, getMiddle(gameFrame.getSquaresX())*square - image.getWidth(this)/2,
                    image.getHeight(this), this);

            Font font = g.getFont().deriveFont(30f);
            g.setColor(Color.CYAN);
            g.setFont(font);
            g.drawString("Speed = " + (100 - delay), getMiddle(gameFrame.getSquaresX())*square - image.getWidth(this)/2,
                    image.getHeight(this)*3);

            // Plus button
            g.drawRoundRect(plusButtonCoordinateX, speedButtonsCoordinateY,
                    buttonSideLength, buttonSideLength, 5, 5);
            g.drawString("+", getMiddle(gameFrame.getSquaresX())*square - image.getWidth(this)/2 + 172,
                    image.getHeight(this)*3);

            // Minus button
            g.drawRoundRect(minusButtonCoordinateX, speedButtonsCoordinateY,
                    buttonSideLength, buttonSideLength, 5, 5);
            g.drawString("-", getMiddle(gameFrame.getSquaresX())*square - image.getWidth(this)/2 + 210,
                    image.getHeight(this)*3 - 3);

        } catch (IOException ioException) {
            System.out.println("Something went wrong!");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!openMenu) {
            xHeadPos += velX;
            yHeadPos += velY;

            if (xHeadPos < 0) {
                xHeadPos = gameFrame.getSquaresX()-1;
            }
            if (xHeadPos > gameFrame.getSquaresX()-1) {
                xHeadPos = 0;
            }

            if (yHeadPos < 0) {
                yHeadPos = gameFrame.getSquaresY()-1;
            }
            if (yHeadPos > gameFrame.getSquaresY()-1) {
                yHeadPos = 0;
            }

            xPos = gameFrame.getX()[xHeadPos];
            yPos = gameFrame.getY()[yHeadPos];

            position = new Point2D.Double(xPos, yPos);

            if (position.getX() == applePos.getX() && position.getY() == applePos.getY()) {
                eatApple = true;
                score += 1;
                bodyCount += bodyIncrease;
                if (delay > speedIncrease) {
                    timer.setDelay(delay -= speedIncrease);
                }
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

    public int getMiddle(int squares) {
        int middle;
        try {
            middle = Integer.parseInt(squares/2 + "");
        } catch (NumberFormatException exception) {
            middle = (int)(squares/2 - 0.5);
        }
        return middle;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == KeyEvent.VK_SPACE) {
            timer.setDelay(delay - speedBoost);
        }

        if (keyCode == KeyEvent.VK_UP && (direction == 'R' || direction == 'L')) {
            up();
            direction = 'U';
        }
        else if (keyCode == KeyEvent.VK_DOWN && (direction == 'R' || direction == 'L')) {
            down();
            direction = 'D';
        }
        else if (keyCode == KeyEvent.VK_RIGHT && (direction == 'U' || direction == 'D')) {
            right();
            direction = 'R';
        }
        else if (keyCode == KeyEvent.VK_LEFT && (direction == 'U' || direction == 'D')) {
            left();
            direction = 'L';
        }
        else if (keyCode == KeyEvent.VK_ESCAPE && openMenu == false) {
            openMenu = true;
        } else if (keyCode == KeyEvent.VK_ESCAPE && openMenu == true) {
            openMenu = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_SPACE) {
            timer.setDelay(delay);
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}


    @Override
    public void mouseClicked(MouseEvent e) {
        if (openMenu) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            if (mouseY >= speedButtonsCoordinateY && mouseY <= speedButtonsCoordinateY + buttonSideLength) {
                if (mouseX >= plusButtonCoordinateX && mouseX <= plusButtonCoordinateX + buttonSideLength) {
                    if (startDelay > 1) {
                        startDelay -= 1;
                        delay = startDelay;
                        timer.setDelay(delay);
                    }
                } else if (mouseX >= minusButtonCoordinateX && mouseX <= minusButtonCoordinateX + buttonSideLength) {
                    if (startDelay < 99) {
                        startDelay += 1;
                        delay = startDelay;
                        timer.setDelay(delay);
                    }
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
