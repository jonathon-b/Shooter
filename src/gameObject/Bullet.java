package gameObject;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Bullet extends Rectangle2D.Double {
	public static final double baseVel=5;
	public double dx,dy;
	static double size=3;
	public Bullet(Point2D.Double p, double sin, double cos, Ship s, boolean velControl){
		super(p.getX()-(size/2),p.getY(),size,size);
		if(!velControl) {
			this.dx = baseVel * sin + s.dx;
			this.dy = -baseVel * cos + s.dy;
		}
		else{
			this.dx=cos;
			this.dy=sin;
		}
		}
	public void update(){
		this.x+=dx;this.y+=dy;
	}
	public boolean kill(){
		if(this.getMaxY()<0)
			return true;
		return false;
	}
	public void draw(Graphics2D win, Color c){
		win.setColor(c);
		win.fill(this);
	}
}
