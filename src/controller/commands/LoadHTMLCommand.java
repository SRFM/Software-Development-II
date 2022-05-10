package controller.commands;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import controller.LatexEditorController;
import model.Document;

public class LoadHTMLCommand implements Command{
	
	private boolean isFirstTableItem = false;
	
	public LoadHTMLCommand () {
		super();
	}
	
	@Override
	public void execute() {
		LatexEditorController controller = LatexEditorController.getInstance();
		
		String fileContents = "";
		        
        switch(controller.getType()) {
        	case "articleTemplate": fileContents += "\\documentclass[11pt,twocolumn,a4paper]{article}\n";
        					break;
        	case "bookTemplate": 	fileContents += "\\documentclass[11pt,a4paper]{book}\n";
						 	break;
        	case "reportTemplate": 	fileContents += "\\documentclass[11pt,a4paper]{report}\n";
			 			   	break;
        	case "letterTemplate": 	fileContents += "\\documentclass{letter}\n";
			 				break;
        }	
        
		try {
			Scanner scanner = new Scanner(new FileInputStream(controller.getFilename()));
			while(scanner.hasNextLine()) {
				fileContents = fileContents + getLatexCommand(scanner.nextLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Document currentDocument = new Document();
		currentDocument.setContents(fileContents);
		controller.setDocument(currentDocument);
	}

	
	private String getLatexCommand(String command) {
		
		if (command.toLowerCase().strip().contains("<body>")) {
			return "\\begin{document}\n";
		}
		else if (command.toLowerCase().strip().contains("</body>")) {
			return "\\end{document}\n";
		}
		else if (command.toLowerCase().contains("<h1>")) {
			String chapter = command.split("1>")[1];
			return "\\chapter{" + chapter + "}" + "\n";
		}
		else if (command.toLowerCase().contains("<h2>")) {
			String section = command.split("2>")[1];
			return "\\section{" + section + "}" + "\n";
		}
		else if (command.toLowerCase().contains("<h3>")) {
			String subsection = command.split("3>")[1];
			return "\\subsection{" + subsection + "}" + "\n";
		}
		else if (command.toLowerCase().contains("<h4>")) {
			String subsubsection = command.split("4>")[1];
			return "\\subsubsection{" + subsubsection + "}" + "\n";
		}
		else if (command.toLowerCase().contains("<ol>")) {
			return "\\begin{enumerate}" + "\n";
		}	
		else if (command.toLowerCase().contains("</ol>")) {
			return "\\end{enumerate}" + "\n";
		}
		else if (command.toLowerCase().contains("<ul>")) {
			return "\\begin{itemize}" + "\n";
		}
		else if (command.toLowerCase().contains("</ul>")) {
			return "\\end{itemize}" + "\n";
		}
		else if (command.toLowerCase().contains("<li>")) {
			String item = command.strip().substring(4, command.strip().length()-5);
			return "\\item " + item + " \n";
		}
		else if (command.toLowerCase().contains("<table")) {
			return "\\begin{table}" + "\n" + "\\begin{tabular}{ |c|c|c| }" + "\n";
		}
		else if (command.toLowerCase().contains("</table>")) {
			return "\\end{tabular}" + "\n" + "\\end{table}" + "\n";
		}
		else if (command.toLowerCase().contains("<tr>")) {
			isFirstTableItem = true;
			return "";
		}
		else if (command.toLowerCase().contains("</tr>")) {
			return "\\\\" + "\n";
		}
		else if (command.toLowerCase().contains("<th>")) {
			String item = command.strip().substring(4, command.strip().length()-5)+ " ";
			if (isFirstTableItem) {
				isFirstTableItem = false;
				return item;
			}
			return " & " + item;
		}
		else if (command.toLowerCase().strip().contains("<figure")) {
			return "\\begin{figure}" + "\n";
		}
		else if (command.toLowerCase().strip().contains("</figure>")) {
			return "\\end{figure}" + "\n";
		}
		else if (command.toLowerCase().strip().contains("<img")) {
			
			String src = command.strip().split("src=")[1];
			src = src.split("\\s+")[0];
			src = src.substring(1, src.length()-1);
			
			return "\\includegraphics{" + src + "}" + "\n";
		}
		else if (command.toLowerCase().strip().contains("<figcaption>")) {
			return "\\caption{" + command.strip().substring(12, command.strip().length()-13) + "}\n";
		}
		else if (command.strip().contentEquals("")) {
			return "\n";
		}
		else if (command.strip().contains("DOCTYPE")) {
			return "";
		}
		else if (command.strip().contains("<html>")) {
			return "";
		}
		else if (command.strip().contains("<!--")) {
			String newLine = command.replace("<!--", "").replace("-->", "");
			
			if (!newLine.strip().startsWith("\\"))
				return "%" + newLine + "\n";
			
			return newLine + "\n";
		}
		
		return "%" + command + "\n";
	}
}







