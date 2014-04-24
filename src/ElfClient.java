import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class ElfClient extends JFrame{
	public static Random random = new Random();
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	
	Object[] options = {"简单", "一般", "复杂"};
	
	private static boolean showWinMessageFlag = false;
	
	private Image offScreenImage = null;
	
	List<Elf> elfs = new ArrayList<Elf>();
	Elf elf1 = new Elf(70, 80, this);
	Elf elf2 = new Elf(500, 500, this);
	Elf elf3 = new Elf(300, 200, this);
	
	Thief thief = new Thief(400, 200, this);
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static Image backGround = tk.createImage(ElfClient.class.getClassLoader().getResource("images/background.jpg"));
	
	private List<Food> foods = new ArrayList<Food>();
	
	public Wall w1 = new Wall(70, 100, 10, 300);
	public Wall w2 = new Wall(400, 300, 200, 10);
	
	static int foodsNumber = 10 + random.nextInt(20);
	
	public static void main(String[] args) {
		SwingOperation.run(new ElfClient(), WIDTH, HEIGHT);
	}
	
	
	public ElfClient() {
		lunchJFrame();
	}
	
	public void lunchJFrame() {
		setLocation(300, 100);
		setResizable(false);
		setTitle("ELF VS THIEF GAME");
		
		elfs.add(elf1);
		elfs.add(elf2);
		elfs.add(elf3);
		
		addKeyListener(new KeyMonitor(this));
		new Thread(new RepaintThread()).start();
	}
	
	public void paint(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(WIDTH, HEIGHT);
		}
		Graphics mg = offScreenImage.getGraphics();
		Color c = mg.getColor();
//		mg.fillRect(0, 0, ElfClient.WIDTH, ElfClient.HEIGHT);
		mg.drawImage(backGround, 0, 0, null);
		mg.setColor(Color.black);
//		mg.setColor(Color.white);
		mg.drawString("小偷当前的得分值为：" + thief.getScore(), 20, 40);
		mg.drawString("剩余水果的数量为：" + foodsNumber, 20, 60);
		mg.setColor(c);
		for(int i=0; i<elfs.size(); i++) {
			Elf elf = elfs.get(i);
			elf.draw(mg);
			elf.collideWithWall(w1);
			elf.collideWithWall(w2);
			elf.collideWithElfs(elfs);
		}
		
		thief.catchedByElfs(elfs);
		thief.draw(mg);
		thief.eatFoods(foods);
		thief.collideWithWall(w1);
		thief.collideWithWall(w2);
		
		for(int i=0; i<foods.size(); i++) {
			foods.get(i).draw(mg);
		}
		
		w1.draw(mg);
		w2.draw(mg);
		
		g.drawImage(offScreenImage, 0, 0, null);
		
		if(foods.size() < 3) {
			addFood();
			if(foods.size() == 0) {
				if(!showWinMessageFlag) {
					showWinMessageFlag = true;
					JOptionPane.showConfirmDialog(this, "你赢得了游戏", "恭喜你", JOptionPane.CLOSED_OPTION, JOptionPane.DEFAULT_OPTION);
				}
			}
		}
	}
	
	private void addFood() {
		int flag = 0;
		Watermelon watermelon = null;
		Apple apple = null;
		Food food = null;
		if(foodsNumber <= 0) {
			return;
		}
		if(foodsNumber <= 4) {
			int j = foods.size();
			for(int i=0; i<foodsNumber - j; i++) {
				flag = random.nextInt(2);
				if(flag == 0) {
					food = new Watermelon(random.nextInt(740) + 10, random.nextInt(520) + 30, this);
				} else if(flag == 1) {
				    food = new Apple(random.nextInt(740) + 10, random.nextInt(520) + 30, this);
				}
				foods.add(food);
			}
		} else {
			flag = random.nextInt(3);
			if(flag == 0) {
				watermelon = new Watermelon(random.nextInt(740) + 10, random.nextInt(520) + 30, this);
				foods.add(watermelon);
			} else if(flag == 1) {
				apple = new Apple(random.nextInt(740) + 10, random.nextInt(520) + 30, this);
				foods.add(apple);
			} else if(flag == 2) {
				watermelon = new Watermelon(random.nextInt(740) + 10, random.nextInt(520) + 30, this);
				foods.add(watermelon);
				apple = new Apple(random.nextInt(740) + 10, random.nextInt(520) + 30, this);
				foods.add(apple);
			}
		}
	}

	
	private class RepaintThread implements Runnable {
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private class KeyMonitor extends KeyAdapter {

		ElfClient ec = null;
		
		public KeyMonitor(ElfClient ec) {
			this.ec = ec;
		}
		@Override
		public void keyPressed(KeyEvent e) {
			thief.keyPressed(e);
			int keyCode = e.getKeyCode();
			switch(keyCode) {
			case KeyEvent.VK_F1:
				thief = new Thief(400, 200, ec);
				foodsNumber = 10 + random.nextInt(20);
				for(int i=0; i<elfs.size(); i++) {
					elfs.get(i).setMove(true);
				}
				foods.clear();
				break;
			case KeyEvent.VK_F2:
				
				int sel = JOptionPane.showOptionDialog(null, "请选择游戏难度", "游戏难度设定", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
						null, options, options[0]);
				if(sel == 0) {
					for(int i=0; i<elfs.size(); i++) {
						elfs.get(i).setAiLvl(AILevel.L);
					}
				} else if(sel == 1) {
					for(int i=0; i<elfs.size(); i++) {
						elfs.get(i).setAiLvl(AILevel.M);
					}
				} else if(sel == 2) {
					for(int i=0; i<elfs.size(); i++) {
						elfs.get(i).setAiLvl(AILevel.H);
					}
				}
				break;
			}
		}
		
		public void keyReleased(KeyEvent e) {
			thief.keyReleased(e);
			int keyCode = e.getKeyCode();
			switch(keyCode) {
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
			}
		}
	}
  
}
