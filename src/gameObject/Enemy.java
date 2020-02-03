package gameObject;

import Test.DrawShip;

import java.awt.geom.Rectangle2D;

public class Enemy extends GameObject{

	public boolean kill=false;

	public Enemy(double x, double y, double dx, double dy, double size) {
		super(x, y, dx, dy, size);
	}

	public void killEnemy(double x, double y){
		this.kill=true;
		DrawShip.Explosions.add(new ExplosionArray(x,y,3,20,40,4));
		DrawShip.addRumble(15);
		if(!DrawShip.sd.isPlaying(1))
			DrawShip.sd.play(1);
	}

	public boolean isDead(){return kill;}

}
