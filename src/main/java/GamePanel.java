import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Random;
import org.json.*;
import java.io.FileWriter;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 850;
    static final int SCREEN_HEIGHT = 700;
    static final int UNIT_SIZE = 25;
    static final int DELAY = 120;
    char direction = 'R'; //the game starts snake going right
    boolean running = false;
    Timer timer;
    Random random;
    Apple apple;
    Snake snake;
    int[] x;
    int[] y;
    String name;
    int score;
    JTextField userText = new JTextField(20);
    JButton button = new JButton("Save score");
    JSONObject JsonObj = new JSONObject();

    GamePanel(){
        random = new Random();
        apple = new Apple();
        snake = new Snake();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
        this.setBackground(Color.GREEN);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        startGame();
        getArray(snake);
    }

    public void startGame(){
        running = true;
        timer = new Timer(DELAY,this); //how fast the game is running
        timer.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        drawSnake(g, snake);
        drawApple(g, apple);
        drawScore(g, apple);
    }

    public void getArray(Snake snake){
        x = snake.snakeX();
        y = snake.snakeY();
    }

    public void drawApple(Graphics g, Apple apple){
        if (running) {
            g.setColor(Color.red); //color of an apple
            g.fillOval(apple.appleX(), apple.appleY(), UNIT_SIZE, UNIT_SIZE); //draw an apple on a map
        }
    }

    public void drawSnake(Graphics g, Snake snake){

        if(running) {
            for (int i = 0; i < snake.getBodyParts(); i++) {
                g.setColor(Color.black); // drawing a snake
                g.fillOval(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
        else
        {
            gameOver(g);
        }
    }

    public void drawScore(Graphics g, Apple apple)
    {
        g.setColor(Color.black);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " +apple.score(),(SCREEN_WIDTH - metrics.stringWidth("Score: " +apple.score()))/2, g.getFont().getSize());
    }

    public void move(Snake snake){

        for(int i = snake.getBodyParts(); i > 0; i--)
        {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction)
        {
            case 'U':
                y[0] = y[0] - UNIT_SIZE; // going up
                break;

            case 'D':
                y[0] = y[0] + UNIT_SIZE; // going down
                break;

            case 'L':
                x[0] = x[0] - UNIT_SIZE; // going left
                break;

            case 'R':
                x[0] = x[0] + UNIT_SIZE; // going right
                break;
        }
    }

    public void checkApple(Apple apple){
        //if the apple is eaten

        if((x[0] == apple.appleX()) && (y[0] == apple.appleY()))
        {
            snake.increaseBody();
            apple.increaseApple(); //increase the score
            apple.newApple();
        }
        if(!running)
        {
            timer.stop();
        }
    }

    public void checkCollisionsWithBody(Snake snake) {

        //game over if head collides with body
        for (int i = snake.getBodyParts(); i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false; //game over if head collides with body
            }
        }
        if(!running)
        {
            timer.stop();
        }
    }

    public void checkCollisionsWithLeftBorder() {

        //if head collides with left border
        if (x[0] < 0) {
            running = false;
        }
        if(!running)
        {
            timer.stop();
        }
    }

    public void checkCollisionsWithRightBorder() {

        //if head collides with right border
        if (x[0] == SCREEN_WIDTH) {
            running = false;
        }
        if(!running)
        {
            timer.stop();
        }
    }

    public void checkCollisionsWithTopBorder() {

        //if head collides with top border
        if (y[0] < 0) {
            running = false;
        }
        if(!running)
        {
            timer.stop();
        }
    }

    public void checkCollisionsWithBottomBorder() {

        //if head collides with bottom border
        if (y[0] == SCREEN_HEIGHT) {
            running = false;
        }
        if(!running)
        {
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        g.setColor(Color.black);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("GAME OVER",(SCREEN_WIDTH - metrics2.stringWidth("GAME OVER"))/2, SCREEN_HEIGHT/2);

        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics3 = getFontMetrics(g.getFont());
        g.drawString("Your name:",(SCREEN_WIDTH - metrics3.stringWidth("Your name"))/2, 500);

        userText.setBounds((SCREEN_WIDTH - metrics3.stringWidth("Your name")+40)/2,510,165,40);
        this.add(userText);

        button.setBounds((SCREEN_WIDTH - metrics3.stringWidth("Your name")+64)/2, 560, 140, 30);
        this.add(button);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               saveToJSON();
            }
        });
    }

    public void saveToJSON() {
        name = userText.getText();
        JsonObj.put("name", name);
        score = apple.score();
        JsonObj.put("score", score);

        try {
            FileWriter file = new FileWriter("output.json", true);
            file.write(JsonObj.toString());
            file.close();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
        System.out.println("JSON file created: " + JsonObj);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running)
        {
            move(snake);
            checkCollisionsWithBody(snake);
            checkCollisionsWithBottomBorder();
            checkCollisionsWithLeftBorder();
            checkCollisionsWithRightBorder();
            checkCollisionsWithTopBorder();
            checkApple(apple);
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()) //contolling moves
            {
                case KeyEvent.VK_LEFT:
                    if(direction != 'R')
                    {
                        direction = 'L';
                    }
                    break;

                case KeyEvent.VK_RIGHT:
                    if(direction != 'L')
                    {
                        direction = 'R';
                    }
                    break;

                case KeyEvent.VK_UP:
                    if(direction != 'D')
                    {
                        direction = 'U';
                    }
                    break;

                case KeyEvent.VK_DOWN:
                    if(direction != 'U')
                    {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
