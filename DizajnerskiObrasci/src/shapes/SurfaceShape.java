package shapes;

import java.awt.Color;
import java.awt.Graphics;
//oblici koji imaju povrsinu tj bojim im unutrasnjost
public abstract class SurfaceShape extends Shape {

	private Color insideColor;
	
	public abstract void fill(Graphics g);
	
	public Color getInsideColor() {
		return insideColor;
	}

	public void setInsideColor(Color insideColor) {
		this.insideColor = insideColor;
	}
}
