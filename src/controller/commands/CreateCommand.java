package controller.commands;

import controller.LatexEditorController;
import model.Document;
import model.DocumentManager;

public class CreateCommand implements Command {
	private DocumentManager documentManager = new DocumentManager();
	//private VersionsManager versionsManager;
	
	public CreateCommand() {
		super();
	}

	@Override
	public void execute() {
		LatexEditorController controller = LatexEditorController.getInstance();
		
		String type = controller.getType();
		Document document = documentManager.createDocument(type);
		controller.setDocument(document);
	}

}