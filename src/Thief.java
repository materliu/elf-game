import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import sun.audio.AudioPlayer;


public class Thief {
	private int x, y, xOld, yOld;
	public int getX() {
		return x;
	}

	private int score = 0;
	
	public int getScore() {
		return score;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	private String thief_move_str = Thief.class.getClassLoader().getResource("sounds/thief_move.wav").toString();
	
	private String game_over_str = Thief.class.getClassLoader().getResource("sounds/game_over.wav").toString();
	
	private String have_a_food_str = Thief.class.getClassLoader().getResource("sounds/have_a_food.wav").toString();
	
	private boolean live = true;
	
	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	
	public static final int XSPEED = 20;
	public static final int YSPEED = 20;
	
	private ElfClient ec = null;
	
	private boolean bL = false,
					bU = false,
					bR = false,
					bD = false;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] imgs = null;
	private static Map<String, Image> images = new HashMap<String, Image>();
	
	private Direction dir = Direction.STOP;
	
	static {
		imgs = new Image[] {
			tk.createImage(Thief.class.getClassLoader().getResource("images/thief_normal.png")),
			tk.createImage(Thief.class.getClassLoader().getResource("images/thief_apple.png")),
			tk.createImage(Thief.class.getClassLoader().getResource("images/thief_watermelon.png")),
		};
		
		images.put("thief_normal", imgs[0]);
		images.put("thief_apple", imgs[1]);
		images.put("thief_watermelon", imgs[2]);
	}
	
	public Thief(int x, int y, ElfClient ec) {
		this.x = x;
		this.y = y;
		this.ec = ec;
		thief_move_str = thief_move_str.replace("file:/", "");
		game_over_str = game_over_str.replace("file:/", "");
		have_a_food_str = have_a_food_str.replace("file:/", "");
	}
	
	
	private int drawStep1 = 0;
	private int drawStep2 = 0;
	public void draw(Graphics g) {
		if(!live) return;
		if(drawStep1 > 0) {
			g.drawImage(images.get("thief_apple"), x-20, y-20, null);
			drawStep1 --;
		}else if(drawStep2 > 0) {
			g.drawImage(images.get("thief_watermelon"), x-20, y-20, null);
			drawStep2 --;
		} else { 
			g.drawImage(images.get("thief_normal"), x, y, null);
		}
	}


	
    public void keyPressed(KeyEvent e) {
    	try {
			AudioPlayer.player.start(new FileInputStream(thief_move_str));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
    	int keyCode = e.getKeyCode();
		switch(keyCode) {
		case KeyEvent.VK_UP:
			bU = true;
			break;
		case KeyEvent.VK_DOWN:
			bD = true;
			break;
		case KeyEvent.VK_LEFT:
			bL = true;
			break;
		case KeyEvent.VK_RIGHT:
			bR = true;
			break;
		}
		
		if(bU && !bD && !bL && !bR) dir = Direction.U;
		else if(!bU && bD && !bL && !bR) dir = Direction.D;
		else if(!bU && !bD && bL && !bR) dir = Direction.L;
		else if(!bU && !bD && !bL && bR) dir = Direction.R;
		else if(bU && !bD && bL && !bR) dir = Direction.LU;
		else if(bU && !bD && !bL && bR) dir = Direction.RU;
		else if(!bU && bD && bL && !bR) dir = Direction.LD;
		else if(!bU && bD && !bL && bR) dir = Direction.RD;
		else dir = Direction.STOP;
		move();
	}
    
	private void move() {
		xOld = x;
		yOld = y;
		switch(dir) {
		case L:
			x -= XSPEED;
			break;
		case U:
			y -= YSPEED;
			break;
		case R:
			x += XSPEED;
			break;
		case D:
			y += YSPEED;
			break;
		case LU:
			x -= XSPEED;
			y -= YSPEED;
			break;
		case RU:
			x += XSPEED;
			y -= YSPEED;
			break;
		case RD:
			y += YSPEED;
			x += XSPEED;
			break;
		case LD:
			x -= XSPEED;
			y += YSPEED;
			break;
		}
		detectScreenEdge();
	}

	
	public Rectangle getRect() {
		return new Rectangle(x, y, Thief.WIDTH, Thief.HEIGHT);
	}
	
	public boolean catchedByElf(Elf elf) {
		if(this.live && this.getRect().intersects(elf.getRect())) {
			try {
				AudioPlayer.player.start(new FileInputStream(game_over_str));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			this.live = false;
			this.bD = false;
			this.bU = false;
			this.bL = false;
			this.bR = false;
			for(int i=0; i<ec.elfs.size(); i++) {
				ec.elfs.get(i).setMove(false);
			}
			JOptionPane.showConfirmDialog(ec, "the game is over", "game over", JOptionPane.CLOSED_OPTION);
			return true;
		}
		return false;
	}
	
	public boolean catchedByElfs(List<Elf> elfs) {
		for(int i=0; i<elfs.size(); i++) {
			this.catchedByElf(elfs.get(i));
		}
		return false;
	}
	
	public boolean eatFood(Food food) {
		if(this.isLive() && this.getRect().intersects(food.getRect()) && food.isLive()) {
			try {
				AudioPlayer.player.start(new FileInputStream(have_a_food_str));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			ec.foodsNumber --;
			
			if(food instanceof Watermelon) {
				score += 10;
				drawStep2 = 20;
			} else {
				score += 2;
				drawStep1 = 20;
			}
			food.setLive(false);
			
			return true;
		}
		return false;
	}
	
	public boolean eatFoods(List<Food> foods) {
		for(int i=0; i<foods.size(); i++) {
			if(eatFood(foods.get(i))) {
				foods.remove(i);
				return true;
			}
		}
		return false;
	}
	
	private void detectScreenEdge() {
		if(x < 10) {
			x = 10;
		}else if(x > 750) {
			x = 750;
		}
		
		if(y < 30) {
			y = 30;
		}else if(y > 550) {
			y = 550;
		}
	}

	
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch(keyCode) {
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break;
		case KeyEvent.VK_LEFT:
			bL = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		}
	}
	
	public void collideWithWall(Wall w) {
		if(this.isLive() && this.getRect().intersects(w.getRect())) {
			this.returnOneMoveStep();
		}
	}

	private void returnOneMoveStep() {
		x = xOld;
		y = yOld;
	}
}
