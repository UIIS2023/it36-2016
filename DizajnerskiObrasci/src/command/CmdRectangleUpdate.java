package command;

import shapes.Rectangle;

public class CmdRectangleUpdate implements Command{

	private Rectangle oldState;
	private Rectangle newState;
	private Rectangle original = new Rectangle();
	
	public CmdRectangleUpdate(Rectangle oldState, Rectangle newState) {
		this.oldState = oldState;
		this.newState = newState;
	}

	@Override
	public void execute() {
		original = oldState.clone(original);
		oldState = newState.clone(oldState);
	}

	@Override
	public void unexecute() {
		
		oldState = original.clone(oldState);
	}

}
