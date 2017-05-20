

/**
 * The GameSetup class does the following:
 * 1) Selects the first player to start the game
 * 2) Shuffles the deck randomly
 * 3) Assigns cards to the players
 *
 */
public class GameSetup {

  private final int MAX_CARDS = 40;
  private Card[] dOfcards;
  private int noCards;
  private Player[] players;
  private int index = 0;
  private int currentPlayer;
  private int numPlayers;
  private Card[][] cardSplit;


  // private GameLogic gameLogic;

  public GameSetup(int numPlayers) {
    dOfcards = new Card[MAX_CARDS]; // instantiates a card object of length 40
    this.numPlayers = numPlayers;
    noCards = 0;
  }


  /**
   * Method randomly selects the first player who
   * who must choose a category to start the game.
 * @return randomly selected player index number
 */
	public int selectFirstPlayer() {
	    currentPlayer = (int) (Math.random() * numPlayers);
	    return currentPlayer;
	  }

  public void setCardValues(String cardData) {
    Card cardInfo = new Card(cardData); // Create new card object
    dOfcards[noCards] = cardInfo;
    noCards++;
  }


  /**
   * Method prints out the deck of cards.
   * This is done for test purposes.
 * @return string containing the deck of cards.
 */
	public String printCards() {
	    StringBuilder stringBuilder = new StringBuilder();
	    for (int i = 0; i < dOfcards.length; i++) {
	      stringBuilder.append(dOfcards[i].getDescription() + " " + dOfcards[i].getCategory1() + " "
	          + dOfcards[i].getCategory2() + " " + dOfcards[i].getCategory3() + " " + dOfcards[i].getCategory4() + " " + dOfcards[i].getCategory5() + "\n");
	    }
	    stringBuilder.append("-----------------------------------------------------");
	    String printCardString = stringBuilder.toString();
	    return printCardString;
	  }


  /**
   * Method used to randomly shuffle the deck of cards.
 * @return shuffled Card[] object
 */
	public Card[] shuffleDeck() {
	    for (int i = 0; i < dOfcards.length; i++) {
	      //Assigning a random index for the shuffling
	      int shuffleIndex = (int) (Math.random() * dOfcards.length);
	      Card temp = new Card();
	      temp = dOfcards[i];
	      dOfcards[i] = dOfcards[shuffleIndex];
	      dOfcards[shuffleIndex] = temp;
	    }

	    // Return shuffled Card[] object
	    return dOfcards;
  }


  /**
   * Method assigns cards to each player
   * based on the number of players in the game.
   *
 * @return
 */
	public Player[] initPlayerDeck()
	{
	    shuffleDeck();
	
	    int split = 0;
	
	    split = MAX_CARDS / numPlayers;
	
	
	    // Card split logic for 3 players in the game
	    if (numPlayers == 3) {
	
	      cardSplit = new Card[numPlayers][];
	      // Assign one additional card to the human player.
	      cardSplit[0] = new Card[split+1];
	      // Assign the rest to the CPU players.
	      cardSplit[1] = new Card[split];
	      cardSplit[2] = new Card[split];
	
	      boolean firstPlayer = true;
	
	      for (int k = 0; k < numPlayers; k++) {
	
	        if (firstPlayer) {
	          for (int h = 0; h < split+1; h++) {
	            cardSplit[k][h] = dOfcards[index];
	            index++;
	          }
	          firstPlayer = false;
	        }else if (!firstPlayer){
	          for (int h = 0; h < split; h++) {
	            cardSplit[k][h] = dOfcards[index];
	            index++;
	          }
	        }
	      }
	    } else {
	
	    // Card split logic if number of players is 2, 4 or 5
	      cardSplit = new Card[numPlayers][split];
	
	      for (int k = 0; k < numPlayers; k++) {
	        for (int h = 0; h < split; h++) {
	          cardSplit[k][h] = dOfcards[index];
	          index++;
	        }
	      }
	    }
	    for (int i = 0; i < numPlayers; i++) {
	    }
	    // players[0] is human by default
	    players = new Player[numPlayers];
	
	    for (int i = 0; i < numPlayers; i++) {
	      players[i] = new Player(cardSplit[i]);
	    }
	
	    // Return player object with the cards assigned to each one.
	    return players;
	  }


}
