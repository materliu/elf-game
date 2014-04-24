import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;


public class Food {
    public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	int x, y;
	static Random random = new Random();
	
	private boolean live = true;
	
//	String flag = null;
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	ElfClient ec = null;
	
	int drawId = 0;
	int moveStep = 50 + random.nextInt(30);
	
	public Food(int x, int y, ElfClient ec) {
		this.x = x;
		this.y = y;
		this.ec = ec;
	}
	
	public static Toolkit tk = Toolkit.getDefaultToolkit();
	public Image imgs[] = {
		tk.createImage(Food.class.getClassLoader().getResource("images/watermelon_one.png")),
		tk.createImage(Food.class.getClassLoader().getResource("images/watermelon_two.png")),
		tk.createImage(Food.class.getClassLoader().getResource("images/apple_one.png")),
		tk.createImage(Food.class.getClassLoader().getResource("images/apple_two.png"))
	};
	
	public void move() {
		if(moveStep == 0) {
			x = random.nextInt(740) + 10;
			y = random.nextInt(520) + 30;
			moveStep = random.nextInt(30) + 50;
		}
		moveStep --;
	}
	
	public Rectangle getRect() {
		return new Rectangle(x, y, WIDTH, HEIGHT);
	}

	public void draw(Graphics mg) {
		
	}
}











