/*
 * Eric Ma
 * 6/3/12
 * This class represents each individual spot on the board of the game.
 * Each cell has an int value, and booleans whether it has a bomb, 
 * is opened, is flagged, or is falsely flagged.
 * Has 3 constructors and getters and setters for the class data.
 */
public class Cell{
	//class data includes an int value, and booleans whether it has a 
	//bomb, is opened, is flagged, or is falsely flagged
	private boolean hasBomb;
	private boolean isOpened;
	private boolean isFlagged;
	private boolean hasFalseFlag;
	private int value;
	
	//default constructor: all booleans are set to false and the value is not initialized
	public Cell(){
		hasBomb = false;
		isOpened = false;
		isFlagged = false;
		hasFalseFlag = false;
	}
	
	//takes in a boolean to set to the hasBomb boolean
	//all other booleans are set to false and the value is not initialized
	public Cell(boolean hasBomb){
		this.hasBomb = hasBomb;
		isOpened = false;
		isFlagged = false;
		hasFalseFlag = false;
	}
	
	//takes in an int and sets the value variable to that int
	//all other booleans are set to false
	public Cell(int value){
		this.value = value;
		hasBomb = false;
		isOpened = false;
		isFlagged = false;
		hasFalseFlag = false;
	}
	
	//getters and setters for each of the five class variables
	public void setHasFalseFlag(boolean hasFalseFlag) {
		this.hasFalseFlag = hasFalseFlag;
	}

	public void setOpened(boolean isOpened) {
		this.isOpened = isOpened;
	}

	public void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
	}

	public void setHasBomb(boolean hasBomb){
		this.hasBomb = hasBomb;
	}
	
	public void setValue(int newValue){
		value = newValue;
	}
	
	public boolean getHasBomb(){
		return hasBomb;
	}
	
	public boolean isOpened() {
		return isOpened;
	}

	public boolean isFlagged() {
		return isFlagged;
	}

	public boolean isHasFalseFlag() {
		return hasFalseFlag;
	}

	public int getValue(){
		return value;
	}
}
