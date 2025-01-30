import javax.swing.*;
//imports a window to my game. you can explicitly import by changing * to JFrame

public class App {
    public static void main(String[] args) {
    //square window, 600 px
        int boardWidth = 600;
        int boardHeight = boardWidth;

        //making a window
        JFrame frame = new JFrame("Snake Game");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); //null opens the window in the center of the screen
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //makes the program terminate when the user clicks on the X button on the window

        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame);
        //as of now the height of our box isnt exactly 600px bc it counts with the title bar
        frame.pack(); //so it places the panel inside the frame with full dimensions
        snakeGame.requestFocus(); //now the snakeGame will be the one listening to the key presses
    }
}