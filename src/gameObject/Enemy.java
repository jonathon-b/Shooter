package gameObject;

import java.awt.geom.Rectangle2D;

public class Enemy extends GameObject{

	public boolean kill=false;

	public Enemy(double x, double y, double dx, double dy, double size) {
		super(x, y, dx, dy, size);
	}

	public boolean isDead(){return kill;}

}
