import java.awt.*;
import java.util.Random;

public class Fruit {

    protected final int X;
    protected final int Y;
    protected final Color color;

    Fruit(Random rand, int screenWidth, int screenHeight, int unitSize, Color color) {
        X = rand.nextInt(screenWidth/unitSize)*unitSize;
        Y = rand.nextInt(screenHeight/unitSize)*unitSize;
        this.color = color;
    }

    int getX() {
        return X;
    }

    int getY() {
        return Y;
    }

    Color getColor() {
        return color;
    }
}
