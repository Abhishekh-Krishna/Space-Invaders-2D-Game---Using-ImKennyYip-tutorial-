import java.awt.*; //for game
import java.util.ArrayList; //for game
import javax.swing.*; //for storing all the aliens and bullets in the game

public class SpaceInvaders extends JPanel {
    class Block {
        int x; //coordinates
        int y; //coordinates
        int width;
        int height;
        Image img;
        boolean alive = true; //aliens
        boolean used = false; //bullets

        Block(int x, int y, int width, int height, Image img){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }
    //board
    int tileSize = 32; 
    int rows = 16;
    int columns = 16;
    int boardWidth = tileSize * columns; //32*16=512
    int boardheight = tileSize * rows; //32*16=512

    Image shipImg;
    Image alienImg;
    Image alienCyanImg;
    Image alienMagentaImg;
    Image alienYellowImg;
    ArrayList<Image> alienImgArray;

    //ship
    int shipWidth = tileSize*2; //width is gonna be 2 squares, 64px
    int shipHeight = tileSize; //32px 
    int shipX = tileSize*columns/2 - tileSize;
    int shipY = boardheight - tileSize * 2; 

    Block ship;

    @SuppressWarnings("Convert2Diamond")
    SpaceInvaders() { //constructor
        setPreferredSize(new Dimension(boardWidth, boardheight)); //method that belongs to Jpanel
        setBackground(Color.black); //background colour
        
        //load image
        shipImg = new ImageIcon(getClass().getResource("./ship.png")).getImage(); //getclass is the Spaceinvaders.java,getResource gets the location from the source folder,gets image icon
        alienImg = new ImageIcon(getClass().getResource("./alien.png")).getImage(); //getclass is the Spaceinvaders.java,getResource gets the location from the source folder,gets image icon
        alienCyanImg = new ImageIcon(getClass().getResource("./alien-cyan.png")).getImage(); //getclass is the Spaceinvaders.java,getResource gets the location from the source folder,gets image icon
        alienMagentaImg = new ImageIcon(getClass().getResource("./alien-magenta.png")).getImage(); //getclass is the Spaceinvaders.java,getResource gets the location from the source folder,gets image icon
        alienYellowImg = new ImageIcon(getClass().getResource("./alien-yellow.png")).getImage(); //getclass is the Spaceinvaders.java,getResource gets the location from the source folder,gets image icon

        alienImgArray = new ArrayList<Image>();
        alienImgArray.add(alienImg);
        alienImgArray.add(alienCyanImg);
        alienImgArray.add(alienMagentaImg);
        alienImgArray.add(alienYellowImg);

        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg); 
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); //call the paintComponent from JPanel
        draw(g);
    }

    public void draw(Graphics g) { //drawing the ship,aliens
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);
    }
}