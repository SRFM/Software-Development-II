package controller.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import controller.LatexEditorController;
import model.Document;

public class LoadCommand implements Command {
	
	public LoadCommand() {
		super();
	}

	@Override
	public void execute() {

		LatexEditorController controller = LatexEditorController.getInstance();
		
		String fileContents = "";
		try {
			Scanner scanner = new Scanner(new FileInputStream(controller.getFilename()));
			while(scanner.hasNextLine()) {
				fileContents = fileContents + scanner.nextLine() + "\n";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Document currentDocument = new Document();
		currentDocument.setContents(fileContents);
		controller.setDocument(currentDocument);
		
		String type = "emptyTemplate";
		
		fileContents = fileContents.trim();
		if(fileContents.startsWith("\\documentclass[11pt,twocolumn,a4paper]{article}")) {
			type = "articleTemplate";
		}
		else if(fileContents.startsWith("\\documentclass[11pt,a4paper]{book}")) {
			type = "bookTemplate";
		}
		else if(fileContents.startsWith("\\documentclass[11pt,a4paper]{report}")) {
			type = "reportTemplate";
		}
		else if(fileContents.startsWith("\\documentclass{letter}")) {
			type = "letterTemplate";
		}
		
		controller.setType(type);
	}

}
