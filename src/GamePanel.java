import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    private enum Direction{
        RIGHT,
        LEFT,
        UP,
        DOWN
    }

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int UNIT_SIZE = 25;
    private static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
    private static final int DELAY = 75;

    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 50;

    private int[] x = new int[GAME_UNITS];    // x-coordinates of the snake body, x[0] is head
    private int[] y = new int[GAME_UNITS];    // y-coordinates of the snake body, y[0] is head
    private int bodyLength = 6;
    private int applesEaten = 0;
    private int appleX;
    private int appleY;
    private Direction direction = Direction.RIGHT;
    private boolean isRunning = false;
    private Timer timer;
    private final Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    private void startGame() {
        spawnApple();
        timer = new Timer(DELAY, this);
        timer.start();
        resetSnake();
    }

    private void resetSnake() {
        isRunning = true;
        bodyLength = 6;
        applesEaten = 0;
        x = new int[GAME_UNITS];
        y = new int[GAME_UNITS];
        direction = Direction.RIGHT;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if(isRunning) {
            // drawGrids(g);

            drawApple(g);
            drawSnake(g);
            drawScore(g);

        }else {
            gameOver(g);
        }
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten,
                (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten))/2,
                g.getFont().getSize());
    }

    private void drawSnake(Graphics g) {
        // draw head
        g.setColor(Color.green);
        g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);

        // draw body
        for(int i=1; i<bodyLength; i++) {
            g.setColor(new Color(45, 180, 0));
            g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
        }
    }

    private void drawApple(Graphics g) {
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
    }

    /*
     * Helper method that draws grids on the panel
     */
    private void drawGrids(Graphics g) {

        int numberOfGrids = SCREEN_HEIGHT/UNIT_SIZE;

        for(int i=0; i<numberOfGrids; i++) {
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
        }
    }

    private void spawnApple() {
        appleX = random.nextInt(SCREEN_WIDTH/UNIT_SIZE)*UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT/UNIT_SIZE)*UNIT_SIZE;
    }

    private void move() {
        for(int i = bodyLength; i>0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case UP -> y[0] = y[0] - UNIT_SIZE;
            case DOWN -> y[0] = y[0] + UNIT_SIZE;
            case LEFT -> x[0] = x[0] - UNIT_SIZE;
            case RIGHT -> x[0] = x[0] + UNIT_SIZE;
        }
    }

    private void checkApple() {
        if((x[0] == appleX) && (y[0] == appleY)) {
            bodyLength++;
            applesEaten++;
            spawnApple();
        }
    }

    private void checkCollisions() {
        // check if head collides with body
        for(int i=bodyLength; i>0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                isRunning = false;
                break;
            }
        }

        // check if head collides with the four borders
        if(x[0] < 0 || x[0] > SCREEN_WIDTH ||
           y[0] < 0 || y[0] > SCREEN_HEIGHT) {
            isRunning = false;
        }

        if(!isRunning) {
            timer.stop();
        }
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER",
                    (SCREEN_WIDTH - metrics.stringWidth("GAME OVER"))/2,
                    SCREEN_HEIGHT/2);

        drawScore(g);
        addRestartButton();
    }

    private void addRestartButton() {
        JButton button = new JButton("Start Again");
        Font font = new Font("Ink Free", Font.BOLD, 20);
        button.setFont(font);
        button.setBounds((SCREEN_WIDTH - BUTTON_WIDTH)/2, SCREEN_HEIGHT*2/3, BUTTON_WIDTH, BUTTON_HEIGHT);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getSource() == button) {
                    restart();
                }
            }
        });
        this.add(button);
    }

    private void restart() {
        this.removeAll();
        revalidate();
        startGame();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(isRunning) {
            move();
            checkApple();
            checkCollisions();
        }

        repaint();

    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> {
                    if(direction != Direction.RIGHT) {
                        direction = Direction.LEFT;
                    }
                }
                case KeyEvent.VK_RIGHT -> {
                    if(direction != Direction.LEFT) {
                        direction = Direction.RIGHT;
                    }
                }
                case KeyEvent.VK_UP -> {
                    if(direction != Direction.DOWN) {
                        direction = Direction.UP;
                    }
                }
                case KeyEvent.VK_DOWN -> {
                    if(direction != Direction.UP) {
                        direction = Direction.DOWN;
                    }
                }
            }
        }
    }

}
