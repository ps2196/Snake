/**
 * 
 */
package snake;

import java.awt.*;
import java.util.*;

/**
 * @author Piotr S³ysz 
 * Snake - snake model
 */
public class Snake {
	private ArrayList<Point> body; // Snakes body - a collection of Points

	public static final Point // moves
	UP = new Point(0, -1), DOWN = new Point(0, 1), RIGHT = new Point(1, 0), LEFT = new Point(-1, 0);

	/**
	 * Default constructor calls Snake(int, int) with 0, 0 params
	 */
	public Snake() {
		this(0, 0);
	}

	/**
	 * @param x
	 *            - head x coordinate
	 * @param y
	 *            - head y coordinate
	 */
	public Snake(int x, int y) {
		body = new ArrayList<Point>();
		// Create 3 initial segments
		body.add( new Point(x, y) );
		body.add( new Point(x, y + 1) );
		body.add( new Point(x, y + 2) );
	}
	/**
	 * 
	 * @return Snakes body - ArrayList<Point>
	 */
	public ArrayList<Point> getBody() {
		return this.body;
	}

	/**
	 * 
	 * @return Snakes head position - a Point
	 */
	public Point getHead() {
		return this.body.get(0);
	}

	/**
	 * 
	 * @return Snakes size
	 */
	public int getSize() {
		return this.body.size();
	}

	/**
	 * Changes position of snakes body according to move parameter
	 * @param move - a Point, should be one of: Snake.UP, Snake.DOWN, Snake.LEFT, Snake.Right
	 */
	public void doMove(Point move) {
		for( int i=body.size()-1; i > 0; i--) {
			Point segAhead = body.get(i-1);
			body.set(i, new Point(segAhead.x, segAhead.y));
		}
		Point head = body.get(0);
		body.set(0, new Point(head.x + move.x, head.y + move.y));
	}

	/**
	 * Adds new segment to snakes body
	 */
	public void addSegment() {
		Point tail = body.get( body.size()-1 );
		Point newSegment = new Point(tail.x, tail.y); // this method is called just before moving snake, so current tail spot will be empty on the board
		body.add(newSegment);
	}
}
