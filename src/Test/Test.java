package Test;

import Util.GDV5;
import gameObject.GameObject;

import java.awt.*;

public class Test extends GDV5 {

	GameObject g1=new GameObject(0,0,10,10,this.getWidth(),this.getHeight());

	public Test(){
		g1.dx=1;
		g1.dy=1;
	}

	public static void main(String args[]){
		Test t=new Test();
		t.start();
	}

	@Override
	public void update() {

	}

	@Override
	public void draw(Graphics2D win) {
		win.setColor(Color.red);
		win.fill(g1);
		win.setColor(Color.WHITE);
		g1.drawPath(win);
	}
}
