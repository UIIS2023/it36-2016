package shapes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ListIterator;

public abstract class Shape implements Moveable, Comparable, Serializable, Cloneable{

	private boolean selected;
	private Color outsideColor;
	private ArrayList<Integer> positionHistory;
	public Shape() {
		this.positionHistory= new ArrayList<Integer>();
	}
	
	public Shape(boolean selected) {
		this.selected = selected;
		this.positionHistory= new ArrayList<Integer>();
	}
	
	
	public abstract boolean contains(int x, int y);
	public abstract void draw(Graphics g);
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public Color getOutsideColor() {
		return outsideColor;
	}

	public void setOutsideColor(Color outsideColor) {
		this.outsideColor = outsideColor;
	}
	
	public ArrayList<Integer> getPositionHistory (){
		return positionHistory;
	}
	 
	public void setPositionHistory(ArrayList<Integer> positionHistory){
		this.positionHistory = new ArrayList<Integer>(positionHistory);
//		System.out.println(this.positionHistory + "w");
	}
}
