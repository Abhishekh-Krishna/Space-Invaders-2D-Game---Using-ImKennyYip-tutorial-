import java.awt.*; //for game
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random; //for game
import javax.swing.*;

public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
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
    int shipVelocityX = tileSize; //ship moving speed
    Block ship;

    //aliens
    ArrayList<Block> alienArray;
    int alienWidth = tileSize*2;
    int alienHeight = tileSize;
    int alienX = tileSize;
    int alienY = tileSize;

    int alienRows = 2;
    int alienColumns = 3;
    int alienCount = 0; //numer of aliens to defeat
    int alienVelocityX = 1; //alien moves 1px speed

    //bullets
    ArrayList<Block> bulletArray;
    int bulletWidth = tileSize/8;
    int bulletHight = tileSize/2;
    int bulletVelocityY = -10; //bullet moving speed

    Timer gameLoop;
    int score = 0;
    boolean gameOver = false;

    SpaceInvaders() { //constructor
        setPreferredSize(new Dimension(boardWidth, boardheight)); //method that belongs to Jpanel
        setBackground(Color.black); //background colour
        setFocusable(true); //Space invaders JPanel listening for key presses
        addKeyListener(this);
        
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
        alienArray = new ArrayList<Block>();
        bulletArray = new ArrayList<Block>();

        //game timer
        gameLoop = new Timer(1000/60, this); //1000/60= 16.7
        createAliens(); //to create aliens before the timer created function
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); //call the paintComponent from JPanel
        draw(g);
    }

    public void draw(Graphics g) { //drawing the ship,aliens
        //ship 
        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);
        
        //aliens
        for(int i = 0; i < alienArray.size(); i++){
            Block alien = alienArray.get(i);
            if (alien.alive) {
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
            } 
        }

        //bullets
        g.setColor(Color.white);
        for(int i = 0; i < bulletArray.size(); i++) {
            Block bullet = bulletArray.get(i);
            if (!bullet.used) {
                g.drawRect(bullet.x, bullet.y, bullet.width,bullet.height);
            }
        }

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Aerial",Font.PLAIN, 32));
        if (gameOver) {
            g.drawString("Game Over: " + String.valueOf(score), 10, 35);
        }
        else {
            g.drawString(String.valueOf(score), 10, 35);
        }
    }

    @SuppressWarnings("SizeReplaceableByIsEmpty")
    public void move() {
        //aliens
        for(int i = 0; i < alienArray.size(); i++) {
            Block alien = alienArray.get(i);
            if (alien.alive) {
                alien.x += alienVelocityX; 

                //if alien touches the borders
                if (alien.x + alien.width >= boardWidth || alien.x <= 0){ //if the alien touches right or left side it reverses course
                    alienVelocityX *= -1;
                    alien.x +=alienVelocityX*2; 

                    //move all aliens down by one row
                    for(int j = 0; j < alienArray.size(); j++) {
                        alienArray.get(j).y += alienHeight; 
                    } 
                }

                if (alien.y >= ship.y) {
                    gameOver = true;
                }
            }
        }

        //bullets
        for(int i = 0; i < bulletArray.size(); i++) {
            Block bullet = bulletArray.get(i);
            bullet.y += bulletVelocityY;

            //bulllet collision with aliens
            for(int j = 0; j < alienArray.size(); j++) {
                Block alien = alienArray.get(j);
                if (!bullet.used && alien.alive && detectCollision(bullet, alien)) {
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += 100; 
                }
            }
        }

        //clear bullets
        while(bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 0)) {
            bulletArray.remove(0); //removes the first element of the array
        }

        //next level
        if (alienCount == 0) {
            //increase the number of columns and rows by 1
            score += alienColumns * alienRows * 100; //bonus points for clearing the level ;}
            alienColumns = Math.min(alienColumns + 1, columns/2 - 2); //cap column at 16/2-2=6
            alienRows = Math.min(alienRows + 1, rows - 6); //cap rows at 16-6=10
            alienArray.clear();
            bulletArray.clear();
            alienVelocityX = 1;
            createAliens();
        }
    }

    public void createAliens(){
        Random random = new Random(); //used for randomly selecting an image for the  aliens
        for(int r = 0; r < alienRows; r++ ){
            for(int c = 0; c < alienColumns; c++){
                int randomImgIndex = random.nextInt(alienImgArray.size());
                Block alien = new Block(
                    alienX + c*alienWidth,
                    alienY + r*alienHeight,
                    alienWidth,
                    alienHeight,
                    alienImgArray.get(randomImgIndex)
                );
                alienArray.add(alien);
            }
        }
        alienCount = alienArray.size();
    }

    public boolean detectCollision(Block a, Block b) {
        return a.x < b.x + b.width &&
               a.x + a.width > b.x && 
               a.y < b.y + b.height && 
               a.y + a.height > b.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move(); //update all the x position of the aliens every second
        repaint(); //60 frames ps it will keep repainting over and over
        if (gameOver) {
            gameLoop.stop();
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameOver) { //any key to restart
            ship.x = shipX;
            alienArray.clear();
            bulletArray.clear();
            score = 0;
            alienVelocityX = 1;
            alienColumns = 3;
            alienRows =2;
            gameOver = false;
            createAliens();
            gameLoop.start();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && ship.x - shipVelocityX >=0) { //here if we only put vk_left it can go out of border hence ship.x-shipvelocityx>=0 indicates that it cant go out of border bec left side is 0
            ship.x -= shipVelocityX; //move left by one tile
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && ship.x + shipWidth + shipVelocityX<=boardWidth) { //similar reason as above but here we use width bec x refers to left side if we add width we get right side <=boardWidth 
            ship.x += shipVelocityX; //move right by one tile
        }
        else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            Block bullet = new Block(ship.x + shipWidth*15/32, ship.y, bulletWidth, bulletHight, null);
            bulletArray.add(bullet);
        }
    } 
}
