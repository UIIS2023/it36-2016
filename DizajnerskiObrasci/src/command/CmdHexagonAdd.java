package command;

import adapter.HexagonAdapter;
import mvc.DrawingModel;

public class CmdHexagonAdd implements Command {
	
	private DrawingModel model;
	private HexagonAdapter hexagon;
	
	public CmdHexagonAdd(DrawingModel model, HexagonAdapter hexagon) {
		this.model = model;
		this.hexagon = hexagon;
	}

	@Override
	public void execute() {

		hexagon.getPositionHistory().add(model.getShapes().size());

		model.add(hexagon);
	}

	@Override
	public void unexecute() {
		model.remove(hexagon);
		//hexagon.getPositionHistory().clear();
	}
}
