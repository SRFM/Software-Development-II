package controller.commands;

import controller.LatexEditorController;
import model.VersionsManager;

public class EditCommand implements Command {
	
	public EditCommand() {
		super();
	}


	@Override
	public void execute() {

		LatexEditorController controller = LatexEditorController.getInstance();
		VersionsManager manager = VersionsManager.getInstance();
		
		if(manager.isEnabled() && !controller.getDocument().getContents().contentEquals(controller.getEditorPane().getText())) {
			manager.putVersion(controller.getDocument());
			controller.getDocument().changeVersion();
		}
		
		String text = controller.getEditorPane().getText();
		controller.getDocument().setContents(text);		
	}

}
