//NOTES & MESSAGES
/*
NEED TO ADD IN USER INPUT FOR SEED
implement ben's backtracking soon
 */

//importations
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Random;
import java.util.Arrays; 

//class name+ implementations, note i like to use graphic
public class Sudoku implements ActionListener, MouseListener{
    static String[] myString ={"a","","",""};
    //helps with initialization+ renderer
    public static Sudoku objectname;
    //paint component
    public Renderer renderer;
    //JFrame to dispose
    JFrame frame;
    //seed to help recreate if the user chooses, or else will be random
    static int seed;
    //controls algorithm of changing the puzzle
    Random randgen;
    //Variables to utilize randgen when shuffling
    int rand,rand2;
    //GUI positioning
    int centerX, centerY, x, y;
    //Strings arrays to hold values
    String [][] nums = new String[9][9];
    String [][] orig = new String[9][9];
    String [] temp;
    String numVal = "";
    //Board Colors
    static Color c1;
    static Color c2;
    
    //Solved puzzzle that will be shuffled (idk why the commit got messed up)
    String [][] solvedPuzzle = {{"6", "1", "3", "5", "4", "2", "8", "9", "7"},
				{"8", "9", "7", "3", "6", "1", "5", "4", "2"},
	                        {"5", "4", "2", "9", "8", "7", "3", "1", "6"}, 
	                        {"4", "6", "1", "7", "3", "9", "2", "8", "5"},
	                        {"7", "5", "8", "4", "2", "6", "1", "3", "9"}, 
	                        {"3", "2", "9", "1", "5", "8", "7", "6", "4"},  
	                        {"2", "3", "6", "8", "7", "4", "9", "5", "1"}, 
	                        {"1", "7", "5", "6", "9", "3", "4", "2", "8"},
	                        {"9", "8", "4", "2", "1", "5", "6", "7", "3"}};
    //win method, boolean to check if two 2d arrays are equal
    public static boolean win(String [][] a, String[][] b) {
	boolean winner = true;
	for (int i = 0; i < a.length; i++) {
	    for (int j = 0; j < a[0].length; j++)
		if (!(a[i][j].equals(b[i][j])))
		    winner = false;
	}
	return winner;
    }
    //for switching two rows
    public static void switchRows(String [][] a, int row1, int row2) {
	String [] temp = a[row1];
	a[row1] = a[row2]; 
	a[row2] = temp;
    }

    //for switching two columns
    public static void switchColumns(String [][] a, int column1, int column2) {
	for (int i = 0; i < a.length; i++) {
	    String temp = a[i][column1];
	    a[i][column1] = a[i][column2];
	    a[i][column2] = temp;
	}
    }

    //constructor
    public Sudoku(int seed){
	//assign randgen
	randgen = new Random(seed);
	//meticulously  shuffling of solved puzzle using the seed to maintain Sudoku viability
	for (int i = 0; i < 5; i++){
	    rand = randgen.nextInt(9);
	    if (rand % 3 == 0){
		switchRows(solvedPuzzle, rand, 0);
	        switchRows(solvedPuzzle, rand+1, 1);
	        switchRows(solvedPuzzle, rand+2, 2);
	    }
	    if (rand % 3 == 1){
	        switchRows(solvedPuzzle, rand, 1);
	        switchRows(solvedPuzzle, rand-1, 0);
	        switchRows(solvedPuzzle, rand+1, 2);
	    }
	    if (rand % 3 == 2){
	        switchRows(solvedPuzzle, rand, 2);
	        switchRows(solvedPuzzle, rand-1, 1);
	        switchRows(solvedPuzzle, rand-2, 0);
	    }
	    rand = randgen.nextInt(9);
	    if (rand % 3 == 0){
	        switchColumns(solvedPuzzle, rand, 0);
	        switchColumns(solvedPuzzle, rand+1, 1);
	        switchColumns(solvedPuzzle, rand+2, 2);
	    }
	    if (rand % 3 == 1){
	        switchColumns(solvedPuzzle, rand, 1);
	        switchColumns(solvedPuzzle, rand-1, 0);
	        switchColumns(solvedPuzzle, rand+1, 2);
	    }
	    if (rand % 3 == 2){
		switchColumns(solvedPuzzle, rand, 2);
	        switchColumns(solvedPuzzle, rand-1, 1);
	        switchColumns(solvedPuzzle, rand-2, 0);
	    }
	}
	//answer key to compare against
	for (int i = 0; i < 9; i++){
	    for (int j = 0; j < 9; j++)
		nums[i][j] = solvedPuzzle[i][j];
	}
	//blanks values
	int max=1;
	if("easy".equals(myString[1]))
	    max=10;
	if("medium".equals(myString[1]))
	    max=20;
	if("hard".equals(myString[1]))
	    max=30;
	for (int i = 0; i < max; i++){
	    rand = randgen.nextInt(9);
	    rand2 = randgen.nextInt(9);
	    if (solvedPuzzle[rand][rand2].equals("")) {
		rand = randgen.nextInt(9);
		rand2 = randgen.nextInt(9);
	    }
	    solvedPuzzle[rand][rand2] = "";
	}
	//System.out.println(myString[1]);
	//GUI
	for (int i = 0; i < 9; i++){
	    for (int j = 0; j < 9; j++){
		orig[i][j] = solvedPuzzle[i][j];
	    }
	}
	//Test Methods (terminal rn)
        //////System.out.println(Arrays.deepToString(orig).replace("[", "").replace("], ","\n"));
	////////System.out.println("\n");
        ////////System.out.println(Arrays.deepToString(nums).replace("[", "").replace("], ","\n"));
	JFrame jframe = new JFrame("Sudoku Puzzle");
	Timer timer = new Timer(20, this);
	frame=jframe;
	renderer = new Renderer();
	jframe.add(renderer);
	jframe.addMouseListener(this);
	jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jframe.setSize(650, 470);
	jframe.setVisible(true);
	jframe.setResizable(false);
	timer.start();
    }

    //method to use renderer
    public void actionPerformed(ActionEvent e){
	renderer.repaint();
    }

    //method to design the GUI
    public void repaint(Graphics g) {
	//Seed display
	g.setColor(Color.black);
	Font mundane=new Font ("Arial",Font.BOLD,20);
	g.setFont(mundane);
	g.drawString("SEED", 550, 30);
	g.drawString(""+seed, 557, 50);
	g.drawString("CHANGE", 540, 80);
	g.drawString("SEED", 550, 100);
	g.drawString("DIFFICULTY", 525, 150);
	int place=550;
	if("medium".equals(myString[1]))
	    place=538;
	g.drawString(""+myString[1].toUpperCase(), place, 180);
	g.drawString("MAIN", 550, 400);
	g.drawString("MENU", 547, 420);
	//THIS IS THE SUDOKU GRID
	g.setColor(c2);
      	g.fillRect(0,  0,  450,  450);
	int i = 50;
	g.setColor(c1);
	g.fillRect(450,  0,  70, 450);
	while (i < 450){
	    g.fillRect(i, 0, 1, 450);
	    g.fillRect(0,  i, 450, 1);
	    if (i == 150 || i == 300) {
		g.fillRect(i, 0, 3, 450);
		g.fillRect(0,  i, 450, 3);
	    }
	    i += 50;
	}
	i = 15;
	int num =  1;
	g.setColor(c2);
	while (i < 450){
	    g.fillRect(475, i, 20, 20);
	    g.setColor(Color.black);
	    Font font = new Font("Saab", Font.BOLD, 16);
	    g.setFont(font);
	    g.drawString(num + "", 481, i+15);
	    i+=50;
	    num++;
	    g.setColor(c2);
	}
	//END OF JUST SUDOKU GRID

	//Draws in the puzzle
	g.setColor(Color.black);
	for (int k = 0; k < solvedPuzzle.length; k++) {
	    for (int j = 0; j < solvedPuzzle[0].length; j++) {
		if (orig[k][j].equals(""))
		    g.setColor(Color.blue);
		g.drawString(solvedPuzzle[k][j]+"", 50 * j + 25, 50 * k + 25);
	        g.setColor(Color.black);
	    }
	}
	//win message
	if (win(nums, solvedPuzzle)){
	    g.fillRect(0,  0,  450,  450);
	    g.setColor(Color.white);
	    Font won = new Font("Helvetica", Font.BOLD, 70);
	    g.setFont(won);
	    g.drawString("You win!", 110, 220);
	}
    }

    public static void main (String [] a) {
	int x;
	try {
	    x = Integer.parseInt(a[0]);
	    x %= 1000;
	}catch (Exception e){
	    x = (int) (Math.random() * 1000);
	}
	seed = x;
	try{
	    myString[1]=a[1];
	}catch(Exception e){
	    myString[1]="medium";}
	try{
	    myString[2]=a[2];
	    myString[3]=a[3];
	}catch(Exception e){
	    myString[2]="cyan";
	    myString[3]="white";}
	c1=Settings.colors(myString[2]);
	c2=Settings.colors(myString[3]);
	objectname = new Sudoku(seed);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	//makes it easier to use y and x coordinates
	int xcor = e.getX();
	int ycor = e.getY();
	///////////System.out.println(xcor+","+ycor);
	//User input from number pad
	if (xcor > 475 && xcor < 495 && ycor > 40 && ycor < 460 &&
	    ((ycor % 100 > 40 && ycor % 100 < 60) || (ycor % 100 < 10 || ycor % 100 > 90))){
	    numVal = (ycor + 15) / 50 + "";
	}
	if (xcor < 475){
	    x = ((xcor + 50) / 50);
	    y = ((ycor + 25) / 50);
	    centerX = 50 * x - 25;
	    centerY = 50 * y - 25;
	    if (orig[y-1][x-1].equals("")){
		solvedPuzzle[y-1][x-1] = numVal;
	    }
	}
	if(xcor >=540 && xcor<= 610 && ycor >=405 && ycor<= 445){
	    frame.dispose();
	    Menu.main(myString); 
	}
	if(xcor >=535 && xcor<= 630 && ycor >=85 && ycor<= 125){
	    frame.dispose();
	    myString[0] = ""+seed;
	    ChooseSeed.main(myString); 
	}
    }
 


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
    
