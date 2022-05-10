package controller.commands;

import model.VersionsManager;

public class ChangeVersionsStrategyCommand implements Command {
	
	public ChangeVersionsStrategyCommand() {
		super();
	}

	@Override
	public void execute() {
		VersionsManager.getInstance().changeStrategy();
	}

}
