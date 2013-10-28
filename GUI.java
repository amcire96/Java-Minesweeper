import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.sound.sampled.Clip;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/*
 * Eric Ma
 * 6/5/12
 * This class's purpose is to create a graphical user interface for to Minesweeper game
 * The class contains a MineSweeper game, a Color for the unopened spots,
 * 	Images for each number, bombs, flags, and false flags, 
 * 	ints for the timer counter, number of bombs, and frame height and width,
 * 	booleans telling whether the game is over not not and if the click was the first of the game, 
 * 	a JFrame which contains a JPanel which contains JLabels and a JButton,
 * 		a Timer, a Listener, and a JMenuBar which contains JMenus and JMenuItems
 * The class contains the following methods:
 * 	getters and setters for the private class data, a resetBoard method which restores the original board
 * 	startGame method which receives the row and col ints for the first click and sends it to the game's playGame method
 * 	endGame method which acts if user wins or loses the game
 * 	loseGame method which plays the bomb sound and calls the loseGameMessage which shows the losing message
 * 	hasWon method which tells if the user won. winGame which plays the win sound and calls winGameMessage which shows the win message
 * 	revealSpot which reveals a Cell and checks if the game is won or lost. if the Cell's value is 0, it calls revealNeighbors
 * 	revealNeighbors calls revealSpot around all valid neighbors of the location sent to the method
 * 	paintComponent checks the booleans and value of each Cell and paints the corresponding color/image
 * 	colorOptionPane and difficultyOptionPane give options for selecting the color and difficulty
 * 	frameMaker instantiates the frame, boardMaker makes an instance of this class and adds it to the frame
 * 	menuMaker adds JMenus to the JMenuBar and adds JMenuItems to the JMenus. the the frame's JMenuBar is set to the JMenuBar
 * 	panelMaker instantiates the JPanel and adds on the JLabels and the JButton
 * 	timerLabelMaker and bombNumberLabelMaker instantiates the JLabels which are added to the panel
 * 	initializeTimer sets the Timer to go off every second and adds on the Listener
 * 	play calls all the methods that are needed to play the game
 */
public class GUI extends JComponent{
	//see class comments for class private data
	private static JPanel labelPanel;
	private static JLabel bombNumberLabel;
	private static JLabel timerLabel;
	private static Timer timer;
	private static Listener listener = new Listener();
	private static MineSweeper game;
	private static JFrame frame;
	private static JMenuBar menuBar;
	private static JButton restart;
	private static boolean isFirstClick = true;
	private Image zero = LoadImage.getImg(0);
	private Image one = LoadImage.getImg(1);
	private Image two = LoadImage.getImg(2);;
	private Image three = LoadImage.getImg(3);;
	private Image four = LoadImage.getImg(4);;
	private Image five = LoadImage.getImg(5);;
	private Image six = LoadImage.getImg(6);;
	private Image seven = LoadImage.getImg(7);;
	private Image eight = LoadImage.getImg(8);;
	private Image falseFlagImage = LoadImage.getFalseFlagImage();
	private Image bombImage = LoadImage.getBombImage();
	private Image flagImage = LoadImage.getFlagImage();
	private static Color color;
	private static int timeCount;
	private static int bombNumber;
	private static int frameWidth;
	private static int frameHeight;
	private static boolean gameOver = false;
	
	//getters and setters for the class data
	public static JLabel getTimerLabel() {
		return timerLabel;
	}
	public static int getBombNumber() {
		return bombNumber;
	}
	public static int getTimeCount(){
		return timeCount;
	}
	public static JLabel getBombNumberLabel() {
		return bombNumberLabel;
	}
	public static boolean getIsFirstClick(){
		return isFirstClick;
	}
	public static Timer getTimer(){
		return timer;
	}
	public static JMenuBar getMenuBar(){
		return menuBar;
	}
	public static MineSweeper getGame(){
		return game;
	}
	public static JFrame getFrame(){
		return frame;
	}
	public static void setBombNumber(int bombNumber) {
		GUI.bombNumber = bombNumber;
	}

	public static void setTimeCount(int newTimeCount){
		timeCount = newTimeCount;
	}

	public static void setIsFirstClick(boolean b){
		isFirstClick = b;
	}

	public static void setFrame(JFrame newFrame){
		frame = newFrame;
	}

	public void setGame(MineSweeper newGame){
		game = newGame;
	}

	/*
	 * resets the frame size to the updated width and height dimensions
	 * all the Cells' booleans are set to false which are the default
	 * a mouseListener is added on to the frame if the frame doesn't have one yet
	 * the bombNumberLabel is set, the timer stops its label is updated, and the timer count is reset
	 */
	public static void resetBoard(){
		frame.setSize(frameWidth, frameHeight);
		Board gameBoard = game.getBoard();
		for(int i = 0; i < gameBoard.getRows(); i ++){
			for(int j = 0; j < gameBoard.getCols(); j++){
				Cell current = gameBoard.getCell(i, j);
				current.setOpened(false);
				current.setFlagged(false);
				current.setHasFalseFlag(false);
				current.setHasBomb(false);
			}
		}
		if(frame.getMouseListeners().length == 0){
			frame.addMouseListener(listener);
		}
		bombNumber = game.getBombNumber();
		bombNumberLabel.setText(bombNumber + " Bombs");
		timer.stop();
		timeCount = 0;
		timerLabel.setText(0 + " Seconds");
	}
	
	/*
	 * gets the 2 ints for the location of the first click
	 * calls the playGame and sends it the location of the first click
	 * timer is restart and the isFirstClick variable is set to false
	 */
	public static void startGame(int j, int i){
		gameOver = false;
		getGame().playGame(j,i);
		timer.restart();
		setIsFirstClick(false);
	}
	
	/*
	 * opens all the unflagged bombs and sets if the Cell has been falsely flagged
	 * stops the timer and removes the MouseListener and sets gameOver to true
	 */
	public static void endGame(){
		Board gameBoard = game.getBoard();
		//goes through all the spots in the board
		for(int i = 0; i < gameBoard.getRows(); i ++){
			for(int j = 0; j < gameBoard.getCols(); j++){
				if(gameBoard.getCell(i,j).getHasBomb() && !gameBoard.getCell(i,j).isFlagged()){
					gameBoard.getCell(i,j).setOpened(true);
				}
				else if(!gameBoard.getCell(i,j).getHasBomb() && gameBoard.getCell(i,j).isFlagged()){
					gameBoard.getCell(i,j).setHasFalseFlag(true);
				}
			}
		}
		timer.stop();
		frame.removeMouseListener(listener);
		gameOver = true;
	}
	
	/*
	 * acts when game is lost
	 * plays the exploding clip, ends the game, and shows the lose message
	 */
	public static void loseGame(){
		Clip explosion = SoundEffect.getExplosionClip();
		SoundEffect.play(explosion);
		endGame();
		loseGameMessage();
	}
	
	/*
	 * shows a JOptionPane telling the user they lost
	 * also gives user options whether to play again or not
	 * "yes" restarts the game and "no" ends the program
	 */
	public static void loseGameMessage(){
		String[] playAgain = {"Yes", "No"};
		int choiceNumber =  JOptionPane.showOptionDialog(frame, "You Lost \n" +
				"Time In Seconds: " + timeCount + "\n" +
				"Bombs Remaining: " + bombNumber + "\n" +
				"Play Again?", "YOU LOSE!",
				 JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, playAgain, "");
		if(choiceNumber == 0){
			resetBoard();
			setIsFirstClick(true);
		}
		if(choiceNumber == 1){
			System.exit(0);
		}
	}
	
	/*
	 * returns boolean if the game has been won
	 * if any Cell is unopened that does not have a bomb, false is returned
	 */
	public static boolean hasWon(){
		for(int row = 0; row < game.getBoard().getRows(); row++){
			for(int col = 0; col < game.getBoard().getCols(); col++){
				Cell cell = game.getBoard().getCell(row,col);
				if(!cell.getHasBomb() && !cell.isOpened()){
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * acts when game is won
	 * plays the win clip, ends the game, and shows the win message
	 */
	public static void winGame(){
		Clip clip = SoundEffect.getWinClip();
		SoundEffect.play(clip);
		endGame();
		winMessage();
	}

	/*
	 * shows a JOptionPane telling the user they won
	 * also gives user options whether to play again or not
	 * "yes" restarts the game and "no" ends the program
	 */	
	public static void winMessage(){
		String[] playAgain = {"Yes", "No"};
		int choiceNumber =  JOptionPane.showOptionDialog(frame, "Congrats You Win! Play Again? \n" +
				"Time: " + timeCount + " Seconds", "YOU WIN!",
				 JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, playAgain, "");
		if(choiceNumber == 0){
			resetBoard();
			setIsFirstClick(true);
		}
		if(choiceNumber == 1){
			System.exit(0);
		}
	}
	
	/*
	 * Receives the row and col number of the spot whose neighbors are to be opened
	 * Goes through the spots neighbors and if the neighbor is in bounds and the game isn't over,
	 * 	if the cell is unflagged then the spot is revealed and sent to the revealSpot method
	 */
	public static void revealNeighbors(int row, int col){
		if(!game.getBoard().isInBounds(row,col)){
			return;
		}
			for(int i = row-1; i <= row + 1; i ++){
				for(int j = col - 1; j <= col + 1; j++){
					if(game.getBoard().isInBounds(i,j) && !gameOver){
						if(!game.getBoard().getCell(i, j).isFlagged()){
							revealSpot(i,j);
						}
					}
				}
			}
		
	}
	
	/*
	 * Receives the row and col number of the spot which is to be opened
	 * if the spot is out of bounds, opened already, or flagged, the method does nothing
	 * sets the isOpened variable of the Cell in that spot to true
	 * checks if the game is won or lost when revealing the spot
	 * if the Cell's value is 0, then the row and col ints are sent to the revealNeighbors method
	 */
	public static void revealSpot(int row, int col){
		if(!game.getBoard().isInBounds(row,col) || game.getBoard().getCell(row,col).isOpened()
				|| game.getBoard().getCell(row,col).isFlagged()){
			return;
		}
		Cell current = game.getBoard().getCell(row,col);
		current.setOpened(true);

		if(current.getHasBomb()){
			loseGame();
		}
		if(hasWon()){
			winGame();
		}		
		
		
		if(current.getValue() == 0){
			revealNeighbors(row,col);
		}

	}
	
	/*
	 * Paints all the Images and Rectangles and fills in the Rectangles accordingly
	 * First, Rectangles 23 by 23 are made and filled in with the color class variable
	 * If the Cell at that spot is flagged, the class flagImage is placed where the Rectangle was
	 * If the Cell was falsely flagged, the class falseFlagImage is placed where the Rectangle was
	 * If the Cell is opened and has a bomb, the class bombImage is placed where the Rectangle was
	 * 	if not, the value of the Cell determines which class number image is placed in that spot
	 * Then the Rectangles are drawn in black which are the borders of the Cells
	 */
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		for(int row = 0; row < game.getBoard().getRows(); row++){
			for(int col = 0; col < game.getBoard().getCols(); col++){
				Rectangle rec = new Rectangle(col *23,row*23,23,23);
				g2.setColor(color);
				//each spot is filled originally with the color variable of the class
				g2.fill(rec);
				//draws flagImage if Cell is flagged
				if(game.getBoard().getCell(row,col).isFlagged()){
					g2.drawImage(flagImage, col*23,row*23,23,23,null);
				}
				//draws falseFlagImage is Cell is falsely flagged
				if(game.getBoard().getCell(row,col).isHasFalseFlag()){
					g2.drawImage(falseFlagImage, col*23,row*23,23,23,null);
				}
				else if(game.getBoard().getCell(row,col).isOpened()){
					//if the Cell is opened and has a bomb, the bombImage is drawn
					if(game.getBoard().getCell(row, col).getHasBomb()){
						g2.drawImage(bombImage, col*23,row*23,23,23,null);
					}
					else{
						//gets the value of the cell and each case shows which number image to draw
						switch(game.getBoard().getCell(row,col).getValue()){
							case 0: {
								g2.drawImage(zero, col*23,row*23,23,23,null);
								break;
							}
							case 1: {
								g2.drawImage(one, col*23,row*23,23,23,null);
								break;
							}
							case 2: {
								g2.drawImage(two, col*23,row*23,23,23,null);
								break;
							}
							case 3: {
								g2.drawImage(three, col*23,row*23,23,23,null);
								break;
							}
							case 4: {
								g2.drawImage(four, col*23,row*23,23,23,null);
								break;
							}
							case 5: {
								g2.drawImage(five, col*23,row*23,23,23,null);
								break;
							}
							case 6: {
								g2.drawImage(six, col*23,row*23,23,23,null);
								break;
							}
							case 7: {
								g2.drawImage(seven, col*23,row*23,23,23,null);
								break;
							}
							case 8: {
								g2.drawImage(eight, col*23,row*23,23,23,null);
								break;
							}
						}
					}

				}
				//each Rectangle is drawn in black for the borders
				g2.setColor(Color.black);
				g2.draw(rec);
				repaint();
			}
		}
	}
	
	/*
	 * Opens JOptionPane which gives you 6 options for changing the color of the original squares
	 * Reads which option is chosen and sets the color class variable to a corresponding Color
	 * If no color is chosen (JOptionPane is 'x'ed out) then the color will be set to red 
	 */
	public static void colorOptionPaneMaker(){
		String[] colors = {"Red", "Blue", "Green", "Purple", "Orange", "Pink"};
		int choiceNumber = JOptionPane.showOptionDialog(frame, "Please Select A Color" , "Colors"
				, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, colors, "");
		if(choiceNumber == 0){ 
			color = new Color(238,0,0);
		}
		else if(choiceNumber == 1){
			color = new Color(77,77,255);
		}
		else if(choiceNumber == 2){
			color = new Color(0,238,0);
		}
		else if(choiceNumber == 3){
			color = new Color(209,95,238);
		}
		else if(choiceNumber == 4){
			color = new Color(255,127,36);
		}
		else if(choiceNumber == 5){
			color = new Color(255,181,197);
		}
		//if the JOptionPanel is exited without selecting an option
		else{
			if(color == null){
				color = new Color(238,0,0);
			}
				
		}
	}
	
	/*
	 * Opens JOptionPane which gives you the 3 options for the difficulty of the game
	 * Sets class game variable to a new MineSweeper game with corresponding number
	 * 	of rows, columns, and bombs depending on which difficulty is selected
	 * Also sets the frame to a specified width and height depending on the difficulty
	 * If no difficulty is chosen (JOptionPane is 'x'ed out) then the difficultly will be set to beginner
	 * isFirstClick class variable is set to true
	 * Also the bombNumberLabel is made
	 */
	public static void difficultyOptionPaneMaker(){
		String[] difficulties = {"Beginner", "Intermediate", "Expert"};
		int choiceNumber = JOptionPane.showOptionDialog(frame, "Please Select A Difficulty" , "Difficulty"
				, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, difficulties, "");
		if(choiceNumber == 0){ 
			frameWidth = 224;
			frameHeight = 306;
			//if the game is not null, the board will be reset
			if(game != null){
				resetBoard();
			}
			//beginner has 9 rows, 9 columns, and 10 bombs
			game = new MineSweeper(9,9,10);
			game.setBombNumber(10);
			bombNumberLabelMaker();
			setIsFirstClick(true);
		}
		else if(choiceNumber == 1){
			frameWidth = 385;
			frameHeight = 466;
			//if the game is not null, the board will be reset
			if(game != null){
				resetBoard();
			}
			//intermediate has 16 rows, 16 columns, and 40 bombs
			game = new MineSweeper(16,16,40);
			game.setBombNumber(40);
			bombNumberLabelMaker();
			setIsFirstClick(true);
		}
		else if(choiceNumber == 2){
			frameWidth = 707;
			frameHeight = 468;
			//if the game is not null, the board will be reset
			if(game != null){
				resetBoard();
			}
			//expert has 16 rows, 30 columns, and 99 bombs
			game = new MineSweeper(16,30,99);
			game.setBombNumber(99);
			bombNumberLabelMaker();
			setIsFirstClick(true);
		}
		//if the JOptionPanel is exited without selecting an option
		else{
			//if the game is null, the difficulty is beginner
			if(game == null){
				game = new MineSweeper(9,9,10);
				game.setBombNumber(10);
				bombNumberLabelMaker();
				frameWidth = 224;
				frameHeight = 306;				
			}
			//if there is already a game ongoing, nothing will happen
		}
	}
	
	/*
	 * Instantiates the JFrame, makes it non-resizable, sets the title, and adds the MouseListener
	 */
	public static void frameMaker(){
		frame = new JFrame();
		frame.setResizable(false);
		frame.setTitle("MineSweeper");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addMouseListener(listener);
	}
	
	/*
	 * Calls the class default constructor, adds it to the frame, resizes the frame,
	 * 	sets the frame visible and makes it non-resizable
	 */
	public static void boardMaker(){
		GUI component = new GUI();
		frame.add(component);
		frame.setSize(frameWidth, frameHeight);
		frame.setVisible(true);
		frame.setResizable(false);
	}
	
	/*
	 * Makes a JMenuBar with 2 JMenus. The 1st JMenu has 4 JMenuItems and the 2nd has 1 JMenuItem.
	 * The 1st JMenu is called game and it has JMenuItems:
	 * 		the newGame JMenuItem restarts the game
	 * 		the setDifficulty JMenuItem calls the difficulty JOptionPane method
	 * 		the setColor JMenuItem calls the color JOptionPane method
	 * 		the exit JMenuItem exits the program
	 * The 2nd JMenu is called help and it has a help JMenuItem which shows a JOptionPane with the rules
	 * a Listener is added to each JMenuItem
	 */
	public static void menuMaker(){
		menuBar = new JMenuBar();
		JMenu game = new JMenu("Game");
		menuBar.add(game);
		JMenu help = new JMenu("Help");
		menuBar.add(help);
		frame.setJMenuBar(menuBar);
		
		JMenuItem newGame = new JMenuItem("New Game", null);
		newGame.addActionListener(listener);
		game.add(newGame);
		
		JMenuItem setDifficulty = new JMenuItem("Set Difficulty", null);
		setDifficulty.addActionListener(listener);
		game.add(setDifficulty);
		
		JMenuItem setColor = new JMenuItem("Set Color", null);
		setColor.addActionListener(listener);
		game.add(setColor);
		
		JMenuItem exit = new JMenuItem("Exit", null);
		exit.addActionListener(listener);
		game.add(exit);
		
		JMenuItem rules = new JMenuItem("Rules", null);
		rules.addActionListener(listener);
		help.add(rules);
	}
	/*
	 * The labelPanel variable is made. 
	 * The timer label is made and added with the timerLabelMaker method 
	 * The restart button is made and added to the frame. A Listener is added to the button.
	 */
	public static void panelMaker(){
		labelPanel = new JPanel();
		timerLabelMaker();
		restart = new JButton("Restart");
		restart.setActionCommand("restart");
		restart.addActionListener(listener);
		labelPanel.add(restart, BorderLayout.CENTER);
		frame.add(labelPanel, BorderLayout.SOUTH);
	}

	/*
	 * Makes the timerLabel by using the timeCount variable in the class.
	 * The timerLabel is then added to the JPanel
	 */
	public static void timerLabelMaker(){
		timerLabel = new JLabel(timeCount + " Seconds");
		labelPanel.add(timerLabel, BorderLayout.EAST);
	}
	
	/*
	 * Makes the bombNumberLabel by getting the number of bombs in the game.
	 * The timerLabel is then added to the JPanel.
	 */
	public static void bombNumberLabelMaker(){
		bombNumber = game.getBombNumber();
		if(bombNumberLabel != null){
			bombNumberLabel.setText(bombNumber + " Bombs");
			return;
		}
		bombNumberLabel = new JLabel(bombNumber + " Bombs");
		labelPanel.add(bombNumberLabel, BorderLayout.EAST);
	}
	
	/*
	 * This method initializes the timer.
	 * The timer calls actionPerformed method in Listener class every second.
	 */
	public static void initializeTimer(){
		timer = new Timer(1000,listener);
		timer.setActionCommand("timer");
	}
	
	/*
	 * This method is called to start and play the game
	 * Uses other methods to make the JFrame, make the two JOptionPanes appear,
	 * 	add the JPanel to the JFrame, add the menu to the JFrame, add the board to the JFrame,
	 * 	and initialize the Timer
	 */
	public static void play(){
		frameMaker();panelMaker();
		difficultyOptionPaneMaker();
		colorOptionPaneMaker();
		
		menuMaker();
		boardMaker();	
		initializeTimer();		
	}

	public static void main(String [] args){
		//play the game
		play();
	}
}
