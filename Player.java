import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable{
	
	/**
	 * A player object is assigned to each client. It contains all the info for him
	 */
	private static final long serialVersionUID = 1L;
	
	//attributes
	private ArrayList<Card> cards;   													//player's current hand
	private String name;																//player's name
	private int points,bet,ID,chips;													
	private boolean active,dealer,hasBet,hasStand,hasBlackJack,hasLost,hasNatural21;	//booleans that are used for every action in the game
	
	//constructor
	public Player(String name) {		//initialization of player's attributes
		this.name=name;
		cards=new ArrayList<Card>();
		chips=1000;
		active=false;
		dealer=false;
		hasBet=false;
		hasStand=false;
		points=0;
		bet=0;
		hasBlackJack=false;
		hasNatural21=false;
		hasLost=false;
	}
	
	//a method that calculates the points a player has in his hand
	public void calculatePoints() {
		int sum=0;
		int acesAmount=0;
		for(Card card: cards) {						//for every card in his deck
			if(!card.getName().contains("Ace")){	//if the card is not an ace
				sum+=card.getValue();				//add the value of the card to the total sum
			}else {									//if the card is an ace
				acesAmount++;						//add 1 to the amount of aces in the hand
			}
		}
		int aces=acesAmount;
		for(int i=0;i<acesAmount;i++) {				//repeat for every ace
			if(sum<11 && aces==1) {					//if total sum is less than 11 and there is one ace
				sum+=11;							//add 11 points to the sum
			}else {									//in any other case
				if(aces==0) {						//if there aren't any other aces left break out of the loop
					break;
				}
				aces--;								//subtract one from the amount of aces
				sum++;								//add one to the total sum
			}
		}
		points=sum;									//assign sum value to points attribute of player
	}
	
	//re-initialization of the player
	public void reset() {		//this method is called after every new round
		hasBet=false;
		hasStand=false;
		hasBlackJack=false;
		hasNatural21=false;
		bet=0;
		points=0;
		cards.clear();
	}
	
	//a method that receives a card object reference and adds the corresponding object to player's hand
	public void addCard(Card card) {
		cards.add(card);
	}
	
	//getters and setters
	public int getPoints() {
		return points;
	}
	public String getName() {
		return name;
	}
	public int getChips() {
		return chips;
	}
	public ArrayList<Card> getCards(){
		return cards;
	}
	public int getBet() {
		return bet;
	}
	public void setBet(int bet) {
		this.bet=bet;
	}
	public void betChips(int bet) {
		chips-=bet;
	}
	public void setChips(int chips) {
		this.chips=chips;
	}
	public void setID(int ID) {
		this.ID=ID;
	}
	public int getID() {
		return ID;
	}
	public void setActive(boolean active) {
		this.active=active;
	}
	public boolean getActive() {
		return active;
	}
	public void setDealer(boolean dealer) {
		this.dealer=dealer;
	}
	public boolean getDealer() {
		return dealer;
	}
	public void setHasBet(boolean hasBet) {
		this.hasBet=hasBet;
	}
	public boolean getHasBet() {
		return hasBet;
	}
	public void setHasStand(boolean hasStand) {
		this.hasStand=hasStand;
	}
	public boolean getHasStand() {
		return hasStand;
	}
	public void setHasBlackJack(boolean hasBlackJack) {
		this.hasBlackJack=hasBlackJack;
	}
	public boolean getHasBlackJack() {
		return hasBlackJack;
	}
	public void setHasLost(boolean hasLost) {
		this.hasLost=hasLost;
	}
	public boolean getHasLost() {
		return hasLost;
	}
	public boolean getNatural21() {
		return hasNatural21;
	}
	public void setNatural21(boolean hasNatural21) {
		this.hasNatural21=hasNatural21;
	}
}
