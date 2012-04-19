
public class ModifyEvent extends Event {

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
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public ModifyEvent(int turn, int action, int source, int destination,
			int cost) {
		super(turn, action);
		this.source = source;
		this.destination = destination;
		this.cost = cost;
	}
	protected int source; 
	protected int destination ; 
	protected int cost ; 
	

}
