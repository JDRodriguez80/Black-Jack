//imports
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class BlackJack {
    //creating a card
    private class Card {
        String value;
        String type;
        Card(String value, String type){
            this.value=value;
            this.type=type;
        }
        public String toString(){
            return value + "-" + type;
        }
        public int getValue(){
            if("AJQK".contains(value)){
                if(value=="A"){
                    return 11;
                }//checking if its an ace
                return 10;//for queen,joke or king
            } return Integer.parseInt(value); //if values are 2-10
        }
        public boolean itsAce(){
            return value=="A";
        }
        public String getImgPath(){
            return "cards/"+ toString() + ".png";
        }
    }

    //creating an array to store all cards
    ArrayList<Card>deck;
    ArrayList<Card>shuffledDeck;

    //shuffling the deck
    Random random =new Random();

    //dealer
    Card hiddenCard;
        ArrayList<Card> dealerHand;
        int dealerSum;
        int dealerAceCount;

        //player
    ArrayList<Card> playerHand;
    int playerSum=0;
    int playerAceCount=0;    
    
    //window
    int boardWidth = 600;
    int boardHeight = boardWidth;
    int cardsWidth = 110;
    int cardsHeight = 154;
    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel= new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            //drawing dealer cards
            //hidden card
            try {
                Image hiddenCaImg =new ImageIcon(getClass().getResource("./cards/back.png")).getImage();
                if(!stayButton.isEnabled()){
                        hiddenCaImg = new ImageIcon(getClass().getResource(hiddenCard.getImgPath())).getImage();
                    };
            g.drawImage(hiddenCaImg,20,20,cardsWidth,cardsHeight,null);
            // draw dealer card
                for (int i=0; i< dealerHand.size(); i++){
                    Card card = dealerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImgPath())).getImage();
                    g.drawImage(cardImg, cardsWidth+25 + (cardsWidth+5)*i, 20, cardsWidth, cardsHeight, null);
                }
                //drawing player cards
                for (int i=0; i< playerHand.size(); i++){
                    Card card = playerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImgPath())).getImage();
                    String playerCount = Integer.toString(playerSum);
                    g.drawImage(cardImg, 20 +(cardsWidth+5)*i,300, cardsWidth, cardsHeight, null);
                    g.setFont (new Font("Arial", Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(playerCount,15, 300+cardsHeight+30);
                    
                }
                //deciding who wins
                if(!stayButton.isEnabled()){
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();
                    System.out.println("Stay");
                    System.out.println("Dealer sum: " + dealerSum);
                    System.out.println("Player sum: " + playerSum);
                    String message = "";
                    if(playerSum>21){
                        message = "You Loose MOFO";
                    }else if (dealerSum >21){
                        message ="you Win";
                    }else if (playerSum == dealerSum){
                        message ="its a Tie";
                    }else if (playerSum > dealerSum){
                        message="You Win";
                    }else if (playerSum < dealerSum)
                    {
                        message ="You Loose";
                    }
                    g.setFont (new Font("Arial", Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(message, 220, 250);
                    String dealerCount = Integer.toString(dealerSum);
                    g.setFont (new Font("Arial", Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(dealerCount,15, cardsHeight+50);
                };
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };
    //bottom buttons
    JPanel buttoPanel= new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton stayButton = new JButton("Stay");
//constructor
    BlackJack(){
        startGame();
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);
        hitButton.setFocusable(false);
        buttoPanel.add(hitButton);
        stayButton.setFocusable(false);
        buttoPanel.add(stayButton);
        frame.add(buttoPanel, BorderLayout.SOUTH);
        
        //action listeners for buttons
        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Card card = deck.remove(deck.size()-1);
                playerSum+=card.getValue();
                playerAceCount+= card.itsAce()?1:0;
                playerHand.add(card);
                if(reducePlayerAce() >21){
                    hitButton.setEnabled(false);
                }  
                gamePanel.repaint();
            }
        });
        stayButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);

                //dealer deals cards until he has 17 or more
                while(dealerSum<17){
                    Card card = deck.remove(deck.size()-1);
                    dealerSum+=card.getValue();
                    dealerAceCount+= card.itsAce()?1:0;
                    dealerHand.add(card);
                }
                gamePanel.repaint();
            }
        });

        gamePanel.repaint();
        
    }
    public void startGame(){
        //build deck
        buildDeck();
        //suffle it
        shuffleDeck();
        //deal 2 card to dealer
            //initializing delaer variables
        dealerHand= new ArrayList<Card>();
        dealerSum=0;
        dealerAceCount=0;
        hiddenCard = deck.remove(deck.size()-1);//this removes last cards from the deck  
        dealerSum+=hiddenCard.getValue();

        //checking if hidden card its an ace
        dealerAceCount += hiddenCard.itsAce() ? 1 : 0;

        //addin another not hidden card to delers hand
        Card card = deck.remove(deck.size()-1);
        dealerSum += card.getValue();
        dealerAceCount+= card.itsAce()?1:0;
        dealerHand.add(card);

        //debug showing results
        System.out.println("Dealers stuff");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerSum);
        System.out.println(dealerAceCount);
        //deal 2 cards to player
        playerHand= new ArrayList<Card>();
        playerSum=0;
        playerAceCount=0;

        for(int i =0; i<2; i++){
            card=deck.remove(deck.size()-1);
            playerSum+=card.getValue();
            playerAceCount+= card.itsAce()?1:0;
            playerHand.add(card);
        }
        System.out.println("player stuff");
        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(playerAceCount);

        

    }
    public void buildDeck(){
        deck= new ArrayList<Card>();
        //listing all card types
        String[] values={"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
        String[] types={"C","H","D","S"};

        //building combinations
        for(int i = 0; i < types.length; i++){
            for(int j = 0; j < values.length; j++){
                Card card = new Card(values[j],types[i]);
                deck.add(card);
            }
        }

        // 
        System.out.println("build deck:");
        System.out.println(deck);
    }
    // shuffling the deck
    public void shuffleDeck(){
        for( int i=0; i< deck.size(); i++){
            int j = random.nextInt(deck.size());
            Card currCard =deck.get(i);
            Card ranCard = deck.get(j);
            deck.set(i, ranCard);
            deck.set(j, currCard);
        }
        System.out.println("Shuffled ");
        System.out.println(deck);
    }
    public int reducePlayerAce(){
        while(playerSum>21 && playerAceCount>0){
            playerSum-=10;
            playerAceCount-=1;
        }
        return playerSum;
    }
    public int reduceDealerAce(){
        while(dealerSum>21 && dealerAceCount>0){
            dealerSum-=10;
            dealerAceCount-=1;
        }
        return dealerSum;
    }
}

