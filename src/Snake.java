import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Arrays;

public class Snake {

    // Singleton
    private static final Snake snake = new Snake();

    private final SnakeKeyAdapter adapter = new SnakeKeyAdapter();

    private enum Direction{
        RIGHT, LEFT, UP, DOWN
    }

    private boolean isRunning = false;

    private int bodyLength = 6;
    private int[] x;    // x-coordinates of the snake body, x[0] is head
    private int[] y;    // y-coordinates of the snake body, y[0] is head
    private Direction direction = Direction.RIGHT;

    private Snake() {}

    static Snake getInstance() {
        return snake;
    }

    void resetSnake(int maxBodyLength) {
        isRunning = true;
        bodyLength = 6;
        x = new int[maxBodyLength];
        y = new int[maxBodyLength];
        direction = Direction.RIGHT;
    }

    SnakeKeyAdapter getAdapter() {
        return adapter;
    }

    int[][] getSnakeBody() {
        return new int[][]{Arrays.copyOf(x, bodyLength+1), Arrays.copyOf(y, bodyLength+1)};
    }

    int getBodyLength() {
        return bodyLength;
    }

    void incrementBodyLength(int increment) {
        bodyLength += increment;
    }

    void stopRunning() {
        isRunning = false;
    }

    boolean isRunning() {
        return isRunning;
    }

    void move(int unitSize) {
        for(int i = bodyLength; i>0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction) {
            case UP -> y[0] = y[0] - unitSize;
            case DOWN -> y[0] = y[0] + unitSize;
            case LEFT -> x[0] = x[0] - unitSize;
            case RIGHT -> x[0] = x[0] + unitSize;
        }
    }

    class SnakeKeyAdapter extends KeyAdapter {
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
