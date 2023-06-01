import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements ActionListener {

    private final GameModel gameModel = GameModel.getInstance();

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int UNIT_SIZE = 25;
    private static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
    private static final int DELAY = 75;

    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 50;

    private Timer timer;

    GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(gameModel.getSnake().getAdapter());
        startGame();
    }

    private void startGame() {
        timer = new Timer(DELAY, this);
        timer.start();
        gameModel.resetGame(SCREEN_WIDTH, SCREEN_HEIGHT, UNIT_SIZE, GAME_UNITS);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if(gameModel.getSnake().isRunning()) {
            // drawGrids(g);

            drawFruit(g);
            drawSnake(g);
            drawScore(g);
        }else {
            gameOver(g);
        }
    }

    private void drawScore(Graphics g) {
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());

        int score = gameModel.getScore();
        String scoreToString = "Score: ".concat(String.valueOf(score));
        int positionX = (SCREEN_WIDTH - metrics.stringWidth(scoreToString))/2;
        int positionY = g.getFont().getSize();

        g.drawString(scoreToString, positionX, positionY);
    }

    private void drawSnake(Graphics g) {
        Snake snake = gameModel.getSnake();
        int[][] snakeBody = snake.getSnakeBody();

        // draw head
        g.setColor(Color.green);
        g.fillRect(snakeBody[0][0], snakeBody[1][0], UNIT_SIZE, UNIT_SIZE);

        // draw body
        for(int i = 1; i< snake.getBodyLength(); i++) {
            g.setColor(new Color(45, 180, 0));
            g.fillRect(snakeBody[0][i], snakeBody[1][i], UNIT_SIZE, UNIT_SIZE);
        }
    }

    private void drawFruit(Graphics g) {
        Fruit fruit = gameModel.getFruit();
        g.setColor(fruit.getColor());
        g.fillOval(fruit.getX(), fruit.getY(), UNIT_SIZE, UNIT_SIZE);
    }

    /*
     * Visualizes game units on the panel.
     */
    private void drawGrids(Graphics g) {
        int numberOfGrids = SCREEN_HEIGHT/UNIT_SIZE;

        for(int i=0; i<numberOfGrids; i++) {
            g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
            g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
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
        Font font = new Font("Arial", Font.BOLD, 20);
        button.setFont(font);
        button.setBounds((SCREEN_WIDTH - BUTTON_WIDTH)/2, SCREEN_HEIGHT*2/3, BUTTON_WIDTH, BUTTON_HEIGHT);
        button.addActionListener(e -> restartGame());
        this.add(button);
    }

    private void restartGame() {
        this.removeAll();
        startGame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Snake snake = gameModel.getSnake();

        if(snake.isRunning()) {
            snake.move(UNIT_SIZE);
            gameModel.checkFruit();
            gameModel.checkCollision(SCREEN_WIDTH, SCREEN_HEIGHT);
        }

        if(!gameModel.getSnake().isRunning()) {
            timer.stop();
        }

        repaint();

    }

}
