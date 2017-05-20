import javax.swing.JOptionPane;

/**
 * Game Logic contains the methods that control
 * the main functioning of the game.
 */
public class GameLogic {
	private final int MAX_CARDS = 40;
	
	private Player[] players; // Stores the players with their cards
	private Card[] storeDrawCards = new Card[MAX_CARDS]; // Stores cards in the event of a draw
	private Card[] notNullCards; // Stores the non-null elements of storeDrawCards[] after draw/s
	
	private int[] playerDraw; // Store the indexes (from players[] array) of players who have drawn
	private int[] currentPlayers; // Store the indexes of players who are currently in the round/draw
	private int[] activePlayers; // Store the indexes of players who are still active in the game
	
	int[] drawValues;
	
	public int currentPlayer; // Index of player whose turn it is
	private int numPlayers;	
	private int roundNumber = 1;
	private int drawNumber = 1;
	private int totalDrawNumber = 0;
	private int arrayCount = 1; // Incrementor for storing number of players who draw
	private int aCount = 0; // Incrementor for incrementing playerDraw[] array
	private int roundWinner = -1;
	private int highestValue = -1; // Highest category value in a round
	private int highestMatchingValue = 0;
	private int storeDrawCardsIndex = 0;
	public int catIndex; // Current player's chosen category index
	private int gameWinner;
	
	private boolean draw = false; // Boolean value dependent on draw
	private int humanRoundWins;
	private int CPURoundWins;
	private int drawStart = 1;

	private int[] playerDrawValues;
	private int[] numberOfCards;
	private int[] storedCurrentPlayers;
	private int currentCatIndex;
	private int previousCurrentPlayer;
	
	/**
	 * @param playersIn is an array of all player objects in the game, with their cards.
	 * @param firstPlayer stores index of starting player.
	 * Initiate instance variables. activePlayers and currentPlayers are set
	 * to length of number of players. activePlayers elements are set to the 
	 * indices of all players in the players array.
	 */
	public GameLogic(Player[] playersIn, int firstPlayer) 
	{
		humanRoundWins = 0;
		CPURoundWins = 0;
		this.currentPlayer = firstPlayer;
		this.players = playersIn;
		numPlayers = this.players.length;
		activePlayers = new int[numPlayers];
		currentPlayers = new int[numPlayers];
		this.players[0].setHuman();

		for (int i = 0; i < activePlayers.length; i++) {
			activePlayers[i] = i;
		}
	}
		
	/**
	 * Initiate the first stage of the round.
	 * Check for active players, compare chosen categories and check for draw.
	 * See comments in method for more detailed information.
	 */
	public void startRound()
	{
		// Variables for setting up the draw loop
		draw = false;
		drawNumber = 1;
		storeDrawCards = new Card[MAX_CARDS];
		storeDrawCardsIndex = 0;
		aCount = 0; // Increments playerDraw array.
				
		// Check all players who are left in the game and update activePlayers[]
		// with their location in the players array.
		removeActiveNulls();
		
		// Set currentPlayers to activePlayers - this is need for any draws
		currentPlayers = new int[activePlayers.length];
		int iterator = 0;
		for (int i = 0; i < activePlayers.length; i++) {
			currentPlayers[iterator++] = activePlayers[i];
		}
		
		// Prints details of all players' top card to console.
		printTopCardValues();
		 
		// Assign catIndex to the chosen category index of the current player
		if (currentPlayer != 0) {
			catIndex = categorySelection();
		}
		
		// Print out the chosen category and the value for each player to console
		printCategoryAndValues();
		
		// Compare chosen category index to other players
		compareCategoryValues();
		
		// If there are at least 2 players drawn and they are the highest value
		// set draw to true
		if (arrayCount > 1 && highestMatchingValue == highestValue) {
			draw = true;
			setUpDrawLoop();
		} 
		// Update the roundWinner variable so that the correct player shows on the GUI
		currentPlayer = roundWinner;
		
	}
	
	/*
	 * Store the index of the players in the first draw to playerDraw[] and store
	 * the first round of top cards in storeDrawCards[].
	 * See comments inside the method for detailed informaiton.
	 */
	public void setUpDrawLoop() 
	{		
		totalDrawNumber++;
		// Initialise playerDraw to all players in the current draw
		playerDraw = new int[arrayCount];
		for (int i = 0; i < currentPlayers.length; i++) {
			if (players[currentPlayers[i]].selectCategoryValue(catIndex) == highestValue) {
				// Sets array values to the index (from players[] array) of players who have drawn.
				playerDraw[aCount] = currentPlayers[i];
				aCount++;
			}
		}	
		// Get Player values in draw for GUI
		getPlayerDrawValues();
		// Get current category index for GUI
		getCategoryIndex();
		
		// Remove the top cards from the players in the round and store in storeDrawCards[]
		for (int i = 0; i < currentPlayers.length; i++) {
			storeDrawCards[i] = players[currentPlayers[i]].getTopmostCard();
			players[currentPlayers[i]].removeTopCards();
			// Increment the array index for the next iteration if there is another draw
			storeDrawCardsIndex++;
		}
		// Check playerDraw[] for players with 0 cards then remove and update
		removeDrawNulls();		
		// Get number of cards of each player for GUI string
		getPlayerCardNumbers();

		// Assign currentPlayers[] to drawn players
		for (int i = 0; i < playerDraw.length; i++) {		
			currentPlayers[i] = playerDraw[i];
		}		
		// Print out communal pile
		printCommunalPile();
		
		// Choose/check current player - if current player from previous draw is
		// out, choose new current player randomly
		checkCurrentPlayer();
	}	
	
	/**
	 * Continue the draw by checking for players who have been put out, 
	 * comparing category values, checking for another draw and 
	 * storing information at specific points for accurate GUI display.
	 * See comments in the method for detailed information.
	 */
	public void drawLoop() 
	{	
		drawStart++;
		
		// Check all players who are left in the game and update activePlayers[]
		// with their location in the players array.
		removeActiveNulls();

		// Set currentPlayers to players in the draw.
		currentPlayers = new int[playerDraw.length];
		for (int i = 0; i < playerDraw.length; i++) {
			currentPlayers[i] = playerDraw[i];
		}
		
		// Prints details of all players' top card to console.
		printTopCardValues();
		
		getPreviousRoundPlayer();

		// Choose/check current player - if current player from previous draw is
		// out, choose new current player randomly
		checkCurrentPlayer();

		// Check if player is not human select random category and assign to catIndex
		if (currentPlayer != 0) {
			catIndex = categorySelection();
		}

		// Print out the chosen category and the value for each player to console
		printCategoryAndValues();
				
		// Check playerDraw[] for players with 0 cards then remove and update
		removeDrawNulls();
		// Compare chosen category index to other players in the draw
		compareDrawCategoryValues();
		
		// Get Player values in draw for GUI display
		getPlayerDrawValues();
		// Get current category index for GUI display
		getCategoryIndex();
		// Store the current players at this point in time for GUI display
		storeCurrentPlayers();	
		
		// Check currentPlayers[] for players with 0 cards then remove and update
		removeCurrentNulls();

		// Reinitialise playerDraw and store the players[] index of all drawn players
		playerDraw = new int[arrayCount];
		aCount = 0;
		for (int p = 0; p < currentPlayers.length; p++) {
			if (players[currentPlayers[p]].selectCategoryValue(catIndex) == highestValue) {
				// Sets array values to the index (from players[] array) of players who have drawn.
				playerDraw[aCount] = currentPlayers[p];
				aCount++;
			}
		}

		// If there are no more draws then set draw to false so it does not loop again
		if (arrayCount < 2 || highestMatchingValue != highestValue) {
			draw = false;		
		} else {
			
			// Remove the top cards from the players in the draw and store in storeDrawCards[]
			for (int i = 0; i < currentPlayers.length; i++) {
				storeDrawCards[storeDrawCardsIndex + i] = players[currentPlayers[i]].getTopmostCard();
				players[currentPlayers[i]].removeTopCards();
			}
									
			// Increase storeDrawCardsIndex to correct location of storeDrawCards[]
			// for subsequent draws
			storeDrawCardsIndex += currentPlayers.length;

			// Get number of cards at of each player for GUI display
			getPlayerCardNumbers();
					
			// Check currentPlayers[] for players with 0 cards then remove and update
			removeCurrentNulls();
			
			drawNumber++;
			totalDrawNumber++;
		}
		
		// Print out communal pile
		printCommunalPile();
		
		// Update roundWinner variable so that the correct player shows on the GUI
		currentPlayer = roundWinner;
	}
	
	/**
	 * Finish the round and redistribute the final cards according to 
	 * whether there has been a draw or not.
	 */
	public void finishRound()
	{
		currentPlayer = roundWinner;

		// Store round wins for database insertion.
		if (roundWinner == 0) {
			humanRoundWins ++;
		} else {
			CPURoundWins ++;
		}
		
		// If there has not been a draw and cards can be redistributed
		if (!getStoredCards()) {
			redistributeCards(roundWinner);
		} else {
			// If there has been a draw redistribute cards
			redistributeDrawnCards(notNullCards, currentPlayers, roundWinner);
		}

		// PRINT OUT NUMBER OF CARDS FOR EACH PLAYER (TESTING AND READABILITY
		printPlayersAllocation();

		drawStart = 1;
		roundNumber++;
	}
	
	
	/**
	 * Randomly select first player
	 */
	public Player selectFirstPlayer() {
		currentPlayer = (int) (Math.random() * numPlayers);
		return players[currentPlayer];
	}
	
	/**
	 * If any player has 40 cards, store their index in gameWinner.
	 * @return true if game is over
	 */
	public boolean gameOver()
	{
		for (int i = 0; i < numPlayers; i++) {
			if (players[i].getNoCards() == 40) {
				gameWinner = i;
				printGameWinner();
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Print game winner to console.
	 */
	public void printGameWinner()
	{
		if (gameWinner == 0) {
			System.out.println("Human player wins the game!");
  	  } else {
  		  	System.out.println("CPU " + gameWinner + " wins the game!");
  	  }
	}
	
		
	/*
	 * Generate a random category and return it as int.
	 */
	public int categorySelection() {
		int randIndex = (int) (Math.random() * 5);
		return randIndex;
	}

	/*
	 * Check activePlayers[] for any players with 0 cards then remove them and update
	 */
	public int[] removeActiveNulls() {

		int[] tempArr = new int[activePlayers.length];
		int iterator = 0;
		for (int i = 0; i < activePlayers.length; i++) {
			if (players[activePlayers[i]].getNoCards() > 0) {
				tempArr[iterator++] = activePlayers[i];
			}
		}

		activePlayers = new int[iterator];
		iterator = 0;
		for (int i = 0; i < activePlayers.length; i++) {
			activePlayers[iterator++] = tempArr[i];

		}
		return activePlayers;
	}

	/*
	 * Check playerDraw[] for players with 0 cards then remove and update
	 */
	public int[] removeDrawNulls() {
		int[] tempArr = new int[playerDraw.length];
		int iterator = 0;
		for (int i = 0; i < playerDraw.length; i++) {
			if (players[currentPlayers[i]].getNoCards() > 0) {
				tempArr[iterator++] = playerDraw[i];
			}
		}

		playerDraw = new int[iterator];
		iterator = 0;
		for (int i = 0; i < playerDraw.length; i++) {
			playerDraw[iterator++] = tempArr[i];
		}
		return playerDraw;
	}

	/*
	 * Check currentPlayers[] for players with 0 cards then remove and update
	 */
	public int[] removeCurrentNulls() {
		int[] tempArr = new int[currentPlayers.length];
		int iterator = 0;
		for (int i = 0; i < currentPlayers.length; i++) {
			if (players[currentPlayers[i]].getNoCards() > 0) {
				tempArr[iterator++] = currentPlayers[i];
			}
		}

		currentPlayers = new int[iterator];
		iterator = 0;
		for (int i = 0; i < currentPlayers.length; i++) {
			currentPlayers[iterator++] = tempArr[i];

		}
		return currentPlayers;
	}

	/*
	 * Compare chosen category index with all current players.
	 * Increment arrayCount to store the number of players who have drawn.
	 * Check if players who have drawn also have the highest value.
	 */
	public void compareCategoryValues() {
		roundWinner = -1;
		highestValue = -1;
		arrayCount = 1;
		highestMatchingValue = 0;

		for (int i = 0; i < currentPlayers.length; i++) {
			// check if the category with index categoryIndex is higher than the
			// current highestValue
			if (players[currentPlayers[i]].selectCategoryValue(catIndex) > highestValue) {
				highestValue = players[currentPlayers[i]].selectCategoryValue(catIndex);
				roundWinner = currentPlayers[i];
			} else if (players[currentPlayers[i]].selectCategoryValue(catIndex) == highestValue) {
				highestMatchingValue = highestValue;
				// Increment arrayCount to hold the number of drawn players
				arrayCount++;
			}
		}
	}
	
	/*
	 * Compare chosen category index with all drawn players.
	 * Increment arrayCount to store the number of players who have drawn again.
	 * Check if players who have drawn also have the highest value.
	 */
	public void compareDrawCategoryValues() {
		highestValue = -1;
		arrayCount = 1;
		highestMatchingValue = 0;
		for (int i = 0; i < playerDraw.length; i++) {
			// check if the category with index categoryIndex is higher than the
			// current highestValue
			if (players[playerDraw[i]].selectCategoryValue(catIndex) > highestValue) {
				highestValue = players[playerDraw[i]].selectCategoryValue(catIndex);
				roundWinner = playerDraw[i];
			} else if (players[playerDraw[i]].selectCategoryValue(catIndex) == highestValue) {
				highestMatchingValue = highestValue;
				arrayCount++;

			}
		}
	}
	
	public void printCategoryAndValues()
	{
		System.out.println("Current category index: " + (catIndex+1));
		for (int i = 0; i < currentPlayers.length; i++) {
			if (players[currentPlayers[i]].getNoCards() != 0) {
				if (currentPlayers[i] == 0) {
					System.out.println("Human player's card value: "
							+ players[currentPlayers[i]].selectCategoryValue(catIndex));
				} else {
					System.out.println("CPU " + currentPlayers[i] + "'s card value: "
							+ players[currentPlayers[i]].selectCategoryValue(catIndex));
				}
			}			
		}
	}
	
	/*
	 * Structure string of results for GUI when there is a draw.
	 */
	public String initialDrawString()
	{
		StringBuilder sBuilder = new StringBuilder();
		if (!draw){
			sBuilder.append("-------- No more draws. Results for Draw "+ drawNumber +": -------\n\n");
		} else {
			sBuilder.append("----------- IT'S A DRAW! Draw Number: "+ drawNumber +"---------\n\n");
		}
	    sBuilder.append("Player\tCards     Chosen Category     Value\n");
		sBuilder.append("----------------------------------------------\n");
		
		// If its the first draw, selectedArray is set to index of all players.
		int[] selectedArray = new int[numPlayers];		
		for (int i = 0; i < numPlayers; i++) {
			selectedArray[i] = i;
		}
		
		// If its after the first draw, selectedArray is set to index of all players
		// who are still in the game.
		if (drawStart > 1){
			selectedArray = storedCurrentPlayers;
		}
		
		// Create string of player names, card numbers, chosen category and category values.
	    for (int i = 0; i < selectedArray.length; i++) {
	    	// If player is human
		      if (selectedArray[i] == 0) {
		    	  sBuilder.append("You\t" + numberOfCards[i]);
		    	  // If Human player is current player.
		    	  if (selectedArray[i] == previousCurrentPlayer) {
		    		  // If player has a value i.e. if player is in the draw
		    		  if (playerDrawValues[i] > 0) {
		    			  sBuilder.append("\t" + (currentCatIndex+1) + "\t" +
		    			  + playerDrawValues[i] + "\n");
		    		  } else {
		    			  sBuilder.append("\n");
		    		  }
		    	  } else {
		    		  if (playerDrawValues[i] > 0) {
		    			  sBuilder.append("\t - \t" + playerDrawValues[i] + "\n");
		    		  }
		    		  else {
		    			  sBuilder.append("\n");
		    		  }
		    	  }
		      }		
		      // If player is CPU
		      else {
		    	  sBuilder.append("CPU " + selectedArray[i] + "\t"+ numberOfCards[selectedArray[i]]);
			      if (selectedArray[i] == previousCurrentPlayer) {
		    		  if (playerDrawValues[i] > 0) {
		    			  sBuilder.append("\t" + (currentCatIndex+1) + "\t"
		    			  + playerDrawValues[i] + "\n");
		    		  } else {
		    			  sBuilder.append("\n");
		    		  }
			      }
			      else {
		    		  if (playerDrawValues[i] > 0) {
		    			  sBuilder.append("\t - \t"
		    			  + playerDrawValues[i] + "\n");
		    		  } else {
		    			  sBuilder.append("\n");
		    		  }
			      }
		      }
	     }
	    // Add communal pile to string
	     sBuilder.append(getCommunalPile());    
		 return sBuilder.toString();
	}
	
	
	/**
	 * Stores the category values of each current player in 
	 * playerDrawValues[]. This is used in printing the 
	 * draw string to the GUI.
	 */
	public void getPlayerDrawValues()
	{
		// If its the first draw of the game
		if (drawStart == 1){			
			playerDrawValues = new int[numPlayers];
			for (int i = 0; i < numPlayers; i++){
				if (players[i].getNoCards() != 0){ 
					playerDrawValues[i] = players[i].selectCategoryValue(catIndex);
				} else {
					playerDrawValues[i] = 0;
				}
			}
		} else {
			playerDrawValues = new int[currentPlayers.length];
			for (int i = 0; i < currentPlayers.length; i++){
				if (players[currentPlayers[i]].getNoCards() != 0){ 
					playerDrawValues[i] = players[currentPlayers[i]].selectCategoryValue(catIndex);
				} else {
					playerDrawValues[i] = 0;
				}
			}
		}
	}
	
	/**
	 * Set previousCurrentPlayer to currentPlayer to be used
	 * for the accurate display the end of round/draw results in the GUI.
	 */
	public void getPreviousRoundPlayer()
	{
		previousCurrentPlayer = currentPlayer;
	}
	
	/**
	 * Set currentCatIndex to current chosen category index to be used
	 * for the accurate display the end of round/draw results in the GUI.
	 */
	public void getCategoryIndex()
	{
		currentCatIndex = catIndex;
	}
	
	/**
	 * Set currentCatIndex to current chosen category index to be used
	 * for the accurate display the end of round/draw results in the GUI.
	 */
	public void getPlayerCardNumbers() 
	{
		numberOfCards = new int[numPlayers];
		for (int i = 0; i < numPlayers; i++){
			numberOfCards[i] = players[i].getNoCards();
		}
	}
		
	/*
	 * In storedCurrentPlayers store the index of all current players to be used
	 * for the accurate display the end of round/draw results in the GUI.
	 */
	public void storeCurrentPlayers()
	{
		storedCurrentPlayers = new int[currentPlayers.length];		
		for (int i = 0; i < storedCurrentPlayers.length; i++) {
			storedCurrentPlayers[i] = currentPlayers[i];
		}		
	}
	

	/*
	 * Check if current player exists in playerDraw[].
	 * If not, choose a random player who is still in the draw.
	 */
	public void checkCurrentPlayer() 
	{
		boolean changePlayer = true;
		for (int i = 0; i < playerDraw.length; i++) {
			if (players[playerDraw[i]] == players[currentPlayer]) {
				changePlayer = false;
			}
		}

		if (changePlayer) {
			int randomiseIndex = (int) (Math.random() * playerDraw.length);
			// Current player is set to the position of the player in the
			// original players array
			currentPlayer = playerDraw[randomiseIndex];
		}
	}

	/*
	 * Check if storeDrawCards[] holds any cards, then trim the null values.
	 * If storeDrawCards does hold cards ( meaning there has been a draw)
	 * return true, otherwise return false.
	 */
	public boolean getStoredCards() {
		// Check if storeDrawCards[] has cards
		int tempInt = 0;
		for (int i = 0; i < 40; i++) {
			if (storeDrawCards[i] != null) {
				tempInt++;
			}
		}
		if (tempInt == 0) {
			return false;
		}
		// Trim the null values from storeDrawCards
		notNullCards = new Card[tempInt];
		int nIndex = 0;
		for (int i = 0; i < 40; i++) {
			if (storeDrawCards[i] != null) {
				notNullCards[nIndex++] = storeDrawCards[i];
			}
		}
		return true;
	}

	/*
	 * After a draw has finished and produced a winner, distribute the cards 
	 * in the draw pile (storeDrawCards[]) and get the top cards of the 
	 * last players left in the draw.
	 */
	public void redistributeDrawnCards(Card[] drawCards, int[] lastPlayers, int winningPlayer) 
	{
		// Temporary array that stores all top cards of players left in the round
		Card[] storeCards = new Card[lastPlayers.length + drawCards.length];

		// TRANSFER ALL THE TOP CARDS OF THE FINAL PLAYERS
		for (int i = 0; i < lastPlayers.length; i++) {
			// Store top cards of all final players
			storeCards[i] = players[lastPlayers[i]].getTopmostCard();
			// remove top card from all final players
			players[lastPlayers[i]].removeTopCards();
		}
		// Add draw cards to the storeCards array after the last players' cards
		// have been stored
		int drawCardsIndex = 0;
		for (int i = lastPlayers.length; i < storeCards.length; i++) {
			storeCards[i] = drawCards[drawCardsIndex++];
		}
		// Current length of winner's hand
		int winHandLength = players[winningPlayer].getNoCards();
		// New length of winner's hand after all cards are transferred from
		// other players
		int newWinHandLength = winHandLength + storeCards.length;
		// Pass array of topcards and length of winner's new hand to add winning
		// cards method
		players[winningPlayer].addWinningCards(storeCards, newWinHandLength);

	}

	/*
	 * After a draw has finished and produced a winner, distribute the cards 
	 * in the draw pile (storeDrawCards[]) and get the top cards of the 
	 * last players left in the draw.
	 */
	public void redistributeCards(int winningPlayer) {
		// Temporary array that stores all top cards
		Card[] storeTopCards = new Card[currentPlayers.length];

		// TRANSFER ALL BUT THE TOP CARD OF ALL LOSING PLAYERS TO NEW ARRAY
		for (int i = 0; i < currentPlayers.length; i++) {
			// Store top cards of all players
			storeTopCards[i] = players[currentPlayers[i]].getTopmostCard();
			// remove top card from all players
			players[currentPlayers[i]].removeTopCards();

		}
		// Current length of winner's hand
		int winHandLength = players[winningPlayer].getNoCards();
		// New length of winner's hand after all cards are transferred from
		// other players
		int newWinHandLength = winHandLength + currentPlayers.length;

		// Pass array of topcards and length of winner's new hand to add winning
		// cards method
		players[winningPlayer].addWinningCards(storeTopCards, newWinHandLength);
	}
	
	/**
	 * Print card details and number of cards of each player in Console.
	 */
	public void printPlayersAllocation()
	{
	    System.out.println("\n------------Player Card Allocation---------------");
	    for (int i = 0; i < numPlayers; i++) {
	    	if(i == 0){
	    		System.out.println(
	    		  "\nHuman player's number of cards: " + players[i].getNoCards());
	    	} else {
	    		System.out.println(
	    		   "\nCPU " + i + " number of cards: " + players[i].getNoCards());
	    	}
	    	System.out.println("The cards are:");
	    	for (int j = 0; j < players[i].getNoCards(); j++) {
	    		System.out.println(players[i].getpInitialCards()[j].getDescription());
	    	}	      
	    }
	    System.out.println("-------------------------------------------------");
	}
  		
	public void printTopCardValues()
    {
		int[] currentArray;
		currentArray = currentPlayers;
		
		for (int i = 0; i < currentArray.length; i++){
			if (players[currentArray[i]].getNoCards() !=0) {
				if (currentArray[i] == 0) {
					System.out.print("Human player's top card: "
							+ players[currentArray[i]].getTopmostCard().getDescription() + " - ");
				} else {
					System.out.print("CPU " + currentArray[i] + "'s top card: "
							+ players[currentArray[i]].getTopmostCard().getDescription() + " - ");
				}
				for (int j = 0; j < 5; j++){
					System.out.print(players[currentArray[i]].getTopmostCard().getValues()[j] + " ");
				}
			}
			
			System.out.println();
  	  	}
		System.out.println("----------------------------");
    }
	
	/**
	 * Print communal pile to console 
	 */
	public void printCommunalPile()
	{
	    System.out.println(getCommunalPile());
	    System.out.println("-----------------------------------------------\n");
	}
  
	/**
	 * @return contents of communal pile as a String for console and GUI.
	 */
	public String getCommunalPile()
	{
		String s = "";
	    s += "\n-------------THIS IS THE COMMUNAL PILE--------------\n";
		    for (int i = 0; i < storeDrawCardsIndex; i++){
		      s += (i+1) + ") " + storeDrawCards[i].getDescription() + "\n";
		    }
	    return s;
	}
		
	/*
	 * Series of getter methods for variables 
	 */
	public int getGameWinner() 
	{
		return gameWinner;
	}	
	public void setCatIndex(int index)
	{
		catIndex = index;
	}
	public int getNumberOfRounds() 
	{
		return roundNumber;
	}	
	public int getNumberOfDraws() 
	{
		return drawNumber;
	}	
	public int[] getCurrentPlayers() 
	{
		return currentPlayers;
	}
	public int[] getActivePlayers() 
	{
		return activePlayers;
	}
	public void setCurrentPlayers(int[] currentPlayers) {
		this.currentPlayers = currentPlayers;
	}
	public Player[] getPlayers() 
	{
		return players;
	}	
	public int getCurrentPlayer()
	{
		return currentPlayer;
	}	
	public int getNumberOfCards(int playerIndex) {
		return players[playerIndex].getNoCards();
	}
	public int[] getDrawPlayers()
	{
		return playerDraw;
	}
	public int getCatIndex() 
	{
		return catIndex;
	}
	public int getRoundWinner() 
	{
		return roundWinner;
	}
	public int getTotalDrawNumber()
	{
		return totalDrawNumber;
	}
	public boolean getDrawStatus()
	{
		return draw;
	}	
	public int getHumanRoundWins()
	{
		return humanRoundWins;
	}
	public int getCPURoundWins()
	{
		return CPURoundWins;
	}
	
}
