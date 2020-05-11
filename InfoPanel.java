import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.Serializable;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class InfoPanel extends JPanel implements Serializable{
	/**
	 * Info panel is a JPanel that contains the name and the chips of a player
	 */
	private static final long serialVersionUID = 1L;
	//attributes
	private String name;				//a string for the player's name 
	private int chips;					//an integer for the chips
	public JLabel nameLabel,chipsLabel; //to labels to assign the two values
	
	//constructor
	public InfoPanel(String name,int chips) { //it is passed a string for the name and an integer for the chips
		this.name=name;
		this.chips=chips;
		components();						 //call the components method to add the components on the JPanel
	}
	public void components() {
		nameLabel=new JLabel("\t"+name);
		chipsLabel=new JLabel("$"+chips);
		nameLabel.setFont(new Font("Serif",Font.PLAIN,20));
		nameLabel.setForeground(Color.WHITE);
		chipsLabel.setFont(new Font("Serif",Font.PLAIN,20));
		chipsLabel.setForeground(Color.WHITE);
		this.setLayout(new GridLayout(1,2));
		this.add(nameLabel);
		this.add(chipsLabel);
		this.setBackground(new Color(26,148,49));
	}
	
	//a method that updates the labels
	public void update(String name,int chips) {
		nameLabel.setText(name);
		chipsLabel.setText("$"+chips);
	}
}
