import java.util.Random;

public class Apple {
    int appleX;
    int appleY;
    int applesEaten = 0;
    static final int UNIT_SIZE = 25;
    static final int SCREEN_WIDTH = 850;
    static final int SCREEN_HEIGHT = 700;
    Random random;

    Apple() {
        random = new Random();
        generateAppleY();
        generateAppleX();
    }

    public void generateAppleX() {
        appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE; // x posission on a map
    }

    public void generateAppleY() {
        appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE; // y posission on a map
    }

    public int appleX() { return appleX; }

    public int appleY() {
        return appleY;
    }

    public void increaseApple() {
        applesEaten++;
    }

    public int score() {
        return applesEaten;
    }

    public void newApple() {
        generateAppleY();
        generateAppleX();
    }
}