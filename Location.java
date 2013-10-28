/*
 * Eric Ma
 * 6/3/12
 * This Location class stores two ints, a row and a column of a location or spot of a board/grid
 */
public class Location {
	//private class variables are the location's row and column ints
	private int row;
	private int column;
	//constructor takes in the location's row and column ints
	public Location(int row, int column){
		this.row = row;
		this.column = column;
	}
	
	//getter that returns the row number
	public int getRow(){
		return row;
	}
	
	//getter that returns the column number
	public int getColumn(){
		return column;
	}
	
	//returns a String representation of the location by having the row comma column
	public String toString(){
		return "(" + row + "," + column + ")";
	}
}
