package mvc;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import adapter.HexagonAdapter;
import command.CmdBringBackward;
import command.CmdBringForward;
import command.CmdBringToBack;
import command.CmdBringToFront;
import command.CmdCircleAdd;
import command.CmdCircleRemove;
import command.CmdCircleUpdate;
import command.CmdDonutAdd;
import command.CmdDonutRemove;
import command.CmdDonutUpdate;
import command.CmdHexagonAdd;
import command.CmdHexagonRemove;
import command.CmdHexagonUpdate;
import command.CmdLineAdd;
import command.CmdLineRemove;
import command.CmdLineUpdate;
import command.CmdPointAdd;
import command.CmdPointRemove;
import command.CmdPointUpdate;
import command.CmdRectangleAdd;
import command.CmdRectangleRemove;
import command.CmdRectangleUpdate;
import command.CmdShapeAdd;
import command.CmdShapeDeselect;
import command.CmdShapeRemove;
import command.CmdShapeSelect;
import command.CmdStack;
import command.Command;
import dialogs.DlgCircle;
import dialogs.DlgDonut;
import dialogs.DlgHexagon;
import dialogs.DlgLine;
import dialogs.DlgPoint;
import dialogs.DlgRectangle;
import observer.ButtonsObserver;
import observer.ButtonsObserverUpdate;
import shapes.Circle;
import shapes.Donut;
import shapes.Line;
import shapes.Point;
import shapes.Rectangle;
import shapes.Shape;
import strategy.SaveLog;
import strategy.SaveManager;
import strategy.SaveShapes;

//kontroler
public class DrawingController {

	private DrawingModel model;
	private DrawingFrame frame;
	private CmdStack cmdStack = new CmdStack();
	public CmdStack getCmdStack() {
		return cmdStack;
	}
	

	private Point startPoint;
	private Shape selectedShape;
	
	//lista oblika
	private List<Shape> selectedShapes = new ArrayList<Shape>();

	//dijalozi za oblike
	private DlgPoint dlgPoint = new DlgPoint();
	private DlgLine dlgLine = new DlgLine();
	private DlgRectangle dlgRectangle = new DlgRectangle();
	private DlgCircle dlgCircle = new DlgCircle();
	private DlgDonut dlgDonut = new DlgDonut();
	private DlgHexagon dlgHexagon = new DlgHexagon();
	

	//ucitavanje loga
	private ArrayList<String> logList = new ArrayList<String>();
	private String line;
	private int logCounter=0; 
	private boolean loadLogEnd=false;
	
	public boolean isloadLogEnd() {
		return loadLogEnd;
	}
	
	private ButtonsObserver buttonsObserver = new ButtonsObserver();
	private ButtonsObserverUpdate buttonsObserverUpdate;
	//konstruktor
	public DrawingController(DrawingModel model, DrawingFrame frame) {
		this.model = model;
		this.frame = frame;
		//observeri
		buttonsObserverUpdate = new ButtonsObserverUpdate(frame);
		buttonsObserver.addPropertyChangeListener(buttonsObserverUpdate);
		}
	
	//Definisanje klika na povrsinu
	public void mouseClicked(MouseEvent arg0) {
		//Da li je ista selektovano ako je izabrano select
		if (frame.getTglbtnSelect().isSelected()) {
			ListIterator<Shape> it = model.getShapes().listIterator();
			while (it.hasNext()) {
				selectedShape = it.next();
				if(selectedShape.contains(arg0.getX(),arg0.getY())) {
					if (selectedShape.isSelected()) {
						CmdShapeDeselect cmd = new CmdShapeDeselect(this, selectedShape);
						cmd.execute();
						frame.getTextAreaLog().append("Deselected - "+selectedShape+"\n");		
						// UNDO REDO
						
						undoRedoHelper(cmd,selectedShape,"Selected","Deselected");
						frame.getBtnUndo().setEnabled(true);
						frame.getBtnRedo().setEnabled(false);
						frame.getBtnloadNext().setEnabled(false); //UCITAVANJE LOGA
						loadLogEnd = true;
						// selectedShapes.remove(selectedShape);
//						System.out.println(selectedShapes.size());
				} else {
					CmdShapeSelect cmd = new CmdShapeSelect(this, selectedShape);
					cmd.execute();
					frame.getTextAreaLog().append("Selected - "+selectedShape+"\n");		
					// UNDO REDO
					undoRedoHelper(cmd,selectedShape,"Deselected","Selected");
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
					frame.getBtnloadNext().setEnabled(false); //UCITAVANJE LOGA
					loadLogEnd = true;

//					System.out.println(selectedShapes.size());
				}
					selectEditUpdate();
			}
			frame.getView().repaint();
		
			}
		} else if (frame.getTglbtnPoint().isSelected()) { //Nadalje crtanje
			dlgPoint = new DlgPoint();
			dlgPoint.setTitle("Point");
			dlgPoint.getTextFieldX().setText(Integer.toString(arg0.getX()));
			dlgPoint.getTextFieldY().setText(Integer.toString(arg0.getY()));
			dlgPoint.setColor(frame.getBtnInsideColor().getBackground());
			dlgPoint.getBtnColor().setBackground(frame.getBtnInsideColor().getBackground());
			dlgPoint.pack();
			dlgPoint.setVisible(true);
			if (dlgPoint.isOK()) {
				Point p = new Point(arg0.getX(), arg0.getY(), dlgPoint.getColor(), false);
				CmdPointAdd cmd = new CmdPointAdd(model, p);
				cmd.execute();
				frame.getTextAreaLog().append("Added - "+p+"\n");
				undoRedoHelper(cmd,p,"Removed","Added");
				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);
				frame.getBtnloadNext().setEnabled(false); //UCITAVANJE LOGA
				loadLogEnd = true;
				frame.getView().repaint();
		} } else if (frame.getTglbtnLine().isSelected()) {
			if (startPoint == null) {
				startPoint = new Point(arg0.getX(), arg0.getY());
			} else {
				dlgLine = new DlgLine();
				Point endPoint = new Point(arg0.getX(), arg0.getY());
				dlgLine.setTitle("Line");
				dlgLine.getTextFieldStartX().setText(Integer.toString(startPoint.getX()));
				dlgLine.getTextFieldStartY().setText(Integer.toString(startPoint.getY()));
				dlgLine.getTextFieldEndX().setText(Integer.toString(endPoint.getX()));
				dlgLine.getTextFieldEndY().setText(Integer.toString(endPoint.getY()));
				dlgLine.setColor(frame.getBtnInsideColor().getBackground());
				dlgLine.getBtnColor().setBackground(frame.getBtnInsideColor().getBackground());
				dlgLine.pack();
				dlgLine.setVisible(true);
				if (dlgLine.isOK()) {
					Line l = new Line(startPoint, endPoint, dlgLine.getColor());
					CmdLineAdd cmd = new CmdLineAdd(model, l);
					cmd.execute();
					frame.getTextAreaLog().append("Added - "+l+"\n");
					//undoRedoHelper(cmd);
					undoRedoHelper(cmd,l,"Removed","Added");
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
					frame.getBtnloadNext().setEnabled(false); //UCITAVANJE LOGA
					loadLogEnd = true;
					frame.getView().repaint();
				}
				startPoint = null;
			}
		} else if (frame.getTglbtnRectangle().isSelected()) {
			dlgRectangle = new DlgRectangle();
			dlgRectangle.setTitle("Rectangle");
			int width = 0, height = 0;
			dlgRectangle.getTextFieldUpperLeftX().setText(Integer.toString(arg0.getX()));
			dlgRectangle.getTextFieldUpperLeftY().setText(Integer.toString(arg0.getY()));
			dlgRectangle.setInsideColor(frame.getBtnInsideColor().getBackground());
			dlgRectangle.getBtnInsideColor().setBackground(frame.getBtnInsideColor().getBackground());
			dlgRectangle.setEdgeColor(frame.getBtnOutsideColor().getBackground());
			dlgRectangle.getBtnEdgeColor().setBackground(frame.getBtnOutsideColor().getBackground());
			dlgRectangle.pack();
			dlgRectangle.setVisible(true);
			if (dlgRectangle.isOK()) {
				width = Integer.parseInt(dlgRectangle.getTextFieldWidth().getText());
				height = Integer.parseInt(dlgRectangle.getTextFieldHeight().getText());

				Rectangle r = new Rectangle(new Point(arg0.getX(), arg0.getY()), height, width,
						dlgRectangle.getInsideColor(), dlgRectangle.getEdgeColor(), false);
				r.setSelected(false);
				try {
					CmdRectangleAdd cmd = new CmdRectangleAdd(model, r);
					cmd.execute();
					frame.getTextAreaLog().append("Added - "+r+"\n");
					//undoRedoHelper(cmd);
					undoRedoHelper(cmd,r,"Removed","Added");
					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
					frame.getBtnloadNext().setEnabled(false); //UCITAVANJE LOGA
					loadLogEnd = true;
					frame.getView().repaint();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Wrong entry!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			dlgRectangle.getTextFieldHeight().setText("");
			dlgRectangle.getTextFieldWidth().setText("");
		} else if (frame.getTglbtnCircle().isSelected()) {
			dlgCircle = new DlgCircle();
			dlgCircle.setTitle("Circle");
			dlgCircle.getTextFieldCenterX().setText(Integer.toString(arg0.getX()));
			dlgCircle.getTextFieldCenterY().setText(Integer.toString(arg0.getY()));
			dlgCircle.setInsideColor(frame.getBtnInsideColor().getBackground());
			dlgCircle.getBtnInsideColor().setBackground(frame.getBtnInsideColor().getBackground());
			dlgCircle.setEdgeColor(frame.getBtnOutsideColor().getBackground());
			dlgCircle.getBtnEdgeColor().setBackground(frame.getBtnOutsideColor().getBackground());
			int radius = 0;
			dlgCircle.pack();
			dlgCircle.setVisible(true);
			if (dlgCircle.isOK()) {
				radius = Integer.parseInt(dlgCircle.getTextFieldRadius().getText());
				Circle circle = new Circle(new Point(arg0.getX(), arg0.getY()), radius, dlgCircle.getInsideColor(),
						dlgCircle.getEdgeColor());
				try {
					CmdCircleAdd cmd = new CmdCircleAdd(model, circle);
					cmd.execute();
					frame.getTextAreaLog().append("Added - "+circle+"\n");
					//undoRedoHelper(cmd);
					undoRedoHelper(cmd,circle,"Removed","Added");

					frame.getBtnUndo().setEnabled(true);
					frame.getBtnRedo().setEnabled(false);
					frame.getBtnloadNext().setEnabled(false); //UCITAVANJE LOGA
					loadLogEnd = true;
					frame.getView().repaint();
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(frame, "Wrong entry!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			dlgCircle.getTextFieldRadius().setText("");
		} else if (frame.getTglbtnDonut().isSelected()) {
			dlgDonut= new DlgDonut();
			dlgDonut.setTitle("Donut");
			dlgDonut.getTextFieldCenterX().setText(Integer.toString(arg0.getX()));
			dlgDonut.getTextFieldCenterY().setText(Integer.toString(arg0.getY()));
			dlgDonut.setInsideColor(frame.getBtnInsideColor().getBackground());
			dlgDonut.getBtnInsideColor().setBackground(frame.getBtnInsideColor().getBackground());
			dlgDonut.setEdgeColor(frame.getBtnOutsideColor().getBackground());
			dlgDonut.getBtnEdgeColor().setBackground(frame.getBtnOutsideColor().getBackground());
			int donutRadius = 0;
			int innerRadius = 0;
			dlgDonut.pack();
			dlgDonut.setVisible(true);
			if (dlgDonut.isOK()) {
				donutRadius = Integer.parseInt(dlgDonut.getTextFieldRadius().getText());
				innerRadius = Integer.parseInt(dlgDonut.getTextFieldInnerRadius().getText());
				Donut donut = new Donut(new Point(arg0.getX(), arg0.getY()), donutRadius, innerRadius,
						dlgDonut.getInsideColor(), dlgDonut.getEdgeColor());
				CmdDonutAdd cmd = new CmdDonutAdd(model, donut);
				cmd.execute();
				frame.getTextAreaLog().append("Added - "+donut+"\n");
				//undoRedoHelper(cmd);
				undoRedoHelper(cmd,donut,"Removed","Added");

				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);
				frame.getBtnloadNext().setEnabled(false); //UCITAVANJE LOGA
				loadLogEnd = true;
				frame.getView().repaint();
			}
			dlgDonut.getTextFieldInnerRadius().setText("");
			dlgDonut.getTextFieldRadius().setText("");
		} else if (frame.getTglbtnHexagon().isSelected()) {
			dlgHexagon = new DlgHexagon();
			dlgHexagon.getTextFieldCenterX().setText(Integer.toString(arg0.getX()));
			dlgHexagon.getTextFieldCenterY().setText(Integer.toString(arg0.getY()));
			dlgHexagon.setInsideColor(frame.getBtnInsideColor().getBackground());
			dlgHexagon.getBtnInsideColor().setBackground(frame.getBtnInsideColor().getBackground());
			dlgHexagon.setEdgeColor(frame.getBtnOutsideColor().getBackground());
			dlgHexagon.getBtnEdgeColor().setBackground(frame.getBtnOutsideColor().getBackground());
			int hexagonRadius = 0;
			dlgHexagon.pack();
			dlgHexagon.setVisible(true);
			if (dlgHexagon.isOK()) {
				hexagonRadius = Integer.parseInt(dlgHexagon.getTextFieldRadius().getText());
				HexagonAdapter hexagon = new HexagonAdapter(new Point(arg0.getX(), arg0.getY()), hexagonRadius,
						dlgHexagon.getInsideColor(), dlgHexagon.getEdgeColor());
				CmdHexagonAdd cmd = new CmdHexagonAdd(model, hexagon);
				cmd.execute();
				frame.getTextAreaLog().append("Added - "+hexagon+"\n");
				//undoRedoHelper(cmd);
				undoRedoHelper(cmd,hexagon,"Removed","Added");

				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);
				frame.getBtnloadNext().setEnabled(false); //UCITAVANJE LOGA
				loadLogEnd = true;
				frame.getView().repaint();
			}
			dlgHexagon.getTextFieldRadius().setText("");
		}
		selectEditUpdate();	
}
	
	//undo dugme
	public void undo() {
		frame.getTextAreaLog().append("Undo "+cmdStack.getLogCommandsUndo().get(cmdStack.getCurrent())+" - "+cmdStack.getShapes().get(cmdStack.getCurrent())+"\n");
	
		//U slucaju da je undo brisanja, izvrsi i selektovanje vracenog shape-a
		if(cmdStack.getLogCommandsUndo().get(cmdStack.getCurrent()).equals("Added")) {
			CmdShapeSelect cmdS = new CmdShapeSelect(this,cmdStack.getShapes().get(cmdStack.getCurrent()));
			cmdS.execute();
		}
		cmdStack.undo();
		selectEditUpdate();
		frame.getView().repaint();
	}
	//redo dugme
	public void redo() {
		cmdStack.redo();
		frame.getTextAreaLog().append(cmdStack.getLogCommandsRedo().get(cmdStack.getCurrent())+" - "+cmdStack.getShapes().get(cmdStack.getCurrent())+"\n");
		if(cmdStack.getLogCommandsRedo().get(cmdStack.getCurrent()).equals("Removed")) {
			CmdShapeDeselect cmdDS = new CmdShapeDeselect(this,cmdStack.getShapes().get(cmdStack.getCurrent()));
			cmdDS.execute();
		}
		selectEditUpdate();
		frame.getView().repaint();
	}


	
	//zbog multiselect
	public List<Shape> getSelectedShapes() {
		return selectedShapes;
	}
	//
	private void undoRedoHelper(Command cmd, Shape s, String undo, String redo) {
		cmdStack.deleteElementsAfterPointer(cmdStack.getCurrent());
		cmdStack.getList().add(cmd);
		cmdStack.getShapes().add(s);
		cmdStack.getLogCommandsUndo().add(undo);
		cmdStack.getLogCommandsRedo().add(redo);
		cmdStack.setCurrent(cmdStack.getCurrent() + 1);
	}
	
	//Bring to back
	public void bringToBack() {
		frame.getBtnRedo().setEnabled(false);
		CmdBringToBack cmd = new CmdBringToBack(model);
		cmd.execute();
		frame.getTextAreaLog().append("Moved to back - "+selectedShapes.get(0)+"\n");
		
		undoRedoHelper(cmd,selectedShapes.get(0),"Moved to front","Moved to back");

		frame.getView().repaint();
	}
	//bring to front
	public void bringToFront() {
		frame.getBtnRedo().setEnabled(false);
		CmdBringToFront cmd = new CmdBringToFront(model);
		cmd.execute();
		frame.getTextAreaLog().append("Moved to front - "+selectedShapes.get(0)+"\n");
		
		undoRedoHelper(cmd,selectedShapes.get(0),"Moved to back","Moved to front");

		frame.getView().repaint();
	}
	//to front 
	public void bringForward() {
		frame.getBtnRedo().setEnabled(false);
		CmdBringForward cmd = new CmdBringForward(model);
		cmd.execute();
		frame.getTextAreaLog().append("Moved forward - "+selectedShapes.get(0)+"\n");

		undoRedoHelper(cmd,selectedShapes.get(0),"Moved backward","Moved forward");

		frame.getView().repaint();
	}
	//to back
	public void bringBackward() {
		frame.getBtnRedo().setEnabled(false);
		CmdBringBackward cmd = new CmdBringBackward(model);
		cmd.execute();
		frame.getTextAreaLog().append("Moved backward - "+selectedShapes.get(0)+"\n");

		undoRedoHelper(cmd,selectedShapes.get(0),"Moved forward","Moved backward");

		frame.getView().repaint();
	}
	//Edit dugme
	public void mouseClickedEdit(ActionEvent arg0) {
		if (selectedShapes.get(0) instanceof Point) {
			Point helpP = (Point) selectedShapes.get(0);
			dlgPoint.setTitle("Point edit");
			dlgPoint.getTextFieldX().setText(Integer.toString(helpP.getX()));
			dlgPoint.getTextFieldY().setText(Integer.toString(helpP.getY()));
			dlgPoint.getBtnColor().setBackground(helpP.getOutsideColor());
			dlgPoint.setColor(helpP.getOutsideColor());
			dlgPoint.pack();
			dlgPoint.setVisible(true);
			if (dlgPoint.isOK()) {
				Point p = new Point(Integer.parseInt(dlgPoint.getTextFieldX().getText()),
						Integer.parseInt(dlgPoint.getTextFieldY().getText()), dlgPoint.getColor(), false);
				p.setPositionHistory(helpP.getPositionHistory());
				CmdPointUpdate cmd = new CmdPointUpdate(helpP, p);
				cmd.execute();
				frame.getTextAreaLog().append("Modified - "+p+"\n");
				
				undoRedoHelper(cmd,p,"Modified","Modified");
				frame.getView().repaint();
			}
		} else if (selectedShapes.get(0) instanceof Line) {
			Line helpL = (Line) selectedShapes.get(0);
			dlgLine.setTitle("Line edit");
			dlgLine.getTextFieldStartX().setText(Integer.toString(helpL.getStartPoint().getX()));
			dlgLine.getTextFieldStartY().setText(Integer.toString(helpL.getStartPoint().getY()));
			dlgLine.getTextFieldEndX().setText(Integer.toString(helpL.getEndPoint().getX()));
			dlgLine.getTextFieldEndY().setText(Integer.toString(helpL.getEndPoint().getY()));
			dlgLine.getBtnColor().setBackground(helpL.getOutsideColor());
			dlgLine.setColor(helpL.getOutsideColor());
			dlgLine.pack();
			
			dlgLine.setVisible(true);
			if (dlgLine.isOK()) {
				Line l = new Line(
						new Point(Integer.parseInt(dlgLine.getTextFieldStartX().getText()),
								Integer.parseInt(dlgLine.getTextFieldStartY().getText())),
						new Point(Integer.parseInt(dlgLine.getTextFieldEndX().getText()),
								Integer.parseInt(dlgLine.getTextFieldEndY().getText())),
						dlgLine.getColor());
				l.setPositionHistory(helpL.getPositionHistory());
				CmdLineUpdate cmd = new CmdLineUpdate(helpL, l);
				cmd.execute();
				frame.getTextAreaLog().append("Modified - "+l+"\n");
				// UNDO REDO
				undoRedoHelper(cmd,l,"Modified","Modified");
				frame.getView().repaint();
			}
		} else if (selectedShapes.get(0) instanceof Rectangle) {
			Rectangle helpR = (Rectangle) selectedShapes.get(0);
			dlgRectangle.setTitle("Rectangle edit");
			dlgRectangle.getTextFieldUpperLeftX().setText(Integer.toString(helpR.getUpperLeftPoint().getX()));
			dlgRectangle.getTextFieldUpperLeftY().setText(Integer.toString(helpR.getUpperLeftPoint().getY()));
			dlgRectangle.getTextFieldHeight().setText(Integer.toString(helpR.getHeight()));
			dlgRectangle.getTextFieldWidth().setText(Integer.toString(helpR.getWidth()));
			dlgRectangle.getBtnEdgeColor().setBackground(helpR.getOutsideColor());
			dlgRectangle.getBtnInsideColor().setBackground(helpR.getInsideColor());
			dlgRectangle.setInsideColor(helpR.getInsideColor());
			dlgRectangle.setEdgeColor(helpR.getOutsideColor());
			dlgRectangle.setVisible(true);
			if (dlgRectangle.isOK()) {
				Rectangle r = new Rectangle(
						new Point(Integer.parseInt(dlgRectangle.getTextFieldUpperLeftX().getText()),
								Integer.parseInt(dlgRectangle.getTextFieldUpperLeftY().getText())),
						Integer.parseInt(dlgRectangle.getTextFieldHeight().getText()),
						Integer.parseInt(dlgRectangle.getTextFieldWidth().getText()), dlgRectangle.getInsideColor(),
						dlgRectangle.getEdgeColor());
				
				r.setPositionHistory(helpR.getPositionHistory());

				CmdRectangleUpdate cmd = new CmdRectangleUpdate(helpR, r);
				cmd.execute();
				//System.out.println(r.getPositionHistory());
				//System.out.println(helpR.getPositionHistory());
				frame.getTextAreaLog().append("Modified - "+r+"\n");
				// UNDO REDO
				undoRedoHelper(cmd,r,"Modified","Modified");
				frame.getView().repaint();
			}
		} else if (selectedShapes.get(0) instanceof Donut) {
			Donut helpD = (Donut) selectedShapes.get(0);
			dlgDonut.setTitle("Donut edit");
			dlgDonut.getTextFieldCenterX().setText(Integer.toString(helpD.getCenter().getX()));
			dlgDonut.getTextFieldCenterY().setText(Integer.toString(helpD.getCenter().getY()));
			dlgDonut.getTextFieldRadius().setText(Integer.toString(helpD.getRadius()));
			dlgDonut.getTextFieldInnerRadius().setText(Integer.toString(helpD.getInnerRadius()));
			dlgDonut.getBtnEdgeColor().setBackground(helpD.getOutsideColor());
			dlgDonut.getBtnInsideColor().setBackground(helpD.getInsideColor());
			dlgDonut.setInsideColor(helpD.getInsideColor());
			dlgDonut.setEdgeColor(helpD.getOutsideColor());
			dlgDonut.setVisible(true);
			if (dlgDonut.isOK()) {
				Donut d = new Donut(
						new Point(Integer.parseInt(dlgDonut.getTextFieldCenterX().getText()),
								Integer.parseInt(dlgDonut.getTextFieldCenterY().getText())),
						Integer.parseInt(dlgDonut.getTextFieldRadius().getText()),
						Integer.parseInt(dlgDonut.getTextFieldInnerRadius().getText()), dlgDonut.getInsideColor(),
						dlgDonut.getEdgeColor());
				d.setPositionHistory(helpD.getPositionHistory());
				CmdDonutUpdate cmd = new CmdDonutUpdate(helpD, d);
				cmd.execute();
				// UNDO REDO
				//undoRedoHelper(cmd);
				undoRedoHelper(cmd,d,"Modified","Modified");
				frame.getTextAreaLog().append("Modified - "+d+"\n");
				frame.getView().repaint(); // DOVRSITI
			}

		} else if (selectedShapes.get(0) instanceof Circle) {
			Circle helpC = (Circle) selectedShapes.get(0);
			dlgCircle.setTitle("Circle edit");
			dlgCircle.getTextFieldCenterX().setText(Integer.toString(helpC.getCenter().getX()));
			dlgCircle.getTextFieldCenterY().setText(Integer.toString(helpC.getCenter().getY()));
			dlgCircle.getTextFieldRadius().setText(Integer.toString(helpC.getRadius()));
			dlgCircle.getBtnEdgeColor().setBackground(helpC.getOutsideColor());
			dlgCircle.getBtnInsideColor().setBackground(helpC.getInsideColor());
			dlgCircle.setInsideColor(helpC.getInsideColor());
			dlgCircle.setEdgeColor(helpC.getOutsideColor());
			dlgCircle.setVisible(true);
			if (dlgCircle.isOK()) {
				Circle c = new Circle(
						new Point(Integer.parseInt(dlgCircle.getTextFieldCenterX().getText()),
								Integer.parseInt(dlgCircle.getTextFieldCenterY().getText())),
						Integer.parseInt(dlgCircle.getTextFieldRadius().getText()), dlgCircle.getInsideColor(),
						dlgCircle.getEdgeColor());
				c.setPositionHistory(helpC.getPositionHistory());
				CmdCircleUpdate cmd = new CmdCircleUpdate(helpC, c);
				cmd.execute();
				frame.getTextAreaLog().append("Modified - "+c+"\n");
				// UNDO REDO
				//undoRedoHelper(cmd);
				undoRedoHelper(cmd,c,"Modified","Modified");
				frame.getView().repaint();
				
			}
		} else if (selectedShapes.get(0) instanceof HexagonAdapter) {
			HexagonAdapter helpH = (HexagonAdapter) selectedShapes.get(0);
			dlgHexagon.setTitle("Hexagon edit");
			dlgHexagon.getTextFieldCenterX().setText(Integer.toString(helpH.getCenter().getX()));
			dlgHexagon.getTextFieldCenterY().setText(Integer.toString(helpH.getCenter().getY()));
			dlgHexagon.getTextFieldRadius().setText(Integer.toString(helpH.getRadius()));
			dlgHexagon.getBtnEdgeColor().setBackground(helpH.getOutsideColor());
			dlgHexagon.getBtnInsideColor().setBackground(helpH.getInsideColor());
			dlgHexagon.setInsideColor(helpH.getInsideColor());
			dlgHexagon.setEdgeColor(helpH.getOutsideColor());
			dlgHexagon.setVisible(true);
			if (dlgHexagon.isOK()) {
				HexagonAdapter h = new HexagonAdapter(
						new Point(Integer.parseInt(dlgHexagon.getTextFieldCenterX().getText()),
								Integer.parseInt(dlgHexagon.getTextFieldCenterY().getText())),
						Integer.parseInt(dlgHexagon.getTextFieldRadius().getText()), dlgHexagon.getInsideColor(),
						dlgHexagon.getEdgeColor());
				h.setPositionHistory(helpH.getPositionHistory());
				CmdHexagonUpdate cmd = new CmdHexagonUpdate(helpH, h);
				cmd.execute();
				frame.getTextAreaLog().append("Modified - "+h+"\n");
				// UNDO REDO
				//undoRedoHelper(cmd);
				undoRedoHelper(cmd,h,"Modified","Modified");
				frame.getView().repaint();
			}
		}
		frame.getBtnloadNext().setEnabled(false); //UCITAVANJE LOGA
		frame.getBtnRedo().setEnabled(false);
		frame.getBtnUndo().setEnabled(true);
		loadLogEnd = true;
	}
	
	public void mouseClickedDelete(ActionEvent arg0) {
		int n = JOptionPane.showConfirmDialog(null, "Are you sure?");
		for (int j = selectedShapes.size() - 1; j >= 0; j--) {
			if (n == 0) {
				if (selectedShapes.get(j) instanceof Point) {
					CmdPointRemove cmd = new CmdPointRemove(model, (Point) selectedShapes.get(j));
					cmd.execute();
					frame.getTextAreaLog().append("Removed - "+(Point) selectedShapes.get(j)+"\n");
					undoRedoHelper(cmd, (Point) selectedShapes.get(j),"Added","Removed");
					frame.getView().repaint();
					selectedShapes.remove(j);
				} else if (selectedShapes.get(j) instanceof Line) {
					CmdLineRemove cmd = new CmdLineRemove(model, (Line) selectedShapes.get(j));
					cmd.execute();
					frame.getTextAreaLog().append("Removed - "+(Line) selectedShapes.get(j)+"\n");
					undoRedoHelper(cmd, (Line) selectedShapes.get(j),"Added","Removed");
					frame.getView().repaint();
					selectedShapes.remove(j);
				} else if (selectedShapes.get(j) instanceof Rectangle) {
					CmdRectangleRemove cmd = new CmdRectangleRemove(model, (Rectangle) selectedShapes.get(j));
					cmd.execute();
					frame.getTextAreaLog().append("Removed - "+(Rectangle) selectedShapes.get(j)+"\n");
					undoRedoHelper(cmd, (Rectangle) selectedShapes.get(j),"Added","Removed");
					frame.getView().repaint();
					selectedShapes.remove(j);
				} else if (selectedShapes.get(j) instanceof Donut) {
					CmdDonutRemove cmd = new CmdDonutRemove(model, (Donut) selectedShapes.get(j));
					cmd.execute();
					frame.getTextAreaLog().append("Removed - "+(Donut) selectedShapes.get(j)+"\n");
					undoRedoHelper(cmd, (Donut) selectedShapes.get(j),"Added","Removed");
					frame.getView().repaint();
					selectedShapes.remove(j);
				} else if (selectedShapes.get(j) instanceof Circle) {
					CmdCircleRemove cmd = new CmdCircleRemove(model, (Circle) selectedShapes.get(j));
					cmd.execute();
					frame.getTextAreaLog().append("Removed - "+(Circle) selectedShapes.get(j)+"\n");
					undoRedoHelper(cmd, (Circle) selectedShapes.get(j),"Added","Removed");
					frame.getView().repaint();
					selectedShapes.remove(j);
				} else if (selectedShapes.get(j) instanceof HexagonAdapter) {
					CmdHexagonRemove cmd = new CmdHexagonRemove(model, (HexagonAdapter) selectedShapes.get(j));
					cmd.execute();
					frame.getTextAreaLog().append("Removed - "+(HexagonAdapter) selectedShapes.get(j)+"\n");
					undoRedoHelper(cmd, (HexagonAdapter) selectedShapes.get(j),"Added","Removed");
					frame.getView().repaint();
					selectedShapes.remove(j);
				}
				if (selectedShapes.size() == 0) {
					buttonsObserver.setEditButtonActivated(false);
					buttonsObserver.setDeleteButtonActivated(false);
				}
				frame.getBtnloadNext().setEnabled(false);
				frame.getBtnUndo().setEnabled(true);
				frame.getBtnRedo().setEnabled(false);
				selectEditUpdate();
				loadLogEnd = true; 
			}
		}
	}
	
	//cuvanje celog crteza na eksternu memoriju
	public void savePainting() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save painting");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".bin", "bin");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(filter);

		int userSelection = fileChooser.showSaveDialog(null);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File paintingToSave = fileChooser.getSelectedFile();
			File logToSave;
			String filePath = paintingToSave.getAbsolutePath();
			if (!filePath.endsWith(".bin") && !filePath.contains(".")) {
				paintingToSave = new File(filePath + ".bin");
				logToSave=new File(filePath + ".txt");
			}

			String filename = paintingToSave.getPath();
			System.out.println("Save painting as: " + paintingToSave.getAbsolutePath());
			System.out.println(filename.substring(filename.lastIndexOf("."), filename.length()));
			if (filename.substring(filename.lastIndexOf("."), filename.length()).contentEquals(".bin")) {
				filename=paintingToSave.getAbsolutePath().substring(0,filename.lastIndexOf("."))+".txt";
				logToSave=new File(filename);
				SaveManager savePainting = new SaveManager(new SaveShapes());
				SaveManager saveLog = new SaveManager(new SaveLog());
				savePainting.save(model, paintingToSave);
				saveLog.save(frame, logToSave);
			} else {
				JOptionPane.showMessageDialog(null, "Wrong file extension!");
			}
		}
	}
	
	//cuvanje log-a na eksternu memoriju
	public void saveLog() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Save log");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt","txt");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(filter);
		
		if (fileChooser.showSaveDialog(frame.getParent()) == JFileChooser.APPROVE_OPTION) {
			System.out.println("Successfully saved " + fileChooser.getSelectedFile().getName() + " file!");
			
			File file = fileChooser.getSelectedFile();
			String filePath = file.getAbsolutePath();
			//String filename = file.getPath();
			File logToSave = new File(filePath + ".txt");
			
			SaveManager manager = new SaveManager(new SaveLog());
			manager.save(frame, logToSave);
		}
		frame.repaint();
	}
	
	//ucitaj crtez sa eksterne
	public void openPainting() throws IOException, ClassNotFoundException {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".bin","bin");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(filter);

		fileChooser.setDialogTitle("Open painting");
		int userSelection = fileChooser.showOpenDialog(null);

		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File paintingToLoad = fileChooser.getSelectedFile();
			loadPaintingFile(paintingToLoad);
		}
	}
	//ucitaj log sa eksterne
	public void openLog() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".txt","txt");
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(filter);

		fileChooser.setDialogTitle("Open log");
		int userSelection = fileChooser.showOpenDialog(null);
		if (userSelection == JFileChooser.APPROVE_OPTION) {
			File paintingToLoad = fileChooser.getSelectedFile();
			loadLog(paintingToLoad);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void loadPaintingFile(File paintingToLoad) throws FileNotFoundException, IOException, ClassNotFoundException {
		frame.getTextAreaLog().setText("");
		File f = new File(paintingToLoad.getAbsolutePath()/*.replaceAll("bin", "txt")*/);
		File logFile = new File(paintingToLoad.getAbsolutePath().replaceAll("bin", "txt"));
		BufferedReader br = new BufferedReader(new FileReader(logFile));
		String logLine;

		while ((logLine = br.readLine()) != null) {
			frame.getTextAreaLog().append(logLine + '\n');

		}
		br.close();

		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(paintingToLoad));
		try {
			cmdStack.setCurrent(-1);
			model.getShapes().clear();
			cmdStack.getList().clear();
			cmdStack.getShapes().clear();
			cmdStack.getLogCommandsUndo().clear();
			cmdStack.getLogCommandsRedo().clear();
			selectedShapes.clear();
			frame.getBtnUndo().setEnabled(false);
			frame.getBtnRedo().setEnabled(false);

			

			model.getShapes().addAll((ArrayList<Shape>) ois.readObject());
			ois.close();
		
		} catch (IOException exc) {
			// some other I/O error: print it, log it, etc.
			exc.printStackTrace(); // for example
		}
		for (int i = 0; i < model.getShapes().size(); i++) {
			if (model.getShapes().get(i).isSelected()) {
				selectedShapes.add(model.getShapes().get(i));
			}
		}
		frame.getView().repaint();
	}
	
	public void loadLog(File paintingToLoad) throws IOException {
		loadLogEnd=false;
		cmdStack.setCurrent(-1);
		model.getShapes().clear();
		cmdStack.getList().clear();
		cmdStack.getShapes().clear();
		cmdStack.getLogCommandsUndo().clear();
		cmdStack.getLogCommandsRedo().clear();
		selectedShapes.clear();
		logList.clear();
	
		logCounter=0;
		frame.getBtnUndo().setEnabled(false);
		frame.getBtnRedo().setEnabled(false);
		frame.getTextAreaLog().setText("");
		//selectEditUpdate();
		
		BufferedReader br = new BufferedReader(new FileReader(paintingToLoad));
		
		while ((line = br.readLine()) != null) {
			logList.add(line);
			//System.out.println(line);
		}
		br.close();
		//System.out.println(logList);
		frame.getBtnloadNext().setEnabled(true);
		frame.getBtnUndo().setEnabled(true);
		loadNext();
	}
	public void loadNext() {
		Shape s = null;
		//System.out.println(logCounter + "  " + logList.size());
		if(logCounter < logList.size()) {
			line=logList.get(logCounter);
			System.out.println(line);
			if(line.contains("Point")) { 		  
				int x = Integer.parseInt(line.substring(line.indexOf("(")+1, line.indexOf(",")));
				int y = Integer.parseInt(line.substring(line.indexOf(",")+2,line.indexOf(")")));
				String color = line.substring(line.lastIndexOf("(")+1, line.lastIndexOf(")"));
				Color c = frame.getBtnInsideColor().getBackground();
				c= new Color(Integer.parseInt(color));
				s = new Point(x,y,c);
				/*CmdPointAdd cmd = new CmdPointAdd(model, (Point)s);
				cmd.execute();*/
				//s.setColor(c);
				if(logCounter == logList.size()-1) {
					frame.getBtnloadNext().setEnabled(false);
					loadLogEnd = true;
				}
			} else if(line.contains("Line")) {
				int x = Integer.parseInt(line.substring(line.indexOf("(")+1, line.indexOf(",")));
				int y = Integer.parseInt(line.substring(line.indexOf(",")+2,line.indexOf(")")));				
				int x1 = Integer.parseInt(line.substring(ordinalIndexOf(line, "(", 1)+1, ordinalIndexOf(line, ",", 1)));
				int y1 = Integer.parseInt(line.substring(ordinalIndexOf(line, ",", 1)+2, ordinalIndexOf(line, ")", 1)));
				String color = line.substring(line.lastIndexOf("(")+1, line.lastIndexOf(")"));
				Color c = frame.getBtnInsideColor().getBackground();

				c=new Color(Integer.parseInt(color));

				s = new Line(new Point(x,y),new Point(x1,y1),c);
				//s.setColor(c);
				if(logCounter == logList.size()-1) {
					frame.getBtnloadNext().setEnabled(false);
					loadLogEnd = true;
				}
			} else if(line.contains("Rectangle")) {
				int x = Integer.parseInt(line.substring(line.indexOf("(")+1, line.indexOf(",")));
				int y = Integer.parseInt(line.substring(line.indexOf(",")+2, line.indexOf(")")));
			 	int h = Integer.parseInt(line.substring(ordinalIndexOf(line,"=",0)+1, ordinalIndexOf(line,",",2)));
			 	System.out.println(""+h);
				int w = Integer.parseInt(line.substring(ordinalIndexOf(line,"=",1)+1, ordinalIndexOf(line, ",", 3)));
				String insideColor = line.substring(ordinalIndexOf(line, "(", 1)+1, ordinalIndexOf(line, ")", 1));
				String outsideColor = line.substring(line.lastIndexOf("(")+1, line.lastIndexOf(")"));
				
				Color iC = frame.getBtnInsideColor().getBackground();
				Color oC = frame.getBtnOutsideColor().getBackground();

				iC=new Color(Integer.parseInt(insideColor));
				oC=new Color(Integer.parseInt(outsideColor));

				s = new Rectangle(new Point(x,y), h, w, iC, oC);
				
				if(logCounter == logList.size()-1) {
					frame.getBtnloadNext().setEnabled(false);
					loadLogEnd = true;
				}
			} else if(line.contains("Circle")) {
				int x = Integer.parseInt(line.substring(line.indexOf("(")+1, line.indexOf(",")));
				int y = Integer.parseInt(line.substring(line.indexOf(",")+2, line.indexOf(")")));
			 	int r = Integer.parseInt(line.substring(ordinalIndexOf(line,"=",0)+1, ordinalIndexOf(line,",",2)));
				String insideColor = line.substring(ordinalIndexOf(line, "(", 1)+1, ordinalIndexOf(line, ")", 1));
				String outsideColor = line.substring(line.lastIndexOf("(")+1, line.lastIndexOf(")"));
			
				Color iC = frame.getBtnInsideColor().getBackground();
				Color oC = frame.getBtnOutsideColor().getBackground();

				iC=new Color(Integer.parseInt(insideColor));
				oC=new Color(Integer.parseInt(outsideColor));
				
				s = new Circle(new Point(x,y), r, iC, oC);
				
				if(logCounter == logList.size()-1) {
					frame.getBtnloadNext().setEnabled(false);
					loadLogEnd = true;
				}
			} else if(line.contains("Donut")) {
				
				int x = Integer.parseInt(line.substring(line.indexOf("(")+1, line.indexOf(",")));
				int y = Integer.parseInt(line.substring(line.indexOf(",")+2, line.indexOf(")")));
			 	int bigR = Integer.parseInt(line.substring(ordinalIndexOf(line,"=",0)+1, ordinalIndexOf(line,",",2)));
				String bigInsideColor = line.substring(ordinalIndexOf(line, "(", 1)+1, ordinalIndexOf(line, ")", 1));
				String bigOutsideColor = line.substring(ordinalIndexOf(line, "(", 2)+1, ordinalIndexOf(line, ")", 2));
			 	int smallR = Integer.parseInt(line.substring(ordinalIndexOf(line,"=",1)+1, line.lastIndexOf(".")));
				/*String smallInsideColor = line.substring(ordinalIndexOf(line, "(", 3)+1, ordinalIndexOf(line, ")", 3));
				String smallOutsideColor = line.substring(line.lastIndexOf("(")+1, line.lastIndexOf(")"));*/

				Color bigIC = frame.getBtnInsideColor().getBackground();
				Color bigOC = frame.getBtnOutsideColor().getBackground();
				/*Color smallIC = frame.getBtnInsideColor().getBackground();
				Color smallOC = frame.getBtnOutsideColor().getBackground();*/

				bigIC=new Color(Integer.parseInt(bigInsideColor));
				bigOC=new Color(Integer.parseInt(bigOutsideColor));
				/*smallIC=new Color(Integer.parseInt(smallInsideColor));
				smallOC=new Color(Integer.parseInt(smallOutsideColor));*/

				s = new Donut(new Point(x,y), bigR, smallR, bigIC, bigOC);
			
				if(logCounter == logList.size()-1) {
					frame.getBtnloadNext().setEnabled(false);
					loadLogEnd = true;
				}
			} else if(line.contains("Hexagon")) {
				int x = Integer.parseInt(line.substring(line.indexOf("(")+1, line.indexOf(",")));
				int y = Integer.parseInt(line.substring(line.indexOf(",")+2, line.indexOf(")")));
			 	int r = Integer.parseInt(line.substring(ordinalIndexOf(line,"=",0)+1, ordinalIndexOf(line,",",2)));
				String insideColor = line.substring(ordinalIndexOf(line, "(", 1)+1, ordinalIndexOf(line, ")", 1));
				String outsideColor = line.substring(line.lastIndexOf("(")+1, line.lastIndexOf(")"));

				Color iC = frame.getBtnInsideColor().getBackground();
				Color oC = frame.getBtnOutsideColor().getBackground();

				iC=new Color(Integer.parseInt(insideColor));
				oC=new Color(Integer.parseInt(outsideColor));
				
				s = new HexagonAdapter(new Point(x,y), r, iC, oC);
				
				if(logCounter == logList.size()-1) {
					frame.getBtnloadNext().setEnabled(false);
					loadLogEnd = true;
				}
			}
			
			if(line.contains("Undo Added")) {
				CmdShapeAdd cmd = new CmdShapeAdd(model,s);
				CmdShapeSelect cmdS = new CmdShapeSelect(this, s);
				cmd.execute();
				cmdS.execute();
				frame.getTextAreaLog().append("Added - "+s+"\n");
				
				//undoRedoHelper(cmd);
				undoRedoHelper(cmd,s,"Removed","Added");
			} else if(line.contains("Added")) {
				CmdShapeAdd cmd = new CmdShapeAdd(model,s);
				cmd.execute();
				frame.getTextAreaLog().append("Added - "+s+"\n");
				
				//undoRedoHelper(cmd);
				undoRedoHelper(cmd,s,"Removed","Added");

			} else if (line.contains("Selected")) {
				for(int i=0; i< model.getShapes().size(); i++) {
					if(s.equals(model.getShapes().get(i))) {
						s = model.getShapes().get(i);
					}
				}
				if(s.isSelected()) {
					selectedShapes.remove(s);
				}
				CmdShapeSelect cmd = new CmdShapeSelect(this, s);
				cmd.execute();
				frame.getTextAreaLog().append("Selected - "+s+"\n");		

				//undoRedoHelper(cmd);
				undoRedoHelper(cmd,s,"Deselected","Selected");

			} else if(line.contains("Deselected")) {
				for(int i=0; i< model.getShapes().size(); i++) {
					if(s.equals(model.getShapes().get(i))) {
						s = model.getShapes().get(i);
					}
				}
				CmdShapeDeselect cmd = new CmdShapeDeselect(this, s);
				cmd.execute();
				frame.getTextAreaLog().append("Deselected - "+s+"\n");		

				//undoRedoHelper(cmd);
				undoRedoHelper(cmd,s,"Selected","Deselected");

			} else if(line.contains("Removed")) {
				CmdShapeRemove cmd = new CmdShapeRemove(model,s);
				CmdShapeDeselect cmdDS = new CmdShapeDeselect(this, s);
				cmd.execute();
				cmdDS.execute();
				frame.getTextAreaLog().append("Removed - "+s+"\n");

				//undoRedoHelper(cmd);
				undoRedoHelper(cmd,s,"Added","Removed");

			} else if(line.contains("Modified")) {
				
				if(s instanceof Point) {
					Point p = (Point) s;
					Point helpP = (Point) selectedShapes.get(0);
					CmdPointUpdate cmd = new CmdPointUpdate(helpP, p);
					p.setPositionHistory(helpP.getPositionHistory());
					cmd.execute();
					frame.getTextAreaLog().append("Modified - "+p+"\n");

					//undoRedoHelper(cmd);
					undoRedoHelper(cmd,p,"Modified","Modified");
				} else if (s instanceof Line) {
					Line l = (Line) s;
					Line helpL = (Line) selectedShapes.get(0);
					CmdLineUpdate cmd = new CmdLineUpdate(helpL, l);
					l.setPositionHistory(helpL.getPositionHistory());
					cmd.execute();
					frame.getTextAreaLog().append("Modified - "+l+"\n");

					//undoRedoHelper(cmd);
					undoRedoHelper(cmd,l,"Modified","Modified");
				} else if (s instanceof Rectangle) {
					Rectangle r = (Rectangle) s;
					Rectangle helpR = (Rectangle) selectedShapes.get(0);
					CmdRectangleUpdate cmd = new CmdRectangleUpdate(helpR, r);
					r.setPositionHistory(helpR.getPositionHistory());
					cmd.execute();
					frame.getTextAreaLog().append("Modified - "+r+"\n");

					//undoRedoHelper(cmd);
					undoRedoHelper(cmd,r,"Modified","Modified");
				} else if (s instanceof Donut) {
					Donut d = (Donut) s;
					Donut helpD = (Donut) selectedShapes.get(0);
					CmdDonutUpdate cmd = new CmdDonutUpdate(helpD, d);
					d.setPositionHistory(helpD.getPositionHistory());
					cmd.execute();
					frame.getTextAreaLog().append("Modified - "+d+"\n");

					//undoRedoHelper(cmd);
					undoRedoHelper(cmd,d,"Modified","Modified");
				} else if (s instanceof Circle) {
					Circle c = (Circle) s;
					Circle helpC = (Circle) selectedShapes.get(0);
					CmdCircleUpdate cmd = new CmdCircleUpdate(helpC, c);
					c.setPositionHistory(helpC.getPositionHistory());
					cmd.execute();
					frame.getTextAreaLog().append("Modified - "+c+"\n");

					//undoRedoHelper(cmd);
					undoRedoHelper(cmd,c,"Modified","Modified");
				} else if (s instanceof HexagonAdapter) {
					HexagonAdapter h = (HexagonAdapter) s;
					HexagonAdapter helpH = (HexagonAdapter) selectedShapes.get(0);
					CmdHexagonUpdate cmd = new CmdHexagonUpdate(helpH, h);
					h.setPositionHistory(helpH.getPositionHistory());
					cmd.execute();
					frame.getTextAreaLog().append("Modified - "+h+"\n");

					//undoRedoHelper(cmd);
					undoRedoHelper(cmd,h,"Modified","Modified");
				}
				
			} else if(line.contains("back")){
				CmdBringToBack cmd = new CmdBringToBack(model);
				cmd.execute();
				frame.getTextAreaLog().append("Moved to back - "+selectedShapes.get(0)+"\n");

				//undoRedoHelper(cmd);
				undoRedoHelper(cmd,selectedShapes.get(0),"Moved to front","Moved to back");
			}
			else if(line.contains("backward")){
				CmdBringBackward cmd = new CmdBringBackward(model);
				cmd.execute();
				frame.getTextAreaLog().append("Moved backward - "+selectedShapes.get(0)+"\n");

				//undoRedoHelper(cmd);
				undoRedoHelper(cmd,selectedShapes.get(0),"Moved forward","Moved backward");
			}
			else if(line.contains("front")){
				CmdBringToFront cmd = new CmdBringToFront(model);
				cmd.execute();
				frame.getTextAreaLog().append("Moved to front - "+selectedShapes.get(0)+"\n");

				//undoRedoHelper(cmd);
				undoRedoHelper(cmd,selectedShapes.get(0),"Moved to back","Moved to front");
			}
			else if(line.contains("forward")) {
				CmdBringForward cmd = new CmdBringForward(model);
				cmd.execute();
				frame.getTextAreaLog().append("Moved forward - "+selectedShapes.get(0)+"\n");

				//undoRedoHelper(cmd);
				undoRedoHelper(cmd,selectedShapes.get(0),"Moved backward","Moved forward");
			}
			logCounter++;
			frame.getView().repaint();
		}
		//selectEditUpdate();
	}
	//pomocna metoda koja pronalazi mesto karaktera u stringu
	public int ordinalIndexOf(String str, String substr, int n) {
	    int pos = -1;
	    do {
	        pos = str.indexOf(substr, pos + 1);
	    } while (n-- > 0 && pos != -1);
	    return pos;
	}
	
	public void selectEditUpdate() {
		if (selectedShapes.size() != 0) {
			if (selectedShapes.size() == 1) {
				//edit samo ako jedno selectovano
				buttonsObserver.setEditButtonActivated(true);
				buttonsUpdate();
			} else {//vise od 1
				buttonsObserver.setEditButtonActivated(false);
				buttonsObserver.setBringToBackButtonActivated(false);
				buttonsObserver.setBringToFrontButtonActivated(false);
				buttonsObserver.setToFrontButtonActivated(false);
				buttonsObserver.setToBackButtonActivated(false);
			}
			//mozemo brisati vise odjednom
			buttonsObserver.setDeleteButtonActivated(true);
		} else {
			//ako nista nije selected nemamo nista od dugmica
			buttonsObserver.setDeleteButtonActivated(false);
			buttonsObserver.setEditButtonActivated(false);

			buttonsObserver.setBringToBackButtonActivated(false);
			buttonsObserver.setBringToFrontButtonActivated(false);
			buttonsObserver.setToFrontButtonActivated(false);
			buttonsObserver.setToBackButtonActivated(false);
		}
	}
	
	public void buttonsUpdate() {
		ListIterator<Shape> it = model.getShapes().listIterator();
		while (it.hasNext()) {
			selectedShape = it.next();
			if (selectedShape.isSelected()) {
				//System.out.println("Koji je po redu: " + getIndexOf(model.getShapes(), selectedShape));
				if (model.getShapes().size() != 1) { //jeste selectovan ali ne jedini
					if (selectedShape.equals(model.get(model.getShapes().size() - 1))) {
						//ako je poslednji
						buttonsObserver.setToFrontButtonActivated(false);
						buttonsObserver.setBringToFrontButtonActivated(false);
						buttonsObserver.setToBackButtonActivated(true);
						buttonsObserver.setBringToBackButtonActivated(true);
					} else if (selectedShape.equals(model.get(0))) {
						//ako je prvi
						buttonsObserver.setBringToBackButtonActivated(false);
						buttonsObserver.setToBackButtonActivated(false);
						buttonsObserver.setBringToFrontButtonActivated(true);
						buttonsObserver.setToFrontButtonActivated(true);
					} else {
						//ako je u sredini
						buttonsObserver.setBringToBackButtonActivated(true);
						buttonsObserver.setBringToFrontButtonActivated(true);
						buttonsObserver.setToBackButtonActivated(true);
						buttonsObserver.setToFrontButtonActivated(true);
					}
				}
			}
		}
	}
	
//	public int getIndexOf(List<Shape> list, Shape shape) {
//		int pos = 0;
//		for (Shape myObj : list) {
//			if (shape.equals(myObj))
//				return pos;
//			pos++;
//		}
//		return -1;
//	}
	
}
