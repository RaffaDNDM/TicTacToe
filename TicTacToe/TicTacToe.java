package TicTacToe;

// Definition of ImageIcon for the buttons
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Container;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
// Definition of The Frame and menu
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
// Dialogue windows 
import javax.swing.JOptionPane;
import javax.swing.JPanel;
// Look
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
// Listeners
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * @author Raffaele Di Nardo Di Maio
 */
public class TicTacToe extends JFrame
{
	private final static int PLAYER0 = 0; //ID of first player when player=0
	private final static int PLAYER1 = 1; //ID of second player when player=1
	private final static int NUM_ROWS = 3; //Number of rows in the matrix
	private final static int NUM_COLUMNS = 3; //Number of columns in the matrix
	private final static String DARK_PATH = "Images/Dark/"; //Path of dark symbols
	private final static String LIGHT_PATH = "Images/Light/"; //Path of light symbols
	private final static String X = "X.png"; //Name of X icon file
	private final static String O = "Circle.png"; //Name of O icon file
	
    
	private String name; //Name of the application
    private String first_opponent; //Name of the first player
    private String second_opponent; //Name of the second player
    private boolean end = true; //true if no possibility of writing the symbol 
    private int[][] grid = new int[NUM_ROWS][NUM_COLUMNS]; //grid used to check if someone won
    private int player = 0; //current player (0 or 1)
    private int occupied = 0; //number of cells occupied in the table
    private String item0; //path of current X symbol icon
    private String item1; //path of current O symbol icon
    private Color std_cell; //color of standard cell
    private Color win_cell; //color of cell for the victory
    
    //Definition of main components of the frame
    private JMenuBar mb;
    private JMenu edit, help, change_style;
    private JMenuItem dark_theme, light_theme, info, play, rules;
    private Container c;
    private JButton[] boxes = new JButton[NUM_ROWS*NUM_COLUMNS]; //Set of cells (buttons) in the frame
    
    /**
     * TicTacToe definition
     * @param name name of the application
     * @param first_opponent name of first player
     * @param second_opponent name of second player
     */
    private TicTacToe(String name, String first_opponent, String second_opponent)
    {
        super(name);

        this.first_opponent = first_opponent;
        this.second_opponent = second_opponent;
        
        this.item0 = DARK_PATH + X;
        this.item1 = DARK_PATH + O;
        this.std_cell = TTTColor.DARK_STD;
        this.win_cell = TTTColor.DARK_WIN;
        

        //Set of general look for Menubar
        UIManager.put("MenuBar.opaque", true);
        UIManager.put("Menu.opaque", true);
        UIManager.put("MenuItem.opaque", true);
        UIManager.put("MenuBar.background", TTTColor.DARK_MENU);
        UIManager.put("Menu.background", TTTColor.DARK_MENU);
        UIManager.put("MenuItem.background", TTTColor.DARK_ITEM);
        
        ManageMenuBar listener_menubar = new ManageMenuBar();
        
        //Definition of edit option in Menubar
        edit = new JMenu("Edit");
        play = new JMenuItem("Play");
        play.addActionListener(listener_menubar);
        play.setForeground(TTTColor.DARK_EDIT_TEXT);
        change_style = new JMenu("Change Style");
        change_style.setBackground(TTTColor.DARK_EDIT_TEXT);
        change_style.setForeground(TTTColor.STYLE_TEXT);
        dark_theme = new JMenuItem("Dark");
        light_theme = new JMenuItem("Light");
        change_style.add(dark_theme);
        change_style.add(light_theme);
        dark_theme.addActionListener(listener_menubar);
        light_theme.addActionListener(listener_menubar);
        dark_theme.setForeground(TTTColor.DARK_EDIT_TEXT);
        light_theme.setForeground(TTTColor.DARK_EDIT_TEXT);
        edit.add(change_style);
        edit.add(play);
        edit.setForeground(TTTColor.DARK_EDIT_TEXT);

        //Definition of help option in Menubar
        help = new JMenu("Help");
        info = new JMenuItem("Info");
        rules = new JMenuItem("Rules");
        info.addActionListener(listener_menubar);
        rules.addActionListener(listener_menubar);        
        info.setForeground(TTTColor.DARK_HELP_TEXT);
        rules.setForeground(TTTColor.DARK_HELP_TEXT);
        help.setForeground(TTTColor.DARK_HELP_TEXT);
        help.add(info);
        help.add(rules);
        
        //Add edit and help to Menubar
        mb = new JMenuBar();
        mb.add(edit);
        mb.add(help);
        
        
        // Main container of the frame 
        c = getContentPane();

        JPanel center = new JPanel();
        ManageBottons listener_bottons = new ManageBottons();
        
        c.setLayout(new BorderLayout());
        
        center.setLayout(new GridLayout(3,3));

        // Initialization of the matrix 3x3
        for(int i=0; i<3 ;i++)
        {
        	for(int j=0; j<3; j++)
        	{
        		grid[i][j]= -1;
        	}
        }
        
        //Definition of matrix 3x3 of clickable buttons
        for(int i=0; i<9; i++)
        {
            boxes[i] = new JButton("");

            boxes[i].addActionListener(listener_bottons);
            boxes[i].setBackground(std_cell);
            center.add(boxes[i]);
        }

        
        // Matrix table in the center of the window
        c.add(new JPanel(), BorderLayout.NORTH);
        c.add(center, BorderLayout.CENTER);
        c.add(new JPanel(), BorderLayout.WEST);
        c.add(new JPanel(), BorderLayout.EAST);
        c.add(new JPanel(), BorderLayout.SOUTH);
        

        // General behaviour (add bar, fix dimension, define exit_op, visible frame)
        setJMenuBar(mb);
        setSize(600,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
    }

    /**
     * Implementation of Listener for events from buttons 
     * @author raffa
     */
    public class ManageBottons implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {   
            int i=0;
            
            // Identification of clicked button
            while(e.getSource()!=boxes[i])
            	i++;    
        
            // Definition of cell of grid from the index of the clicked button 
            int row = i/NUM_ROWS;
            int column = i%NUM_COLUMNS;
            
            // Change image of a cell only if not clicked
            if(grid[row][column] == -1 && !end)
            {
            	// Understand which symbol must be used
            	String choice = item0;
            	grid[row][column] = PLAYER0;
            		
            	if(player == 1)
            	{
            		choice = item1;
            		grid[row][column] = PLAYER1;
            	}
            	
            	occupied++;
            	
            	// Image resized w.r.t. dimension of button
            	Dimension size = boxes[i].getSize();
            	Image img=null;
				
            	try 
				{
					img = ImageIO.read(new File(choice));
				} 
            	catch (IOException e1) 
            	{
					e1.printStackTrace();
				}
            	
            	Image newImg = img.getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH);
            	
        		((JButton) e.getSource()).setIcon(new ImageIcon(newImg));
            	player = (player+1)%2;
            
            	// Check if the player won
            	int[] won = checkMove(grid[row][column]);
            	
            	//The player won
            	if(won[0]>=0)
            	{
            		// Underline the three cells that made the victory
            		for(i=0; i<NUM_ROWS; boxes[won[i++]].setBackground(win_cell));
            		end = true;
            		
            		String winner = (grid[row][column]==PLAYER0) ? first_opponent : second_opponent;
            		JOptionPane.showMessageDialog(null, winner+" wins");            	
            		
            		reset();
            	}
            	//no one won
            	else if(occupied==9)
            	{
            		JOptionPane.showMessageDialog(null, "No one wins");
            		reset();
            	}
            }   
        }
    }

    /**
     * Reset the symbols inside the matrix 3x3 of buttons
     * Reset the grid matrix, the actual player and the numer of symbols inserted
     */
    private void reset()
    {
    	for(int i=0; i<9; i++)
    	{	
    		boxes[i].setIcon(null);
    		boxes[i].setBackground(std_cell);
    	}
    	
    	
    	for(int i=0; i<NUM_ROWS; i++)
    	{
    		for(int j=0; j<NUM_COLUMNS; j++)
    		{
    			grid[i][j] = -1;
    		}
    	}
    	
		player = PLAYER0;
		occupied = 0;
    }
    
    /**
     * Implementation of Listener for events from menubar 
     * @author Raffaele Di Nardo Di Maio
     *
     */
    public class ManageMenuBar implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {   
        	if(e.getSource()==dark_theme)
        		setTheme(true);
        		
        	if(e.getSource()==light_theme)
        		setTheme(false);
        		
        	if(e.getSource()==play)
        		end = false;
        		
        	if(e.getSource()==info)
        		JOptionPane.showMessageDialog(null, "Tic-Tac-Toe game\n Author: Raffaele Di Nardo Di Maio ");
        	
        	if(e.getSource()==rules)
        	{
        		String rule1 = "You need to alternate with your opponent, inserting your symbol (X or O)\n";
        		String rule2 = "The first one that creates a sequence of 3 identical symbols\n";        		
        		JOptionPane.showMessageDialog(null, rule1+rule2);
        	}
    	}
    }
    

    /**
     * Update the theme of the frame
     * @param dark true if dark theme selected, false if light time
     */
    private void setTheme(boolean dark)
    {
    	// JMenuBar look 
    	if(dark)
    	{
    		item0 = DARK_PATH + X;
    		item1 = DARK_PATH + O;
    		std_cell = TTTColor.DARK_STD;
    		win_cell = TTTColor.DARK_WIN;
    		
    		mb.setBackground(TTTColor.DARK_MENU);
    		edit.setBackground(TTTColor.DARK_MENU);
    		help.setBackground(TTTColor.DARK_MENU);
    		
    		play.setForeground(TTTColor.DARK_EDIT_TEXT);
    		play.setBackground(TTTColor.DARK_ITEM);
            change_style.setBackground(TTTColor.DARK_EDIT_TEXT);
            change_style.setForeground(TTTColor.STYLE_TEXT);
            dark_theme.setForeground(TTTColor.DARK_EDIT_TEXT);
            dark_theme.setBackground(TTTColor.DARK_ITEM);
            light_theme.setForeground(TTTColor.DARK_EDIT_TEXT);
            light_theme.setBackground(TTTColor.DARK_ITEM);
            edit.setForeground(TTTColor.DARK_EDIT_TEXT);
            
            info.setForeground(TTTColor.DARK_HELP_TEXT);
            info.setBackground(TTTColor.DARK_ITEM);
            rules.setForeground(TTTColor.DARK_HELP_TEXT);
            rules.setBackground(TTTColor.DARK_ITEM);
            help.setForeground(TTTColor.DARK_HELP_TEXT);
    	}
    	else
    	{
    		item0 = LIGHT_PATH + X;
    		item1 = LIGHT_PATH + O;
    		std_cell = TTTColor.LIGHT_STD;
    		win_cell = TTTColor.LIGHT_WIN;
            
    		mb.setBackground(TTTColor.LIGHT_MENU);
    		edit.setBackground(TTTColor.LIGHT_MENU);
    		help.setBackground(TTTColor.LIGHT_MENU);
    		
    		play.setForeground(TTTColor.LIGHT_EDIT_TEXT);
    		play.setBackground(TTTColor.LIGHT_ITEM);
            change_style.setBackground(TTTColor.LIGHT_EDIT_TEXT);
            change_style.setForeground(TTTColor.STYLE_TEXT);
            dark_theme.setForeground(TTTColor.LIGHT_EDIT_TEXT);
            dark_theme.setBackground(TTTColor.LIGHT_ITEM);
            light_theme.setForeground(TTTColor.LIGHT_EDIT_TEXT);
            light_theme.setBackground(TTTColor.LIGHT_ITEM);
            edit.setForeground(TTTColor.LIGHT_EDIT_TEXT);
            
            info.setForeground(TTTColor.LIGHT_HELP_TEXT);
            info.setBackground(TTTColor.LIGHT_ITEM);
            rules.setForeground(TTTColor.LIGHT_HELP_TEXT);
            rules.setBackground(TTTColor.LIGHT_ITEM);
            help.setForeground(TTTColor.LIGHT_HELP_TEXT);
    	}
    	
    	
    	//Buttons and icons inside of them
    	for(int i=0; i<NUM_ROWS; i++)
    	{
    		for(int j=0; j<NUM_COLUMNS; j++)
    		{
    			if(grid[i][j]>=0)
    			{
    				String choice = grid[i][j]==0 ? item0:item1;
    		    	
    				Dimension size = boxes[i*NUM_COLUMNS+j].getSize();
    			
	            	Image img = null;
					
	            	try 
					{
						img = ImageIO.read(new File(choice));
					} 
	            	catch (IOException e1) 
	            	{
						e1.printStackTrace();
					}
	            	
	            	Image newImg = img.getScaledInstance(size.width, size.height, java.awt.Image.SCALE_SMOOTH);
	            	
	        		boxes[i*NUM_COLUMNS+j].setIcon(new ImageIcon(newImg));
    			}
    			
        		boxes[i*NUM_COLUMNS+j].setBackground(std_cell);
    		}
    	}
    }
    
    
    /**
     * Control if player won
     * @param player ID of the player (PLAYER0 or PLAYER1) 
     * @return won[] of 3 indices of JButtons of the victory (won[0]=-1 if no victory) 
     */
    private int[] checkMove(int player)
    {
    	int[] check = new int[NUM_ROWS];
    	int count;
    	
    	
    	for(int i=0; i<3; i++)
    	{
    		count = 0;

        	//Is there a solution on rows?
    		for(int j=0; j<3 && grid[i][j]==player; j++)
    		{
    			count++;
    			
    			check[j] = i*NUM_COLUMNS+j;
    			
    			if(count==3)
    				return check;    			
    		}
    		
    		count = 0;
    	
        	//Is there a solution on rows?
    		for(int j=0; j<3 && grid[j][i]==player; j++)
    		{
    			count++;
    			
    			check[j] = j*NUM_ROWS+i;
    			
    			if(count==3)
    				return check;    			
    		}
    	}
    	    	
    	
    	count = 0;
    	int i=0, j=0;
    	
    	//Is there a solution on diagonals?
    	while(i<3 && grid[i][j]==player)
    	{
    		check[i]=i*NUM_ROWS+j;
    		i++;
    		j++;
    		count++;
    	}
    	
    	
    	if(count==3)
    		return check;
    	
    	count = 0;
    	i=0; j=NUM_COLUMNS-1;
    	
    	while(i<NUM_ROWS && j>=0 && grid[i][j]==player)
    	{
    		check[i]=i*NUM_ROWS+j;
    		i++;
    		j--;
    		count++;
    	}
    	
    	if(count==3)
    		return check;
    	
    	
    	//No solution found
    	check[0]=-1;
    	return check;
    }
    
    
    public static void main(String args[])
    {
    	//Initialization of the frame
        TicTacToe win = new TicTacToe("Tic Tac Toe", "Raffaele", "Cristina");
    }
}
