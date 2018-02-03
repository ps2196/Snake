/**
 * 
 */
package snake;

import java.util.Comparator;

/**
 * @author Piotr S³ysz
 *  Highscore - helper class, represents single highscore record, provides compare method
 */
public class Highscore implements Comparator<Highscore>{
	public String name;
	public Integer score;
	
	/**
	 * Default constructor sets set name for "" and score for 0
	 */
	public Highscore() {
		name = "";
		score = 0;
	}
	/**
	 * 
	 * @param name Player name
	 * @param score Players score
	 */
	public Highscore(String name, Integer score) {
		this.name = name;
		this.score = score;
	}
	
	@Override
	/**
	 * Compares highscores based on score value 
	 */
	public int compare(Highscore h1, Highscore h2) {
		return -1*(Integer.compare(h1.score, h2.score));
	}
}
