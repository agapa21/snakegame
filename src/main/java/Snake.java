public class Snake {

    static final int UNIT_SIZE = 25;
    static final int SCREEN_WIDTH = 850;
    static final int SCREEN_HEIGHT = 700;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    final int X[] = new int[GAME_UNITS];
    final int Y[] = new int[GAME_UNITS];
    int bodyParts;

    Snake() {
        bodyParts = 3;
    }

    public int[] snakeX() {
        return X;
    }

    public int[] snakeY() {
        return Y;
    }

    public void increaseBody(){
        bodyParts++;
    }

    public int getBodyParts(){
        return bodyParts;
    }
}
