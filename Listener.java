
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

/*
 * Eric Ma
 * 6/4/12
 * This class is used by the GUI class to act on the MouseEvents and ActionEvents
 * The ActionListener and the MouseListener interfaces are implemented
 * The class contains/overrides the actionPerformed method of the ActionListener interface
 * 		and also all 5 methods of the MouseListener Interface
 * The actionPerformed method acts upon the restart button, menu items, and timer
 * The mouseReleased method performs the actions of when the mouse is clicked by the user
 */

public class Listener implements ActionListener,MouseListener {
	/*method overridden from the ActionListener Interface
	 *interacts with the restart button, each of the 5 menu items, and the timer
	 *all 7 items share the same Listener so they are distinguished using getActionCommand and getSource
	 */
    public void actionPerformed(ActionEvent e) {
    	//if the restart button is pressed, the board is reset and
    	//the game restarts by setting the boolean isFirstClick to true, which will call the playGame method
    	if(e.getActionCommand().equals("restart")){
	   		GUI.resetBoard();
	   		GUI.setIsFirstClick(true);
    	}
    	//the JMenuBar is accessed from the GUI class to see if a JMenuItem has been clicked 
    	JMenuBar menuBar = GUI.getMenuBar();
    	//if the newGame JMenuItem in the GUI class has been clicked, 
    	//the board is reset and the isFirstClick variable in GUI is set to true
	   	if(e.getSource() == menuBar.getMenu(0).getItem(0)){
	   		GUI.resetBoard();
	   		GUI.setIsFirstClick(true);
	   	}
	   	//if the setDifficulty JMenuItem in the GUI class has been clicked,
	   	//the difficulty JOptionPane is shown using the difficultyOptionPaneMaker method in GUI
	   	else if(e.getSource() == menuBar.getMenu(0).getItem(1)){
	   		GUI.difficultyOptionPaneMaker();
	    	}
	   	//if the setColor JMenuItem in the GUI class has been clicked,
	   	//the color JOptionPane is shown using the colorOptionPaneMaker method in GUI
	   	else if(e.getSource() == menuBar.getMenu(0).getItem(2)){
	   		GUI.colorOptionPaneMaker();
	   	}
	   	//if the exit JMenuItem in the GUI class is clicked, the program ends
    	else if(e.getSource() == menuBar.getMenu(0).getItem(3)){
    		System.exit(0);
    	}
	   	//if the rules JMenuItem in the GUI class has been clicked,
	   	//a new JOptionPane is shown with the basic rules on MineSweeper displayed
    	else if(e.getSource() == menuBar.getMenu(1).getItem(0)){
   			JOptionPane.showMessageDialog(GUI.getFrame(), "The rules in Minesweeper are simple: \n" +
				"Uncover a mine, and the game ends. \n" +
				"Uncover an empty square, and you keep playing. \n" +
				"Uncover a number, and it tells you how many mines lay hidden in the eight surrounding squares.", 
				"Rules", JOptionPane.INFORMATION_MESSAGE);
    	}
	   	//every second, the timer calls the actionPerformed method
	   	//the timeCount variable in the GUI class is incremented and the JLabel is updated with the timeCount
   		else if(e.getActionCommand().equals("timer")){
   			//the timer stops at 999 so that the user doesn't feel too ashamed
   			if(GUI.getTimeCount() == 999){
   				GUI.getTimer().stop();
   				return;
   			}
   			GUI.setTimeCount(GUI.getTimeCount() + 1);
   			GUI.getTimerLabel().setText(GUI.getTimeCount() + " Seconds");
    	}
    }
    //the following 4 methods have to be overridden but are empty
	public void mouseClicked(MouseEvent e) {		
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	}
	/*method overridden from the MouseEvents interface
	 *when the user releases the mouse, this method calculates which cell the user clicked in
	 *	if the cell is opened and the cell is surrounded by the correct number of bombs, 
	 *		the user can double click which uses the getModifiersEx method
	 *	if the cell is unopened, the double click will not do anything
	 *		the user can left click the spot to open it or right click the spot to flag/unflag it
	 */
	public void mouseReleased(MouseEvent e) {
		int lengthColumn = 23;
		int lengthRow = 23;
		//the mouseX and mouseY variables were off
		int mouseX = e.getX() - 4;
		int mouseY = e.getY() - 51;
		for(int col = 0; col < GUI.getGame().getBoard().getCols(); col++){
			for(int row = 0; row < GUI.getGame().getBoard().getRows(); row++){
				//calculates if the mouse click was in a spot, and if it was, which cell is at that spot
				if(mouseX >= col * lengthColumn && mouseX < (col * lengthColumn) + lengthColumn &&
					mouseY >= row * lengthRow && mouseY < (row * lengthRow) + lengthRow){
					Cell click = GUI.getGame().getBoard().getCell(row,col);
					//if the cell is already opened, the only thing the user can possibly do it double click
					if(click.isOpened()){
						//the if statement checks if the user double clicked
						if(e.getModifiersEx() == 1024 || e.getModifiersEx() == 4096){
							int flaggedNeighbors = 0;
							//the number of flags around the cell double clicked is counted
							for(int i = row-1; i <= row + 1; i ++){
								for(int j = col - 1; j <= col + 1; j++){
									if(GUI.getGame().getBoard().isInBounds(i,j)){
										if(GUI.getGame().getBoard().getCell(i,j).isFlagged()){
											flaggedNeighbors ++;
										}
									}
								}
							}
							//the the number of flags surrounding the cell match the cell's value,
							//the revealNeighbors method in GUI is called on the spot of the grid
							if(flaggedNeighbors == click.getValue()){
								GUI.revealNeighbors(row, col);
							}
						}
					}
					//if the cell clicked is unopened, the cell cannot be double clicked
					//this checks if a cell has been left clicked or right clicked
					else if(!click.isOpened()){
						//if the unopened cell was double clicked the method ends
						if(e.getModifiersEx() == 1024 || e.getModifiersEx() == 4096){
							return;
						}		
						//if the cell is left clicked, the cell is opened if it is not flagged
						//if the cell is the first click of the game, the startGame method in GUI is called
						else if(e.getButton() == MouseEvent.BUTTON1){
							if(click.isFlagged()){
								return;
							}
							if(GUI.getIsFirstClick()){
								GUI.startGame(row,col);
							}
							GUI.revealSpot(row,col);
						}
						//if the cell is right clicked, the cell is either flagged or unflagged
						//the number of flags is either incremented or decremented 
						//the JLabel that tells the number of bombs remaining is updated using the bombNumber
						else if(e.getButton() == MouseEvent.BUTTON3){
							//if the cell is already flagged, it will be unflagged so the bombNumber is incremented
							if(click.isFlagged()){
								GUI.setBombNumber(GUI.getBombNumber()+1);
							}
							//if the cell is not flagged, it will be flagged so the bombNumber is decremented
							else{
								GUI.setBombNumber(GUI.getBombNumber()-1);
							}
							GUI.getBombNumberLabel().setText(GUI.getBombNumber() + " Bombs");
							//the cell's isFlagged variable is set to the opposite value
							click.setFlagged(!click.isFlagged());
						}
					}
				}
			}
		}
	}
}
