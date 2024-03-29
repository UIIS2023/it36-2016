package command;

import mvc.DrawingModel;
import shapes.Point;

public class CmdPointRemove implements Command{

	private DrawingModel model;
	private Point point;
	
	public CmdPointRemove(DrawingModel model, Point point) {
		this.model = model;
		this.point = point;
	}

	@Override
	public void execute() {
		model.remove(point);
		//point.getPositionHistory().clear();
	}

	@Override
	public void unexecute() {
		point.getPositionHistory().add(model.getShapes().size());
		model.add(point);
		System.out.println(point.getPositionHistory());
	}

}
