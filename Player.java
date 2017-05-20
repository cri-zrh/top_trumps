
/*
 * Handles the functioning of indiviual players
 */
public class Player {
	private Card[] pCards;
	private int score;
	private  int noCards;
	private boolean isHuman = false;
	public boolean humanWin = true;
	
	
	public Player(){

	}
	
	public Player(Card[] c){
		pCards = c;		
	}
	
	/*
	 * Method to return an int representing a category value of a given category.
	 * Takes in an integer which represents the index of the category (see 
	 * attribute "values" in card class).
	 */
	public int selectCategoryValue(int categoryIndex)
	{
		int categoryValue;
		
		Card card = this.getTopmostCard();
		categoryValue = card.getValues()[categoryIndex];
		
//		System.out.println("" + categoryValue);
		return categoryValue;
	}
	
	/*
	 * Series of getter and setter methods for instance variables.
	 */
	public Card getTopmostCard()
	{
		return pCards[0];
	}
	
	public Card getTopCardAttribute()
	{
		return pCards[0];
	}

	public Card[] getpInitialCards() {
		return pCards;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getNoCards() {
		noCards = pCards.length;
		return noCards;
	}

	public void setHuman()
	{
		isHuman = true;
	}
	public boolean isHuman()
	{
		return isHuman;
	}
	
	/*
	 * Adds a given set of cards to the player's deck
	 */
	public void addWinningCards(Card[] cArray, int arrayLength)
	{	
		// Transfer all pCards to temporary array
		Card[] tempCards = new Card[pCards.length];
		for (int i = 0; i < pCards.length; i++) {
			tempCards[i] = pCards[i];
		}
		// Create new pCards array with the new array length that includes redistributed cards
		pCards = new Card[arrayLength];
		for (int i = 0; i < tempCards.length; i++) {
			pCards[i] = tempCards[i];
		}
		int index = 0;
		for (int i = tempCards.length; i < pCards.length; i++) {
			pCards[i] = cArray[index++];
		}
	}
	
	/*
	 * Takes off all top cards from player
	 */
	public void removeTopCards(){
		Card[] tempCards = new Card[pCards.length-1];
		for (int i = 1; i < pCards.length; i++) {
			tempCards[i-1] = pCards[i];
		}
		pCards = new Card[tempCards.length];
		for (int i = 0; i < pCards.length; i++) {
			pCards[i] = tempCards[i];
		}
	}
	
}
