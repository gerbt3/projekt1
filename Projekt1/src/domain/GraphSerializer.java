package domain;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import examples.Edge;
import examples.Graph;
import examples.Vertex;

/*
 * Serializes and deserializes graphs permanently and temporally
 * for saving graphs and animating algorithms
 */
public class GraphSerializer<V,E> {

	private ArrayList<byte[]> byteEditorGraphs;
	private int editorIndex = 0;
	private ArrayList<byte[]> byteAlgoGraphs;
	ArrayList<Graph<V,E>> algoGraphs;
	private int algoIndex = 0;
	private boolean isStart = true;
	
	public GraphSerializer(){
		byteEditorGraphs= new ArrayList<byte[]>();
		byteAlgoGraphs= new ArrayList<byte[]>();
		algoGraphs = new ArrayList<Graph<V,E>>();
	}
	
	//------------------------------------------------------------------------------------//
	// Methods for storing graphs permanently
	//------------------------------------------------------------------------------------//
	
	/*
	 * Saves a graph permanently as a file by serializing
	 */
	public void saveGraph(String name, Graph<V,E> graph) throws IOException {
		
		//Clears all selected vertices or edges before saving the graph
		Iterator<Vertex<V>> vIt = graph.vertices();
		while (vIt.hasNext()) {
			vIt.next().set(Attribut.color, Color.black);
		}
		
		Iterator<Edge<E>> eIt = graph.edges();
		while (eIt.hasNext()) {
			eIt.next().set(Attribut.color, Color.black);
		}
		
		String filename = "GraphFiles/" + name + ".ser";
		ObjectOutputStream oos = null;
		try {			
			oos = new ObjectOutputStream(new FileOutputStream(new File(filename)));	
		    oos.writeObject(graph); 
		} catch (IOException e1) {
			System.out.println("@GraphSerializer: GraphSerializer failed to serialize a graph");
			e1.printStackTrace();
		} finally {
			oos.close();
		}
	}
	
	/*
	 * Opens a saved graph from a file by deserializing
	 */
	public Graph<V,E> openGraph(String name) throws IOException {
		String filename = "GraphFiles/" + name + ".ser";
		Graph<V,E> graph = null;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(filename));
			 graph = (Graph<V,E>) ois.readObject();
		} catch (IOException e) {
			System.out.println("@GraphSerializer: GraphSerializer failed to deserialize a graph");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("@GraphSerializer: GraphSerializer failed to deserialize a graph");
			e.printStackTrace();
		} finally {
			ois.close();
		}
		return graph;
	}

	//------------------------------------------------------------------------------------//
	// Methods for coping graph temporarily in the graph editor
	//------------------------------------------------------------------------------------//
	
	/*
	 * Makes a temporary copy of a graph by serializing
	 * For undoing and redoing actions in the graph editor
	 */
	public void serializeEditorGraph(Graph<V, E> g, boolean undo) throws IOException{
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {			
			oos = new ObjectOutputStream(bos);
			oos.writeObject(g);
			if(editorIndex < byteEditorGraphs.size()) {
				for (int i = editorIndex; i < byteEditorGraphs.size(); i++) byteEditorGraphs.remove(i);
				byteEditorGraphs.add(editorIndex, bos.toByteArray());
			}
			else byteEditorGraphs.add(editorIndex, bos.toByteArray());
			if (!undo) editorIndex++;
		} catch (IOException e1) {
			System.out.println("@GraphSerializer: GraphSerializer failed to serialize a graph");
			e1.printStackTrace();
		} finally {
			oos.close();
		}
	}
	
	/*
	 * Deserializes all temporary copies of the graphs
	 * For undoing and redoing actions in the graph editor
	 * Returns null, if the graph doesn't exist
	 */
	public Graph<V,E> deserializeEditorGraph(int index) throws IOException, ClassNotFoundException {
		
		ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(byteEditorGraphs.get(index)));
		try {			
			return (Graph<V,E>) ois.readObject();
		} catch (IOException e1) {
			System.out.println("@GraphSerializer: GraphSerializer failed to deserialize a graph");
			e1.printStackTrace();
		} finally {
			ois.close();
		}
		return null;	
	}
	
	/*
	 * Checks if the arraylist of graphs has a previous element,
	 * going from the editorIndex
	 */
	public boolean isUndoPossible() {
		if (editorIndex <= 0) return false;
		else return true;
	}
	
	/*
	 * Returns the previous element in the arraylist of graphs
	 * If the previous element exists, has to be checked first
	 * Returns null, if the graph doesn't exist
	 */
	public Graph<V,E> undo(Graph<V,E> g) throws ClassNotFoundException, IOException {
		serializeEditorGraph(g, true);
		if (editorIndex > 0) editorIndex--;
		return deserializeEditorGraph(editorIndex);
	}
	
	/*
	 * Checks if the arraylist of graphs has a next element,
	 * going from the editorIndex
	 */
	public boolean isRedoPossible() {
		if (editorIndex > byteEditorGraphs.size()-1) return false;
		else return true;
	}
	
	/*
	 * Returns the next element in the arraylist of graphs
	 * If the next element exists, has to be checked first
	 * Returns null, if the graph doesn't exist
	 */
	public Graph<V,E> redo() throws ClassNotFoundException, IOException {
		if (editorIndex < byteEditorGraphs.size()-1) editorIndex++;
		return deserializeEditorGraph(editorIndex);
	}
	
	/*
	 * Resets the byte array and the index
	 * for saving graphs for undoing and redoing actions
	 */
	public void clearEditorGraphs() {
		byteEditorGraphs.clear();
		editorIndex = 0;
	}
	
	//------------------------------------------------------------------------------------//
	// Methods for coping graph temporarily in the algorithm editor
	//------------------------------------------------------------------------------------//
	
	/*
	 * Makes a temporary copy of a graph by serializing
	 * For animating an algorithm in the algorithm editor
	 */
	public void serializeAlgoGraph(Graph<V, E> g) throws IOException{
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {			
			oos = new ObjectOutputStream(bos);
			oos.writeObject(g);
			byteAlgoGraphs.add( bos.toByteArray());
		} catch (IOException e1) {
			System.out.println("@GraphSerializer: GraphSerializer failed to serialize a graph");
			e1.printStackTrace();
		} finally {
			oos.close();
		}
	}
	
	/*
	 * Returns true if an animation of an algorithm gets started
	 * Returns false if an animation of an algorithm gets paused
	 */
	public boolean isStart() {
		return isStart;
	}
	
	/*
	 * Deserializes all temporary copies of the graphs
	 * For animating an algorithm in the algorithm editor
	 */
	public boolean deserializeAlgoGraphs() throws IOException, ClassNotFoundException {
		
		//Only deserializes the graphs, when the animation gets started
		//non when it gets paused
		if (isStart) {
			
			//Clears the list of deserialized graphs
			algoGraphs.clear();
			algoIndex = 0;
			
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(byteAlgoGraphs.get(0)));
			
			for (int i = 0; i < byteAlgoGraphs.size(); i++) {
			
				ois = new ObjectInputStream(new ByteArrayInputStream(byteAlgoGraphs.get(i)));
				
				try {			
					algoGraphs.add((Graph<V,E>) ois.readObject());
				} catch (IOException e1) {
					System.out.println("@GraphSerializer: GraphSerializer failed to deserialize a graph");
					e1.printStackTrace();
				} finally {
					ois.close();
				}
			}
			
			//Clears the list of serialized graphs
			byteAlgoGraphs.clear();
			
			isStart = false;
			return true;
		}
		
		return false;
	}

	/*
	 * Checks if the arraylist of graphs has a next element,
	 * going from the algoIndex
	 */
	public boolean hasNextGraph() {
		if (algoIndex >= algoGraphs.size()-1) return false;
		else return true;
	}
	
	/*
	 * Returns the next element in the arraylist of graphs
	 * If the next element exists, has to be checked first
	 */
	public Graph<V,E> getNextGraph() {
		algoIndex++;
		return algoGraphs.get(algoIndex-1);
	}
	
	/*
	 * Checks if the arraylist of graphs has a previous element,
	 * going from the algoIndex
	 */
	public boolean hasPreviousGraph() {
		if (algoIndex <= 0) return false;
		else return true;
	}
	
	/*
	 * Returns the previous element in the arraylist of graphs
	 * If the previous element exists, has to be checked first
	 */
	public Graph<V,E> getPreviousGraph() {
		algoIndex--;
		return algoGraphs.get(algoIndex+1);
	}
	
	/*
	 * Clears the byte array, the array list and the index
	 * of graphs for animating algorithms
	 */
	public void clearAlgoGraphs() {
		byteAlgoGraphs.clear();
		algoGraphs.clear();
		algoIndex = 0;
		isStart = true;
	}
}
