package controller.commands;

import controller.LatexEditorController;
import model.VersionsManager;

public class SaveCommand implements Command {
		
	public SaveCommand() {
	}
	
	@Override
	public void execute() {

		VersionsManager versionsManager = VersionsManager.getInstance();
		LatexEditorController controller = LatexEditorController.getInstance();
				
		if(versionsManager.isEnabled()) {
			versionsManager.putVersion(controller.getDocument());
			controller.getDocument().changeVersion();
		}
		
		if (controller.getEditorPane() != null)
			controller.getDocument().setContents(controller.getEditorPane().getText());
		controller.getDocument().save(controller.getFilename());
	}

}
