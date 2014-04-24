import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;


public class Wall {
	private int x, y, width ,height;
	
	public Wall(int x, int y, int width , int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, width, height);
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.green);
		g.fillRect(x, y, width, height);
		g.setColor(c);
	}
	
}
