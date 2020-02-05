package Test;

import Util.GDV5;
import Util.SoundDriverHo;
import gameObject.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;

public class DrawShip extends GDV5 {

	enum GameState{
		MENU, LEVEL1;
	}

	GameState g=GameState.MENU;

	static Random r = new Random();
	boolean re=false;
	public static ArrayList<ExplosionArray> Explosions = new ArrayList<>();
	public ArrayList<Particle> parts = new ArrayList<>();
	public ArrayList<Star> stars = new ArrayList<>();
	public ArrayList<Bullet> bullets = new ArrayList<>();
	public ArrayList<Enemy> enemies = new ArrayList<>();
	Rectangle2D.Double menu[]=new Rectangle2D.Double[3];
	String sounds[]=new String[2];
	int transX=0,transY=0;
	static int rumbleCount=0;
	final static int maxRumble=20;
	static int rumbleX[]=new int[maxRumble], rumbleY[]=new int[maxRumble];
	public static SoundDriverHo sd;
	int score=0;

	Ship s;

	public DrawShip(){
		for(int i=0;i<maxRumble;i++) {
			rumbleX[i] = 0;
			rumbleY[i] = 0;
		}
		 s= new Ship(7,720,1280,1280/2+5,720,3);
		 sounds[0]="jonathon_bower_laser.wav";
		 sounds[1]="jonathon_bower_explosion.wav";
		 sd=new SoundDriverHo(sounds,this);
		 sd.setVolume(0,(float)0.0005);
		 sd.setVolume(1,6);
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

	public static void addRumble(int bound){
		for(int i=0;i<rumbleX.length;i++){
			rumbleX[i]=r.nextInt(bound);
			rumbleY[i]=r.nextInt(bound);
		}
		rumbleCount=0;
	}

	public void updateRumble(){
		if(rumbleCount<rumbleX.length) {
			transX=rumbleX[rumbleCount];
			transY=rumbleY[rumbleCount];
			rumbleCount++;
		}
		else {
			transX = 0;
			transY = 0;
		}
		}

	private void returnToMenu(){
		s.lives=3;
		g=GameState.MENU;
		enemies.clear();
	}

	private void exitCheck(){
		if(GDV5.KeysPressed[KeyEvent.VK_ESCAPE]) {
			returnToMenu();
		}
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
				bullets.add(new Bullet(s.rotatedGun[0], Bullet.baseVel*Math.sin(s.angle), Bullet.baseVel*Math.cos(s.angle),s,false));
				bullets.add(new Bullet(s.rotatedGun[1],  Bullet.baseVel*Math.sin(s.angle), Bullet.baseVel*Math.cos(s.angle),s,false));
				if(!sd.isPlaying(0))sd.play(0);
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
				parts.add(new Thruster(s.rotatedThrust[0].x, s.rotatedThrust[0].y, size, maxMovement, s.angle, distribution));
				parts.add(new Thruster(s.rotatedThrust[1].x, s.rotatedThrust[1].y, size, maxMovement, s.angle, distribution));
			}
		}

		for(int i=0;i<1;i++) {
			if(Star.r.nextFloat()>.4)
			stars.add(new Star(3, this.getWidth(), 15, this.getHeight(),s));
		}
		addBullets();
		}

		private void addAstroids(){
		double asteroidSize = 5;
		if(Star.r.nextFloat()<0.025){
			enemies.add(new Enemy(Star.r.nextInt((int)(this.getWidth()-asteroidSize)),0,0,5, asteroidSize));
			}
		}


		private void updateEnemies(){
				for(Enemy e :
						enemies) {
					e.update();
					if(e instanceof ShipEnemy)
						((ShipEnemy) e).shoot((float)0.8,bullets);
					for(Bullet b:
					    bullets) {
						e.collisionCheck(b);
					}
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

	private void addNormalEnemies(int num){
		for(int i=0;i<num;i++) {
			int size=60;
			if(r.nextFloat()>0.992) {
				enemies.add(new ShipEnemy(r.nextInt(this.getWidth()-size),0,r.nextInt(5)-2,r.nextInt(7)+1,size,this));
			}
		}
	}

	private void drawScore(Graphics2D win, Color c){
		win.setColor(c);
		win.setFont(new Font("TREBUCHET MS",Font.PLAIN,15));
		win.drawString("Lives: " + s.lives,5,20);
		win.drawString("Score: " +score,5,40);
	}

	public void checkMenuTiles(){
		for(Bullet i:
		    bullets) {
				if(this.collisionDirection(menu[0], i, i.dx, i.dy,0,0)!=0)
					g=GameState.LEVEL1;
		}
	}

	void shipCollisionCheck() {
		for(Enemy e:
		    enemies) {
			if(this.collisionDirection(s.hitBox, e,e.dx,e.dy,s.dx,s.dy)!=0){
				e.killEnemy(e.getX(),e.getY());
				if(s.isAlive()){s.lives--;}
			}
		}
		enemies.removeIf(e -> e.isDead());
		if(!s.isAlive()){returnToMenu();}
	}

	public void updateExplosions(){
		for(ExplosionArray e:
				Explosions) {
			e.update();
		}
	}


	public void drawExplosions(Graphics2D win, Color c){
		for(ExplosionArray e:
				Explosions) {
			e.fill(win, c);
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
		updateExplosions();
		updateRumble();
		stars.removeIf(Star::kill);
		parts.removeIf(Particle::kill);
		bullets.removeIf(Bullet::kill);
		Explosions.removeIf(ExplosionArray::kill);
		if(g.equals(GameState.MENU)){
			checkMenuTiles();
		}
		else{
			addAstroids();
			addNormalEnemies(1);
			updateEnemies();
			shipCollisionCheck();
		}
	}

	@Override
	public void draw(Graphics2D win) {
		win.translate(transX,transY);
		drawStars(win);
		drawParts(win);
		drawBullets(win);
		drawExplosions(win, Color.GREEN);
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
		}
		else {
			exitCheck();
			drawScore(win,Color.GREEN);
			drawEnemies(win);
		}
	}
}
