
public class Entry {
	protected int id_source;
	protected int id;
	protected int cost;
	public static final int NO_NODE = -1;
	
	public Entry(int id, int cost) {
		super();
		this.id_source = NO_NODE;
		this.id = id;
		this.cost = cost;
	}
	public int getId_source() {
		return id_source;
	}
	public void setId_source(int id_source) {
		this.id_source = id_source;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public String toString(){
		
		return id_source+" "+id+" "+cost;
		
	}

	
	
}
