package mvc;

import java.awt.Graphics;
import java.util.ListIterator;

import javax.swing.JPanel;

import shapes.Shape;

public class DrawingView extends JPanel{

	public DrawingView() {
		
	}
	
	private DrawingModel model = new DrawingModel();
	
	public void setModel(DrawingModel model) {
		this.model = model;
	}
	
	public void paint(Graphics g)
	{
		super.paint(g);
		ListIterator<Shape> it = model.getShapes().listIterator();
		while(it.hasNext()) {
			it.next().draw(g);
		}
	}
}
