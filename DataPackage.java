import java.io.Serializable;
import java.util.ArrayList;

public class DataPackage implements Serializable{
	/**
	 * DataPackage contains all the info from the server that need to be passed to Client and SwingWorker
	 */
	private static final long serialVersionUID = 1L;
	private final ArrayList<Player> players; //the array list of players currently in the game
	private final ArrayList<Player> waiting; //the array list of players that are waiting the round to be finished so they can enter the game
	private final boolean newRound;		     //a boolean value that indicates if there is a new round
	private final int cardsBeenPlayed;	     //an integer of the amount of cards that have been drawn from the deck
	
	//constructor
	public DataPackage(ArrayList<Player>players,ArrayList<Player> waiting,boolean newRound,int cardsBeenPlayed) {
		this.players=players;
		this.waiting=waiting;
		this.newRound=newRound;
		this.cardsBeenPlayed=cardsBeenPlayed;
	}

	//getters
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public ArrayList<Player> getWaiting(){
		return waiting;
	}
	public boolean getNewRound() {
		return newRound;
	}
	public int getCardsBeenPlayed(){
		return cardsBeenPlayed;
	}
}
