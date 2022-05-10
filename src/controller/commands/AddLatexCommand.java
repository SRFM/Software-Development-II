package controller.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import controller.LatexEditorController;
import model.VersionsManager;

public class AddLatexCommand implements Command  {

	public AddLatexCommand() {
		super();
	}

	@Override
	public void execute() {
		
		VersionsManager versionsManager = VersionsManager.getInstance();
		LatexEditorController controller = LatexEditorController.getInstance();
		
		if(versionsManager.isEnabled()) {
			versionsManager.putVersion(controller.getDocument());
			controller.getDocument().changeVersion();
		}
		
		String type = controller.getAddedLatexCommand();
		
		String contents = controller.getEditorPane().getText();
		String before = contents.substring(0, controller.getEditorPane().getCaretPosition());
		String after = contents.substring(controller.getEditorPane().getCaretPosition());
		
		contents = before + getLatexCommand(type) + after;
		
		controller.getEditorPane().setText(contents);
		controller.getDocument().setContents(contents);
		
	}
	
	public String getLatexCommand(String type) {
		String data = "";
		
		try {
			Scanner myReader = new Scanner(new File("./commands/" + type + ".txt"));
			
			while (myReader.hasNextLine()) {
				 data += myReader.nextLine() + "\n";
			}
			myReader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred while reading the ./commands/" + type + ".txt file.");
			e.printStackTrace();
		}
		
		return data;
	}
}
