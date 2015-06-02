package domain;

import java.awt.Dimension;
import java.awt.Point;
import examples.Graph;


public class ViewHandler<V,E> {

	private GraphTool<V,E> graphTool;
	private GraphFrame<V,E> frame;
	private GraphView<V,E> graphView;
	
	public ViewHandler(GraphTool<V,E> gt){
		this.graphTool=gt;
		frame=new GraphFrame<V,E>(this.graphTool);
		this.graphView=frame.getGraphView();
	}
	
	/*
	 * paints the graph
	 */
	public void setGraph(Graph<V,E> g){
		graphView.paintGraph(g);
	}

	/*
	 * deletes an unfinished line
	 */
	public void deleteEdge() {
		graphView.deleteEdge();
		
	}

	/*
	 * draws an unfinished line
	 */
	public void insertEdge(Point p1, Point p2) {
		graphView.insertEdge(p1, p2);
		
	}

	/*
	 * choose if a vertex is directed or not
	 */
	public boolean chooseGraphOption() {
		return frame.chooseGraphOption();
	}

	/*
	 * Resets the color of the startbutton
	 * after the animations of algorithms has finished
	 */
	public void resetStartButton() {
		frame.resetStartButton();
	}
}
