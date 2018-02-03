/**
 *  
 */
package snake;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.swing.Timer;


/**
 * @author Piotr S³ysz
 * Game - controller for the game, provides communication between the model and GUI
 */
public class Game  implements ActionListener, KeyListener{

	private int move;
	private int delay;
	private int ticksCounter;
	private Timer timer;

	public enum State { MAIN_MENU, PLAYING, GAME_OVER, HIGHSCORE};
	public State state;
	public SnakeView mainView;
	public Board board;
	
	public static final int MAX_NICK_LEN = 20; // maximum length of players nick
	public static final String HIGHSCORE_FNAME= "highscore.txt";
	
	
	//MAIN
	public static void main(String[] args) {
		//create new game instance and display main menu
		Game game = new Game();
		game.mainView.showMainMenu();
	}
	
	/**
	 * Default constructor creates new game instance along with Board and SnakeView
	 */
	public Game() {
		state = State.MAIN_MENU;
		board = new Board(this);
		mainView = new SnakeView(this);
		mainView.addKeyListener(this);
	}
	
	@Override
	/**
	 * Whenever key is pressed potential move is interpreted by model
	 */
	public void keyPressed(KeyEvent event) {
		int keyCode = event.getKeyCode();
		if( keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) {
			if( move != Board.GO_DOWN )
				move = Board.GO_UP;
		}
		else if( keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) {
			if( move != Board.GO_UP )
				move = Board.GO_DOWN;
		}
		else if( keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) {
			if( move != Board.GO_LEFT )
				move = Board.GO_RIGHT;
		}
		else if( keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) {
			if( move != Board.GO_RIGHT )
				move = Board.GO_LEFT;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {	
	}

	@Override
	/**
	 *  On Timer ticks manages content displayed by GUI
	 */
	public void actionPerformed(ActionEvent arg0) {
		if( this.state == State.PLAYING ) {
			mainView.repaintGamePanel();
			ticksCounter++;
			if(ticksCounter % delay == 0) {
				ticksCounter = 0;
				board.doMove(move);
				
				if(board.gameOver()) {
					this.timer.stop();
					state = State.GAME_OVER;
					mainView.showGameOverScreen();
				}
			}
		}
		else if ( this.state == State.MAIN_MENU ) {
			this.mainView.showMainMenu();
		}
		else if( this.state == State.GAME_OVER ) {
			this.mainView.showGameOverScreen();
		}
		else if( this.state == State.HIGHSCORE ) {
			this.mainView.showHighscore();
		}
	}

	/**
	 * @return
	 */
	public Board getBoard() {
		return this.board;
	}

	/**
	 * @param s - state value
	 */
	public void setState(State s) {
		this.state = s;
	}

	/**
	 *  Starts new game
	 */
	public void startGame() {
		board = new Board(this);
		move = Board.GO_UP; // initial snake direction
		timer = new Timer(20, this);
		ticksCounter = 0;
		delay = 7;
		state = State.PLAYING;
		mainView.showBoard();
		timer.start();
	}

	/**
	 * Manage delay value, called whenever apple is eaten
	 */
	public void manageDelay() {
		int score = this.score();
		if(score >= 300) {
			delay = 1;
			return;
		}
		if(score >= 170) {
			delay = 2;
			return;
		}
		if(score >= 140) {
			delay = 3;
			return;
		}
		if(score >= 110) {
			delay = 4;
			return;
		}
		if(score >= 80) {
			delay = 5;
			return;
		}
		if(score >= 50) { 
			delay = 6;
			return;
		}
	}

	/**
	 * @return Current score which is snakeSize * 10 - 10*intialSnakeSize
	 */
	public int score() {
		return (board.getSnakeSize() * 10) - 30;
	}

	/**
	 * @return Data read from highscore file as sorted Highscore array
	 */
	public Highscore[] getSortedHighscores() {
	
		int scoresRead = 10;
		Highscore[] highscores = new Highscore[scoresRead];
		Path path = Paths.get("", "resources", Game.HIGHSCORE_FNAME);
		String sPath = path.toAbsolutePath().toString();

		BufferedReader reader = null;
		try {
			File file = new File(sPath);
			reader = new BufferedReader( new FileReader(file));
			for(int i = 0; i < 10; i++) { // read 10 lines from file
				String line;
				if( (line = reader.readLine()) ==  null)
					break;
				String name = line.substring(0, Game.MAX_NICK_LEN - 1);
				String scoreStr = line.substring(Game.MAX_NICK_LEN).trim();
				highscores[i] = new Highscore(name,  Integer.valueOf(scoreStr));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				reader.close();
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		Arrays.sort(highscores, new Highscore());
		return highscores;
	}

	/**
	 * @param playerName -  name read from snakeView  
	 * @param score - players score
	 * Called when game end, score is checked against current highscores and remembered if it should make it to the list
	 */
	public void checkHighscore(String playerName, int score) {
		
		Highscore[] hs = this.getSortedHighscores();
		if(score <= hs[ hs.length-1 ].score)
			return; // player doesnt make it to highscore table
		
		hs[hs.length - 1] = new Highscore(playerName, score);
		// Write new highcores to file
		Path path = Paths.get("", "resources", Game.HIGHSCORE_FNAME);
		String sPath = path.toAbsolutePath().toString();
		File fold=new File(sPath);
		fold.delete();
		File fnew=new File(sPath);
		try {
		    FileWriter f2 = new FileWriter(fnew, false);
		    for(int i = 0; i < hs.length; i++) {
		    	String name = hs[i].name;
		    	if(name.length() >= Game.MAX_NICK_LEN) {
					name = name.substring(0, Game.MAX_NICK_LEN-1);
				}
				else {
					int fillLen = Game.MAX_NICK_LEN + 10;
					char[] fill = new char[fillLen];
					Arrays.fill(fill , ' ');
					name += new String(fill);
				}
		    	f2.write(name + hs[i].score.toString() + "\n");
		    }
		    f2.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}           
	}
	
}
