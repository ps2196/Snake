/**
 * 
 */
package snake;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author Piotr S³ysz
 * SnakeView - main GUI class manages contents of main frame.
 */
public class SnakeView {
	private JFrame frame; // main frame
	private GamePanel gamePanel; // a panel on which the board is drawn
	private Game game; // a Game instance
	
	public static final int PX_SIZE = 10;
	
	/**
	 * Creates GUI instance for the game.
	 * @param game - Game instance this is connected to
	 */
	public SnakeView(Game game) {
		this.game = game;
		frame = new JFrame("Snake");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMinimumSize( new Dimension(game.getBoard().getWidth()*PX_SIZE + 17, game.getBoard().getHeight()*PX_SIZE + 40) );
		frame.setResizable(false);
	}
	/**
	 * Repaints game panel
	 */
	public void repaintGamePanel() {
		gamePanel.repaint();
	}
	/**
	 * Registers obj as frame KeyListener
	 * @param obj
	 */
	public void addKeyListener(Object obj) {
		frame.addKeyListener( (KeyListener) obj ); 
	}
	/**
	 * 
	 * @return a Board
	 */
	public Board getBoard() {
		return game.getBoard();
	}

	
	/**
	 * Displays main menu in frame
	 */
	@SuppressWarnings("serial")
	public void showMainMenu() {
		frame.getContentPane().removeAll();
		JPanel panel = new JPanel(new GridBagLayout() ) {
			@Override
			protected void paintComponent(Graphics g) {
				// TODO Auto-generated method stub
				super.paintComponent(g);
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, game.getBoard().getWidth() * PX_SIZE, game.getBoard().getHeight() * PX_SIZE );
			}
		};
		panel.setMinimumSize(new Dimension(game.getBoard().getWidth() * PX_SIZE, game.getBoard().getHeight() * PX_SIZE));
		//Buttons constraints
		GridBagConstraints constr = new GridBagConstraints();
		constr.gridx = 1;
		constr.fill = GridBagConstraints.VERTICAL;
		
		// New game button
		JButton newGameButton = this.newTransparentButton("New Game");
		newGameButton.setHorizontalAlignment(JButton.CENTER);
		newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		newGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.startGame();
			}
			
		});
		constr.anchor = GridBagConstraints.PAGE_START;
		panel.add(newGameButton, constr);
		// Highscore button
		JButton highscoreButton = this.newTransparentButton("Highscore");
		highscoreButton.setHorizontalAlignment(JButton.CENTER);
		highscoreButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		highscoreButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.setState(Game.State.HIGHSCORE);
				game.mainView.showHighscore();
			}
			
		});
		constr.anchor = GridBagConstraints.CENTER;
		panel.add(highscoreButton,constr);
		
		// Exit button
		JButton exitButton = this.newTransparentButton("Exit");
		exitButton.setHorizontalAlignment(JButton.CENTER);
		exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		constr.anchor = GridBagConstraints.PAGE_END;
		panel.add(exitButton, constr);
		
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null); // center frames location
		frame.setVisible(true);
	}

	/**
	 * Displays game panel in frame
	 */
	public void showBoard() {
		frame.getContentPane().removeAll();
		gamePanel = new GamePanel(game.getBoard());
		frame.add(gamePanel);
		frame.revalidate();
		frame.repaint();
		frame.requestFocus(); // Focus is required by KeyListener
	}

	/**
	 * Displays game over screen in frame
	 */
	@SuppressWarnings("serial")
	public void showGameOverScreen() {
		frame.getContentPane().removeAll();
		//main panel for gameOver screen
		JPanel gameOverPanel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, game.getBoard().getWidth() * PX_SIZE, game.getBoard().getHeight() * PX_SIZE);
			}
		};
		//Top panel
		JPanel topPanel = new JPanel( new BorderLayout() );
		topPanel.setOpaque(false); //make panel transparent
		// game over label
		JLabel gameOver = new JLabel("Game Over");
		gameOver.setFont(new Font( "Forte", Font.BOLD, 40 ) );
		gameOver.setForeground(Color.WHITE);
		gameOver.setHorizontalAlignment(JLabel.CENTER);
		topPanel.add( gameOver, BorderLayout.PAGE_START );
		//Score info
		JLabel scoreInfo = new JLabel("Score: "+(game.getBoard().getSnake().getSize() * 10 - 30));
		scoreInfo.setFont(new Font( "Forte", Font.BOLD, 30 ));
		scoreInfo.setForeground(Color.WHITE);
		scoreInfo.setHorizontalAlignment(JLabel.CENTER);
		topPanel.add(scoreInfo, BorderLayout.CENTER);
		
		//Central panel
		JPanel centralPanel = new JPanel( new FlowLayout() );
		centralPanel.setOpaque(false); //make panel transparent
		//Label
		JLabel nameLabel = new JLabel("Enter your name here:");
		nameLabel.setFont( new Font("Forte", Font.BOLD, 30) );
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setHorizontalAlignment(JLabel.LEFT);
		//Text field
		final JTextField playerName = new JTextField("Player");
		playerName.setFont( new Font("Forte", Font.BOLD, 30) );
		playerName.setForeground(Color.BLUE);
		playerName.setMinimumSize( new Dimension( 150, 40 ));
		playerName.setHorizontalAlignment(JTextField.LEFT);

		centralPanel.add(nameLabel);
		centralPanel.add(playerName);
		
		//Buttons panel
		JPanel buttonPanel = new JPanel( new BorderLayout() );
		buttonPanel.setOpaque(false);
		//Back to main menu button
		JButton mainMenuButton = this.newTransparentButton("Main Menu");
		mainMenuButton.setHorizontalAlignment(JButton.RIGHT);
		mainMenuButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.checkHighscore( playerName.getText(), game.getBoard().getSnake().getSize() * 10 - 30 );
				game.setState(Game.State.MAIN_MENU);
				game.mainView.showMainMenu();
			}
		});
		buttonPanel.add(mainMenuButton, BorderLayout.WEST);
		//Replay button
		JButton replayButton = this.newTransparentButton("Replay");
		replayButton.setHorizontalAlignment(JButton.LEFT);
		replayButton.addActionListener( new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.checkHighscore( playerName.getText(), game.getBoard().getSnake().getSize() * 10 - 30 );
				game.setState(Game.State.PLAYING);
				game.startGame();
			}
		});
		
		buttonPanel.add(replayButton, BorderLayout.EAST);
		
		gameOverPanel.add(buttonPanel, BorderLayout.SOUTH);
		gameOverPanel.add(centralPanel, BorderLayout.CENTER);
		gameOverPanel.add(topPanel, BorderLayout.PAGE_START);
		frame.add(gameOverPanel);
		frame.revalidate();
		frame.repaint();
		
	}

	/**
	 * Displays highscore table in frame
	 */
	@SuppressWarnings("serial")
	public void showHighscore() {
		frame.getContentPane().removeAll();
		Highscore[] highscores = game.getSortedHighscores();
		JPanel mainPanel = new JPanel(new BorderLayout()) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.BLACK);
				g.fillRect(0, 0, game.getBoard().getWidth() * PX_SIZE, game.getBoard().getHeight() * PX_SIZE);
			}
		};
		
		JPanel listPanel = new JPanel(new GridLayout(0,2));
		listPanel.setOpaque(false);
		Font font = new Font("Forte", Font.BOLD, 30);
		for(int i=0; i < highscores.length; i++) {
			JLabel name = new JLabel(highscores[i].name);
			name.setFont(font);
			name.setForeground(Color.WHITE);
			name.setHorizontalAlignment(JLabel.RIGHT);
			JLabel score = new JLabel( highscores[i].score.toString() );
			score.setFont(font);
			score.setForeground(Color.WHITE);
			score.setHorizontalAlignment(JLabel.CENTER);
			
			listPanel.add(name);
			listPanel.add(score);
		}
		
		JPanel topPanel = new JPanel(new GridLayout(0,2));
		topPanel.setOpaque(false);
		//Title label
		font = new Font("Forte", Font.BOLD, 40);
		JLabel title = new JLabel("Highscore");
		title.setFont(font);
		title.setForeground(Color.WHITE);
		title.setHorizontalAlignment(JLabel.LEFT);
		// Back button
		font = new Font("Forte", Font.BOLD, 20);
		JButton back = this.newTransparentButton("Back");
		back.setFont(font);
		back.setHorizontalAlignment(JButton.LEFT);
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				game.setState(Game.State.MAIN_MENU);
				game.mainView.showMainMenu();
			}
		});
		
		topPanel.add(back);
		topPanel.add(title);
	
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(listPanel, BorderLayout.CENTER);
		
		frame.add(mainPanel);
		frame.revalidate();
		frame.repaint();
	}
	
	/**
	 * @param text - Text displayed on the button
	 * @return a transparent JButon
	 */
	private JButton newTransparentButton(String text) {
		JButton button = new JButton(text);
		Font font = new Font("Forte", Font.BOLD, 32);
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFont(font);
		button.setForeground(Color.WHITE);
		
		return button;
	}

}
