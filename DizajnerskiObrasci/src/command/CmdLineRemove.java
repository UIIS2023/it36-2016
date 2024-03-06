package command;

import mvc.DrawingModel;
import shapes.Line;

public class CmdLineRemove implements Command{
	private DrawingModel model;
	private Line line;
	
	public CmdLineRemove(DrawingModel model, Line line) {
		this.model = model;
		this.line = line;
	}

	@Override
	public void execute() {
		model.remove(line);
		//line.getPositionHistory().clear();
	}

	@Override
	public void unexecute() {
		line.getPositionHistory().add(model.getShapes().size());

		model.add(line);
	}
}
