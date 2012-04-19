import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Main {

	
	public static void main(String[] args) {

		
		ArrayList<Message> messages = new ArrayList<Message>(); // messages sent at each turn
		ArrayList<Node> nodes 		= new ArrayList<Node>();	// list of the nodes
		ArrayList<Node> _nodesBase 	= new ArrayList<Node>();
		ArrayList<Event> events 	= new ArrayList<Event>();	// list of the events
		ArrayList<Entry> entries 	= new ArrayList<Entry>();	// list of the entries (output)
		int numNodes				= 0; 						// number of nodes
		int numTurns				= 0;						// number of turns
		int numEvents				= 0;						// number of events
		
		/**************************
		 * STEP 1: opening files
		 **************************/
		//Three arguments are given:
		if ( 3!= args.length )
			System.out.println("Wrong arguments \n"+"usage: java Main GraphFile EventFile OutputFile");
		
		//- the filename of the file containing the graph
		String graphFile = args[0];
		BufferedReader graphReader=null;
		
		//- the filename of the file containing the events
		String eventFile = args[1];
		BufferedReader eventReader = null;
		//- the filename of the file where to store the output
		String outputFile = args[2];
		PrintWriter output = null;
		
		try {
			graphReader = new BufferedReader(new FileReader(graphFile));
			eventReader = new BufferedReader(new FileReader(eventFile));
			output = new PrintWriter(outputFile); 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		/**
		 * Parsing the graph and building it
		 * 
		 * The graph file is made of: the number of node (n) in the first line
		 * followed by n lines with the following format
		 * node_id neighbor1 cost1 neighbor2 cost2 ... neighbor_k cost_k
		 * where node_id is the id of a node in the graph
		 * neighbor_k are the id of the neighbors nodes
		 * cost_k is the cost to reach those nodes
		 */
		
		try {
			numNodes = Integer.parseInt(graphReader.readLine());
			for (int i = 0 ; i < numNodes ; i ++){
				//For each node
				String line = graphReader.readLine();
				String[] line_splitted = line.split(" ");
				int id_node = Integer.parseInt(line_splitted[0]);
				Node n = new Node(id_node);
				Node _nBase = new Node(id_node);
				//2 by 2 to get the neighbor and the cost at the same time
				for (int k = 1; k < line_splitted.length ; k+=2) {
					n.addNeighbor(Integer.parseInt(line_splitted[k]),Integer.parseInt(line_splitted[k+1]));
					_nBase.addBaseN(Integer.parseInt(line_splitted[k]));
				}
				nodes.add(n);
				_nodesBase.add(_nBase);

			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		
		/**
		 * Parsing the events and building the event list
		 * 
		 * The even file is made of: 
		 * the number of turns of the simulation in the first line
		 * the number of events(m) in the second line
		 * m lines describing events ordered by turns, an event can be the modification of a link or the removal of a node
		 * 32 R 2 
		 * means removing node 2 at the 32 th turn of the algorithm
		 * 39 M 1 3 78
		 * means modifying the cost of the path 1 to 3 to 78 at the 39th turn of the simulation
		 */
		

		try {
			numTurns 	= Integer.parseInt(eventReader.readLine());
			numEvents 	= Integer.parseInt(eventReader.readLine());
			for (int i = 0 ; i < numEvents ; i ++){
				//For each event
				String line = eventReader.readLine();
				String[] line_splitted = line.split(" ");
				int turn 			= Integer.parseInt(line_splitted[0]);
				boolean is_removal 	= line_splitted[1].equals("R");
				Event e ;
				if (is_removal)
					e = new RemoveEvent(
							turn, 
							Event.ACTION_REMOVE, 
							Integer.parseInt(line_splitted[2]), 
							Integer.parseInt(line_splitted[3]));
				else
					e = new ModifyEvent(
							turn, 
							Event.ACTION_MODIFY, 
							Integer.parseInt(line_splitted[2]), 
							Integer.parseInt(line_splitted[3]), 
							Integer.parseInt(line_splitted[4]));
				events.add(e);
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
		
		
		ArrayList<Message> tempList = new ArrayList<Message>(); // Temporary list to store messages of a node, used to check the id and avoid spoofing
		
		/*********************
		 * Run the simulation
		 *********************/
		for (int i = 0 ; i < numTurns ; i ++){
			messages.clear();
			
			//Process the event of this turn
			for (Event e: events)
				if (e.turn==i)
					if (e.getAction() == Event.ACTION_REMOVE) {
						//remove a link
						int s = e.getSource();
						int d = e.getDestination();
						nodes.get(s).removeNeighbor(d);
						nodes.get(d).removeNeighbor(s);
						_nodesBase.get(s).removeBaseN(d);
						_nodesBase.get(d).removeBaseN(s);

					}
					else {
						//change a link cost
						int s = e.getSource();
						int d = e.getDestination();
						nodes.get(s).changeCost(nodes.get(d).getId(), ((ModifyEvent) e).getCost());
					}

			// Get the messages of the nodes
			for ( int k = 0 ; k < numNodes ; k ++){
				
				tempList= new ArrayList<Message>();
				if(null != nodes.get(k)){
					int id = nodes.get(k).getId();
					tempList = nodes.get(k).turn();
					if( null != tempList)
					for (Message m : tempList){
						m.setId_source(id);
						// gets the message only if the source is actually linked to the destination
						if (_nodesBase.get(m.getId_source()).hasBaseN(m.getId_destination()))
							messages.add(m);
					}
				}
			}
			
			
			//Send the messages
			for (Message m : messages){
				Node dest = nodes.get(m.getId_destination());
				if (null != dest && dest.getId() == m.getId_destination())
					dest.ReceiveMessage(m);
			}
			
		}
		
		
		
		/*********************
		 * Store the results
		 ********************/

		ArrayList<Entry> entries_node	= new ArrayList<Entry>();	// list of the entries for a node
		
		for (Node n: nodes){
			entries_node = n.sendOutput();
			for (Entry e:entries_node)
				e.setId_source(n.getId());
			entries.addAll(entries_node);
		}
		
		for (Entry e:entries)
			output.println(e);
		
		//TODO
		//write the outputs in the file
		
		/***********************
		 * STEP N: Closing files
		 ***********************/
		try {
			eventReader.close();
			output.close();
			graphReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

}
