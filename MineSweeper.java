/*
 * Eric Ma
 * 6/3/12
 * This class represents the structural construction of the minesweeper game
 * Contains a Board and calls methods in Board to help set up the game
 * The only constructor takes in 3 ints, rows and columns of the board and the number of bombs
 * The setBombs and setOthers methods simply call the Board class's version of the method
 */
public class MineSweeper {
	//private class data is a Board that the game will be played on
	private Board board;
	
	//constructor that takes in 3 ints, rows and columns of the board and the number of bombs
	public MineSweeper(int rows,int columns,int bombNumber){
		board = new Board(rows,columns,bombNumber);
	}
	
	//getters and setters
	public int getRows(){
		return board.getRows();
	}
	
	//getter that returns the number of columns
	public int getCols(){
		return board.getCols();
	}
	
	//getter that returns the number of bombs
	public int getBombNumber(){
		return board.getBombNumber();
	}
	
	//sets bombNumber
	public void setBombNumber(int newBombNumber){
		board.setBombNumber(newBombNumber);
	}
	
	/*takes in two ints: the row and column number of the first click of the game
	 *sends the two ints to the Board class's setBombs method to set the bombs in the board 
	 */
	public void setBombs(int rowClick, int colClick){
		board.setBombs(rowClick, colClick);
	}
	
	/*sets all the other cells in the board
	 *calls on the Board class's setOthers method 
	 */
	public void setOthers(){
		board.setOthers();
	}
	
	//getter that returns the Board board
	public Board getBoard(){
		return board;
	}
	
	/*method called to play the game
	 *receives the first click's row and column's int number
	 *sends the 2 ints to the setBombs method and also calls the setOthers
	 */
	public void playGame(int firstClickRow, int firstClickCol){
		setBombs(firstClickRow,firstClickCol);
		setOthers();
	}
}
