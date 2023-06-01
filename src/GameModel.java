public final class GameModel {

    // Singleton
    private static final GameModel game = new GameModel();

    private final FruitFactory factory = new FruitFactory();

    // Game entities
    private Fruit fruit;
    private final Snake snake = Snake.getInstance();

    private int score = 0;

    private GameModel() {}

    static GameModel getInstance() {
        return game;
    }

    void resetGame(int screenWidth, int screenHeight, int unitSize, int numberOfGameUnits) {
        fruit = factory.spawnNewFruit(FruitType.APPLE, screenWidth, screenHeight, unitSize);
        snake.resetSnake(numberOfGameUnits);
    }

    Fruit getFruit() {
        return fruit;
    }

    Snake getSnake() {
        return snake;
    }

    int getScore() {
        return score;
    }

    /**
     * Checks if the snake eats the fruit by checking whether its head collides with the fruit.
     */
    void checkFruit() {
        int[][] snakeBody = snake.getSnakeBody();
        if((snakeBody[0][0] == fruit.getX()) && (snakeBody[1][0] == fruit.getY())) {
            snake.incrementBodyLength(1);
            score++;
        }
    }

    /**
     * Checks for any collisions. A collision happens when the snake's head overlaps with one of
     * borders or with the snake's body.
     */
    void checkCollision(int screenWidth, int screenHeight) {
        int[][] snakeBody = snake.getSnakeBody();

        // check if head collides with body
        for(int i=snake.getBodyLength(); i>0; i--) {
            if((snakeBody[0][0] == snakeBody[0][i]) && (snakeBody[1][0] == snakeBody[1][i])) {
                snake.stopRunning();
                break;
            }
        }

        // check if head collides with the borders
        if(snakeBody[0][0] < 0 || snakeBody[0][0] > screenWidth ||
           snakeBody[1][0] < 0 || snakeBody[1][0] > screenHeight) {
            snake.stopRunning();
        }
    }

}
