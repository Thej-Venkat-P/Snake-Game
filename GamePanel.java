import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {

    static final int ScreenWidth = 500;
    static final int ScreenHeight = 500;
    static final int UnitSize = 25;
    static final int GameUnits = (ScreenWidth * ScreenHeight) / (UnitSize * UnitSize);
    static final int Delay = 100;
    final int x[] = new int[GameUnits];
    final int y[] = new int[GameUnits];
    int bodyParts = 5;
    int applesEaten = 0;
    Random random;
    int appleX, appleY;
    char direction = 'R';
    boolean running = false;
    Timer timer;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(ScreenWidth, ScreenHeight));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(Delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            for (int i = 0; i < ScreenHeight / UnitSize; i++) {
                g.drawLine(i * UnitSize, 0, i * UnitSize, ScreenHeight);
                g.drawLine(0, i * UnitSize, ScreenWidth, i * UnitSize);
            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, UnitSize, UnitSize);
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], UnitSize, UnitSize);
                } else {
                    g.setColor(new Color(0, 180, 90));
                    g.fillRect(x[i], y[i], UnitSize, UnitSize);
                }
            }
        }
        else{
            gameOver(g);
        }
    }

    public void newApple() {
        appleX = random.nextInt(ScreenWidth / UnitSize) * UnitSize;
        appleY = random.nextInt(ScreenHeight / UnitSize) * UnitSize;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - UnitSize;
                break;
            case 'D':
                y[0] = y[0] + UnitSize;
                break;
            case 'L':
                x[0] = x[0] - UnitSize;
                break;
            case 'R':
                x[0] = x[0] + UnitSize;
                break;
        }
    }

    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        for (int i = 1; i < bodyParts; i++) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        if (x[0] < 0 || x[0] >= ScreenWidth || y[0] < 0 || y[0] >= ScreenHeight) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Aria",Font.BOLD,40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten,(ScreenWidth-metrics.stringWidth("Score: "+applesEaten))/2,g.getFont().getSize());
        g.setColor(Color.red);
        g.setFont(new Font("Aria",Font.BOLD,75));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over",(ScreenWidth-metrics1.stringWidth("Game Over"))/2,ScreenHeight/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_D:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_W:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_S:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
