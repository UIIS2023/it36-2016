package command;

import shapes.Line;

public class CmdLineUpdate implements Command{
	private Line oldState;
	private Line newState;
	private Line original = new Line();
	
	public CmdLineUpdate(Line oldState, Line newState) {
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
