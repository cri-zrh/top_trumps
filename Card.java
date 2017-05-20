
import java.util.Scanner;

/*
 * Handles the attributes of each card
 * complete with getter and setter methods
 */
public class Card {

	// integer array to hold the values of the five categories
	private int[] values;
	// String to hold the name of the card
	private String description;
	// Int for categories
	private int category1, category2, category3, category4, category5;	
	
	/*
	 * default contructor
	 */
	public Card(){
		
	}
	
	/*
    * constructor that takes in a String and breaks it up into the description
    * and values of a card
    */
	public Card (String input)
	{
		Scanner sc = new Scanner(input);

		description = sc.next();
		category1 = Integer.parseInt(sc.next());
		category2 = Integer.parseInt(sc.next());
		category3 = Integer.parseInt(sc.next());
		category4 = Integer.parseInt(sc.next());
		category5 = Integer.parseInt(sc.next());
		sc.close();
	}

	/*
	 * Series of getter and setter methods.
	 */
	public int getCategory1() {
		return category1;
	}

	public void setCategory1(int category1) {
		this.category1 = category1;
	}


	public int getCategory2() {
		return category2;
	}

	public void setCategory2(int category2) {
		this.category2 = category2;
	}

	public int getCategory3() {
		return category3;
	}

	public void setCategory3(int category3) {
		this.category3 = category3;
	}

	public int getCategory4() {
		return category4;
	}

	public void setCategory4(int category4) {
		this.category4 = category4;
	}

	public int getCategory5() {
		return category5;
	}

	public void setCategory5(int category5) {
		this.category5 = category5;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}
	
	public int[] getValues()
	{		
		values = new int[] {getCategory1(), getCategory2(), getCategory3(), getCategory4(), getCategory5()};
		return values;
	}
	public int getValue(int x) 
	{
		return values[x];
	}
}