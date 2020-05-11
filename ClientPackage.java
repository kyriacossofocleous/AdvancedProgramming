import java.io.Serializable;

public class ClientPackage implements Serializable {
	/**
	 * ClientPackage contains all the data that are sent from Client to Server
	 */
	private static final long serialVersionUID = 1L;
	//attribute
	private final Player player;		//each Client Package is sent by a specific  Player-Client
	private final boolean hit;			//boolean values that depict player's behavior in the game
	private final boolean stand;
	private final boolean newPlayer;
	private final boolean bet;
	private final int bettingAmount;
	private final boolean newRound;
	
	//constructor
	public ClientPackage(Player player, boolean hit,boolean stand,boolean newPlayer,boolean bet,int bettingAmount,boolean newRound) {
		this.player=player;
		this.hit=hit;
		this.stand=stand;
		this.newPlayer=newPlayer;
		this.bet=bet;
		this.bettingAmount=bettingAmount;
		this.newRound=newRound;
	}
	
	//getters
	public Player getPlayer() {
		return player;
	}
	public boolean getHit() {
		return hit;
	}
	public boolean getStand() {
		return stand;
	}
	public boolean getNewPlayer() {
		return newPlayer;
	}
	public boolean getBet() {
		return bet;
	}
	public int getBettingAmount() {
		return bettingAmount;
	}
	public boolean getNewRound() {
		return newRound;
	}
}
