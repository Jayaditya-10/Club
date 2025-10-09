import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

// GameMap class to draw background
class GameMap extends JPanel {
    private BufferedImage grass, highway;

    // Load images
    public void drawApp() {
        try {
            grass = ImageIO.read(new File("SourcePictures/grass_tile.png"));
            highway = ImageIO.read(new File("SourcePictures/highway.png"));
        } catch (IOException ex) {
            System.out.println("Image not found");
            System.exit(0);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawApp();
        for (int i = 0; i < 150; i += 32) {
            for (int j = 0; j < 1000; j += 32) {
                g.drawImage(grass, i, j, null);
                g.drawImage(grass, 620 + i, j, null);
            }
        }
        g.drawImage(grass, 0, 0, null);
        g.drawImage(grass, 620, 0, null);
        g.drawImage(highway, 150, 0, null);
    }
}

// Car class with game logic
class Car extends GameMap implements ActionListener, KeyListener {
    private Timer timer;
    private BufferedImage playerCar, secondCar, thirdCar;
    private int playerX = 980, playerY = 730;
    private int secondX = 150, secondY = 400;
    private int thirdX = 400, thirdY = 0;
    private int speed = 2;
    private int velocityX = 0, velocityY = 0;
    private int score = 0;

    public Car() {
        timer = new Timer(10, this);
        JOptionPane.showMessageDialog(this, "Press OK to start the game", "Car Race", JOptionPane.INFORMATION_MESSAGE);
        timer.start();
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
    }

    // Load car images
    public void loadCars() {
        try {
            playerCar = ImageIO.read(new File("SourcePictures/PlayerCar.png"));
            secondCar = ImageIO.read(new File("SourcePictures/secondCar.png"));
            thirdCar = ImageIO.read(new File("SourcePictures/thirdCar.png"));
        } catch (IOException ex) {
            System.out.println("Image not found");
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        loadCars();
        g.drawImage(playerCar, playerX, playerY, null);
        g.drawImage(secondCar, secondX, secondY, null);
        g.drawImage(thirdCar, thirdX, thirdY, null);

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        g.drawString("Score: " + score, 300, 320);

        if (!timer.isRunning()) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 80));
            g.drawString("Game Over", 400, 500);
        }
    }

    public void move() {
        secondY += speed;
        thirdY += speed;

        if (secondY >= 920) {
            secondY = -250;
            score++;
        }
        if (thirdY >= 920) {
            thirdY = -250;
            score++;
        }
        if (checkCollision()) {
            timer.stop();
        }
        repaint();
    }

    // Collision detection
    public boolean checkCollision() {
        Rectangle playerRect = new Rectangle(playerX + 65, playerY, playerCar.getWidth() - 130, playerCar.getHeight());
        Rectangle secondRect = new Rectangle(secondX, secondY, secondCar.getWidth() - 75, secondCar.getHeight());
        Rectangle thirdRect = new Rectangle(thirdX + 65, thirdY, thirdCar.getWidth() - 100, thirdCar.getHeight());
        return playerRect.intersects(secondRect) || playerRect.intersects(thirdRect);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int c = e.getKeyCode();
        if (c == KeyEvent.VK_LEFT) {
            velocityX = -35;
            velocityY = 0;
        } else if (c == KeyEvent.VK_UP) {
            velocityX = 0;
            velocityY = -35;
        } else if (c == KeyEvent.VK_RIGHT) {
            velocityX = 35;
            velocityY = 0;
        } else if (c == KeyEvent.VK_DOWN) {
            velocityX = 0;
            velocityY = 35;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        limit();
        playerX += velocityX;
        velocityX = 0;
        playerY += velocityY;
        velocityY = 0;
        repaint();
    }

    // Limit player car movement within road boundaries
    public void limit() {
        if (playerX < 100) {
            velocityX = 0;
            playerX = 100;
        }
        if (playerX > 450) {
            velocityX = 0;
            playerX = 450;
        }
        if (playerY < 0) {
            velocityY = 0;
            playerY = 0;
        }
        if (playerY > 780) {
            velocityY = 0;
            playerY = 780;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyReleased(KeyEvent e) { }
}

// GameFrame class to create and show window
public class GameFrame extends JFrame {
    public GameFrame() {
        setTitle("Car Race");
        setBounds(0, 0, 1950, 1035);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(new Car());
        setVisible(true);
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}