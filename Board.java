import java.util.ArrayList;

/*
 * Eric Ma
 * 6/3/12
 * This class makes a two dimensional board of cells with a certain number of bombs
 * and can set the dimensions, see if a cell is on the board, set a cell value, get a cell value, 
 * get a cell at a spot on the board count number of bombs surrounding a Cell, 
 * set the bombs in the board, set the value of the other non-bomb Cells, and get the number value of a Cell
 */
public class Board {
	//private data include a 2-day array of Cells and an int keeping track of the number of bombs
	private Cell[][] board;
	private int bombNumber;
	
	//default constructor: instantiates each Cell in the board
	public Board(){
		for(int i = 0; i < board.length; i ++){
			for(int j = 0; j < board[0].length; j++){
				board[i][j] = new Cell();
			}
		}
	}

	/* constructor receiving the length and width of the board along with the number of bombs
	 * instantiates each Cell in the board
	 * board is created with the dimensions of the two ints received
	 */
	public Board(int rows, int columns, int bombNumber){
			board = new Cell[rows][columns];
			this.bombNumber = bombNumber;
			for(int i = 0; i < board.length; i ++){
				for(int j = 0; j < board[0].length; j++){
					board[i][j] = new Cell();
				}
			}
	}
	
	//setter that sets bombNumber
	public void setBombNumber(int newBombNumber){
		bombNumber = newBombNumber;
	}
	
	//getter that returns the number of bombs
	public int getBombNumber(){
		return bombNumber;
	}
	
	//getter that returns the number of rows
	public int getRows(){
		return board.length;
	}
	
	//getter that returns the number of columns
	public int getCols(){
		return board[0].length;
	}

	//receives ints x and y and returns if x,y is a valid spot on the board
	public boolean isInBounds(int x, int y){
		boolean inBounds = true;
		if(x >= board.length || y >= board[0].length){
			inBounds = false;
		}
		else if(x < 0 || y < 0){
			inBounds = false;
		}
		return inBounds;
	}

	//sets the board's dimensions to the 2 ints that it receives
	public void setDimensions(int newLength, int newWidth){
		board = new Cell[newLength][newWidth];
	}
	
	//takes in the row and col ints of a spot on the board and returns the cell of that spot
	public Cell getCell(int row, int col){
		return board[row][col];
	}

	//sets the value of Cell x,y to value
	public void setCell(int x, int y, int value){
		if(!isInBounds(x,y)){
			System.out.println(x + "," + y + " is not a coordinate");
			return;
		}
		board[x][y] = new Cell(value);
	}

	//returns the value of Cell x,y
	public Integer getCellValue(int x, int y){
		if(!isInBounds(x,y)){
			System.out.println(x + "," + y + " is not a coordinate");
			return null;
		}
		Integer value = board[x][y].getValue();
		return value;
	}

	//returns how many cells around cell x,y have a bomb
	public Integer getSurroundingBombs(int x, int y){
		Integer neighbors = 0;
		if(!isInBounds(x,y)){
			System.out.println(x + "," + y + " is not a coordinate");
			return null;
		}
		for(int i = x-1; i <= x + 1; i++){
			for(int j = y-1; j<= y + 1; j++){
				if(isInBounds(i, j)){
					if(board[i][j].getHasBomb()){
						neighbors ++;
					}
				}
			}
		}
		return neighbors;
	}
	
	/*given the first click's row and column int numbers, sets the bombs in the board
	 *creates an ArrayList of Locations of spots to put a bomb excluding the original click location
	 *when a bomb is placed in a Location, that Location is removed from the ArrayList
	 *to decide which location, for each bomb to be placed, a random Location in the ArrayList is chosen
	 *the Cell at that location has its hasBomb boolean variable set to true
	 */
	public void setBombs(int rowClick, int colClick){
		ArrayList<Location> locs = new ArrayList<Location>();
		//each spot on the board is made into a Location and added to the ArrayList
		for(int i = 0; i < board.length; i ++){
			for(int j = 0; j < board[0].length; j++){
				Location loc = new Location(i,j);
				locs.add(loc);
			}
		}
		//the Location of the first click is removed from the ArrayList
		for(int i = 0; i < locs.size(); i++){
			if(locs.get(i).getRow() == rowClick && locs.get(i).getColumn() == colClick){
				locs.remove(i);
			}
		}
		//for each bomb placed, bombs are placed at random Location in the ArrayList
		for(int i = 1; i <= bombNumber; i ++){
			int random = (int)(Math.random() * locs.size());
			Location bombLoc = locs.get(random);
			board[bombLoc.getRow()][bombLoc.getColumn()].setHasBomb(true);
			//each Location that has a bomb placed in it is removed from the ArrayList
			locs.remove(bombLoc);
		}
	}
	
	/*for each spot in the board, the Cell is assigned a value
	 *to assign value to the Cell, the getNumber method is called 
	 */
	public void setOthers(){
		for(int i = 0; i < board.length; i ++){
			for(int j = 0; j < board[0].length; j++){
				board[i][j].setValue(getNumber(i,j));
			}
		}
	}
	
	/*2 ints are given to the method: the row and col number of a spot on the board
	 *if the Cell at that spot has a bomb, 9 is returned
	 *if not the getSurroudningBombs method is called with the row and col ints given 
	 */
	public int getNumber(int row, int col){
		if(board[row][col].getHasBomb()){
			return 9;
		}
		return getSurroundingBombs(row,col);
	}

	//converts the board into a String and returns it
	public String toString(){
		String print = new String();
		for (int i = 0; i < board.length; i++) {
	 		for (int j = 0; j < board[0].length; j++) {
        	   print += board[i][j] + " ";
     		}
     	print += "\n";
		}
		return print;
	}
}