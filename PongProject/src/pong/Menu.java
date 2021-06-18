package pong;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Menu {
	boolean isAi;
	
	//create rectangles as buttons
	public Rectangle aiButton = new Rectangle(GamePanel.GAME_WIDTH/2-50, 150, 100, 50);
	public Rectangle playButton = new Rectangle(GamePanel.GAME_WIDTH/2-50, 250, 100, 50);
	public Rectangle helpButton = new Rectangle(GamePanel.GAME_WIDTH/2-50, 350, 100, 50);
	
	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		//create 2 fonts
		Font font1 = new Font("arial", Font.BOLD, 50);
		Font font2 = new Font("arial", Font.PLAIN, 30);
		g.setFont(font1);
		g.setColor(Color.white); //set font colour to white

		g.drawString("A Game of Pong", 300, 100); //title string
		
		g.setFont(font2);
		
		//label of buttons
		g.drawString("1 vs AI", aiButton.x + 5, aiButton.y + 37);
		g.drawString("1 vs 1", playButton.x + 10, playButton.y + 37);
		g.drawString("Help", helpButton.x + 18, helpButton.y + 37);
		
		//draw rectangles
		g2d.draw(aiButton);
		g2d.draw(playButton);
		g2d.draw(helpButton);

		g.drawString("Made by: Brasen & Nathan", 325, 475);
	}
}