package gameObject;

import Test.DrawShip;
import Util.GDV5;

import java.util.ArrayList;
import java.util.Random;

public class ShipEnemy extends Enemy{
	int limit, iterations=0;
	static Random r = new Random();

	public ShipEnemy(double x, double y, double dx, double dy, double size, GDV5 g) {
		super(x, y, dx, dy, size);
		limit=r.nextInt((int)(g.getHeight()/this.dy-1));
	}
	public void shoot(float bound, ArrayList b){
	if(r.nextFloat()>bound);
		//b.add(new Bullet(this.getCenterX(),this.getCenterY(),));
	}
	public void update(){
		iterations++;
		if(this.iterations<this.limit)
			super.update();
	}
}
