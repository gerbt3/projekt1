package domain;


public class ViewController {

	public enum DrawState {
		SELECT,
		DELETE,
		VERTEX,
		EDGE
	}
	
	private DrawState currentState;
	
	public ViewController() {
		
	}
	
	public void setCurrentState(DrawState newState) {
		
		this.currentState = newState;
		System.out.println("currentState: " + this.currentState);
	}
	
	public void startClicked() {
		
		
	}
	
	public void stopClicked() {
		
		
	}
	
	public void nextStepClicked() {
		
		
	}
	
	public void beforeStepClicked() {
		
		
	}
	
	public void commitClicked() {
		
		
	}
	
	public void selectClicked() {
		
		this.currentState = DrawState.SELECT;
	}
	
	public void deleteClicked() {
		
		this.currentState = DrawState.DELETE;
	}
	
	public void drawVertexClicked() {
		
		this.currentState = DrawState.VERTEX;
	}
	
	public void drawEdgeClicked() {
		
		this.currentState = DrawState.EDGE;
	}
}
