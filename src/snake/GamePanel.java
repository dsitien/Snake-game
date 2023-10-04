/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;
import java.util.Random;
import javax.swing.*;

/**
 *
 * @author CNM
 */
public class GamePanel extends JPanel implements ActionListener {

    static final int screen_width = 600;
    static final int screen_height = 600;
    static final int unit_size = 25; // độ lớn vật thể 1 ô trong game
    static final int game_unit = (screen_height * screen_width) / unit_size;
    static final int delay = 150; // độ trễ game

    //chứa toạn độ của con rắn
    final int x[] = new int[game_unit];
    final int y[] = new int[game_unit];

    // bộ phân con rắn ban đầu có chiều dài
    int bodyParts = 6;

    //khai báo trái táo
    int applesEaten;
    int appleX;
    int appleY;

    // khởi tạo hướng
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;

    //tạo nút start
    private JButton startButton;
    private JButton restartButton;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(screen_width, screen_height));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

        startGame();

    }

    public void newApple() {
        appleX = random.nextInt((int) (screen_width / unit_size)) * unit_size;
        appleY = random.nextInt((int) (screen_height / unit_size)) * unit_size;

    }

    public void startGame() {
        
        newApple();
        running = true;
        timer = new Timer(delay, this);
        timer.start();
    }
//    public void startGame() {
//        //...
//        startButton = new JButton("Start");
//        startButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                startButton.setEnabled(false); // Vô hiệu hóa nút bắt đầu sau khi đã bắt đầu
//                startButton.remove(startButton);
//                running = true;
//                timer = new Timer(delay, GamePanel.this);
//                timer.start();
//                requestFocusInWindow(); // Đảm bảo rằng panel có trọng tâm chú ý để phím có thể hoạt động
//            }
//        });
//        startButton.setBounds(screen_width / 2 - 50, screen_height / 2, 100, 50);
//        this.add(startButton);
//        //...
//    }

    public void restartGame() {
        applesEaten = 0;
        bodyParts = 6;
        direction = 'R';
        newApple();
        running = true;
        timer.restart(); // Khởi động lại đồng hồ đếm thời gian
        restartButton.setVisible(false); // Ẩn nút chơi lại
        requestFocusInWindow(); // Đảm bảo rằng panel có trọng tâm chú ý để phím có thể hoạt động
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        if (running) {
//            //...
//            startButton.setEnabled(false);
//            restartButton.setVisible(false);
//            draw(g);
//        } else {
//            startButton.setEnabled(true);
//            restartButton.setVisible(true);
//            gameOver(g);
//        }
//        
//    }

    public void draw(Graphics g) {

        if (running) {
            //tạo một lưới trên màn hình 
            for (int i = 0; i < screen_height / unit_size; i++) {

                g.drawLine(i * unit_size, 0, i * unit_size, screen_height);
                g.drawLine(0, i * unit_size, screen_width, i * unit_size);
            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, unit_size, unit_size);

            //vẽ bộ phận cơ thể
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], unit_size, unit_size);
                } else {
                    g.setColor(Color.PINK);
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                    g.fillRect(x[i], y[i], unit_size, unit_size);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40) {
            });
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + applesEaten, (screen_width - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];

        }
        switch (direction) {
            case 'U':
                y[0] = y[0] - unit_size;

                break;
            case 'D':
                y[0] = y[0] + unit_size;

                break;
            case 'L':
                x[0] = x[0] - unit_size;

                break;
            case 'R':
                x[0] = x[0] + unit_size;

                break;

        }

    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }

    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
            }
            //ktra đầu chạm viền bên trái
            if (x[0] < 0) {
                running = false;
            }
            //ktra đầu chạm viền phải
            if (x[0] > screen_width) {
                running = false;
            }
            //ktra đầu chạm viền trên
            if (y[0] < 0) {
                running = false;
            }
            //ktra đầu chạm viền dưới
            if (y[0] > screen_height) {
                running = false;
            }
            if (!running) {
                timer.stop();
            }
        }
    }

    public void gameOver(Graphics g) {
        //Score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40) {
        });
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (screen_width - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());

        //Game over text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75) {
        });
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (screen_width - metrics1.stringWidth("Game Over")) / 2, screen_height / 2);
        
//        restartButton = new JButton("Play Again");
//        restartButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//               //GamePanel();
//            }
//        });
//         restartButton.setBounds(screen_width / 2 - 100, screen_height / 2 + 50, 200, 50);
//          this.add(restartButton);
    }
//    public void gameOver(Graphics g) {
//        //...
//        restartButton = new JButton("Play Again");
//        restartButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                restartGame();
//            }
//        });
//        restartButton.setBounds(screen_width / 2 - 100, screen_height / 2 + 50, 200, 50);
//        this.add(restartButton);
//        restartButton.setVisible(false); // Ẩn nút chơi lại ban đầu
//        //...
//    }

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
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }

                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }

                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }

                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }

                    break;
            }
        }
    }

}
