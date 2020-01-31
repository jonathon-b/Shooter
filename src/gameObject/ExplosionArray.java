package gameObject;

import java.awt.*;

public class ExplosionArray {
	ExplosionPart[] ep;

	public ExplosionArray(double x, double y, double size, int maxMovement, int arraySize, double distribution){
		ep = new ExplosionPart[arraySize];
		for(int i=0;i<ep.length;i++){
			ep[i]=new ExplosionPart(x-size/2,y-size/2,size, maxMovement,distribution);
		}
	}

	public void fill(Graphics2D win, Color c){
		for(ExplosionPart e:
		    ep) {
			e.fill(win,c);
		}
	}

	public void update(){
		for(ExplosionPart e:
		    ep) {
			e.update();
		}
	}

	public boolean kill(){
		return ep[0].kill();
	}

}
