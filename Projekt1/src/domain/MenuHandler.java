package domain;

import java.io.IOException;


/*
 * Handles everything between the frame and the tool class
 */
public class MenuHandler<V,E> {

	private GraphTool<V,E> graphTool;
	
	public MenuHandler(GraphTool<V,E> g) {
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
		graphTool.itemChanged(attr, selected);
		
	}
	
}
