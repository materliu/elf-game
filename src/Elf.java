import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.List;
import java.util.Random;


public class Elf {
	private int x, y;
	private int xOld, yOld;
	
	public static final int WIDTH = 40;
	public static final int HEIGHT = 40;
	
	public static Random random = new Random();
	
	private boolean move = true;
	
	public boolean isMove() {
		return move;
	}

	public void setMove(boolean move) {
		this.move = move;
	}

	private AILevel aiLvl = AILevel.M;
	
	public AILevel getAiLvl() {
		return aiLvl;
	}

	public void setAiLvl(AILevel aiLvl) {
		this.aiLvl = aiLvl;
	}

	public static int XSPEED = 10;
	public static int YSPEED = 10;
	
	private ElfClient ec = null;
	
	private Direction dir = Direction.values()[random.nextInt(4)];
	
	private int step = 0;
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] imgs= new Image[2];
	
	static {
		imgs[0] = tk.createImage(Elf.class.getClassLoader().getResource("images/elf1.png"));
		imgs[1] = tk.createImage(Elf.class.getClassLoader().getResource("images/elf2.png"));
	}
	
	public Elf(int x, int y, ElfClient ec) {
		this.x = x;
		this.y = y;
		this.ec = ec;
	}
	
	public void draw(Graphics g) {
		switch(aiLvl) {
		case L:
			moveLevelL();
			break;
		case M:
			moveLevelM();
			break;
		case H:
			moveLevelH();
			break;
		}
		if(step > 20) {
			g.drawImage(imgs[1], x, y, null);
			if(40 == step) {
				step = 0;
			}
		} else {
			g.drawImage(imgs[0], x, y, null);
		}
		step ++;
	}
	
	private void moveLevelH() {
		if(!isMove()) return;
		if(!ec.thief.isLive()) {
			moveLevelL();
		} else {
			if(random.nextInt(40) > 20) {
			}else {
				return ;
			}
			int thiefX = ec.thief.getX();
			int thiefY = ec.thief.getY();
			int xSpeed = 2;
			int ySpeed = 0;
			try {
				if(thiefX - x > 20) {
					ySpeed = Math.abs((thiefY - y) * xSpeed /(thiefX - x));
				} else {
					xSpeed = 1;
					ySpeed = 1; 
				}
			} catch (ArithmeticException e) {
				//do nothing
			}
			if(thiefX < x) {
				if(thiefY < y) {
					dir = Direction.LU;
				} else {
					dir = Direction.LD;
				}
			} else {
				if(thiefY < y) {
					dir = Direction.RU;
				} else {
					dir = Direction.RD;
				}
			}
			
			xOld = x;
			yOld = y;
			switch(dir) {
			case LU:
				x -= xSpeed;
				y -= ySpeed;
				break;
			case RU:
				x += xSpeed;
				y -= ySpeed;
				break;
			case RD:
				y += ySpeed;
				x += xSpeed;
				break;
			case LD:
				x -= xSpeed;
				y += ySpeed;
				break;
			}
		}
	}

	private void moveLevelL() {
		if(!isMove()) return;
		if(random.nextInt(40) > 35) {
			
		}else {
			return ;
		}
		if(random.nextInt(40) > 30) {
			dir = Direction.values()[random.nextInt(4)];
		}
		
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
		}
		
		detectScreenEdge();
	}
	
    private void moveLevelM() {
		if(!isMove())return;
		if(random.nextInt(40) > 30) {
			
		}else {
			return ;
		}
		if(random.nextInt(40) > 30) {
			dir = Direction.values()[random.nextInt(8)];
		}
		
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
	
	public Rectangle getRect() {
		return new Rectangle(this.x, this.y , Elf.WIDTH, Elf.HEIGHT);
	}
	
	public boolean collideWithElf(Elf elf) {
		if(this.getRect().intersects(elf.getRect())) {
			if(aiLvl == AILevel.H) {
				this.dir = Direction.STOP;
				switch(elf.dir){
				case LU:
					elf.x += 50;
					elf.y += 50;
					break;
				case RU:
					elf.x -= 50;
					elf.y += 50;
					break;
				case RD:
					elf.y -= 50;
					elf.x -= 50;
					break;
				case LD:
					elf.x += 50;
					elf.y -= 50;
					break;
				}
			} else {
			}
			return true;
		}
		return false;
	}
	
	public boolean collideWithElfs(List<Elf> elfs) {
		for(int i=0; i<elfs.size(); i++) {
			if(this != elfs.get(i)) {
				this.collideWithElf(elfs.get(i));
			}
		}
		return false;
	}
	
	public void collideWithWall(Wall w) {
		if(this.getRect().intersects(w.getRect())) {
			this.returnOneMoveStep();
		}
	}
	
	public void returnOneMoveStep() {
		x = xOld;
		y = yOld;
	}
}





















