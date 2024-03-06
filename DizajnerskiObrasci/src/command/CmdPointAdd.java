package command;

import mvc.DrawingModel;
import shapes.Point;

public class CmdPointAdd implements Command {
	private DrawingModel model;
	private Point point;
	
	public CmdPointAdd(DrawingModel model, Point point) {
		this.model = model;
		this.point = point;
	}

	@Override
	public void execute() {
		point.getPositionHistory().add(model.getShapes().size());
		model.add(point);
		

	}

	@Override
	public void unexecute() {
		//point.getPositionHistory().clear();
		model.remove(point);

	}
	
}
