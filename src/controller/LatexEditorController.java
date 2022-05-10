package controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.JEditorPane;

import controller.commands.Command;
import controller.commands.CommandFactory;
import model.Document;

public class LatexEditorController{
	
	private HashMap<String, Command> commands;
	
	private Document document;
	private String filename;
	private String type;
	private JEditorPane editorPane;
	private String addedLatexCommand;
	
	private String strategy;
	
	private static LatexEditorController controller = null;
	
	private LatexEditorController() {
		CommandFactory commandFactory = new CommandFactory();
		commands = new HashMap<String, Command>(); 
		
		List<String> commandsString = Arrays.asList(new String[] {"addLatex", "changeVersionsStrategy", "create", "disableVersionsManagement",
											"edit", "enableVersionsManagement", "load", "rollbackToPreviousVersion", "save", 
											"saveHTML", "loadHTML"});
		
		for (String command: commandsString) {
			commands.put(command, commandFactory.createCommand(command));
		}
	}
	
	public static LatexEditorController getInstance() {
		if (controller == null) {
			controller = new LatexEditorController();
		}
		
		return controller;
	}
	
	public void enact(String command) {
		commands.get(command).execute();
	}
	
	public void setDocument(Document document) {
		this.document = document;
	}
	
	public Document getDocument() {
		return this.document;
	}
	
	public void setEditorPane(JEditorPane editorPane) {
		this.editorPane = editorPane;
	}
	
	public JEditorPane getEditorPane() {
		return this.editorPane;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStrategy() {
		return strategy;
	}

	public void setStrategy(String strategy) {
		this.strategy = strategy;
	}

	public String getAddedLatexCommand() {
		return addedLatexCommand;
	}

	public void setAddedLatexCommand(String addedLatexCommand) {
		this.addedLatexCommand = addedLatexCommand;
	}
	
}
