package entity;

import java.awt.*;
import java.util.Random;

public class Fruit {

    protected final int X;
    protected final int Y;
    protected final Color color;

    public Fruit(Random rand, int screenWidth, int screenHeight, int unitSize, Color color) {
        X = rand.nextInt(screenWidth/unitSize)*unitSize;
        Y = rand.nextInt(screenHeight/unitSize)*unitSize;
        this.color = color;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public Color getColor() {
        return color;
    }
}
