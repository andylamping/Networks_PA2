
public class RemoveEvent extends Event {

	
	public int getSource() {
		return source;
	}
	public void setSource(int source) {
		this.source = source;
	}
	public int getDestination() {
		return destination;
	}
	public void setDestination(int destination) {
		this.destination = destination;
	}
	RemoveEvent(int turn, int action, int source, int destination) {
		super(turn, action);
		this.source = source;
		this.destination = destination;
	}
	protected int source; 
	protected int destination ; 	
}
