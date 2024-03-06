package command;

import shapes.Donut;

public class CmdDonutUpdate implements Command {
	private Donut oldState;
	private Donut newState;
	private Donut original = new Donut();
	
	public CmdDonutUpdate(Donut oldState, Donut newState) {
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
