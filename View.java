import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class View extends JFrame{
	/**
	 * view class defines the graphical user interface of the game
	 */
	private static final long serialVersionUID = 1L;
	
	//attributes
	private DeckPanel deck;				//DeckPanel object is a panel object that contains an image and how many cards are left in the deck
	private JPanel[] playersPanel;		//an array of JPanels
	private ButtonsPanel[] buttons;		//an array of ButtonsPanel
	private JButton[] sit,hit,hold,bet;	//arrays of 4 buttons
	private Choice[] bettingAmount;		//array of choice components
	private InfoPanel[] info;			//array of InfoPanels that are used to display player's name and coin's balance
	private JButton connect,newRound;	//two buttons
	private JPanel[] pointsPanel;		//array of JPanels that are indicating player's hand points
	private JLabel[] pointsLabel;		//the labels for each JPanel
	private CardComponent[] cardComp;	//array of objects responsible to draw the cards on the gui
	private Border border; 
	private JPanel control;
	private Client controller;		//GameClient that acts as the controller
	
	//constructor
	public View(Client controller) throws IOException {
		this.controller=controller;
		this.setSize(1920,1080);
		this.setBackground(new Color(26,148,49));
		this.setLayout(new GridLayout(2,4));
		this.setTitle("21 Game");
    	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bettingAmount=new Choice[6];				//there are 6 available clusters for a player to sit
		sit=new JButton[6];							//each player has a button for sit, hit, hold and bet
		hit=new JButton[6];
		hold=new JButton[6];
		bet=new JButton[6];
		for (int i=0;i<sit.length;i++) {			//for each one of the 6 clusters
			bettingAmount[i]=new Choice();
			bettingAmount[i].add("25");				//add these options for the choice component
			bettingAmount[i].add("50");				//a player can choose the amount of coins he wants to bet from this choice component
			bettingAmount[i].add("100");
			bettingAmount[i].add("200");
			bettingAmount[i].add("500");
			sit[i]=new JButton("Sit");
			hit[i]=new JButton("Hit");
			hold[i]=new JButton("Hold");
			bet[i]=new JButton("Bet");
			sit[i].addActionListener(controller);  //add action listeners to every one of the four buttons for every player
			hit[i].addActionListener(controller);  //action listeners are in client's class
			hold[i].addActionListener(controller);
			bet[i].addActionListener(controller);
		}
	deck=new DeckPanel();							//create a DeckPanel object
	control=controlPanel();							//create a control panel
	
	this.add(deck);									//add both of the on the gui
	this.add(control);

	playersPanel=new JPanel[6];															//each player's cluster is a new JPanel
	border = BorderFactory.createLineBorder(Color.GREEN.darker().darker(),5);
	buttons=new ButtonsPanel[6];														//ButtonsPanels are containing each player's buttons
	pointsPanel=new JPanel[6];															
	pointsLabel=new JLabel[6];
	cardComp=new CardComponent[6];														//cardComp are drawing the cards on a players cluster
	info=new InfoPanel[6];
	for (int i=0;i<playersPanel.length;i++) {											//for every player's cluster
		playersPanel[i]=new JPanel(new BorderLayout());
		buttons[i]=new ButtonsPanel(sit[i],hit[i],hold[i],bet[i],bettingAmount[i]);		//add the buttons
		playersPanel[i].add(BorderLayout.SOUTH,buttons[i]);								//on the bottom of the panel
		playersPanel[i].setBackground(new Color(26,148,49));							//make them green
		playersPanel[i].setBorder(border);												//set a border
		info[i]=new InfoPanel("",0);													//add the info
		playersPanel[i].add(BorderLayout.NORTH,info[i]);						 		//on the top of the panel
		pointsLabel[i]=new JLabel();
		pointsLabel[i].setText("");
		pointsLabel[i].setFont(new Font("Serif",Font.BOLD,36));
		pointsLabel[i].setForeground(Color.white);
		pointsPanel[i]=new JPanel();
		pointsPanel[i].add(pointsLabel[i]);
		pointsPanel[i].setBackground(new Color(26,148,49));
		playersPanel[i].add(BorderLayout.WEST,pointsPanel[i]);							//add the points label on the left
		cardComp[i]=new CardComponent(new ArrayList<Card>());
		cardComp[i].setBackground(new Color(26,148,49));
		playersPanel[i].add(BorderLayout.CENTER,cardComp[i]);
		this.add(playersPanel[i]);
		}
	}
	
	//a method that return a JPanel with two buttons
	public JPanel controlPanel() {
		JPanel control=new JPanel();
		connect=new JButton("CONNECT");						//one to connect to the game
		connect.addActionListener(controller);	
		newRound=new JButton("NEW ROUND");					//one to begin a new round
		newRound.addActionListener(controller);				//and adds action listeners to them
		newRound.setEnabled(false);
		control.add(connect);
		control.add(newRound);
		control.setBackground(new Color(26,148,49));	
		return control;	
	}
	
	
	//getters
	public InfoPanel getInfo(int i) {
		return info[i];
	}
	public JButton getSit(int i) {
		return sit[i];
	}
	public JButton getBet(int i) {
		return bet[i];
	}
	public JButton getHit(int i) {
		return hit[i];
	}
	public JButton getHold(int i) {
		return hold[i];
	}
	public Choice getBettingAmount(int i) {
		return bettingAmount[i];
	}
	public DeckPanel getDeck() {
		return deck;
	}
	public JLabel getPointsLabel(int i) {
		return pointsLabel[i];
	}
	public CardComponent getCardComponent(int i) {
		return cardComp[i];
	}
	public JPanel getPlayersPanel(int i) {
		return playersPanel[i];
	}
	public JButton getNewRound() {
		return newRound;
	}
	public JButton getConnect() {
		return connect;
	}

}
