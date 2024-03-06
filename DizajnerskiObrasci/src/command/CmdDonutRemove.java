package command;

import mvc.DrawingModel;
import shapes.Donut;

public class CmdDonutRemove implements Command {
	private DrawingModel model;
	private Donut donut;
	
	public CmdDonutRemove(DrawingModel model, Donut donut) {
		this.model = model;
		this.donut = donut;
	}

	@Override
	public void execute() {
		model.remove(donut);
		//donut.getPositionHistory().clear();
	}

	@Override
	public void unexecute() {

		donut.getPositionHistory().add(model.getShapes().size());

		model.add(donut);
	}
}
