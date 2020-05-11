import java.io.Serializable;

public class Card implements Serializable{
	/**
	 * This is the Card class
	 */
	private static final long serialVersionUID = 1L;
	
	//each card has two attributes: name and value
	private String name;
	private int value;
	
	//constructor
	public Card(String name, int value) {
		this.name=name;
		this.value=value;
	}
	
	//getters and toString methods
	public String getName() {
		return name;
	}
	public int getValue() {
		return value;
	}
	public String toString() {
		return name;
	}
}