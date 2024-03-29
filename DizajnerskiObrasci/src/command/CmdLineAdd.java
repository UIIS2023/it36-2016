package command;

import mvc.DrawingModel;
import shapes.Line;

public class CmdLineAdd implements Command {
	private DrawingModel model;
	private Line line;
	
	public CmdLineAdd(DrawingModel model, Line line) {
		this.model = model;
		this.line = line;
	}

	@Override
	public void execute() {

		line.getPositionHistory().add(model.getShapes().size());

		model.add(line);
	}

	@Override
	public void unexecute() {
		model.remove(line);
		//line.getPositionHistory().clear();
	}
}
