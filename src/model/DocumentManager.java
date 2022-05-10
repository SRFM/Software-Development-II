package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class DocumentManager {
	private HashMap<String, Document> templates;
	private List <String> templateTypes = Arrays.asList(new String[] {"reportTemplate", "bookTemplate", "articleTemplate", 
			"letterTemplate", "emptyTemplate"});
	
	public DocumentManager() {
		templates = new HashMap<String, Document>();
				
		for (String type: templateTypes) {
			Document document = new Document();
			document.setContents(getContents(type));
			templates.put(type, document);
		}
	}
	
	public Document createDocument(String type) {
		return templates.get(type).clone();
	}
	
	public String getContents(String type) {
		
		String data = "";
		
		try {
			Scanner myReader = new Scanner(new File("./templates/" + type + ".txt"));
			
			while (myReader.hasNextLine()) {
				 data += myReader.nextLine() + "\n";
			}
			myReader.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred while reading the ./templates/" + type + ".txt file.");
			e.printStackTrace();
		}
		
		return data;
	}		
}
