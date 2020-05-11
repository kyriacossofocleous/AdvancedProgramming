
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DeckPanel extends JPanel{
	/**
	 * Deck Panel is a JPanel that contains an image of a Card's Pile and the amount of cards that are left in the deck
	 */
	private static final long serialVersionUID = 1L;
	
	//attributes
	private JLabel label1,label2;
	private JPanel panel;
	private BufferedImage pileOfCards;
	private int numberOfCardsLeft;

	//constructor
	public DeckPanel() throws IOException {
		numberOfCardsLeft=52;
		this.setLayout(new FlowLayout());
		this.setBackground(new Color(26,148,49));
		pileOfCards=ImageIO.read(new File("PileOfCards.jpg")); //read the jpg file
		label1=new JLabel(new ImageIcon(pileOfCards));		   //set the image to the label
		label2=new JLabel();
		label2.setText(numberOfCardsLeft+"");				   //label2 contains the cards that left
		label2.setFont(new Font("Serif",Font.BOLD,36));
		label2.setHorizontalAlignment(SwingConstants.CENTER);
		label2.setVerticalAlignment(SwingConstants.CENTER);
		label2.setForeground(Color.white);
		panel=new JPanel();
		panel.setBackground(new Color(26,148,49));
		this.add(label1);										//add both on the panel
		this.add(label2);
	}
	
	//a method that changes the number of cards that have left in the deck
	public void setNumberOfCardsLeft(int cardsBeenPlayed) { //it is passed the number of cards that have been played
		numberOfCardsLeft=52-cardsBeenPlayed;				//and subtract them from 52
		label2.setText(numberOfCardsLeft+"");
	}
}
