import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class CardComponent extends JPanel{
	/**
	 * This class is used to draw a player's deck on the Interface of the game
	 */
	private static final long serialVersionUID = 1L;
	//attributes
	private BufferedImage cardImage; //image of a card
	private ArrayList<Card> deck;	 //player's deck
	private int x,y;				 //coordinates of the component
	
	//constructor
   public CardComponent(ArrayList<Card> deck) {
	   this.deck=deck;
    }
 
   //paintComponent method
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g; 																			//graphics instance
		String[] values= {"2","3","4","5","6","7","8","9","10","Jack","Queen","King","Ace"};						//like in Deck class
		String[] kinds= {"Diamonds","Clubs","Hearts","Spades"};														//each card will be distinguished by its name
		x=15;																										//initialization of the coordinates
		y=100;
		final int width=123;																						//size of each card
		final int height=172;
		for (Card toShow:deck) {																					//for every card in player's deck
			try{
				BufferedImage DeckOfCards=ImageIO.read(new File("DeckOfCards.jpeg"));								//read the jpeg file
				for (int i=0;i<kinds.length;i++) {
					for (int j=0;j<values.length;j++) {
						if(toShow.getName().equals(values[j]+" of "+kinds[i])){										//find the name of the card
							cardImage=DeckOfCards.getSubimage(6+(j*width)+j*9,6+(i*height)+i*14,width,height);		//get the subimage from the big image (jpeg file), make the image the appropriate size
							this.setBackground(new Color(26,148,49));
				            g2.drawImage(cardImage, x,y, null);														//draw the image in the right coordinates
						}
					}
				}
	        }catch(Exception e){
	        	e.printStackTrace();
	        }
			x=x+25;																									//draw the next card of player's deck with 25 pixels margin from the last one
		}
    }
	
	//setter
   public void setDeck(ArrayList<Card> deck) {
	   this.deck=deck;
   }
}
