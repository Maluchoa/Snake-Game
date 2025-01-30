import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; //this will be used to store the segments of the snakes body, so it grows after eating
import java.util.Random; //used to get random x,y values to place the food on the screen
import javax.swing.*;

import static java.lang.String.valueOf;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    private class Tile{
        int x;
        int y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize = 25;
    //snake
    Tile snakeHead;
    ArrayList<Tile> snakeBody;
    //food
    Tile food;
    Random random;

    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;//false by default

    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        /** the reason why we need this. keyword is to distinguish boardWidth field from
         * boardWidth parameter. this = this x from this class
         */
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true); // will make my game listen to the key events

        snakeHead = new Tile(5,5); //5 px, not 5 units if we dont multiply snakeHead.x * tileSize
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10,10);
        random = new Random();
        placeFood();

        velocityX =0;
        velocityY=0;

        gameLoop = new Timer(100,this); //drawing frames over and over again every 100 milliseconds
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //grid
        /** for (int i = 0; i < boardWidth/tileSize; i++){
         *      //(x1, x2, y1, y2)
         *      g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
         *      g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
         *      }
         */

        //food
        g.setColor(Color.red);
        //g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);
        //so that the tiles have borders and we no longer need grids

        //snake head
        g.setColor(Color.green);
        //g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        //snake body
        for (int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        //score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver){
            g.setColor(Color.red);
            g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
        else {
            g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }

    }

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize); // 600/25=24 so x will be random from 0 to 24
        food.y = random.nextInt(boardHeight/tileSize);
    }

    public boolean collision(Tile tile1, Tile tile2){
        //to determine if there is a collision we just need to check if the positions are the same
        return tile1.x == tile2.x && tile1.y == tile2.y;//food

    }

    public void move(){
        //eat food
        if (collision(snakeHead, food)){
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //snake body
        for (int i = snakeBody.size()-1; i>= 0; i--){
            Tile snakePart = snakeBody.get(i);
            if (i == 0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            }
            else {
                Tile prevSnakePart = snakeBody.get(i-1);
                snakePart.x = prevSnakePart.x;
                snakePart.y = prevSnakePart.y;
                //so now we are moving the snakes body along with the head
            }
        }

        //snake head
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        //game over conditions
        for (int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            //collide with the snake head
            if (collision(snakeHead, snakePart)){
                gameOver = true;
            }
        }

        if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize >boardWidth ||
            snakeHead.y * tileSize < 0 || snakeHead.y * tileSize >boardHeight) {
            gameOver = true;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e){ //this will get called every 100ms (gameloop)
        move();//will update the x and y position of the snake
        repaint(); //calls draw over and over again
        if (gameOver){
            gameLoop.stop();
        }

    }

    //now we override 3 methods after implementing KeyListener

    @Override
    public void keyPressed(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){//makes so that the snake cant go backwards and collide with its own body
            velocityX=0;
            velocityY= -1;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX=0;
            velocityY=1;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX= -1;
            velocityY=0;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX=1;
            velocityY=0;
        }
    }
    //do not need these
    @Override
    public void keyTyped(KeyEvent e){
    }

    @Override
    public void keyReleased(KeyEvent e){
    }
}
