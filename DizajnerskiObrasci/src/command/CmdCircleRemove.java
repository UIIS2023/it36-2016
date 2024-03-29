package command;

import mvc.DrawingModel;
import shapes.Circle;

public class CmdCircleRemove implements Command {
	private DrawingModel model;
	private Circle circle;
	
	public CmdCircleRemove(DrawingModel model, Circle circle) {
		this.model = model;
		this.circle = circle;
	}

	@Override
	public void execute() {
		//circle.getPositionHistory().clear();
		model.remove(circle);
	}

	@Override
	public void unexecute() {
		circle.getPositionHistory().add(model.getShapes().size());
		model.add(circle);
	}
}
