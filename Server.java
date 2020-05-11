import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
	/*
	 * This class and the inner class are handling the server and the clients that are connected to it
	 */
	
    private class ClientRunner implements Runnable {
    	
        private Socket s = null;
        private Server parent = null;
        private ObjectInputStream inputStream = null;
        private ObjectOutputStream outputStream = null;
        public ClientRunner(Socket s,Server parent) {
            this.s = s;
            this.parent = parent;
            try {
                outputStream = new ObjectOutputStream(this.s.getOutputStream());
                inputStream = new ObjectInputStream(this.s.getInputStream());
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
        public void run() {
            try {
            		ClientPackage p=null;
                while((p = (ClientPackage)inputStream.readObject())!= null) {						//GameServer is reading ClientPackages
                	
                	if (p.getNewPlayer() && p.getPlayer().getID()!=1000) {							//if there is a new player in the client package and the ID of it is not 10000
                		if (parent.model.getFirstRound() || parent.model.getPlayers().size()<2) {	//and the game is in the first round or the players are less than 2
                			parent.model.getPlayers().add(p.getPlayer());							//add the client to the list of players
                		}else{																		//else
                			parent.model.getWaiting().add(p.getPlayer());							//add the client to the waiting list
                		}
                	}
            		parent.model.initialization();													//calling methods from the model
            		parent.model.removeLosers();
            		parent.model.reinitialization();
	                parent.model.betting();
	                if(p.getBet()) {																//if the player in the package has bet
	                	boolean allHaveBet=true;
	                	for (Player player:model.getPlayers()) {									//for every player
	                		if(player.getID()==p.getPlayer().getID()) {								//if the player is the same as the player in the package
	                			player.betChips(p.getBettingAmount());								//get the betting amount from the package and assign it to the corresponding player
	                			player.setBet(p.getBettingAmount());
	                			player.setHasBet(true);												//the player has bet
	                		}
	                	}
	                	for (Player player:model.getPlayers()) {									//for all the players
	                		if (!player.getHasBet()) {												//if a player hasn't bet
	                			allHaveBet=false;													//set the allHaveBet boolean to false
	                		}
	                	}
	                	if (allHaveBet) {															//if all the players have bet
	                		parent.model.allocation();												//allocate the cards to the players
	                	}else {
	                		parent.model.nextPlayer();												//else make the next player active
	                	}
	                }
	                
	                parent.model.play();															//call the play method from model
	                boolean over20=false;
	                
	                
	                if(p.getHit()) {																//if the player in the package has requested a card
	                    parent.model.requestCard(p.getPlayer());									//give the player in the package a card
	                    for (Player player:model.getPlayers()) {									//for all the players in the list
	                    	player.calculatePoints();												//calculate the point in their hand
	                    	if (p.getPlayer().getID()==player.getID()) {							//if the player in the package is the same with the player in the list in model
		                    	if (player.getPoints()>20) {										//if player's points are more than 20
		                    		over20=true;													//set the boolean value to true
		                    	}
		                    	if (player.getPoints()==21) {										//if the player has 21 points
		                    		player.setHasBlackJack(true);									//set the BlackJack attribute to true
		                    	}else {
		                    		player.setHasBlackJack(false);									//else set it to false
		                    	}
	                    	}
	                    }
	                }
	                for (Player player:model.getPlayers()) {										//for all the players in player list in model
	                	if (player.getNatural21()) {												//if a player has a natural 21
	                		if (player.getDealer()) {												//and he is the dealer
	                			for (Player pl:model.getPlayers()) {								
	                				pl.setHasStand(true);											//end the round
	                			}
	                		}
	                	}
	                }
	               
	                if (p.getStand() || over20 ) {
	                	for (Player player:model.getPlayers()) {				
	                		if(player.getID()==p.getPlayer().getID()) {								//find the associated player in model
	                			player.setHasStand(true);											//and set hasStand attribute to true
	                		}	
	                	}
	                	parent.model.nextPlayer();													//set next player to active
	                }
	                parent.model.checkWinner();														//check for winners of the round
	                this.parent.transmit(new DataPackage(parent.model.getPlayers(),parent.model.getWaiting(),p.getNewRound(),parent.model.getCardsI())); //send the package to every 
                }
                inputStream.close();
            }catch(ClassNotFoundException e) {
                e.printStackTrace();
            }catch(IOException e) {
                e.printStackTrace();
            }
           
        }

        public void transmitMessage(DataPackage data) {
            try {
                outputStream.writeObject(data);
                outputStream.flush();
                outputStream.reset();
            }catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Model model;
    private ServerSocket server;
    private ArrayList<ClientRunner> clients = new ArrayList<ClientRunner>();
    public Server() {
    		model=new Model();
        try {
            server = new ServerSocket(8765);
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    public void transmit(DataPackage data) {
        for(ClientRunner c: clients) {
            if(c != null) {
                c.transmitMessage(data);
            }
        }
    }
 
    public void run() {
        while(true) {
            Socket clientSocket = null;
            try {
                clientSocket = server.accept();
                System.out.println("New client connected");
                ClientRunner client = new ClientRunner(clientSocket,this);
                clients.add(client);
                new Thread(client).start();
                
            }catch(IOException e) {
                e.printStackTrace();
            }
            
            }
        }
 
    public static void main(String[] args) {
        Thread t = new Thread(new Server());
        t.start();
        try {
            t.join();
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
        
    }
}