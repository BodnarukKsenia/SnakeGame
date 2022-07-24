package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {


    private final int SIZE = 320;
    private final int DOT_SIZE = 1;
    private final int ALL_DOTS = 400;
    private Image dot;
    private Image apple;
    private int appleX; //позиция яблока
    private int appleY;
    private int[] x = new int[ALL_DOTS];
    private  int[] y = new int[ALL_DOTS];
    private int dots;
    private Timer timer;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = true;
    public GameField(){

        setBackground(Color.DARK_GRAY);
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        setFocusable(true); //метод для связи игрового поля и игровых клавиш. фокус на игровом поле.
    }

    public void loadImages(){ //загрузка изображения

        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();
    }
    public void initGame(){ // инициализация игры
        dots = 30;
        for(int i = 0; i<dots; i++){
            x[i]=48 - i*DOT_SIZE;
            y[i]= 48;
        }
        timer = new Timer(50,this);
        timer.start();
        createApple();
    }
    public void createApple(){ //создания яблока
        appleX = new Random().nextInt(20)*DOT_SIZE;
        appleY = new Random().nextInt(20)*DOT_SIZE;
    }
    @Override
    public void actionPerformed(ActionEvent e) { //запускается метод каждый раз когда тикает таймер каждые 10 мс
        if(inGame){
            checkApple();
            checkCollisions();
            move();
        }
        repaint();
    }
    public void move(){
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        if(left){
            x[0] -= DOT_SIZE;
        }
        if(right){
            x[0] += DOT_SIZE;
        }
        if(up){
            y[0] -= DOT_SIZE;
        }
        if(down){
            y[0] += DOT_SIZE;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(inGame){ //состояние в игре
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
        }
        else {
            String str = "Game over";
            Font f = new Font("Arial", 14, Font.BOLD);
            g.setColor(Color.BLUE);
            g.setFont(f);
            g.drawString(str, 124, SIZE/2);
        }
    }




    public void checkApple(){
        if(x[0] == appleX && y[0] == appleY){
            dots++;
            createApple();
        }
    }
    public void checkCollisions(){ //проверка на столкновение с самим собой или бордюром, это возможно если размер змейки больше 4х ячеек
        for (int i = dots; i > 0 ; i--) {
            if(i>4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }
        if(x[0]> SIZE){
            inGame = false;
        }
        if (x[0]<0){
            inGame = false;
        }
        if (y[0]>SIZE){
            inGame = false;
        }
        if (y[0]<0){
            inGame = false;
        }
    }

    class FieldKeyListener extends KeyAdapter { //управление клавишами
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if(key == KeyEvent.VK_LEFT && ! right){
                left = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_RIGHT && ! left){
                right = true;
                up = false;
                down = false;
            }
            if(key == KeyEvent.VK_UP && ! down){
                left = false;
                up = true;
                down = false;
            }
            if(key == KeyEvent.VK_DOWN && ! up){
                left = false;
                up = false;
                down = true;
            }
        }
    }
}

