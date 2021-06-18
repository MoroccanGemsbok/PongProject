package pong;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GamePanel extends JPanel implements Runnable{
	
	//create variables needed
	static final int GAME_WIDTH = 1000;
	static final int GAME_HEIGHT = (int)(GAME_WIDTH * (0.5555));
	static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
	static final int BALL_DIAMETER = 20;
	static final int PADDLE_WIDTH = 25;
	static final int PADDLE_HEIGHT = 100;
	Thread gameThread;
	Image image;
	Graphics graphics;
	Random random;
	Paddle paddle1;
	Paddle paddle2;
	Ball ball;
	Score score;
	Menu menu;
	
	//create enum
	public static enum STATE{
		MENU,
		AI,
		GAME
	};
	
	public static STATE state = STATE.MENU; //define enum, set it to menu
	
	GamePanel(){
		menu = new Menu(); //create menu object
		newPaddles(); //create paddle objects
		newBall(); //create ball object
		score = new Score(GAME_WIDTH, GAME_HEIGHT); //create score object
		this.setFocusable(true);
		this.addKeyListener(new AL()); //create key listener
		this.setPreferredSize(SCREEN_SIZE);
		this.addMouseListener(new MouseInput()); //create mouse listener
		//start game thread
		gameThread = new Thread(this);
		gameThread.start();
	}
	
	//new ball method
	public void newBall() {
		ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2),(GAME_HEIGHT/2)-(BALL_DIAMETER/2),BALL_DIAMETER,BALL_DIAMETER);
	}
	
	//new paddle method
	public void newPaddles() {
		paddle1 = new Paddle(0,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,1);
		paddle2 = new Paddle(GAME_WIDTH-PADDLE_WIDTH,(GAME_HEIGHT/2)-(PADDLE_HEIGHT/2),PADDLE_WIDTH,PADDLE_HEIGHT,2);
	}
	
	//paint method
	public void paint(Graphics g) {
		image = createImage(getWidth(), getHeight());
		graphics = image.getGraphics();
		draw(graphics); //call draw method
		g.drawImage(image, 0, 0, this);
	}
	
	//draw method
	public void draw(Graphics g) {
		//if state is not menu, call the game
		if(state != STATE.MENU) {
			repaint();
			//draws paddles, balls, and score
			paddle1.draw(g); 
			paddle2.draw(g);
			ball.draw(g);
			score.draw(g);
			Toolkit.getDefaultToolkit().sync(); //makes game smoother
		}
		else if(state == STATE.MENU) {
			menu.draw(g); //call menu
		}
	}
	
	//refreshes the location of the objects
	public void move() {
		paddle1.move();
		paddle2.move();
		ball.move();
	}
	
	//check object collision
	public void checkCollision() {
		
		//bounce ball off top & bottom window edges
		if(ball.y <=0) {
			ball.setYDirection(-ball.yVelocity);
		}
		if(ball.y >= GAME_HEIGHT-BALL_DIAMETER) {
			ball.setYDirection(-ball.yVelocity);
		}
		
		//bounces ball off paddles
		if(ball.intersects(paddle1)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.setXDirection(ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		if(ball.intersects(paddle2)) {
			ball.xVelocity = Math.abs(ball.xVelocity);
			ball.setXDirection(-ball.xVelocity);
			ball.setYDirection(ball.yVelocity);
		}
		
		//stops paddles at window edges
		if(paddle1.y<=0) {
			paddle1.y=0;
		}
		if(paddle1.y>= (GAME_HEIGHT-PADDLE_HEIGHT)) {
			paddle1.y = GAME_HEIGHT-PADDLE_HEIGHT;
		}
		if(paddle2.y<=0) {
			paddle2.y=0;
		}
		if(paddle2.y>= (GAME_HEIGHT-PADDLE_HEIGHT)) {
			paddle2.y = GAME_HEIGHT-PADDLE_HEIGHT;
		}
		//give a player one point and creates new paddles & ball
		if(ball.x <= 0) {
			score.player2++;
			newPaddles();
			newBall();
		}
		if(ball.x >= GAME_WIDTH-BALL_DIAMETER) {
			score.player1++;
			newPaddles();
			newBall();
		}
	}
	
	//run thread
	public void run() {
		//game loop
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while(true) {
			long now = System.nanoTime();
			delta += (now - lastTime)/ns;
			lastTime = now;
			if(delta >= 1) {
				move();
				checkCollision();
				repaint();
				delta--;
			}
		}
	}
	
	//key adapter
	public class AL extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			paddle1.keyPressed(e);
			paddle2.keyPressed(e);
		}
		public void keyReleased(KeyEvent e) {
			paddle1.keyReleased(e);
			paddle2.keyReleased(e);
		}
	}
}