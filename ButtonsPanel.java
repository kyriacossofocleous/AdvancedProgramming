import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.Serializable;

import javax.swing.JButton;
import javax.swing.JPanel;

public class ButtonsPanel extends JPanel implements Serializable{		
	/**
	 * ButtonsPanel contains all the 4 buttons and the choice component of a player's cluster
	 */
	private static final long serialVersionUID = 1L;

	//attributes
	JButton sit,hit,hold,bet; //4 buttons
	Choice bettingAmount;	  //a choice component
	//constructor
	public ButtonsPanel(JButton sit,JButton hit, JButton hold, JButton bet, Choice bettingAmount) {
		this.sit=sit;
		this.hit=hit;
		this.hold=hold;
		this.bet=bet;
		this.bettingAmount=bettingAmount;
        this.setLayout(new FlowLayout());			
        this.setBackground(Color.DARK_GRAY);
        buttons();										//calling of buttons method to add the buttons to the panel
	}

	public void buttons() {
		this.add(sit);			
		this.add(hit);
		this.add(hold);
		this.add(bet);
		this.add(bettingAmount);
		sit.setPreferredSize(new Dimension (39,30));
		hit.setPreferredSize(new Dimension (39,30));
		hold.setPreferredSize(new Dimension (39,30));
		bet.setPreferredSize(new Dimension (39,30));
		bettingAmount.setPreferredSize(new Dimension(80,30));
		sit.setEnabled(false);
		hit.setEnabled(false); 
		hold.setEnabled(false); 
		bet.setEnabled(false);
		bettingAmount.setEnabled(false);
	}
}