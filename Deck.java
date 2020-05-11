
import java.io.Serializable;
import java.util.Random;

public class Deck implements Serializable {
	/**
	 * Deck Class is used to create the deck to be used in the game
	 */
	private static final long serialVersionUID = 1L;
	
	//one attribute
	private Card[] deckOfCards; //an array of card objects
	
	//constructor
	public Deck() {
		String[] values= {"2","3","4","5","6","7","8","9","10","Jack","Queen","King","Ace"}; //all the values a card can get
		String[] kinds= {"Diamonds","Clubs","Hearts","Spades"}; //there are 4 kinds of cards
		deckOfCards=new Card[52]; //all the cards in the deck are 52
		
		//an algorithm to assign value and name to each card
		for (int i=0;i<kinds.length;i++) { 		//for every kind
			for (int j=0;j<values.length;j++) { //and for every value
				int cardValue=0;
				if (j<9) {						//if the card is a numeric card
					cardValue=j+2; 				//then value is the indicator + 2
				}else if (j==12) { 				//if the card is an ace value is 11
					cardValue=11;
				}else {	 						//if it is a face card the value is 10
					cardValue=10;
				}
				deckOfCards[j+i*13]=new Card(values[j]+" of "+kinds[i],cardValue); //populating the array with cards
				//each card has a name consisted of a value and one of the four kinds and also a value in points
			}
		}
	}
	
	/**
	 * a method that shuffles the deck
	 */
	public void shuffleDeck() {
		Random r=new Random();						//declaration of a Random variable
		for (int i=0;i<deckOfCards.length;i++) {	//for every card in the deck
			int pos=r.nextInt(deckOfCards.length);	//get a random number
			Card temp=deckOfCards[i];				//create a new temporary card object, same as the current card
			deckOfCards[i]=deckOfCards[pos];		//change the current card with a random card
			deckOfCards[pos]=temp;					//change the random card with the temporary card object
		}
	}
	
	//getter
	public Card[] getDeckOfCards() {
		return deckOfCards;
	}
	
}
