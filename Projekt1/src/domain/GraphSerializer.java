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

	private ArrayList<byte[]> byteAlgoGraphs;
	ArrayList<Graph<V,E>> algoGraphs;
	private int algoIndex = 0;
	
	public GraphSerializer(){
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
	// Methods for coping graph temporarily
	//------------------------------------------------------------------------------------//
	
	/*
	 * Makes a temporary copy of a graph by serializing
	 */
	public void serializeGraph(Graph<V, E> g) throws IOException{
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
	 * Deserializes all temporary copies of the graphs
	 */
	public void deserializeAlgoGraphs() throws IOException, ClassNotFoundException {
		
		algoGraphs = new ArrayList<Graph<V,E>>();
		
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
		return algoGraphs.get(algoIndex);
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
		return algoGraphs.get(algoIndex);
		
	}
	
	/*
	 * Set the index for the arraylist of graphs 
	 * back to the beginning
	 */
	public void resetAlgoIndex() {
		algoIndex = 0;
	}
	
	/*
	 * Returns the size of the arraylist of graphs
	 */
	public int getAlgoGraphsSize() {
		return algoGraphs.size();
	}
}
