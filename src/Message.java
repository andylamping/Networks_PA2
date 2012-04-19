/**
 * This class describes a message that the node
 * are able to deliver at each turn.
 */

public class Message {

	public static final int NO_SOURCE = 1;
	protected int id_destination;
	protected int id_source;
	protected String text;
	
	/**
	 * This is the constructor that you ought to use
	 * The source will be set by the Main to check that
	 * the node is authorized to deliver this very message
	 * @param id_destination id of the destination node
	 * @param text payload of the message
	 */
	
	public Message(int id_destination, String text) {
		super();
		this.id_destination = id_destination;
		this.text = text;
		this.id_source = NO_SOURCE;
	}

	public int getId_destination() {
		return id_destination;
	}
	public void setId_destination(int id_destination) {
		this.id_destination = id_destination;
	}
	public int getId_source() {
		return id_source;
	}
	public void setId_source(int id_source) {
		this.id_source = id_source;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
