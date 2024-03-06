package command;

import mvc.DrawingModel;
import shapes.Shape;

public class CmdBringForward implements Command {
	private DrawingModel model;

	
	public CmdBringForward(DrawingModel model) {
		this.model=model;
	}

	@Override
	public void execute() {
		for(int j = 0;j<=model.getShapes().size()-1;j++){
			if(model.getShapes().get(j).isSelected()) {
				if(j==model.getShapes().size()-1) {
					return;
				}else {
					Shape s = model.getShapes().get(j+1);
					model.getShapes().set(j+1, model.getShapes().get(j));
					model.getShapes().set(j, s);
					System.out.println("prvi" + model.getShapes().get(j).getPositionHistory()+ " drugi "+ model.getShapes().get(j+1).getPositionHistory());
					model.getShapes().get(j+1).getPositionHistory().set(model.getShapes().get(j+1).getPositionHistory().size()-1,j+1);
					model.getShapes().get(j).getPositionHistory().set(model.getShapes().get(j).getPositionHistory().size()-1,j);
					System.out.println(model.getShapes().get(j).getPositionHistory()+ "  "+ model.getShapes().get(j+1).getPositionHistory());
					
					//DrawingFrame.getTextAreaLog().append("Moved forward: "+model.get(j+1)+"\n");
					return;
				}
			}
		}
	}

	@Override
	public void unexecute() {
		for(int j = 0;j<=model.getShapes().size()-1;j++){
			if(model.getShapes().get(j).isSelected()) {
				if(j==0) {
					return;
				}else {
					Shape s = model.getShapes().get(j-1);
					model.getShapes().set(j-1, model.getShapes().get(j));
					model.getShapes().set(j, s);
					model.getShapes().get(j-1).getPositionHistory().set(model.getShapes().get(j-1).getPositionHistory().size()-1,j-1);
					model.getShapes().get(j).getPositionHistory().set(model.getShapes().get(j).getPositionHistory().size()-1,j);
					//DrawingFrame.getTextAreaLog().append("Moved backward: "+model.get(j-1)+"\n");
					//System.out.println(model.getShapes().get(j).getPositionHistory()+ "  "+ model.getShapes().get(j-1).getPositionHistory());
					return;
				}
			}
		}
		//DrawingFrame.getTextAreaLog().append("\nMoved backward: ");
	}
	
}
