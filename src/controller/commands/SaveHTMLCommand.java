package controller.commands;

import controller.LatexEditorController;
import model.Document;
import model.VersionsManager;

public class SaveHTMLCommand implements Command {
	private boolean isPreviousTable = false;
	
	@Override
	public void execute() {
			
		LatexEditorController controller = LatexEditorController.getInstance();
		String [] lines = controller.getEditorPane().getText().split("\n");
		
		if(VersionsManager.getInstance().isEnabled()) {
			VersionsManager.getInstance().putVersion(controller.getDocument());
			controller.getDocument().changeVersion();
		}
		
		String fileContents = "";
				
		for (String line: lines) {
			fileContents += changeLineTexToHtml(line) + "\n";
		}
		
		Document document = new Document();
		document.setContents(fileContents);
		document.save(controller.getFilename());
	}
	
	private String changeLineTexToHtml(String line) {
				
		if (line.strip().contains("\\documentclass")) {
			return "<!DOCTYPE html>\n<html>";
		}
		else if (line.strip().contains("\\begin{document}")) {
			return "<body>";
		}
		else if (line.strip().contains("\\end{document}")) {
			return "</body>";
		}
		else if (line.strip().contains("\\chapter")) {
			String chapter = line.strip().split("\\{")[1];
			return "<h1>" + chapter.substring(0, chapter.length()-1);
		}
		else if (line.strip().contains("\\section")) {
			String section = line.strip().split("\\{")[1];
			return "<h2>" + section.substring(0, section.length()-1);
		}
		else if (line.strip().contains("\\subsection")) {
			String subsection = line.strip().split("\\{")[1];
			return "<h3>" + subsection.substring(0, subsection.length()-1);
		}
		else if (line.strip().contains("\\subsubsection")) {
			String subsubsection = line.strip().split("\\{")[1];
			return "<h4>" + subsubsection.substring(0, subsubsection.length()-1);
		}
		else if (line.strip().contains("\\begin{enumerate}")) {
			return "<ol>";
		}
		else if (line.strip().contains("\\end{enumerate}")) {
			return "</ol>";
		}
		else if (line.strip().contains("\\begin{itemize}")) {
			return "<ul>";
		}
		else if (line.strip().contains("\\end{itemize}")) {
			return "</ul>";
		}
		else if (line.strip().contains("\\item")) {
			String item = line.strip().replace("\\item ", "");
			return "<li>" + item + "</li>";
		}
		else if (line.strip().contains("\\begin{table}")) {
			isPreviousTable = true;
			return "";
		}
		else if (line.strip().contains("\\end{table}")) {
			isPreviousTable = false;
			return "";
		}	
		else if (line.strip().contains("\\begin{tabular}")) {
			return "<table>";
		}
		else if (line.strip().contains("\\end{tabular}")) {
			return "</table>";
		}
		else if (line.strip().contains("&") && line.strip().contains("\\\\")) {
			String [] items = line.replace("\\\\", "").split("&");
			String finalString = "<tr>\n";
			for (String x: items) {
				finalString += "<th>" + x + "</th>\n";
			}
			return finalString + "</tr>\n";
		}		
		else if (line.strip().contains("\\hline")) {
			return "<tr><td colspan=\"100%\"><hr></td></tr>";
		}
		else if (line.strip().contains("\\begin{figure}")) {
			return "<figure>";
		}
		else if (line.strip().contains("\\end{figure}")) {
			return "</figure>";
		}
		else if (line.strip().contains("\\includegraphics")) {
			
			String src = line.strip().split("\\{")[1];
			return "<img src=\"" + src.substring(0, src.length()-1) + "\">";
		}
		else if (line.strip().contains("\\caption")) {
			if (isPreviousTable)
				return "";
			String caption = line.strip().split("\\{")[1];
			return "<figcaption>" + caption.substring(0, caption.length()-1) + "</figcaption>";
		}
		else if (line.strip().contentEquals("")) {
			return line;
		}
		else if (line.strip().startsWith("%")) {
			String newLine = line.replace("%", "");
			return "<!--" + newLine + "-->";
		}
		
		return "<!--" + line + "-->";
	}

}