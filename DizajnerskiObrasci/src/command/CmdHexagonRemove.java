package command;

import adapter.HexagonAdapter;
import mvc.DrawingModel;

public class CmdHexagonRemove implements Command {
	
	private DrawingModel model;
	private HexagonAdapter hexagon;
	
	public CmdHexagonRemove(DrawingModel model, HexagonAdapter hexagon) {
		this.model = model;
		this.hexagon = hexagon;
	}

	@Override
	public void execute() {
		model.remove(hexagon);
		//hexagon.getPositionHistory().clear();
	}

	@Override
	public void unexecute() {
		hexagon.getPositionHistory().add(model.getShapes().size());
		model.add(hexagon);
	}
}
