package command;

import mvc.DrawingFrame;
import mvc.DrawingModel;
import shapes.Shape;

public class CmdShapeRemove implements Command {

	private DrawingModel model;
	private Shape shape;
	
	public CmdShapeRemove(DrawingModel model, Shape shape) {
		this.model = model;
		this.shape = shape;
	}
	
	@Override
	public void execute() {
		model.remove(shape);
		//shape.getPositionHistory().clear();
		//DrawingFrame.getTextAreaLog().append("Removed - "+shape+"\n");
	}

	@Override
	public void unexecute() {
		shape.getPositionHistory().add(model.getShapes().size());
		model.add(shape);
		//DrawingFrame.getTextAreaLog().append("Added - "+shape+"\n");
	}
}
