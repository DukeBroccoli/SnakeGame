package utils;

import entity.Apple;
import entity.Fruit;

import java.awt.*;
import java.util.Random;

public class FruitFactory {

    private final Random random = new Random();

    public Fruit spawnNewFruit(FruitType type,
                               int screenWidth,
                               int screenHeight,
                               int unitSize) {
        switch(type) {
            case APPLE -> {
                return new Apple(random, screenWidth, screenHeight, unitSize, Color.red);
            }
        }
        return null;
    }
}
