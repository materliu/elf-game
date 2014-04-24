import java.awt.Graphics;


public class Apple extends Food{

	public Apple(int x, int y, ElfClient ec) {
		super(x, y, ec);
		drawId = random.nextInt(2);
	}
	
	public void draw(Graphics g) {
		if(!isLive()) return;
		move();
		if(drawId == 0) {
			g.drawImage(imgs[2], x, y, null);
		} else {
			g.drawImage(imgs[3], x, y, null);
		}
	}
	

}
