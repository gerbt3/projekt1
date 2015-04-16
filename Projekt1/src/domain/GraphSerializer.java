package domain;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import examples.Graph;

public class GraphSerializer<V,E> {

	private ArrayList<byte[]> editorGraph;
	private ArrayList<byte[]> algoGraph;
	private int editorIndex, algoIndex;
	
	public GraphSerializer(){
		editorGraph=new ArrayList<byte[]>();
		algoGraph= new ArrayList<byte[]>();
	}
	public void saveGraph(){

	}

	public void openGraph(){

	}

	public void saveEditorGraph(Graph<V, E> g){
		this.serializeGraph(g, editorGraph);
	}

	public void saveAlgoGraph(Graph<V, E> g){
		this.serializeGraph(g, algoGraph);
	}

	public void undoEditor(){

	}

	public void redoEditor(){

	}

	public void stepBefore(){

	}

	public void nextStep(){

	}

	public void serializeGraph(Graph<V, E> g, ArrayList<byte[]> list){
		ByteArrayOutputStream bos = new ByteArrayOutputStream();		
		try {			
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(g);
			oos.close();
			list.add( bos.toByteArray());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
