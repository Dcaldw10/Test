package snke_the_game;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Entity {
	private int x,y,size;
	public Entity(int size){
		this.size = size;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	public void setPosition(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void move ( int dx, int dy){
		x += dx;
		y += dy;
	}
	public Rectangle getBound(){
		return new Rectangle(x,y,size, size); 
	}
	public boolean isCollsion(Entity o){
		if(o == this) 
			return false;
		return getBound().intersects(o.getBound());
	}
	public void render(Graphics2D g2d){
		g2d.fillOval(x+1,y+2,size,size);
//		Image body = null;
//		try {
//			body = ImageIO.read((ResourceLoader.load("SNKEBOD.png")));
//			//body = ImageIO.read(new File("src\\SNKEBOD.png"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		g2d.drawImage(body, x+1, y+1 , null);
	}
//	public void renderPortalOuter(Graphics2D g2d){
//		g2d.drawOval(x+1, y+1, size - 2, size - 2);
//	}
	public void renderFood(Graphics2D g2d) {
		Image food = null;
		try {
			food = ImageIO.read((ResourceLoader.load("apple.png")));
			//food = ImageIO.read(new File("src\\apple.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g2d.drawImage(food, x+1, y+1 , null);
	}
	public void renderBadFood(Graphics2D g2d) {
		Image badfood = null;
		try {
			badfood = ImageIO.read((ResourceLoader.load("badapple.png")));
			//badfood = ImageIO.read(new File("src\\badapple.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g2d.drawImage(badfood, x+1, y+1 , null);
	}
	public void renderPortal(Graphics2D g2d){
		//g2d.fillOval(x+1,y+1,size - 2,size - 2);
		Image portal = null;
		try {
			portal = ImageIO.read((ResourceLoader.load("Smallportal1.png")));
			//body = ImageIO.read(new File("src\\SNKEBOD.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g2d.drawImage(portal, x+1, y+1 , null);
	}
	public void renderPortal2(Graphics2D g2d){
		//g2d.fillOval(x+1,y+1,size - 2,size - 2);
		Image portal = null;
		try {
			portal = ImageIO.read((ResourceLoader.load("Smallportal2.png")));
			//body = ImageIO.read(new File("src\\SNKEBOD.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		g2d.drawImage(portal, x+1, y+1 , null);
	}
}
