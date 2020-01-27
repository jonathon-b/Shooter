package Test;

import Util.GDV5;
import gameObject.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;

public class DrawShip extends GDV5 {

	enum GameState{
		MENU, LEVEL1;
	}

	GameState g=GameState.MENU;

	boolean re=false;
	public ArrayList<Particle> parts = new ArrayList<>();
	public ArrayList<Star> stars = new ArrayList<>();
	public ArrayList<Bullet> bullets = new ArrayList<>();
	public ArrayList<Enemy> enemies = new ArrayList<>();
	Rectangle2D.Double menu[]=new Rectangle2D.Double[3];
	int score=0;

	Ship s;

	public DrawShip(){
		 s= new Ship(7,720,1280,300,300);
		 menu[0]= new Rectangle2D.Double(100,200,300,100);
		 menu[1]= new Rectangle2D.Double();
		 menu[2]= new Rectangle2D.Double();

	}

	private void controlShip(){
		s.resetVel();
		if(GDV5.KeysPressed[KeyEvent.VK_W]) {
			s.dy = Ship.baseVel*-Math.cos(s.angle);
			s.dx = Ship.baseVel*Math.sin(s.angle);
		}
		if(GDV5.KeysPressed[KeyEvent.VK_S]) {
			s.dy = Ship.baseVel*Math.cos(s.angle);
			s.dx = Ship.baseVel*-Math.sin(s.angle);
		}
		s.update();
	}

	private void rotateShip(double rotation){
		if(GDV5.KeysPressed[KeyEvent.VK_A])
			s.angle -= rotation;
		if(GDV5.KeysPressed[KeyEvent.VK_D])
			s.angle += rotation;
	}

	private void exitCheck(){
		if(GDV5.KeysPressed[KeyEvent.VK_ESCAPE])
			g=GameState.MENU;
	}

	public void addBullets(){
		double xMod=0;
		double yMod=0;
		if(GDV5.KeysPressed[KeyEvent.VK_SPACE]&&!re){
				if(Math.signum(Math.sin(s.angle))==Math.signum(s.dx)){
					xMod=s.dx/2;
				}
				if(Math.signum(Math.cos(s.angle))==Math.signum(s.dy)){
					yMod=s.dx/2;
				}
				bullets.add(new Bullet(s.rotatedGun[0], 3, Bullet.baseVel*Math.sin(s.angle), Bullet.baseVel*Math.cos(s.angle),s));
				bullets.add(new Bullet(s.rotatedGun[1], 3, Bullet.baseVel*Math.sin(s.angle), Bullet.baseVel*Math.cos(s.angle),s));
			re=true;
		}
		else if(!GDV5.KeysPressed[KeyEvent.VK_SPACE]){
			re=false;
		}
	}

	public void addParts(){
		int maxMovement=5;
		int size=3;
		int numParts=3;
		double distribution=0.5;

		if(true) {
			for(int i = 0; i < numParts; i++) {
				parts.add(new Particle(s.rotatedThrust[0].x, s.rotatedThrust[0].y, size, maxMovement, s.angle, distribution));
				parts.add(new Particle(s.rotatedThrust[1].x, s.rotatedThrust[1].y, size, maxMovement, s.angle, distribution));
			}
		}

		for(int i=0;i<1;i++) {
			if(Star.r.nextFloat()>.4)
			stars.add(new Star(3, this.getWidth(), 15, this.getHeight(),s));
		}
		addBullets();
		}

		private void addFallingEnemies(){
		double fallingEnemySize = 5;
		if(Star.r.nextFloat()<0.025){
			enemies.add(new Enemy(Star.r.nextInt((int)(this.getWidth()-fallingEnemySize)),0,0,5, fallingEnemySize));
			}
		}

		private void updateEnemies(){
			for(Enemy e:
			    enemies) {
				e.update();
			}
		}

	private void drawEnemies(Graphics2D win){
		for(Enemy e:
				enemies) {
			e.drawBound(win,Color.RED);
		}
	}

	public void moveParts(){
		for(Particle i:
		    parts) {
			i.update();
		}
		for(Bullet i:
		    bullets) {
			i.update();
		}
	}


	public void moveStars(){
		for(Star s:
		    stars) {
			s.update();
		}
	}

	private void drawBullets(Graphics2D win) {
		for(Bullet i:
		    bullets) {
			i.draw(win, Color.WHITE);
		}
	}

	public void drawParts(Graphics2D win){
		for(Particle i:
				parts) {
			i.fill(win, Color.red);
		}
	}

	void drawMenu(Graphics2D win){
		for(Rectangle2D i:
		    menu) {
			win.draw(i);
		}
	}

	public void drawStars(Graphics2D win){
		for(Star s:
		    stars) {
			s.fill(win,Color.WHITE);
		}
	}

	private void drawScore(Graphics2D win, Color c){
		win.setColor(c);
		win.setFont(new Font("TREBUCHET MS",Font.PLAIN,15));
		win.drawString("Score: "+score,5,20);
	}

	public void checkMenuTiles(){
		for(Bullet i:
		    bullets) {
				if(this.collisionDirection(menu[0], i, i.dx, i.dy)!=0)
					g=GameState.LEVEL1;
		}
	}

	void shipCollisionCheck() {
		for(Enemy e:
		    enemies) {
			if(this.collisionDirection(s.hitBox, e,e.dx,e.dy)!=0){
				g=GameState.MENU;
			}
		}
	}

	public static void main(String[] args){
		DrawShip d=new DrawShip();
		d.start();
	}

	@Override
	public void update() {
		controlShip();
		rotateShip(Math.PI/50);
		addParts(); //also adds stars
		moveParts();
		moveStars();
		stars.removeIf(Star::kill);
		parts.removeIf(Particle::kill);
		bullets.removeIf(Bullet::kill);
		if(g.equals(GameState.MENU)){
			checkMenuTiles();
		}
		else{
			addFallingEnemies();
			updateEnemies();
		}
	}

	@Override
	public void draw(Graphics2D win) {
		drawStars(win);
		drawParts(win);
		drawBullets(win);
		s.fill(win);
		if(g.equals(GameState.MENU)){
			win.setFont(new Font("GOST COMMON",Font.PLAIN,100));
			win.setColor(Color.WHITE);
			win.drawString("SHOOT TO SELECT",240,100);
			win.setFont(new Font("GOST COMMON",Font.BOLD,25));
			win.drawString("WASD TO MOVE",25,70);
			win.drawString("SPACE TO SHOOT",this.getWidth()-240,70);
			drawMenu(win);
			win.setFont(new Font("GOST COMMON",Font.PLAIN,100));
			win.drawString("PLAY",134,286);
			//System.out.println(Arrays.toString(s.xpoints));
		}
		else {
			exitCheck();
			drawScore(win,Color.GREEN);
			drawEnemies(win);
		}
	}
}
