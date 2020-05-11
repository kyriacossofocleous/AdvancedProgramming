
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
public class Client extends JFrame implements ActionListener {
	/**
	 * This class and its inner class are sending Client's requests and handling the gui
	 */
	private static final long serialVersionUID = 1L;
	private class GameWorker extends SwingWorker<Void,DataPackage> {
        private Socket socket = null;
        private ObjectInputStream inputStream = null;
        private Client parent;
        public GameWorker(Socket s, Client parent) {
            this.socket = s;
            this.parent = parent;
            try {
                inputStream = new ObjectInputStream(this.socket.getInputStream());
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
		protected Void doInBackground() throws Exception {
			DataPackage data=null;
			try {
				while((data =(DataPackage) inputStream.readObject())!=null) {		//for every received package from server
					for(Player p:data.getPlayers()) {
						if(p.getID()==currentPlayer.getID()&& p.getID()!=1000) {	//if the player in the package has the same ID as the player in client's class
							currentPlayer=p;										//assign him to the current player
						}
						break;
					}
					publish(data);													//publish() and process() in order to refresh the gui
				}
				
			}catch(IOException e) {
				e.printStackTrace();
			}
			
			return null;
		}
		protected void process(List<DataPackage> data) {
			ArrayList <Player> p=new ArrayList<Player>();							//one array list for players
			ArrayList <Player> w=new ArrayList<Player>();							//one array list for the waiting list
			p=data.get(data.size()-1).getPlayers();	
			w=data.get(data.size()-1).getWaiting();
			for (Player player:w) {													//commands are straight forward
				for(int i=0;i<6;i++) {
					if (i==player.getID()) {
						gui.getInfo(i).update(player.getName(),player.getChips());
						gui.getSit(i).setEnabled(false);
					}
				}
			}
			for (Player player:p) {
				gui.getDeck().setNumberOfCardsLeft(data.get(data.size()-1).getCardsBeenPlayed());
				for(int i=0;i<6;i++) {
					if (i==player.getID()) {
						if(player.getID()==currentPlayer.getID()) {
							if (player.getPoints()==0) {
								gui.getPointsLabel(i).setText("");
							}else {
								gui.getPointsLabel(i).setText(""+player.getPoints());
								if (player.getPoints()>21) {
									gui.getPointsLabel(i).setForeground(Color.RED);
								}else if (player.getPoints()==21) {
									gui.getPointsLabel(i).setForeground(Color.YELLOW);
								}else {
									gui.getPointsLabel(i).setForeground(Color.WHITE);
								}
							}
							gui.getCardComponent(i).setDeck(player.getCards());
							gui.getCardComponent(i).repaint();
							gui.getPlayersPanel(i).add(BorderLayout.CENTER,gui.getCardComponent(i));
						}
						gui.getInfo(i).update(player.getName(),player.getChips());
						gui.getSit(i).setEnabled(false);
						if (player.getDealer()) {
							gui.getInfo(i).nameLabel.setText(player.getName()+" D");
							gui.getInfo(i).nameLabel.setForeground(Color.MAGENTA);
							gui.getBet(i).setEnabled(false);
							gui.getBettingAmount(i).setEnabled(false);
						}else {
							gui.getInfo(i).nameLabel.setText(player.getName());
							gui.getInfo(i).nameLabel.setForeground(Color.WHITE);
						} 
						if (player.getActive() && !player.getHasBet()) {
							if(player.getID()==currentPlayer.getID()) {
								gui.getBet(i).setEnabled(true);
								gui.getBettingAmount(i).setEnabled(true);
							}
						}
						if (player.getHasBet()) {
							if (player.getID()==currentPlayer.getID()) {
								gui.getBet(i).setEnabled(false);
								gui.getBettingAmount(i).setEnabled(false);
							}
						}
						if (player.getActive() && player.getHasBet()) {
							if(player.getID()==currentPlayer.getID()) {
								gui.getCardComponent(i).setDeck(player.getCards());
								gui.getCardComponent(i).repaint();
								boolean allHaveBet=true;
								for (Player pl:p) {
									if (!pl.getHasBet()) {
										allHaveBet=false;
									}
									gui.getHit(i).setEnabled(allHaveBet);
									gui.getHold(i).setEnabled(allHaveBet);
								}
								
							}
						}
						if (player.getHasStand()) {
							if(player.getID()==currentPlayer.getID()) {
								gui.getHit(i).setEnabled(false);
								gui.getHold(i).setEnabled(false);
							}
						}
						boolean allAreDone=true;
						for (Player pl:p) {
							if (!pl.getHasStand()) {
								allAreDone=false;
							}
						}
						int playersOut=0;
						for (Player pl:p) {
							if (pl.getHasLost()) {
								playersOut++;
								
							}
						}
						if (player.getID()==currentPlayer.getID()&& player.getDealer()&& allAreDone && p.size()-playersOut+w.size()>1) {
							gui.getNewRound().setEnabled(true);
						}
						
						if (allAreDone) {
							gui.getCardComponent(i).setDeck(player.getCards());
							gui.getCardComponent(i).repaint();
							gui.getPointsLabel(i).setText(""+player.getPoints());
							if (player.getPoints()>21) {
								gui.getPointsLabel(i).setForeground(Color.RED);
							}else if (player.getPoints()==21) {
								gui.getPointsLabel(i).setForeground(Color.YELLOW);
							}else {
								gui.getPointsLabel(i).setForeground(Color.WHITE);
							}
							
						}
						
						if (data.get(data.size()-1).getNewRound()) {
							gui.getNewRound().setEnabled(false);
							gui.getPointsLabel(i).setText("");
							gui.getCardComponent(i).setDeck(new ArrayList<Card>());
							gui.getCardComponent(i).repaint();
							if(player.getID()==currentPlayer.getID()) {
								gui.getBettingAmount(i).removeAll();
								if (player.getChips()>499) {
									gui.getBettingAmount(i).add("25");
									gui.getBettingAmount(i).add("50");
									gui.getBettingAmount(i).add("100");
									gui.getBettingAmount(i).add("200");
									gui.getBettingAmount(i).add("500");
								}else if (player.getChips()<500 && player.getChips()>199) {
									gui.getBettingAmount(i).add("25");
									gui.getBettingAmount(i).add("50");
									gui.getBettingAmount(i).add("100");
									gui.getBettingAmount(i).add("200");
								}else if (player.getChips()<200 && player.getChips()>99) {
									gui.getBettingAmount(i).add("25");
									gui.getBettingAmount(i).add("50");
									gui.getBettingAmount(i).add("100");
								}else if (player.getChips()<100 && player.getChips()>49) {
									gui.getBettingAmount(i).add("25");
									gui.getBettingAmount(i).add("50");
								}else if (player.getChips()<50) {
									gui.getBettingAmount(i).add("25");
								}
							}
						}
						if (player.getHasLost()) {
							gui.getInfo(i).nameLabel.setText("");
							gui.getInfo(i).chipsLabel.setText("");
							gui.getPointsLabel(i).setText("");
							gui.getCardComponent(i).setDeck(new ArrayList<Card>());
							if (player.getID()==currentPlayer.getID()) {
								currentPlayer.setID(1000);
							}
						}
					}
				}
			}
		}
    }
	
	//attributes of the client
	private int betAm;				//an integer for the betting amount
	private Player currentPlayer;	//a player object is connected to the client
	private View gui;				//the gui of the client
	private ClientPackage player;
    private Socket server = null;
    private ObjectOutputStream outputStream;
    private String name = "Kyriacos";
    public Client()  throws IOException {
    	name = JOptionPane.showInputDialog(this, "What's your name?");
    	currentPlayer=new Player(name);
    	setView(new View(this));
    	gui.setVisible(true);
        connect();
        
        try {
            outputStream = new ObjectOutputStream(server.getOutputStream());
        }catch(IOException e) {
            e.printStackTrace();
        }
	        GameWorker gw = new GameWorker(server,this);
	        gw.execute();
    }
    private void setView(View gui) {
    	this.gui=gui;
    }

    private void connect() {
        try {
            server = new Socket("127.0.0.1",8765);
            System.out.println("Connected");
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    public Player getPlayer() {
    	return currentPlayer;
    }
    
    //action listeners
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource()==gui.getConnect()) {	//if a client presses the connect button
    		gui.getConnect().setEnabled(false);
    		currentPlayer.setID(1000);
    		player=new ClientPackage(currentPlayer,false,false,false,false,0,false);	//send a package that contains the information of the player and the ID 1000 so he knows which seats are available
    		gui.getSit(0).setEnabled(true);
    		gui.getSit(1).setEnabled(true);
    		gui.getSit(2).setEnabled(true);
    		gui.getSit(3).setEnabled(true);
    		gui.getSit(4).setEnabled(true);
    		gui.getSit(5).setEnabled(true);
    	}
    	for (int i=0;i<6;i++) {
	    	if (e.getSource()==gui.getSit(i)) {											//if a player decides to sit
	    		currentPlayer.setID(i);													//assign the ID of him to the one of the cluster
	    		player=new ClientPackage(currentPlayer,false,false,true,false,0,false);	//send a new package with the info of the player, his new ID and a boolean value that there is a new player
	    		gui.getSit(0).setEnabled(false);
	    		gui.getSit(1).setEnabled(false);
	    		gui.getSit(2).setEnabled(false);
	    		gui.getSit(3).setEnabled(false);
	    		gui.getSit(4).setEnabled(false);
	    		gui.getSit(5).setEnabled(false);
	        }else if (e.getSource()==gui.getHit(i)) {
	    		player=new ClientPackage(currentPlayer,true,false,false,false,0,false);	//if the player decides to hit, send the appropriate package with the request
	        }else if(e.getSource()==gui.getHold(i)) {
	        	player=new ClientPackage(currentPlayer,false,true,false,false,0,false);	//if the player decides to hold, set the hold boolean value to true and send the package
	        }else if (e.getSource()==gui.getBet(i)) {
	        	betAm=Integer.parseInt(gui.getBettingAmount(i).getItem(gui.getBettingAmount(i).getSelectedIndex()));	//if a player decides to bet, get the amount that it is indicated in choice box
	        	player=new ClientPackage(currentPlayer,false,false,false,true,betAm,false);								//and send a new client package with this amount and the request for betting
	        }
        }
    	if (e.getSource()==gui.getNewRound()) {											//if the player (dealer) has pressed the new round button
        	gui.getNewRound().setEnabled(false);
        	player=new ClientPackage(currentPlayer,false,false,false,false,0,true);		//inform the server that there is a new round
    	}
    	try {
            outputStream.writeObject(player);
            outputStream.flush();
            outputStream.reset();
   		}catch(IOException ex) {
   			ex.printStackTrace();
   		}
    }
    
    	

    public static void main(String[] args) throws IOException {
    	SwingUtilities.invokeLater(new Runnable() {
    	public void run() {
    	   try {
				new Client();
			}catch (IOException e) {
				e.printStackTrace();
				}
    	    }
    	});
    }
}