package Util;

import java.awt.Canvas;
import java.awt.Color;

/**
 * @(#)GameDriverV4.java
 *
 *
 * updates V4:  jframe included, keylistener included, switch to render, game loop
 *    from tasktimer to thread  - needs more testing sorry
 * Updates V5:  keyTyped, switched to update and render
 *
 * Possible Changes for V6: full-screen (win.scale)
 *
 * @version 5.0 9/13/2019
 */

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

public abstract class GDV5 extends Canvas implements Runnable, KeyListener {

	private int FramesPerSecond;
	protected static boolean[] KeysPressed;

	// it is your responsibility to handle the release on keysTyped
	protected static boolean[] KeysTyped;
	private JFrame frame;
	private String title = "Default";
	private boolean cleanCanvas = true;

	public GDV5(int frames) {
		// set up all variables related to the game
		FramesPerSecond = frames;

		this.addKeyListener(this);

		// key setup
		KeysPressed = new boolean[KeyEvent.KEY_LAST];
		KeysTyped = new boolean[KeyEvent.KEY_LAST];

	}

	public GDV5() {
		// default setting (60 frames per second)
		this(60);

		this.setBackground(Color.BLACK);
	}

	public void start() {

		if (this.getWidth() == 0) {
			this.setSize(1280, 720);
		}

		frame = new JFrame();

		frame.setUndecorated(true);

		frame.add(this);
		frame.pack();
		frame.setTitle(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);

		frame.setVisible(true);

		this.startThread();

	}

	private synchronized void startThread() {
		Thread t1 = new Thread(this);
		t1.start(); // calls run method after paint
		this.setFocusable(true);
	}

	public void setFrames(int num) {
		this.FramesPerSecond = num;
	}

	public abstract void update();

	public abstract void draw(Graphics2D win);

	private void render() {

		BufferStrategy buffs = this.getBufferStrategy();
		if (buffs == null) {
			this.createBufferStrategy(3);
			buffs = this.getBufferStrategy();
		}

		Graphics g = buffs.getDrawGraphics();

		if (this.cleanCanvas) {
			g.setColor(this.getBackground());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
		}

		draw((Graphics2D) g);

		g.dispose();

		buffs.show();

	}

	public void run() {

		long lastTime = System.nanoTime(); // long 2^63
		double nanoSecondConversion = 1000000000.0 / this.FramesPerSecond; // 60 frames per second
		double changeInSeconds = 0;

		while (true) {
			long now = System.nanoTime();

			changeInSeconds += (now - lastTime) / nanoSecondConversion;
			while (changeInSeconds >= 1) {
				update();
				changeInSeconds--;
			}

			render();
			lastTime = now;
		}
	}

	public BufferedImage addImage(String name) {

		BufferedImage img = null;
		try {

			img = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream(name));

		} catch (IOException e) {
			System.out.println(e);
		}

		return img;

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		KeysPressed[e.getKeyCode()] = true;

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		KeysPressed[e.getKeyCode()] = false;
		KeysTyped[e.getKeyCode()] = true;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * Returns the direction of collision (0 = right, 1 - top, 2 - left , 3 -
	 * bottom). stationary - the object we are colliding into projectile - the
	 * object that is moving dx = projectile's x displacement dy = projectile's y
	 * displacement
	 */
	public boolean collides(Rectangle2D stationary, Rectangle2D projectile) {
		if(projectile.getMaxY()>=stationary.getY()&&projectile.getY()<=stationary.getMaxY()&&projectile.getMaxX()>=stationary.getX()&&projectile.getX()<=stationary.getMaxX())
			return true;
	return false;
	}

	public int collisionDirection(Rectangle2D stationary, Rectangle2D projectile, double pdx, double pdy,double sdx, double sdy) { //1 is projectile hits from below

		//bottom
		if(projectile.getY()+pdy<=stationary.getMaxY()+sdy&&projectile.getY()>stationary.getMaxY()&&projectile.getX()+pdx<stationary.getMaxX()+sdx&&projectile.getMaxX()+pdx>stationary.getX()+sdx)
			return 1;
		//top
		if(projectile.getMaxY()+pdy>=stationary.getY()+sdy&&projectile.getMaxY()<stationary.getY()&&projectile.getX()+pdx<stationary.getMaxX()+sdx&&projectile.getMaxX()+sdx>stationary.getX()+sdx)
			return 2;
		//left
		if(projectile.getMaxX()+pdx>=stationary.getX()+pdx&&projectile.getMaxX()<stationary.getX()&&projectile.getY()+pdy<stationary.getMaxY()+sdy&&projectile.getMaxY()+pdy>stationary.getY()+sdy)
			return 3;
		if(projectile.getX()+pdx<=stationary.getMaxX()+sdx&&projectile.getX()>stationary.getMaxX()&&projectile.getY()+pdy<stationary.getMaxY()+sdy&&projectile.getMaxY()+pdy>stationary.getY()+sdy)
			return 4;
			return 0;

	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCleanCanvas(boolean option) {
		this.cleanCanvas = option;
	}

}
