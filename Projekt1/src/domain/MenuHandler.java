package domain;

import java.io.IOException;

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
	
}
