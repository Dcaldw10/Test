package snke_the_game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Window;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Dimension2D;
import java.awt.image.BufferedImage;
import java.awt.peer.KeyboardFocusManagerPeer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

@SuppressWarnings({ "serial", "unused" })
public class GamePanel extends JPanel implements Runnable, KeyListener,
		KeyboardFocusManagerPeer {

	@Override
	public void clearGlobalFocusOwner(Window arg0) {

	}
	public static final int WIDTH = 600;
	public static final int HEIGHT = 600;
	//Render
	private Graphics2D g2d;
	private BufferedImage image;
	
	
	//game loop
	private Thread thread;
	private boolean running;
	private long targetTime;
	public GamePanel(){
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
		addKeyListener(this);
		
		
	}
	//game stuff
	private final int SIZE = 20;
	private Entity head,food,badFood,portal1,portal2;
	private ArrayList<Entity> snake;
	private int score;
	private int level;
	private int timeConfused;
	private boolean gameover,confused=false;
	
	
	//movement
	private int dx,dy;
	
	//key inputs
	private boolean up,down,right,left,start;
	

	
	public void addNotify() { 
		super.addNotify();
		thread = new Thread	(this);
		thread.start();
	}
	private void setUpLevel(){
		snake = new ArrayList<Entity>();
		head = new Entity(SIZE);
		head.setPosition(WIDTH / 2, HEIGHT / 2);
		snake.add(head);
		
		
		for(int i = 1; i<3; i++){
			Entity e = new Entity(SIZE);
			e.setPosition(head.getX() + (i*SIZE), head.getY());
			snake.add(e);
			

			
		}
		portal1 = new Entity(20);
		portal2 = new Entity(20);
		setPortals();
		food = new Entity(SIZE);
		badFood = new Entity(SIZE);
		setBadFood();
		setFood();
		score = 0;
		
		
	}
	public void setFood(){
		int x = (int)(Math.random() * (WIDTH - SIZE));
		int y = (int)(Math.random() * (HEIGHT - SIZE));
		x = x - (x % SIZE);
		y = y - (y % SIZE);
		
		food.setPosition(x,y);
		if(foodIntersectsPortals()){
			setFood();
		}
		if(food.getX() == 0 || food.getY() == 0 || food.getX() == WIDTH - 20 || food.getY() == HEIGHT-20)
			setFood();
		
	}
	
	public void setBadFood(){
		int x = (int)(Math.random() * (WIDTH - SIZE));
		int y = (int)(Math.random() * (HEIGHT - SIZE));
		x = x - (x % SIZE);
		y = y - (y % SIZE);
		
		badFood.setPosition(x,y);
		if(foodIntersectsPortals()){
			setBadFood();
		}
	}
	public void setPortals(){
		setPortal1();
		setPortal2();
	}
	
	
	
	public void setPortal1(){
		int x = (int)(Math.random() * (WIDTH - SIZE));
		int y = (int)(Math.random() * (HEIGHT - SIZE));
		x = x - (x % SIZE);
		y = y - (y % SIZE);
		portal1.setPosition(x, y);
		
	}
	public void setPortal2(){
		int x = (int)(Math.random() * (WIDTH - SIZE));
		int y = (int)(Math.random() * (HEIGHT - SIZE));
		x = x - (x % SIZE);
		y = y - (y % SIZE);
		portal2.setPosition(x, y);
		if (portal2.getX()==portal1.getX() && portal2.getY()==portal1.getY()){
			setPortal2();
		}
		
	}
	private boolean foodIntersectsPortals() {
		boolean test1 = false,
				test2 = false,
				test3 = false,
				test4 = false;
		if(food.getX()==portal1.getX()) test1 = true;
		if(food.getY()==portal1.getY()) test2 = true;
		if(food.getX()==portal2.getX()) test3 = true;
		if(food.getY()==portal2.getY()) test4 = true;
		if(badFood.getX()==portal1.getX()) test1 = true;
		if(badFood.getY()==portal1.getY()) test2 = true;
		if(badFood.getX()==portal2.getX()) test3 = true;
		if(badFood.getY()==portal2.getY()) test4 = true;
		
		if(test1 && test2 && test3 && test4)
			return true;
		else
			return false;
	}
	private void setFPS(int fps){
		targetTime = 1000 / fps;
	}
	
	
	private int getFPS(){
		return (int) targetTime;
	}
	
	
	
	@Override
	public Component getCurrentFocusOwner() {
		
		return null;
	}

	@Override
	public Window getCurrentFocusedWindow() {
		
		return null;
	}

	@Override
	public void setCurrentFocusOwner(Component arg0) {
		

	}

	@Override
	public void setCurrentFocusedWindow(Window arg0) {
		

	}

	@Override
	
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		if (k == KeyEvent.VK_W || k == KeyEvent.VK_UP){
			up = true;
			
		}
		if (k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN){
			down = true;

		}
		if (k == KeyEvent.VK_A || k == KeyEvent.VK_LEFT){
			left = true;

		}
		if (k == KeyEvent.VK_D || k == KeyEvent.VK_RIGHT){
			right = true;

		}
		if ( k == KeyEvent.VK_ENTER) 
			start = true;

		

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int k = e.getKeyCode();

		if ( k == KeyEvent.VK_ENTER) 
			start = false;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		

	}

	@Override
	public void run() {
		if(running) return;
		init();
		long startTime;
		long elapsed;
		long wait;
		while(running){
			startTime = System.nanoTime();
			update();
			requestRender();
			elapsed = System.nanoTime() - startTime;
			wait = targetTime - elapsed / 1000000;
			
			if(wait > 0 ) {
				try{
					Thread.sleep(wait);
					
				}catch(Exception e ){
					e.printStackTrace();
				}
			}
		}
	}
	private void requestRender() {
		render(g2d);
		Graphics f = getGraphics();
		f.drawImage(image, 0,0, null);
		f.dispose();
	}

	private void update() {
		if(!confused){
		if(up && dy == 0 && dx!=0 && !left && !right){
			dy = -SIZE;
			dx = 0;
			up = false;
			
		}
		if(down && dy == 0 && dx != 0 && !left && !right){
			dy = SIZE;
			dx = 0;
			down = false;
		}
		if(left && dx == 0 && !up && !down){
			dy = 0;
			dx = -SIZE;
			left = false;
		}
		if(right && dx == 0 && dy != 0 && !up && !down){
			dy = 0;
			dx = SIZE;
			right = false;
		}
		}
		else if(confused){
			
			if(up && dy == 0 && dx!=0 && !left && !right){
				dy = SIZE;
				dx = 0;
				up = false;
				timeConfused++;
				
			}
			if(down && dy == 0 && dx != 0 && !left && !right){
				dy = -SIZE;
				dx = 0;
				down = false;
				timeConfused++;
			}
			if(left && dx == 0 && !up && !down){
				dy = 0;
				dx = SIZE;
				left = false;
				timeConfused++;
			}
			if(right && dx == 0 && dy != 0 && !up && !down){
				dy = 0;
				dx = -SIZE;
				right = false;
				timeConfused++;
			}
			
			
			
			
			
		}
		if(dx !=0 || dy !=0){
			for (int i = snake.size() - 1; i > 0; i--){
				snake.get(i).setPosition(snake.get(i - 1).getX(), snake.get(i - 1).getY());
			}	
		
			head.move(dx, dy);
		}
		
		for(Entity e : snake){
			if(e.isCollsion(head)){
				gameover = true;
				break;
			}
		
		
		
		
	}
		
		if(food.isCollsion(head)){
			
			score++;	

			//File Yum = new File("src\\GoodFood.wav");;
			//InputStream Yum = (ResourceLoader.load("GoodFood.wav"));
			//MainClass.PlayAud(Yum);
			PlayAud("/GoodFood.wav");
			
			setFood();
			confused = false;
			Entity e = new Entity(SIZE);
			e.setPosition(-100,-100);
			snake.add(e);
			if (score % 10 == 0){
				level++;
				if (level > 10)
					level = 10;
				setFPS(level * 5);
				setPortals();
				
			}
			
		}
		if(badFood.isCollsion(head)){
						
			setBadFood();
			confused = true;
			
			//File Yuck = new File("src\\BadFood.wav");;
			//InputStream Yuck = (ResourceLoader.load("BadFood.wav"));
			//MainClass.PlayAud(Yuck);
			PlayAud("/BadFood.wav");
			}
			
			
		if(head.isCollsion(portal1))
			head.setPosition(portal2.getX(), portal2.getY());
		else if(head.isCollsion(portal2))
			head.setPosition(portal1.getX(), portal1.getY());	
		if(head.getX() < 0)
			head.setX(WIDTH-20);
		if(head.getY() < 0)
			head.setY(HEIGHT-20);
		if(head.getX() > WIDTH-20)
			head.setX(0);
		if(head.getY() > HEIGHT-20)
			head.setY(0);
		
		left = false;
		right = false;
		up = false;
		down = false;
		
	}
	public void render(Graphics2D g2d){
	
		//BackGround Image
		
		
		
		Image BackGround = null, BackGround2 = null, Title = null;
		Image Ready = null, Confused = null, GameOver = null, Sonic = null;
		
		try {			
			//BackGround = ImageIO.read(new File("src\\BackGroundImage3.png"));
			//BackGround2 = ImageIO.read(new File("src\\SoftEdge.png"));
			//Title = ImageIO.read(new File("src\\TitlesAndOtherText.png"));
			
			
			BackGround = ImageIO.read((ResourceLoader.load("BackGroundImage3.png")));
			BackGround2 = ImageIO.read((ResourceLoader.load("SoftEdge.png")));
			Title = ImageIO.read((ResourceLoader.load("TitlesAndOtherText.png")));
	
			
			Ready = ((BufferedImage) Title).getSubimage(0, 0, 561, 211);
			Confused = ((BufferedImage) Title).getSubimage(0, 213, 561, 80);
			GameOver = ((BufferedImage) Title).getSubimage(0, 296, 561, 80);
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		g2d.drawImage(BackGround, 0, 0, WIDTH, HEIGHT, null);
		g2d.drawImage(BackGround2, 0, 0, WIDTH, HEIGHT, null);
		
		
		g2d.setColor(Color.ORANGE);
		for(Entity e : snake){
			e.render(g2d);
		}
		
		badFood.renderBadFood(g2d);
		food.renderFood(g2d);
		
		
		//Font Style
		Font myFont = new Font("forte", Font.BOLD, 45);
		g2d.setFont(myFont);
		
		if(confused)
			g2d.drawImage(Confused, 90, 250, 400, 80, null);

		
		if(gameover){

			confused = false;
			g2d.drawImage(GameOver, 90, 250, 400, 80, null);
			
			try {
				int x = 20;
				PlayAud("/YouLose.wav");
				Thread.sleep(1000, 1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			//File Lose = new File("src\\YouLose.wav");;
//			//InputStream Lose = (ResourceLoader.load("YouLose.wav"));
			dx=0;dy=0;
			if(start){
				setUpLevel();
				dx = 0;
				dy = 0;
				setFPS(5);
				confused = false;
				gameover = false;
				
			

			}
		
		
		}
		portal1.renderPortal(g2d);
		portal2.renderPortal2(g2d);

		g2d.setColor(Color.WHITE);
		g2d.drawString("Score: " + score*10 , 10, 50);
		g2d.drawString("Level: " + level , 430, 50);
		//g2d.drawString("FPS: " + getFPS() , 400, 550);
		
		
		
		if(dx == 0 &&  dy ==0 && gameover == false){
			
			g2d.drawImage(Ready, 90, 200, 400, 180, null);
			
		}
	}

	private void init(){
		image =  new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g2d = image.createGraphics();
		running = true;
		setUpLevel();
		level = 1;
		gameover = false;
		setFPS(5);
	}
	
	
	private void PlayAud(String locate) {
		try {
			InputStream music = getClass().getResourceAsStream(locate);
			AudioStream stream = new AudioStream(music);
			AudioPlayer.player.start(stream);
		} 
		catch (Exception e) {
		}	     
		
	}
	
}



