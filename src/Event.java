
public abstract  class Event {

	protected int turn;
	protected int action;
	public static final int ACTION_REMOVE = 1;
	public static final int ACTION_MODIFY = 2;
	
	
	public int getTurn() {
		return turn;
	}
	public void setTurn(int turn) {
		this.turn = turn;
	}
	public int getAction() {
		return action;
	}
	public void setAction(int action) {
		this.action = action;
	}
	public Event(int turn, int action) {
		super();
		this.turn = turn;
		this.action = action;
	}
	public abstract int getSource();
	public abstract void setSource(int source);
	public abstract int getDestination();
	public abstract void setDestination(int destination) ;
	
	
}
