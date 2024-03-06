package command;

import shapes.Point;

public class CmdPointUpdate implements Command{
	private Point newState;
	private Point oldState;
	private Point original = new Point();
	
	public CmdPointUpdate(Point oldState, Point newState) {
		this.oldState = oldState;
		this.newState = newState;
	}
	

	@Override
	public void execute() {
		
		original = oldState.clone(original);
		oldState = newState.clone(oldState);
		//System.out.println("metoda, oldState -> " + oldState);
	}

	@Override
	public void unexecute() {
		oldState = original.clone(oldState);
	}

}
