package command;

import shapes.Circle;

public class CmdCircleUpdate implements Command {
	private Circle oldState;
	private Circle newState;
	private Circle original = new Circle();
	
	public CmdCircleUpdate(Circle oldState, Circle newState) {
		this.oldState = oldState;
		this.newState = newState;
	}

	@Override
	public void execute() {	
//		System.out.println("a");
		original = oldState.clone(original);
//		System.out.println("b");
		oldState = newState.clone(oldState);
//		System.out.println("C");
	}
	

	@Override
	public void unexecute() {	
		oldState = original.clone(oldState);
	}
}
