/**
 * 
 */
package snake;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Piotr S³ysz
 * Board - board model
 */
public class Board {
	private int width, height;
	private Snake snake;
	private Point apple;
	private Random rand;
	private Game game; // controller
	private boolean gameOver;
	
	public static final int GO_UP = 0, GO_DOWN = 1, GO_RIGHT = 2, GO_LEFT = 3;
	
	/**
	 * Deafult Constructor - default Boarad size is 860x640
	 * @param game - game instance
	 */
	public Board(Game game) {
		this(86,64, game);
	}

	/**
	 * @param w - width
	 * @param h - height
	 * @param g - game instance - controller
	 */
	public Board(int w, int h, Game g) {
		game = g;
		rand = new Random();
		width = w;
		height = h;
		snake = new Snake( (int) w / 2, (int) h / 2 );
		apple = this.randomizeApplePosition();
		gameOver = false;
	}
	
	/**
	 * 
	 * @return true if this.game is over
	 */
	public boolean gameOver() {
		return gameOver;
	}
	
	/**
	 * 
	 * @return a Snake
	 */
	public Snake getSnake() {
		return this.snake;
	}
	
	/**
	 * 
	 * @return Snakes size
	 */
	public int getSnakeSize() {
		return this.snake.getSize();
	}
	
	/**
	 * 
	 * @return Apple position -  a Point
	 */
	public Point getApple() {
		return this.apple;
	}
	
	/**
	 * 
	 * @return Boards width
	 */
	public int getWidth() {
		return this.width;
	}
	
	/**
	 * 
	 * @return Boards height
	 */
	public int getHeight() {
		return this.height;
	}
	
	/**
	 * Checks the effect a move will have on the game, takes appropriate actions, moves the snake
	 * @param moveCode One of: Board.GO_UP, Board.GO_DOWN, Board.GO_RIGHT, Board.GO_LEFT
	 */
	public void  doMove(int moveCode) {
		
		Point move = null;
		Point head = new Point( snake.getHead().x, snake.getHead().y);
		//System.out.println("Board.doMove, code = "+moveCode);
		switch(moveCode) {
		
			case GO_UP:{
				if(head.y-1 < 0) {// head hits edge of the board
					gameOver = true;
					return;
				}
				head.y -= 1;
				move = Snake.UP;
				break;
			}
			
			case GO_DOWN:{
				if( head.y+1 > height-1 ) {
					gameOver = true;
					return;
				}
				head.y += 1;
				move = Snake.DOWN;
				break;
			}
			
			case GO_RIGHT:{
				if(head.x+1 > width-1) {
					gameOver = true;
					return;
				}
				head.x += 1;
				move = Snake.RIGHT;
				break;
			}
			
			case GO_LEFT:{
				if(head.x-1 < 0) {
					gameOver = true;
					return;
				}
				head.x -= 1;
				move = Snake.LEFT;
				break;
			}
		}
		
		if(move == null)
			return;
		
		// heads coords are now set for new position
		if(head.equals(apple)) { // Check if apple will be eaten
			snake.addSegment();
			game.manageDelay();
			apple = this.randomizeApplePosition();
			if(apple == null) {
				gameOver = true;
				return;
			}
		}
		
		ArrayList<Point> snakeBody = snake.getBody();
		for( int i=1; i < snakeBody.size(); i++) { // Check if snake wont bite itself, skip
			if(head.equals(snakeBody.get(i))) {
				gameOver = true;
				return;
			}
		}
		
		snake.doMove(move); // move the snake
	}
	
	/**
	 * Randomizes apple position, called whenever apple is eaten. If board turns out to be full game ends.
	 * @return a Point
	 */
	private Point randomizeApplePosition() {
		if( (width * height) - snake.getSize() <= 0 ) {
			// no more space on the board - game ends
			return null;
		}
		
		while( true ) {
			Point p = new Point(rand.nextInt(width), rand.nextInt(height) );
			boolean ok = true;
			for(Point segment: snake.getBody()){
				//check if position for apple is valid
				if( segment.equals(p) )
					ok = false;
			}
			if( ok ) {
				return p;
			}
		}
	}
	
}
