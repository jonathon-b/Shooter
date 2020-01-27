package gameObject;

import java.awt.geom.Rectangle2D;

public class Enemy extends Rectangle2D.Double{
	double dx,dy;

	public Enemy(double x, double y, double dx, double dy, double size){
		super(0,0,size, size);
		this.dx=dx; this.dy=dy;
	}



}
