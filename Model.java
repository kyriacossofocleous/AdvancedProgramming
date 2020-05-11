import java.util.Random;
import java.util.ArrayList;

public class Model{
	/**
	 * This class contains the methods that are called by the server during the game
	 */
	
	//attributes
	private Deck deck;															//the pile of the cards
	private ArrayList<Player> players;											//list of players currently in the game
	private ArrayList<Player> waiting;											//list of players waiting to get in the game
	private static int cardsI;													//an integer that defines the next card
	private int i;																//an integer that defines next player
	private Random r;															//random number
	private transient static int dealersID,dealersI;							//ID of dealer, position of the dealer
	private boolean firstRound,bettingRound,playRound,checkWinner,secondRound;	//booleans for calling the right method at the right moment
	private int[] seats;														//an array that indicates the sequence of turn
	private transient static int activePlayer;									//the position of the active player
	
	//constructor
	public Model() {
		waiting=new ArrayList<Player>();
		players=new ArrayList<Player>();
		deck=new Deck();
		deck.shuffleDeck();
		r=new Random();
		cardsI=0;
		i=0;
		dealersID=0;
		firstRound=true;
		bettingRound=false;
		playRound=false;
		checkWinner=false;
		secondRound=false;
	}
	
	//a method that is passed a player reference and gives him a card
	public void requestCard(Player player) {
		for(Player p:players) {
			if (p.getID()==player.getID()) {				//for the player in the reference
				p.addCard(deck.getDeckOfCards()[cardsI]);	//add a card to his deck
				cardsI++;									//move to the next card
			}
		}
		
	}
	
	//a method that is passed a player object and the amount of chips he bet and handles the betting
	public void bet(Player player,int amount) {
		for (Player p:players) {
			if (p==player) {
				p.betChips(amount);
			}
		}
	}
	
	//initialization of the game
	public void initialization() {
		if (players.size()>1 && firstRound) {			//game starts when there are 2 players
			firstRound=false;
			bettingRound=true;
			dealersI=r.nextInt(players.size());
			dealersID=players.get(dealersI).getID();
			players.get(dealersI).setDealer(true);
		 }	
	}
	
	//when the game has started players have to bet
	public void betting() {
		if(bettingRound) {
			allocateSeats();							//call allocateSeats() to define the order in which the player will play
			for (Player player:players) {
				if (activePlayer==player.getID()) {
					player.setActive(true);
				}else {
					player.setActive(false);
				}
			}
			for (Player player:players) {
				if (player.getDealer()) {				//dealer will not bet
					player.setHasBet(true);
				}
			}
		}
	}
	
	//if there are loosers after a round remove them
	public void removeLosers() {
		if (secondRound) {
			for (int j=0;j<players.size();j++) {
				if (players.get(j).getHasLost()) {
					players.remove(j);
				}
			}
			if (players.size()<1) {						//wait until there are at least 2 players to start the next round
				secondRound=false;
				firstRound=true;
			}
		}
	}
	
	//a method that it is called at the end of every round and adds the player in the waiting list to players list
	public void addPlayers() {
		for (Player player:waiting) {
			players.add(player);
		}
		waiting.clear();								//remove everyone from waiting list
		for (int j=0;j<players.size();j++) {
			if (players.get(j).getID()==dealersID) {
				dealersI=j;								//find the position of the dealer
			}
		}
		allocateSeats();								//define the order in wich the players they will play
		for (Player player:players) {
			if (activePlayer==player.getID()) {
				player.setActive(true);
			}else {
				player.setActive(false);
			}
		}
		for (Player player:players) {
			if (player.getDealer()) {
				player.setHasBet(true);
			}
		}
		i=0;											//active player is the first player on the list - the player who is sitting next to the dealer
	}
	
	//after the end of every round reinitialize the game
	public void reinitialization() {
		if (players.size()+waiting.size()>1 && secondRound) {
			bettingRound=true;
			playRound=false;
			checkWinner=false;
			secondRound=false;
			addPlayers();								//add the players from the waiting list
			for(Player player:players) {
				player.reset();							//reset every player's attribute
			}
			
			deck.shuffleDeck();							//reshuffle the deck
			cardsI=0;									//start from the first card
			for (int j=0;j<players.size();j++) {
				if (players.get(j).getID()==dealersID) {
					dealersI=j;							//find the position of the dealer
				}
			}
			i=0;										//active player is the player who is sitting next to the dealer
		}
	}
	
	//an algorithm to define the order in which the players are playing
	public void allocateSeats() {
		seats=new int[players.size()];
		int ok=dealersID;
		int min=1000;
		int k=0;
		boolean again=true;
		
		//for the players that have ID greater than dealers ID
		while(again) {
			for (int j=0;j<seats.length;j++) {
				if (players.get(j).getID()>ok && players.get(j).getID()<min) {	
					min=players.get(j).getID();									//find the smallest one
				}
				if (j==seats.length) {
					again=false;
				}
			}
			if (min!=1000) {													//if there is an ID greater than dealer's ID
				seats[k]=min;													//set the smallest greater than dealer's ID to first seat
				ok=min;															//repeat the same procedure comparing to this ID instead of Dealer's ID
				min=1000;	
				k++;															//if you find a new one assign him to the next seat
			}else {																
				again=false;													//else do not do it again
			}
		}
		
		//for the players with ID less than dealers ID
		int max=-1;
		ok=dealersID;
		min=dealersID;
		again=true;
		while(again) {
			for (int j=0;j<seats.length;j++) {
				if (players.get(j).getID()<ok && players.get(j).getID()<min && players.get(j).getID()>max) {	
					min=players.get(j).getID();																	//find the smallest one
				}
				if (j==seats.length) {
					again=false;
				}
			}
				if (min!=dealersID) {
					seats[k]=min;				//if it is not the dealer, assign the player to the next position
					max=min;					//repeat the procedure but now find the smallest one which is greater than the one you already found
					min=dealersID;
					k++;						//place next player to the next position
				}else {
					again=false;				//if there isnt any do not repeat
			}
		}
		seats[seats.length-1]=dealersID;		//last to play is the dealer
		if (i==players.size()) {
			i=0;
		}
		activePlayer=seats[i];
	}
	
	
	//a method that gives the 2 first cards to the players
	public void allocation() {
		i=0;
		bettingRound=false;
 		for (int k=0;k<2;k++) {						//each player gets two cards
 			for (int j=0;j<players.size();j++) {	
 				requestCard(players.get(j));
 			}
 		}
        for (Player player:players) {
        	player.calculatePoints();					//calculate the points for every player
        	if (player.getID()==player.getID()) {
            	if (player.getPoints()==21) {			//if he has 21 points
            		player.setHasBlackJack(true);		//he has a blackjack
            		player.setNatural21(true);			//and a natural 21
            	}else {
            		player.setHasBlackJack(false);		//else he does not have any of them
            	}
        	}
        }
 		playRound=true;
		
	}

	//a method that lets each player to play
	public void play() {
		if (playRound) {
			activePlayer=seats[i];						//active player is the player who sits in position i
			for (Player player:players) {
				if (activePlayer==player.getID()) {
					player.setActive(true);				//set him to active
				}else {
					player.setActive(false);			//set every other one to inactive
				}
			}
		}
	}
	
	//a method to move to the next player
	public void nextPlayer() {
		i++;										//increase the position of active player
		if (i==players.size()) {					//if there are not any other players
			i=0;									//active player is the first one
		}
		activePlayer=seats[i];
		for (Player player:players) {
			if (activePlayer==player.getID()) {
				player.setActive(true);				//make him active
			}else {
				player.setActive(false);			//make everyone else inactive
			}
		}
	}
	
	//a method that it is called at the end of each round to check the winners
	public void checkWinner() {
		checkWinner=true;
		for (Player player:players) {
			if(!player.getHasStand()) {	//if not everyone is done do not check for the winner
				checkWinner=false;
			}
		}
		if (players.size()<2) {			//if there are less than 2 players in the game do not check for the winner
			checkWinner=false;
		}
		if (secondRound) {				//if the game is reinitializing to not check for the winner
			checkWinner=false;
		}
		if (checkWinner) {
			checkWinner=false;
			secondRound=true;
			boolean checkFirst=true;
			for (Player player:players) {
				if (player.getNatural21()) {												//first we need to check if there is a natural 21 in the game
					checkFirst=false;														//if there is then do not proceed to any other check
					if (player.getDealer()) {												//if dealer has a natural 21
						for (Player p:players) {
							if (!p.getNatural21()) {	
								player.setChips(player.getChips()+2*p.getBet());			//he receives double the bets of each player except those with a natural 21
								p.setChips(p.getChips()-p.getBet());
							}
						}
					}else {																	//if the player that has natural 21 is not the dealer
						player.setChips(player.getChips()+player.getBet());					//he receives his bet back
						for (Player p:players) {
							if (!p.getNatural21()) {										//and if the dealer does not have a natural 21
								if (p.getDealer()) {
									p.setChips(p.getChips()-2*player.getBet());				//dealer pays him double his stake
									player.setChips(player.getChips()+2*player.getBet());
								}
							}
						}
					}
				}
			}
			if (checkFirst) {																																//if there is not a natural 21
				for (Player player:players) {
					if (!player.getDealer()) {																												//and player is not the dealer
						if ((player.getPoints()>21 && players.get(dealersI).getPoints()>21)||(player.getPoints()==players.get(dealersI).getPoints())) {		//if both player and dealer have more than 21 points
							player.setChips(player.getChips()+player.getBet());																				//nothing happens between them
						}else if (player.getPoints()>21 && players.get(dealersI).getPoints()<22) {															
							players.get(dealersI).setChips(players.get(dealersI).getChips()+player.getBet());
						}else if (player.getPoints()<22 && players.get(dealersI).getPoints()>21) {
							player.setChips(player.getChips()+2*player.getBet());
							players.get(dealersI).setChips(players.get(dealersI).getChips()-player.getBet());
						}else {
							if (player.getPoints()>players.get(dealersI).getPoints()) {
								player.setChips(player.getChips()+2*player.getBet());
								players.get(dealersI).setChips(players.get(dealersI).getChips()-player.getBet());
							}else {
								players.get(dealersI).setChips(players.get(dealersI).getChips()+player.getBet());
							}
						}
					}
				}
			}
			for (Player player:players) {
				if (player.getDealer()&& !player.getHasBlackJack()) {									//if dealer doesnt have blackjack(21 points)
					for (int i=0;i<players.size();i++) {
						if (!players.get(i).getDealer() && players.get(i).getHasBlackJack()) {			// and there is a player with 21 points
							players.get(i).setDealer(true);												//this player becomes the new dealer
							player.setDealer(false);
							dealersID=players.get(i).getID();
							break;
						}
					}
				}
			}
			int tempDealersI;
			if (players.size()>1) {
				if(players.get(dealersI).getChips()<25) {				//if dealer is eliminated
					tempDealersI=dealersI;
	 				players.get(dealersI).setDealer(false);	
		 			while(dealersI==tempDealersI) {
		 				dealersI=r.nextInt(players.size());				//assign a new dealer
		 			}
		 			dealersID=players.get(dealersI).getID();
		 			players.get(dealersI).setDealer(true);
		 		}
	 		}
			for (Player player:players) {								//if player is eliminated
				 if (player.getChips()<25) {
					 player.setHasLost(true);							//set him to looser and remove them from game at the beggining of next round
				 }
			 }
		}	
	}
	
	//getters
	public int getCardsI() {
		return cardsI;
	}
	public ArrayList<Player> getWaiting(){
		return waiting;
	}
	public ArrayList<Player> getPlayers(){
		return players;
	}
	public boolean getFirstRound() {
		return firstRound;
	}
	public int getActivePlayer() {
		return activePlayer;
	}
}
		
