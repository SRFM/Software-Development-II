package tests;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JEditorPane;

import controller.LatexEditorController;
import model.Document;
import model.VersionsManager;
import model.strategies.VolatileVersionsStrategy;

public class Tests {
	
	private LatexEditorController controller = LatexEditorController.getInstance();
	private VersionsManager versionsManager = VersionsManager.getInstance();
    
    String contents = "\\documentclass[11pt,twocolumn,a4paper]{article}\n" + "\n" + "\\begin{document}\n" + "\\title{Article: Test the Project}\n"
						+ "\\author{Author1 \\and Autho2}\n" + "\\date{\\today}\n" + "\n" + "\\maketitle\n" + "\n" + "\\section{Section Title 1}\n"
						+ "\n" + "\\chapter{Chapter 1}\n" + "\n" + "\\begin{enumerate}\n" + "\\item First Item \n" + "\\item Second Item \n"
						+ "\\end{enumerate}\n"+ "\n"+ "\\subsection{Subsection Test}\n"+ "\n"+ "\\section{Conclusion}\n" + "\n"+ "\\end{document}\n";

    String contentsHTML = "<!DOCTYPE html>\n<html>\n\n<body>\n<!--\\title{Article: Test the Project}-->\n<!--\\author{Author1 \\and Autho2}-->\n" +
    					"<!--\\date{\\today}-->\n\n<!--\\maketitle-->\n\n<h2>Section Title 1\n\n<h1>Chapter 1\n\n<ol>\n<li>First Item</li>\n" +
    					"<li>Second Item</li>\n</ol>\n\n<h3>Subsection Test\n\n<h2>Conclusion\n\n</body>\n";

	
	@Test
	public void testSaveCommand() {
		Document document = new Document();
		document.setContents(contents);
		controller.setDocument(document);
		
		controller.setFilename("./test-files/saveFileTest.tex");
		controller.enact("save");
		
		String result = readFromFile("./test-files/saveFileTest.tex");
		assertEquals(controller.getDocument().getContents(), result);
	}
	
	@Test
	public void testLoadCommand() {
		controller.setFilename("./test-files/loadFileTest.tex");
		controller.enact("load");
		
		assertEquals(controller.getDocument().getContents(), contents);
	}
	
	@Test
	public void testAddLatexCommand() {
		Document document = new Document();
		document.setContents(contents);
		controller.setDocument(document);
		
		JEditorPane pane = new JEditorPane();
		pane.setText(contents);
		pane.setCaretPosition(182);
		controller.setEditorPane(pane);
		
		controller.setAddedLatexCommand("chapter");
		controller.enact("addLatex");
				
		assertEquals(controller.getDocument().getContents(), readFromFile("./test-files/addLatexFileTest.tex"));
	}
	
	@Test
	public void testChangeVersionsStrategyCommand() {
		controller.setStrategy("stable");
		versionsManager.setStrategy(new VolatileVersionsStrategy());
		controller.enact("enableVersionsManagement");
		
		controller.setStrategy("volatile");
		controller.enact("changeVersionsStrategy");
		assertEquals(versionsManager.getStrategy() instanceof VolatileVersionsStrategy, true);
	}
	
	@Test
	public void testCreateCommandCommand() {
		controller.setType("articleTemplate");
		controller.enact("create");
		
		assertEquals(controller.getDocument().getContents(), readFromFile("./templates/articleTemplate.txt"));
	}
	
	@Test
	public void testEnableVersionsManagementCommand() {
		controller.enact("enableVersionsManagement");
		assertEquals(versionsManager.isEnabled(), true);
	}
	
	@Test
	public void testDisableVersionsManagementCommand() {
		controller.enact("disableVersionsManagement");
		assertEquals(versionsManager.isEnabled(), false);
	}
	
	@Test
	public void testEditCommand() {
		Document document = new Document();
		document.setContents(contents);
		controller.setDocument(document);
		
		JEditorPane pane = new JEditorPane();
		pane.setText(contents);
		controller.setEditorPane(pane);
		
		String contents2 = contents + "test edit command\n";
		pane.setText(contents2);
		
		controller.enact("edit");
		
		assertEquals(controller.getDocument().getContents(), contents2);
	}
	
	@Test
	public void testRollbackToPreviousVersionCommand() {
		Document document = new Document();
		document.setContents(contents);
		controller.setDocument(document);
		
		controller.setStrategy("volatile");
		versionsManager.setStrategy(new VolatileVersionsStrategy());
		controller.enact("enableVersionsManagement");
		
		JEditorPane pane = new JEditorPane();
		pane.setText(contents);
		pane.setCaretPosition(182);
		controller.setEditorPane(pane);
		
		controller.setAddedLatexCommand("chapter");
		controller.enact("addLatex");
		
		controller.enact("rollbackToPreviousVersion");
		
		assertEquals(controller.getDocument().getContents(), contents);
	}
	
	@Test
	public void testSaveHTMLCommand() {
		Document document = new Document();
		document.setContents(contents);
		controller.setDocument(document);
		
		controller.setFilename("./test-files/saveHTMLFileTest.html");
		controller.enact("saveHTML");
		
		String result = readFromFile("./test-files/saveHTMLFileTest.html");
		assertEquals(contentsHTML, result);
	}
		
	@Test
	public void testLoadHTMLCommand() {
		controller.setFilename("./test-files/loadHTMLFileTest.html");
		controller.enact("loadHTML");
		
		assertEquals(controller.getDocument().getContents(), contents);
	}
		
	private String readFromFile(String filename) {
		String fileContents = "";
		
		try {
			Scanner scanner = new Scanner(new FileInputStream(filename));
			while(scanner.hasNextLine()) {
				fileContents += scanner.nextLine() + "\n";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return fileContents;
	}
}
