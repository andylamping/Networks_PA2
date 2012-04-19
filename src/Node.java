import java.util.ArrayList;

/**
 * This is a node of the system
 */


public class Node {

	protected int id; // shouldn't be modified once initialized
	protected ArrayList<Integer> N = new ArrayList<Integer>();
	final int INFINITY = 100000;
	private ArrayList<Integer> neighbors = new ArrayList<Integer>(); // holds neighbors of this node
	private ArrayList<Integer> distVector = new ArrayList<Integer>(); // distance vector of this node
	private ArrayList<Message> messages = new ArrayList<Message>(); // messages to be sent on turn()
	private ArrayList<ArrayList<Integer>> allVectors = new ArrayList<ArrayList<Integer>>(); // distance vectors of this vector and those received from neighbors
	private ArrayList<ArrayList<Integer>> outVectors = new ArrayList<ArrayList<Integer>>(); // this node's distance vector version that it will send to each neighbor, implementing posion reverse
	private int hasUpdate = 0; // this variable will let the main function know if this node's distVect has change
	
	
	public Node(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void addBaseN(int id) {
		N.add(id);
	}

	public void removeBaseN(int id) {
		for(int k = 0 ; k < N.size() ; k ++ ) 
			if (N.get(k) == id ) N.remove(k) ;
	}

	public boolean hasBaseN( int id){
		for(int k = 0 ; k < N.size() ; k ++ ) 
			if (N.get(k) == id ) return true;
		return false;
	}



	/**
	 * Add a neighbor to the node and sets the cost to reach it
	 * The id is added to this.neighbors, and then the cost.
	 * So, every index that is an even number will be an id, and every index
	 * that is an odd number will be a cost.
	 * @param id
	 * @param cost
	 */
	public void addNeighbor(int id, int cost){
		// Make sure that this.allVectors and this.outVectors has a spot for this node
		// so that we can save the incoming and outgoing distance vectors for that node
		int vecSize = 0;
		if(id > this.getId())
			vecSize = id;
		else
			vecSize = this.getId();
		if((vecSize + 1) > this.allVectors.size()){
			int j = this.allVectors.size();
			while(j < (vecSize + 1)){
				ArrayList<Integer> dummy = new ArrayList<Integer>();
				this.allVectors.add(dummy);
				this.outVectors.add(dummy);
				j++;
			}
		}
		// add new neighbor to this.neighbors
		this.neighbors.add(id);
		this.neighbors.add(cost);
		// add new neighbor to this.distVector
		this.distVector.add(id);
		this.distVector.add(cost);
		// let turn()/main() know that this.distVector has been updated
		this.hasUpdate = 1;
		return;
		
	}
	
	
	
	/**
	 * Remove the neighbor with the id id
	 * @param id
	 */
	public void removeNeighbor(int id){

		int i = 0;
		// remove neighbor from this.neighbors by setting its distance to 
		// infinity. implementation could also have removed it entirely from
		// the neighbors list but this is more favorable to the updateOutputs() setup as it is
		while(i < this.neighbors.size()){
			if(this.neighbors.get(i) == id){
				this.neighbors.set(i+1, INFINITY);
				break;
			}
			i = i + 2;
		}
	return;
	}
	


	/**
	 * Changes the cost to reach the neighbor with the id id
	 * @param id
	 * @param cost
	 */
	public void changeCost(int id, int cost){
		
		int i = 0;
		// change the cost to node id
		while( i < this.neighbors.size()){
			if(id == this.neighbors.get(i)){
				this.neighbors.set(i + 1, cost);
				break;
			}
			i = i + 2;
		}
		
		return;
	}
	

	/**
	 * Return the list of messages a node wants to send in 
	 * a given turn
	 * @return
	 */
	public ArrayList<Message> turn(){
		// clear messages
		this.messages.clear();
		// update this node's distance vector by running updateOutputs
		// updateOutputs() will return a list of messages and will change
		// this.hasUpdate to 1 if the distance vector has changed so that
		// these messages need to be sent
		ArrayList<ArrayList<Integer>> output = updateOutputs();
		// 
		if(this.hasUpdate == 1){
			this.hasUpdate = 0;
		int x = 0;
		while (x < this.neighbors.size()){
			int a = this.neighbors.get(x);
			String text = vectorToString(output.get(a));
			Message add = new Message(a, text);
			this.messages.add(add);
			x = x + 2;
		}
		 output.clear();
		 return this.messages;
		}
		
		 
		return null;
			
	}


	
	/**
	 * Called when a message is received by the node
	 * @param m
	 */
	public void ReceiveMessage(Message m){
	
		// get id of node who sent the message
		int source = m.getId_source();
		// split the message by , as the message is a distance vector of
		// the source node
		String[] split = m.text.split(",");
		int i = 0;
		// create ArrayList<Integer> that is a distance vector of the source node
		// so we can update this.allVectors
		ArrayList<Integer> update = new ArrayList<Integer>();
		while(i < split.length){
			update.add(Integer.parseInt(split[i]));
			i++;
		}
		
		// update this source vector in this.allVectors
	    this.allVectors.set(source, update);
	    // check to see if we can access any new nodes
	    updateVector(source);
	    return;
	  
		
	}
	/**
	 * Called after a message is received by the node in ReceiveMessage()
	 * This allows the node to see if it can reach any new nodes from after
	 * receiving the neighbor vector
	 * @param source, the node who sent the message
	 */
	public int updateVector(int source){
		int value = 0;
		// if the update came from another node, we check
		if(source != this.getId()){
			int i = 0;
			int have = 0;
			while (i < this.allVectors.get(source).size()){
				int j = 0;
				have = 0;
				// if the node has the current node in question, have is set to 0
				// to bypass to the next node
				while(j < this.distVector.size() && have == 0){
					if(this.distVector.get(j) == this.allVectors.get(source).get(i)){
						have = 1;
					}
			
				j = j + 2;
				
				}
				// if have is still 0, then we have a new node
			if(have == 0 && this.allVectors.get(source).get(i) != this.getId()){
				
				int addon = 0;
				int a = 0;
				while (a < this.distVector.size()){
					// we need to find the distance from the neighbor vector
					// to this new node and add this to the distance from this
					// node to that vector
					if(this.distVector.get(a) == source){
						addon = this.distVector.get(a + 1);
					}
					a = a + 2;
				}
				this.hasUpdate = 1;
				this.distVector.add(this.allVectors.get(source).get(i));
				this.distVector.add(addon + this.allVectors.get(source).get(i+1));
			
			}
			i = i + 2;
		}
	}
		
		return value;
		}

	/**
	 * Return the output after the algorithm
	 * @return
	 */
	public ArrayList<Entry> sendOutput(){
		
		
		ArrayList<Entry> out = new ArrayList<Entry>();
		int i = 0;
		while(i < this.distVector.size()){
			// if the distance from this node to another node is infinity, then
			// we cannot reach it so we must reflect that in our desired output
			if(this.distVector.get(i+1) != INFINITY)
				out.add(new Entry(this.distVector.get(i), this.distVector.get(i+1)));
			i = i + 2;
		}
		
		return out;
	}
	/**
	 * Called when a vector needs to be translated to a string so that 
	 * it can be passed in a Message
	 * @param m
	 */
	public String vectorToString(ArrayList<Integer> entry){
		int j = 0;
		// prepare the message
		String text = "";
		if(entry.size() >= 2){
			text += entry.get(0).toString() + ",";
			text += entry.get(1).toString();
		}
		j = 2;
		while(j < entry.size()){
			text += "," + entry.get(j).toString() + ",";
			text += entry.get(j + 1).toString();
			j = j + 2;
		}
		
		return text;
	}
	
	
	/**
	 * This is where the Distance Vector Algorithm takes place
	 * @param m
	 */
	public  ArrayList<ArrayList<Integer>> updateOutputs(){
		// prepare list to contain the distance vectors that this node will
		// send to its neighbors
		ArrayList<ArrayList<Integer>> output = new ArrayList<ArrayList<Integer>>();
		int t = 0;
		int max = 0;
		while(t < this.distVector.size()){
			if(this.distVector.get(t) > max)
				max = this.distVector.get(t);
			t = t + 2;
		}
		
		t = 0;
		while(t <= max){
			ArrayList<Integer> dummy1 = new ArrayList<Integer>();
			output.add(dummy1);
			t++;
		}
		
		
		// DV algorithm
				int allNodes = 0;
					while(allNodes < this.distVector.size()){
						int neighborNodes = 0;
						int minimum = INFINITY;
						int hopNode = INFINITY;
						
						int destination = this.distVector.get(allNodes);
						// for each neighbor, calculate C(thisnode, neighbor) + C(neighbor, destination)
						int passing = 0;
						while(neighborNodes < this.neighbors.size()){
							int index = 0;
							int distance = INFINITY;
							int neighbor = this.neighbors.get(neighborNodes);
					
							// get C(thisnode, neighbor)
							
							while(index < this.neighbors.size()){
								if(neighbor == this.neighbors.get(index)){
									distance = this.neighbors.get(index + 1);
									break;
								}
								
							index = index + 2;
							
							}
							
							int connection = 0;
							// now if the destination is not the current neighbor,
							// calculate cost from current neighbor to destination
							// if this distance is INFINITY, we set the total distance to INFINITY
						    if(neighbor != destination && distance != INFINITY){
							 connection = INFINITY;
							 index = 0;
							
							 while( index < this.allVectors.get(neighbor).size()){
								if(destination == this.allVectors.get(neighbor).get(index)){
									connection = this.allVectors.get(neighbor).get(index + 1);
									break;
								}
								index = index + 2;
							}
							if(connection != INFINITY){
								
								distance = connection + distance;}
						    
						    if(connection == INFINITY){
						    	distance = INFINITY;
						    }
						    }
						    
						    // compare with the minimum and replace if necessary
						    // if the neighbor node is not the destination, and the total distance
						    // was not infinity, then we have traversed through the neighbor node
						    // to the distance and must record this so we can send a distance vector
						    // to the neighbor node that says the distance to the destination
						    // from this node to the neighbor node is INFINITY to account for poison
						    // reverse implementation
							if(distance < minimum){
								minimum = distance;
								hopNode = neighbor;
								if(hopNode != destination && distance != INFINITY){
									passing = 1;
								}
								else 
									passing = 0;
							}
							// advance to next neighbor node
							neighborNodes = neighborNodes + 2;
						}
						
						
						// found minimum for this destination
						// if there is a change in the distance vector,
						// we set this.hasUpdate to 1
						if(minimum != this.distVector.get(allNodes + 1)){
							this.hasUpdate = 1;
							}
						this.distVector.set(allNodes + 1, minimum);	
						// if there was no "passing" over a neighbor node
						// we add in the actual values to the distance vectors
						// to be sent to neighbors
						if(passing == 0){
							int a = 0;
;							while (a < this.neighbors.size()){
								int b = this.neighbors.get(a);
								output.get(b).add(destination);
								output.get(b).add(minimum);
								a = a + 2;
						}
						}
						// otherwise we must let the neighbor node that we passed
						// over that the distance from this node to the destination
						// is INFINITY
						if(passing == 1){
							int a = 0;
							while (a < this.neighbors.size()){
								int b = this.neighbors.get(a);
								if(b == hopNode){
									output.get(b).add(destination);
									output.get(b).add(INFINITY);
								}
								if(b != hopNode){
									output.get(b).add(destination);
									output.get(b).add(minimum);
								}
								a = a + 2;
						}
						
						}
					
						// advance to next allNodes
						allNodes = allNodes + 2;
						
						
					}
					
			// return the list of distance vectors
			return output;
			} 
	}

