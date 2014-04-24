import java.awt.Graphics;


public class Watermelon extends Food{
	
	
	
	public Watermelon(int x, int y, ElfClient ec) {
		super(x, y, ec);
		drawId = random.nextInt(2);
	}

	public void draw(Graphics g) {
		if(!isLive()) return;
		move();
		if(drawId == 0) {
			g.drawImage(imgs[0], x, y, null);
		} else {
			g.drawImage(imgs[1], x, y, null);
		}
	}
}
