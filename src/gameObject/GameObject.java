package gameObject;

import java.awt.*;
import java.awt.geom.Line2D;

public class GameObject extends Rectangle {
	double subX;
	double subY;
	public double dx=0, dy=0;
	public static int screenWidth, screenHeight;
	Color color = Color.WHITE;

	public GameObject(double x, double y, int width, int height, int screenWidth, int screenHeight){
		super((int)x,(int)y,width,height);
		this.screenWidth=screenWidth;
		this.screenHeight=screenHeight;
		x=this.subX;
		y=this.subY;
	}

	public void setColor(Color c){
		this.color=c;
	}

	public void drawPath(Graphics2D win){
		int pos = 0;//0=left 1=up 2=right 3=down
		if(this.dx<0)
			pos=0;
		else if(this.dx>0)
			pos=2;
		else if(this.dy>0)
			pos=3;
		else if(this.dy<0)
			pos=1;

		Line2D l=new Line2D.Double();
		double x1=(this.subX+this.width+this.subX)/2;
		double y1=(this.subY+this.height+this.subY)/2;
		double x2=0,y2=0;

		if(pos==0){
			x2=0;
			y2=(-(dy/dx)*x1)+y1;
		}
		else if(pos==1){
			x2=(-(dx/dy)*y1)+x1;
			y2=0;
		}
		else if(pos==2){
			x2=this.screenWidth;
			y2=(dy/dx)*(screenWidth-x1)+y1;
		}
		else if(pos==3){
			x2=(dx/dy)*(screenHeight-y1)+x1;
			y2=this.screenHeight;
		}
		l.setLine(x1,y1,x2,y2);
		win.draw(l);
	}

	public void draw(Graphics2D win){
		win.setColor(color);
		win.draw(this);
	}

}
