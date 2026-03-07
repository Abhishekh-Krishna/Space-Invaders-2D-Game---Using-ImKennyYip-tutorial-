import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        //window variables
        int tileSize = 32; //size of one square
        int rows = 16;
        int columns = 16;
        int boardWidth = tileSize * columns; //32*16=512px
        int boardHight = tileSize * rows; //32*16=512px

        //window  
        JFrame frame = new JFrame("Space Invaders"); //name of the window
        //frame.setVisible(true); //to make the window visible otherwise it will be invisible
        frame.setSize(boardWidth, boardHight);
        frame.setLocationRelativeTo(null); //Opens the window at the center of the screen
        frame.setResizable(false); //User cant resize the window by dragging
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //exits when clicked on close

        SpaceInvaders spaceInvaders=new SpaceInvaders();
        frame.add(spaceInvaders);
        frame.pack();
        spaceInvaders.requestFocus();
        frame.setVisible(true); //to make the window visible it should be genereally placed after u add all the components to window
    }
}
