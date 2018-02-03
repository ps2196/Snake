/**
 * 
 */
package snake;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * @author Piotr S³ysz
 * Panel in which the board is displayed
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel {

	private Board board;
	private static Color backgroundColor = Color.BLACK;
	private static Color snakeColor  = Color.GREEN;
	private static Color headColor = new Color(2267476); 
	private static Color appleColor = Color.RED;
	
	/**
	 * Initializes GamePanel - which displays board
	 * @param board - a Board - data source for this panel
	 */
	public GamePanel(Board board) {
		super();
		this.board = board;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		int px_size = SnakeView.PX_SIZE;
		g.setColor(backgroundColor);
		g.fillRect( 0, 0, board.getWidth() * px_size, board.getHeight() * px_size );
			
		//Draw Snake body
		Snake snake = board.getSnake();
		g.setColor(headColor);
		g.fillRect( snake.getHead().x*px_size , snake.getHead().y*px_size, px_size-1, px_size-1 );
		
		g.setColor(snakeColor);
		ArrayList<Point> snakeBody = snake.getBody();
		for( int i = 1; i < snakeBody.size(); i++ ) {
			Point segment = snakeBody.get(i);
			g.fillRect( segment.x*px_size , segment.y*px_size, px_size-1, px_size-1);
			//System.out.println("Segment "+i+" x = "+ segment.x+ " y = "+segment.y + " size = "+ snake.getSize());
		}
		
		// Draw apple
		Point apple = board.getApple();
		g.setColor(appleColor);
		g.fillRect(apple.x * px_size, apple.y * px_size, px_size-1, px_size-1);
	}
}
