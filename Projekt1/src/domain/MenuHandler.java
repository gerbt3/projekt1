package domain;

import java.io.IOException;

import domain.GraphTool.Attribut;

/*
 * Handles everything between the frame and the tool class
 */
public class MenuHandler {

	GraphTool graphTool;
	
	public MenuHandler(GraphTool g) {
		graphTool = g;
	}
	
	public void saveGraph(String name) throws IOException {
		graphTool.saveGraph(name);
	}
	
	public void openGraph(String name) throws IOException {
		graphTool.openGraph(name);
	}
	
	public void createGraph(boolean directed){
		graphTool.createGraph(directed);
	}

	public void itemChanged(Attribut attr, boolean selected) {
		// TODO Auto-generated method stub
		
	}
	
}
