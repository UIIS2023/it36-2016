package command;

import mvc.DrawingModel;
import shapes.Shape;

public class CmdBringToBack implements Command {

	private DrawingModel model;
	private int positionBefore;
	
	public CmdBringToBack(DrawingModel model) {
		this.model=model;
	}	
	
	@Override
	public void execute() {
		for (int j= model.getShapes().size()-1;j>=0;j--) {
			if (model.getShapes().get(j).isSelected()) {
				positionBefore = model.getShapes().get(j).getPositionHistory().get(model.getShapes().get(j).getPositionHistory().size()-1);
				model.getShapes().get(j).getPositionHistory().add(model.getShapes().get(j).getPositionHistory().size()-1, positionBefore);
			}
		}
		
		for(int j = model.getShapes().size()-1;j>=0;j--){
			if(model.getShapes().get(j).isSelected()) {
				if(j==0) {
					//DrawingFrame.getTextAreaLog().append("Moved to back: "+model.getShapes().get(j).toString()+"\n");
					return;
				}else {
					Shape s = model.getShapes().get(j-1);
					model.getShapes().set(j-1, model.getShapes().get(j));
					model.getShapes().set(j, s);
					model.getShapes().get(j-1).getPositionHistory().set(model.getShapes().get(j-1).getPositionHistory().size()-1,j-1);
					model.getShapes().get(j).getPositionHistory().set(model.getShapes().get(j).getPositionHistory().size()-1,j);
					//System.out.println( model.getShapes().get(j-1).getPositionHistory());
				}
		}
	}

		
	}

	@Override
	public void unexecute() {
		for (int j = 0;j<=model.getShapes().size()-1;j++) {
			if (model.getShapes().get(j).isSelected()) {
				if (model.getShapes().get(j).getPositionHistory().get(model.getShapes().get(j).getPositionHistory().size()-1) 
					==model.getShapes().get(j).getPositionHistory().get(model.getShapes().get(j).getPositionHistory().size()-2)) {
					model.getShapes().get(j).getPositionHistory().remove(model.getShapes().get(j).getPositionHistory().size()-1);
					//System.out.println(model.getShapes().get(j).getPositionHistory());
					return;
				} else {
					Shape s = model.getShapes().get(j+1);
					model.getShapes().set(j+1, model.getShapes().get(j));
					model.getShapes().set(j, s);
					model.getShapes().get(j+1).getPositionHistory().set(model.getShapes().get(j+1).getPositionHistory().size()-1,j+1);
					model.getShapes().get(j).getPositionHistory().set(model.getShapes().get(j).getPositionHistory().size()-1,j);
				}
			}
		}
		
//		for(int j = 0;j<=model.getShapes().size()-1;j++){
//			if(model.getShapes().get(j).isSelected()) {
//				if(j==model.getShapes().size()-1 ) {
//					//DrawingFrame.getTextAreaLog().append("Moved to front: "+model.getShapes().get(j).toString()+"\n");
//					return;
//				}else {
//				
//				Shape s = model.getShapes().get(j+1);
//				model.getShapes().set(j+1, model.getShapes().get(j));
//				model.getShapes().set(j, s);
//				
//				
//				}
//			}
//		}
		
	}

}
