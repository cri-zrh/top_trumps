import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/*
 * Handles all methods and attributes of the main GUI
 */
public class ProgGUI extends JFrame implements ActionListener {

  private GameStatistics gameStats;
  private JButton startButton, reportButton, updateButton, statisticsButton, writeButton, startGameButton, nextRoundButton, continueButton;
  private JTextArea taWinner, taCards, taGame;
  private JLabel  lnumPlayers, lCategory, lValue1, lcat1, lcat2, lcat3, lcat4, lcat5;
  private JTextField tfcat1, tfcat2, tfcat3, tfcat4, tfcat5;
  private JComboBox dropdownCat, dropdownPlayers;
  private String  categoryDescription1, categoryDescription2, categoryDescription3, categoryDescription4, categoryDescription5;
  private String[] categories = {"Choose a category", "Category 1", "Category 2", "Category 3", "Category 4", "Category 5"};
  private String[] dropdownP = {"2", "3", "4", "5"};
  private GameSetup gameSetup;
  private GameLogic gameLogic;
  private int numPlayers;

  private JFrame game;

  int[] getPlayerValues;

  public ProgGUI()
  {
    GUIStart();
  }

  /**
   * Method creates initial GUI where you choose
   * either if you want to play or See the Report.
   */
  public void GUIStart()
  {
    //setup start window
    JFrame start = new JFrame();
    start.setTitle("Top Trumps");
    start.setSize(300,200);
    start.setLocation(200,150);
    start.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //create and add buttons
    lnumPlayers = new JLabel("Please choose number of players");
    dropdownPlayers = new JComboBox(dropdownP);
    startButton = new JButton("Play Game");
    reportButton = new JButton("See Report");
    JPanel pan = new JPanel();
    start.add(pan, "Center");

    pan.add(lnumPlayers);
    pan.add(dropdownPlayers);
    pan.add(startButton);
    pan.add(reportButton);

    startButton.addActionListener(this);
    reportButton.addActionListener(this);

    start.setVisible(true);

    gameStats = new GameStatistics();
  }


  /**
   * Main game window
   */
  public void showGUIGame() 
  {
      //setup game window
      GridLayout grid = new GridLayout(3,0);
      GridLayout yourCardGrid = new GridLayout(7,2);
      GridLayout bottomGrid = new GridLayout(2,1);

      game = new JFrame();
      game.setLayout(grid);
      game.setTitle("Top Trumps - Play Game");
      game.setSize(600,800);
      game.setLocation(200,150);

      lCategory = new JLabel("Your top Card");
      lValue1 = new JLabel("Value");
      //Display categories read from deck.txt file
      lcat1 = new JLabel("1) " + categoryDescription1);
      lcat2 = new JLabel("2) " + categoryDescription2);
      lcat3 = new JLabel("3) " + categoryDescription3);
      lcat4 = new JLabel("4) " + categoryDescription4);
      lcat5 = new JLabel("5) " + categoryDescription5);
      tfcat1 = new JTextField(3);
      tfcat2 = new JTextField(3);
      tfcat3 = new JTextField(3);
      tfcat4 = new JTextField(3);
      tfcat5 = new JTextField(3);
      tfcat1.setEditable(false);
      tfcat2.setEditable(false);
      tfcat3.setEditable(false);
      tfcat4.setEditable(false);
      tfcat5.setEditable(false);
      dropdownCat = new JComboBox(categories);
      dropdownCat.setEnabled(false);
      nextRoundButton = new JButton("Next Round");
      nextRoundButton.setEnabled(false);
      startGameButton = new JButton("Start Game");
      nextRoundButton.setEnabled(false);
      continueButton = new JButton("Continue Round");
      continueButton.setEnabled(false);
      taCards = new JTextArea(15,35);
      taGame = new JTextArea(5,35);
      JScrollPane scrollPane = new JScrollPane(taCards);

      //Add jpanel for title
      JPanel toptitle = new JPanel();

      JPanel topGame = new JPanel();
      topGame.setLayout(yourCardGrid);
      topGame.add(lCategory);
      topGame.add(lValue1);
      topGame.add(lcat1);
      topGame.add(tfcat1);
      topGame.add(lcat2);
      topGame.add(tfcat2);
      topGame.add(lcat3);
      topGame.add(tfcat3);
      topGame.add(lcat4);
      topGame.add(tfcat4);
      topGame.add(lcat5);
      topGame.add(tfcat5);
      topGame.add(dropdownCat);

      toptitle.add(topGame, BorderLayout.SOUTH);

      JPanel bottom = new JPanel();
      JPanel infoBottom = new JPanel();
      JPanel bottomGrid1 = new JPanel();
      JPanel bottomGrid2 = new JPanel();


      JPanel bottomGame = new JPanel();
      bottomGame.add(scrollPane);


      //infoBottom with text area and buttons
      infoBottom.setLayout(bottomGrid);

      bottomGrid1.add(taGame);
      infoBottom.add(bottomGrid1);

      bottomGrid2.add(startGameButton);
      bottomGrid2.add(nextRoundButton);
      bottomGrid2.add(continueButton);
      infoBottom.add(bottomGrid2);

      bottom.add(bottomGame, BorderLayout.SOUTH);

      //add panels to frame
      game.add(toptitle);
      game.add(bottom);
      game.add(infoBottom);


      startGameButton.addActionListener(this);
      nextRoundButton.addActionListener(this);
      continueButton.addActionListener(this);

      game.setVisible(true);
  }

	  /**
	 * Method displays the window frame
	 * after the game has ended.
	 */
	public void GUIGameEnd() {
	    JFrame gameEnd = new JFrame();
	    gameEnd.setTitle("Top Trumps - End of Game");
	    gameEnd.setSize(400,400);
	    gameEnd.setLocation(300,300);
	
	    taWinner = new JTextArea("End of game. The winner is XY ");
	    updateButton = new JButton("Update database");
	    statisticsButton = new JButton("View game statistics");
	    writeButton = new JButton("Write statistis to file");
	
	    JPanel panGameEnd = new JPanel();
	    gameEnd.add(panGameEnd);
	    panGameEnd.add(taWinner);
	    panGameEnd.add(updateButton);
	    panGameEnd.add(statisticsButton);
	    panGameEnd.add(writeButton);
	
	    gameEnd.setVisible(true);
	}

  	/**
  	* Method reads deck data from text file.
 	*/
	public void initData()
	{
		String nxL = null;

		try {
			FileReader reader = new FileReader("deck.txt");
			Scanner scanLine = new Scanner(reader);

			// Pass information to Cards line by line
			String title = scanLine.nextLine();
			String[] titleArray = title.split(" ");
			categoryDescription1 = titleArray[1];
			categoryDescription2 = titleArray[2];
			categoryDescription3 = titleArray[3];
			categoryDescription4 = titleArray[4];
			categoryDescription5 = titleArray[5];
			// Loops through and reads the file line-by-line
			while (scanLine.hasNextLine()) {
				nxL = scanLine.nextLine();
				gameSetup.setCardValues(nxL);
			}
			scanLine.close();
		}
		catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Method appends information to textArea at game start
	 */
	private void showStartingGame()
	{
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("------------- Next round is: Round 1 ------------\n\n");
		sBuilder.append("Player\tCards     Chosen Category     Value\n");
		sBuilder.append("----------------------------------------------\n");
		sBuilder.append("You\t"+ gameLogic.getPlayers()[0].getNoCards() + "\t - \n");
		for (int i = 1; i < gameLogic.getPlayers().length; i++) {
			sBuilder.append("CPU " + i + "\t" + gameLogic.getNumberOfCards(i) + "\t - \n");
		}
		taCards.setText(sBuilder.toString());
	}

	/*
	 * Store previous round values so that they can be displayed
	 * accurately in the GUI
	 */
  	private int[] getPreviousRoundValues()
  	{
  		int[] values = new int[numPlayers];
  		for (int i = 0; i < numPlayers; i++) {
  			if (gameLogic.getPlayers()[i].getNoCards() != 0) {
  				values[i] = gameLogic.getPlayers()[i].selectCategoryValue(gameLogic.getCatIndex());
  			}
  		}
  		return values;
  	}

	 /**
	 * Method appends to textArea the gameplay's information after each round
	 * @param values - Values for card values and chosen category
	 */
  	private void showCurrentGame(int[] values) 
  	{
  		StringBuilder sBuilder = new StringBuilder();
  		sBuilder.append("------------- Next round is: Round " + gameLogic.getNumberOfRounds() + " ------------\n\n");
  		sBuilder.append(" Results for Round " + (gameLogic.getNumberOfRounds()-1) + " are: \n\n");
  		sBuilder.append("Player\tCards     Chosen Category     Value\n");
  		sBuilder.append("----------------------------------------------\n");

  		for (int i = 0; i < gameLogic.getPlayers().length; i++) {
  			// If player is human
  			if (i == 0) {
  				sBuilder.append("You\t"+ gameLogic.getPlayers()[0].getNoCards());
  				// If Human player is current player.
  				if (i == gameLogic.currentPlayer) {
  					// If human player has more than 0 cards
  					if (gameLogic.getPlayers()[i].getNoCards() != 0) {
  						sBuilder.append("\t" + (gameLogic.getCatIndex()+1) + "\t" +
  								+ values[i] + "\n");
  					} else {
  						sBuilder.append("\n");
  					}

  				} else {
  					if (gameLogic.getPlayers()[i].getNoCards() != 0) {
  						sBuilder.append("\t - \t" + values[i] + "\n");
  					}
  					else {
  						sBuilder.append("\n");
  					}

  				}
  			}
  			// If player is not human
  			else {
  				sBuilder.append("CPU " + i + "\t"+ gameLogic.getPlayers()[i].getNoCards());
  				if (i == gameLogic.currentPlayer) {
  					if (gameLogic.getPlayers()[i].getNoCards() != 0) {
  						sBuilder.append("\t" + (gameLogic.getCatIndex()+1) + "\t"
  								+ values[i] + "\n");
  					} else {
  						sBuilder.append("\n");
  					}
  				}
  				else {
  					if (gameLogic.getPlayers()[i].getNoCards() != 0) {
  						sBuilder.append("\t - \t"
  								+ values[i] + "\n");
  					} else {
  						sBuilder.append("\n");
  					}
  				}
  			}
  		}
  		// Populate GUI with string.
  		taCards.setText(sBuilder.toString());
  		if (0 == gameLogic.currentPlayer) {
  			taGame.setText("You won the round. It is your turn. Please select a category");
  		}
  		else {
  			taGame.setText("CPU " + gameLogic.currentPlayer + " won the previous round. Player " +
  					gameLogic.currentPlayer + " is starting the round.");
  		}
  	}

	  /**
	 * Method shows the initial draw information.
	 */
	private void showDrawGame()
	  {
	      taCards.setText(gameLogic.initialDrawString());
	
	      if (0 == gameLogic.currentPlayer) {
	        taGame.setText("It's a draw! It's your turn.\nPlease select a category and press 'Continue Round'");
	      }
	      else {
	        taGame.setText("It's a draw! It's CPU " + gameLogic.currentPlayer + "'s turn.\nPlease press 'Continue Round'");
	      }
	  }


	  /**
	 * Method output's human player's top card's values to the appropriate text areas.
	 */
	private void showTopCard() {
	    if (gameLogic.getPlayers()[0].getNoCards() != 0) {
	      lValue1.setText("" + gameLogic.getPlayers()[0].getTopCardAttribute().getDescription());
	      tfcat1.setText("" + gameLogic.getPlayers()[0].getTopCardAttribute().getValues()[0]);
	      tfcat2.setText("" + gameLogic.getPlayers()[0].getTopCardAttribute().getValues()[1]);
	      tfcat3.setText("" + gameLogic.getPlayers()[0].getTopCardAttribute().getValues()[2]);
	      tfcat4.setText("" + gameLogic.getPlayers()[0].getTopCardAttribute().getValues()[3]);
	      tfcat5.setText("" + gameLogic.getPlayers()[0].getTopCardAttribute().getValues()[4]);
	    }
	  }

	/**
	 * If human player's turn and no category selected
	 * then @return false and ensure play goes no further
	 * until category is selected
	 */
	private boolean checkIfCategorySelected()
	{
		if (gameLogic.getCurrentPlayer() == 0){
			if (dropdownCat.getSelectedIndex()==0) {
				JOptionPane.showMessageDialog(null, "Please choose a category.", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
		    } else {
		    	gameLogic.setCatIndex(dropdownCat.getSelectedIndex()-1);
		    }
		}
		return true;
	}
	
	
	/**
	 * Method that groups together all code that is needed for the
	 * execution of the 'Play Game' button.
	 * Initialise GameSetup class and GameLogic class.
	 */
	private void initiatePlayGame()
	{
		// Button availability
        startButton.setEnabled(false);
        reportButton.setEnabled(false);
        dropdownPlayers.setEnabled(false);
        numPlayers = Integer.parseInt((String)dropdownPlayers.getSelectedItem());

        // Initialise GameSetup class
        gameSetup = new GameSetup(numPlayers);
        initData();

        // Show main GUI
        showGUIGame();

        // Print out Deck before the shuffle process has taken place.
        System.out.println("----------- Deck before shuffle --------");
        System.out.println(gameSetup.printCards());

        int firstPlayer = gameSetup.selectFirstPlayer();
        gameLogic = new GameLogic(gameSetup.initPlayerDeck(), firstPlayer);
        taGame.setText("Cards have been shuffled and distributed to players.");

        if (firstPlayer == 0) {
          dropdownCat.setEnabled(true);
          taGame.append("\nYou have been chosen to start the game.\nPlease choose a category and press 'Start Game'");
        } else {
          taGame.append("\nCPU " + firstPlayer + " has been chosen to start the game.");
          taGame.append("\nPress 'Start Game' to begin.");
        }

        // Show round details on GUI
        showStartingGame();
        showTopCard();

        // Print out Deck after the shuffle process has taken place.
        System.out.println("---------- Shuffled Deck ----------");
        System.out.println(gameSetup.printCards());
        
        gameLogic.printPlayersAllocation();
	}
	
	/*
     * Enable or disable the category dropdown 
     */
    public void setDropdownAvailability()
    {
      if (gameLogic.getCurrentPlayer() == 0) {
        dropdownCat.setEnabled(true);
        } else {
        dropdownCat.setEnabled(false);
        }
    }

    /*
     * Start the round, check for draw, finish round if there is
     * not a draw check for game over and show current game details on the GUI.
     */
    public void initRound()
    {
    	gameLogic.startRound();
    	getPlayerValues = getPreviousRoundValues();
    	showTopCard();

        if(gameLogic.getDrawStatus()) {
        	//Break out of initRound() to commence draw
        	return;
        }

        gameLogic.finishRound();

        if (gameLogic.gameOver()) {
        	if (gameLogic.getGameWinner() == 0) {
        		JOptionPane.showMessageDialog(null, "You win the game!");
        	} else {
        		JOptionPane.showMessageDialog(null, "Game over! Winner is CPU " + gameLogic.getGameWinner());
        	}
        	Object[] options = { "Yes", "No" };
            int choice = JOptionPane.showOptionDialog(null,
                "Do you want to save the game results?", "Game Statistics",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
            if (choice == JOptionPane.YES_OPTION) {
            	gameStats.save(gameLogic.getGameWinner(), gameLogic.getNumberOfRounds(), gameLogic.getTotalDrawNumber(), gameLogic.getHumanRoundWins(), gameLogic.getCPURoundWins());
            }
            game.dispose();
            startButton.setEnabled(true);
            reportButton.setEnabled(true);
            dropdownPlayers.setEnabled(true);
        }
        showTopCard();
        showCurrentGame(getPlayerValues);
    }

    /*
     * Initiate drawLoop
     */
    public void initDraw()
	{
    	gameLogic.drawLoop();
	}
	
	/**
	 * Method that groups together all code that is needed for the
	 * execution of the 'Start Game' and 'Play Round' button.
	 * Execute initRound and check for draw.
	 */
	private void initiateStartRound()
	{
		initRound();

    	// If there is a draw then grey out some of the buttons
    	if (gameLogic.getDrawStatus()) {
    		showDrawGame();
    		startGameButton.setEnabled(false);
    		nextRoundButton.setEnabled(false);
    		continueButton.setEnabled(true);
    	} else {
    		startGameButton.setEnabled(false);
    		nextRoundButton.setEnabled(true);
    	}
    	setDropdownAvailability();
	}
	
	/**
	 * Method that groups together all code that is needed for the
	 * execution of the 'Continue Round' button.
	 * Show Draw display on GUI, execute initDraw and check for 
	 * round winner.
	 */
	private void initiateContinueRound()
	{
		showTopCard();
        initDraw();
        if (gameLogic.getDrawStatus()) {
        	showDrawGame();
        } else {
        	continueButton.setEnabled(false);
        	nextRoundButton.setEnabled(true);
        	showDrawGame();
        	gameLogic.finishRound();

        	// Logic showing which winner won the draw.
        	if (gameLogic.getRoundWinner() == 0) {
        		taGame.setText("You won the draw!\n"
                + "Please select a category and press 'Next Round' to proceed.");
        	} else {
        		taGame.setText("CPU " + gameLogic.getRoundWinner() + " won the draw. Press 'Next Round' to proceed.");
        	}
        }
        setDropdownAvailability();
	}
	
    /*
     * Method handles events when a button is clicked
     */
    public void actionPerformed(ActionEvent e)
    {
    	if(e.getSource() == startButton) {
    		initiatePlayGame();
    	}

    	else if(e.getSource() == reportButton) {
    		new ReportGUI();
    	}
      
    	else if(e.getSource() == startGameButton) {
	    	// If human player' turn and no category selected
	    	if (!checkIfCategorySelected()) {
	    		return;
	    	}
	    	initiateStartRound();
    	}
    	
    	else if(e.getSource() == nextRoundButton) {
	    	// If human player' turn and no category selected
	      	if (!checkIfCategorySelected()) {
	      		return;
	      	}
	      	initiateStartRound();
    	}

    	else if(e.getSource() == continueButton) {
    		// If human player' turn and no category selected
    		if (!checkIfCategorySelected()) {
    			return;
    		}
    		initiateContinueRound();
    	}
    }

}
