/******************************************************************************
 *  Compilation:  javac Board.java
 *  Dependencies: none
 *  
 *  Solves the 8-puzzle problem using the A* search algorithm. 
 *  See http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
 *
 ******************************************************************************/
package Week4;
import java.util.Random;
import edu.princeton.cs.algs4.Queue;

public class Board {
	
	// used in the "polarity" version of the solver method 
	/** private boolean blankEven; */
	
	private int n;							
	private int[][]board;
	// construct a board from an n-by-n array of blocks
	public Board(int[][] blocks)							
	{
		this.n = blocks.length;								
		this.board = new int[n][n];	
		for (int i = 0; i < n; i++)	
	        for (int j = 0; j < n; j++) {
	            board[i][j] = blocks[i][j];
	            
	            /** 
	             * used for "polarity" version
	             * returns true if the 0 is found in an "even" row
	            if (board[i][j] == 0 && ((i & 1) == 1)) 	 
	            	blankEven = true;	
				*/
	        }
	}
	public int dimension() { return n; }
	
	/******************************************************************************
	 *  Returns the Hamming distance--i.e., returns the number of blocks in the 
	 *  wrong position plus the number of moves made so far to get to the search node. 
	 *  The 0 does not count as a tile out of place.
	 *  
	 *  Example: 
	 *  	Start Board~~~~~~~~~~~~>End Board
	 *  	8  1  3 		1  2  3 
	 *  	4  0  2 		4  5  6
	 *	7  6  5			7  8  0
	 *
	 *	Hamming
	 *		1  2  3  4  5  6  7  8
	 *		----------------------
	 *		1  1  0  0  1  1  0  1 ====> 5 + 0 (the + 0 is for total moves so far)
	 *
	 ******************************************************************************/
	public int hamming()					
	{ 
		int hamming = 0;
		int start = 1;
		// iterate over grid
		for (int i = 0; i < n; i++)				
	        for (int j = 0; j < n; j++) {
	        	// per spec, we should not count 0 as an incorrect board
	        	if (board[i][j] != 0) {	
	        		// if we are not at the last tile
		        	if (((i+1) * (j+1)) != (n*n))  {			
		        		if (board[i][j] != start) {				
			        		hamming++;							
			        	}
		        	}
		        	else {
		        		// if we are at the end, we should find 0, else increment
		        		if (board[i][j] != 0) hamming++;		 
		        	}
	        	}
	        	// allows us to check against a correct grid
        		start++;										
	        }		
		return hamming;
	}
	
	/******************************************************************************
	 *  Returns the sum of the Manhattan distance. The Manhattan distance, 
	 *  calculated as sum of vertical and horizontal distance, will be the distance
	 *  from the blocks to their goal positions plus the number of moves that 
	 *  have been made so far--i.e., it is the sum of the difference, in absolute value, 
	 *  between two points (x of current - y of goal) + (x of current - y of goal).
	 *  
	 *  Reference: http://math.stackexchange.com/questions/139600/euclidean-manhattan-distance
	 *  
	 *  Example: 
	 *  	
	 *  	Start Board~~~~~~~~~~~~>End Board
	 *  	8  1  3 		1  2  3 
	 *  	4  0  2 		4  5  6
	 *	7  6  5			7  8  0
	 *
	 *	Manhattan
	 *		1  2  3  4  5  6  7  8
	 *		----------------------
	 *		1  2  0  0  2  2  0  3 ====> 10 + 0 (total moves so far)
	 *
	 ******************************************************************************/
	public int manhattan()					
	{ 
		int manhattan = 0; 										
		
		for (int i = 0; i < n; i++) {				
	        for (int j = 0; j < n; j++) {
	        	if (board[i][j] != 0) {
	        		// where we start from
	        		int currentX = i; 						
	        		int currentY = j;	
	        		// where we want to end up
	        		int goalX = ((board[i][j] - 1) / n);	
	        		int goalY = ((board[i][j] - 1) % n);
	        		
	        		// if x = (a, b) and y = (c,d) we need |a-c| + |b-d|.
	        		manhattan += (Math.abs(currentX - goalX) + Math.abs(currentY - goalY));
	        	}
	        }
		}
		return (manhattan); 
	}
	public boolean isGoal()		{ return this.hamming() == 0; }
	
	/******************************************************************************
	 *  ///////////////////////////////////////////////////////////////////////////
	 *  NOT IN USE!! This method uses polarity to check whether a board is 
	 *  solvable or not; however, the current spec relies on the "twin" method 
	 *  to check for solvability. 
	 *  ///////////////////////////////////////////////////////////////////////////
	 *  
	 *  Checks whether the board is solvable or not. A solvable board is one
	 *  in which, given legal moves, there exists a sequence of moves which will 
	 *  result in a correctly configured board (i.e., the 0 is in the location
	 *  (n-1, n-1), the 1 is in position (0,0) and every subsequent integer is 
	 *  more than the one preceding it. 
	 *  
	 *  Per Mark Ryan's 2004 post: 
	 *  	- If the grid width is odd, then the number of inversions in a 
	 *  	  solvable situation is even; conversely, if the grid is odd
	 *  	  and the number of inversions is odd, then the grid is unsolvable.
	 *  
	 *  	- If the grid width is even, and the blank is on an even row 
	 *  	  counting from the bottom (second-last, fourth-last etc), 
	 *  	  then the number of inversions in a solvable situation is odd.
	 *  
	 *  	- If the grid width is even, and the blank is on an odd row 
	 *  	  counting from the bottom (last, third-last, fifth-last etc) 
	 *  	  then the number of inversions in a solvable situation is even.
	 *  
	 *  The formula:
	 *  ( (grid width odd) && (#inversions even) )  ||  
	 *  ( (grid width even) && ((blank on odd row from bottom) == (#inversions even)) )
	 *  
	 *  References: 
	 *  - https://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html
	 *  - http://mathworld.wolfram.com/15Puzzle.html
	 *  
	 * Example: SOLVABLE (even board, even inversions)
	 *  	
	 *  	Start Board~~~~~~~~~~~~>End Board
	 *  	8  1  3 		1  2  3 
	 *  	4  0  2 		4  5  6
	 *	7  6  5			7  8  0
	 *
	 * Inversions
	 * 		8 ===> 7 inversions
	 * 		1 ===> 0 inversions
	 * 		3 ===> 1 inversion
	 * 		4 ===> 1 inversion
	 * 		2 ===> 0 inversions
	 * 		7 ===> 2 inversions
	 * 		6 ===> 1 inversion
	 * 		5 ===> 0 inversions
	 * 		-------------------
	 * 		12 inversions total
	 * 
	 * Grid: odd && inversions is even = solvable 
	 * 
	 * Example 2: UNSOLVABLE (even board, odd inversions)
	 *  	
	 *  	Start Board~~~~~~~~~~~~>End Board
	 *  	1  2  3 		1  2  3 
	 *  	4  5  6 		4  5  6
	 *	8  7  0			7  8  0
	 *
	 * Inversions
	 * 		1 ===> 0 inversions
	 * 		2 ===> 0 inversions
	 * 		3 ===> 0 inversion
	 * 		4 ===> 0 inversion
	 * 		5 ===> 0 inversions
	 * 		6 ===> 0 inversions
	 * 		8 ===> 1 inversion
	 * 		7 ===> 0 inversions
	 * 		-------------------
	 * 		1 inversions total
	 * 
	 ******************************************************************************/
	/**
	public boolean isSolvable() 
	{
		int inversion = 0;
		int counter = 0;
		
		// flattens grid to 1D array in order to check each value against subsequent values
		// uses the "counter" to fill in the appropriate array location 
		int[] flat = new int[n*n];
		for (int i = 0; i < n; i++) {										
			for (int j = 0; j < n; j++) {
				flat[counter] = board[i][j]; 							
				counter++;
			}
		}
		// checks each value in the array against subsequent values																
		// ticker is used so that we do not backtrack in the array, only look forward
		int ticker = 1;													
		for (int i = 0; i < flat.length; i++) {
			for (int j = ticker; j < flat.length; j++) {
				// don't look at the first location, or the location of the 0, but check all others
				if (flat[i] != 0 && flat[j] != 0 && flat[i] > flat[j]) 	
					// increment inversion if we find any value in the subsequent array higher than traversal value
					inversion++;	
			}
			ticker++;
		}
		
		// if our grid is odd and our inversions are even ==> solvable 
		// if our gird is odd and our inversions are odd  ==> not solvable
		if ((n & 1) == 1) {
			if ((inversion & 1) == 0) { return true;  }					
			if ((inversion & 1) == 1) { return false; }					
		}
		
		// if our grid is odd, our blank is in an even row, and inversions are even ==> solvable
		else {
			if (blankEven && ((inversion & 1) == 0)) {return true; }	
		}
		
		// otherwise our grid is odd, blank is in odd row, and inversions are odd   ==> not solvable
		return false; 													
		
	}
	*/
	
	/******************************************************************************
	 *  ///////////////////////////////////////////////////////////////////////////
	 *  IN USE!! This method uses a "twin board" to check whether a board is 
	 *  solvable or not. The twin method checks  a "twin"--i.e., flipping any tile 
	 *  in the grid (except for 0)--at the same time the original boar is checked 
	 *  for a correct sequence. If the twin reaches the goal state, then the original 
	 *  board is not solvable; if the original board reaches the goal state before 
	 *  the twin, then the original board is solvable. 
	 *  ///////////////////////////////////////////////////////////////////////////
	 *  
	 *  A tool to check whether the board is solvable or not. An unsolvable board 
	 *  has a variation (i.e., a twin) that leads to a solvable board--while an
	 *  unsolvable board, given A*, would continue running infinitely. If we know that
	 *  a twin leads to the goal board, then we know the initial board is unsolvable. 
	 *  
	 *  A twin is obtained by switching the integer 1 either up, down, left or right
	 *  depending on where the 0 is located. The 1 should not be switched with the 0 per
	 *  the spec.
	 *  
	 *  Example		
	 *      1  3       3  1       1  3       1  3       1  3       6  3
	 *      4  2  5    4  2  5    2  4  5    4  5  2    4  2  5    4  2  5
	 *      7  8  6    7  8  6    7  8  6    7  8  6    8  7  6    7  8  1
	 *      board      twin       twin       twin       twin       twin
	 *  
	 *  ==> the return of any twin is acceptable, the solver only uses one twin
	 *
	 *
	 ******************************************************************************/
	public Board twin() 					
	{ 
		int[][] twin = new int[n][n];

		for (int i = 0; i < n; i++) {		
	        for (int j = 0; j < n; j++) {
	        	if (board[i][j] == 1) {
	        		
	        		// check that we can move x value up and we are not on first row	
	        		// check that we are not on last row
	        		// check that we are not on right-most row
	        		// check that we are not in first row
	        		
	        		boolean isUpAvailable = (i - 1 < n && i > 0 && board[i-1][j] != 0);				
	        		boolean isDownAvailable = (i + 1 < n && j < n && board[i+1][j] != 0);		
	        		boolean isRightAvailable = (i < n && j + 1 < n && board[i][j+1] != 0);		
	        		boolean isLeftAvailable = (j != 0 && j - 1 < n && board[i][j-1] != 0);		

	        		if (isUpAvailable) {
	        			for (int q = 0; q < n; q++)
	        				// copy board
	        				twin[q] = board[q].clone();	
	        			// swap values, i goes 1 up and j stays the same
	        			int toSwap = board[i-1][j];			
	        			twin[i-1][j] = board[i][j];
	        			twin[i][j] = toSwap;
	        		}
	        		
	        		else if (isDownAvailable) {
	        			for (int q = 0; q < n; q++)
	        				twin[q] = board[q].clone();
	        			// i goes one down and j stays the same
	        			int toSwap = board[i+1][j];			
	        			twin[i+1][j] = board[i][j];
	        			twin[i][j] = toSwap;
	        		}
	        		
	        		else if (isRightAvailable) {
	        			for (int q = 0; q < n; q++)
	        				twin[q] = board[q].clone();
	        			// i goes one down and j stays the same
	        			int toSwap = board[i][j+1];			
	        			twin[i][j+1] = board[i][j];
	        			twin[i][j] = toSwap;
	        		}
	        		
	        		else if (isLeftAvailable) {
	        			for (int q = 0; q < n; q++)
	        				twin[q] = board[q].clone();
	        			// i goes one down and j stays the same
	        			int toSwap = board[i][j-1];			
	        			twin[i][j-1] = board[i][j];
	        			twin[i][j] = toSwap;
	        		}
	        	}
	        }
		}
		Board permutation = new Board(twin);
		return permutation; 
	}
	
	// see Robert Sedgewick, Kevin Wayne, Algorithms on page 103	
	public boolean equals(Object y) 										
	{ 
		if (this == y) 				return true;
        if (y == null) 					return false;
        if (this.getClass() != y.getClass()) 		return false;
        Board that = (Board) y;
        if (this.hamming() != that.hamming())	  	return false; 
        if (this.manhattan() != that.manhattan())	return false; 
        if (this.n != that.n)				return false; 
        
        for (int i = 0; i < n; i++) {
        	for (int j = 0; j < n; j++) {
        		if (this.board[i][j] != that.board[i][j]) {
        			return false; 
        		}
        	}
        }
        return true;
	}
	
	/******************************************************************************
	 *  Returns a queue filled with possible boards (where the 0 may be legally placed)
	 *  given an initial grid makeup. Legal moves involve left, right, up, and down,
	 *  with the exclusion of those moves that take the 0 off-grid or diagonal. The
	 *  queue is at most filled with 4 boards. 
	 *  
	 *  Example 1: 
	 *  	
	 *  	Start Board~~~~~~~~~~~~>  move ~~~~~~~~~~~~>  move
	 *  	0  1  2 		1  0  2 	    4  1  2
	 *  	4  5  6 		4  5  6		    0  5  6
	 *	7  8  3			7  8  3		    7  8  3
	 *
	 *  Example 2: 
	 * 
	 *   	Start Board~~~~~~~~~~~~>  move ~~~~~~~~~~~~> move  ~~~~~~~~~~~~> move  ~~~~~~~~~~~~> move
	 *  	5  1  2 		5  0  2 	   5  1  2		5  1  2		   5  1  2 
	 *  	4  0  6 		4  1  6		   4  8  6		0  4  6		   4  6  0
	 *	7  8  3			7  8  3		   7  0  3		7  8  3		   7  8  3
	 *
	 ******************************************************************************/
	public Iterable<Board> neighbors() 		
	{ 
		Queue<Board> toReturn = new Queue<Board>();
		
		for (int i = 0; i < n; i++) {		
	        for (int j = 0; j < n; j++) {
	        	if (board[i][j] == 0) {
	        		
	        	
	        		boolean isUpAvailable = (i - 1 < n && i > 0);			// check that we can move x value up and we are not on first row		
	        		boolean isDownAvailable = (i + 1 < n && j < n);			// check that we are not on last row
	        		boolean isRightAvailable = (i < n && j + 1 < n);		// check that we are not on right-most row
	        		boolean isLeftAvailable = (j != 0 && j - 1 < n);		// check that we are not in first row

	        		if (isUpAvailable) {
	        			// new 2D array to add to queue
	        			int[][] permutation = new int[n][n];				
	        			for (int q = 0; q < n; q++)
	        				// copy board
	        			    permutation[q] = board[q].clone();	
	        			// swap values, i goes 1 up and j stays the same
	        			int toSwap = board[i-1][j];							
	        			permutation[i-1][j] = board[i][j];
	        			permutation[i][j] = toSwap;
	        			Board permutation1 = new Board(permutation);
	        			toReturn.enqueue(permutation1);
	        		}
	        		
	        		if (isDownAvailable) {
	        			int[][] permutation = new int[n][n];
	        			for (int q = 0; q < n; q++)
	        			    permutation[q] = board[q].clone();
	        			// i goes one down and j stays the same
	        			int toSwap = board[i+1][j];							
	        			permutation[i+1][j] = board[i][j];
	        			permutation[i][j] = toSwap;
	        			Board permutation2 = new Board(permutation);
	        			toReturn.enqueue(permutation2);
	        		}
	        		

	        		if (isLeftAvailable) {
	        			int[][] permutation = new int[n][n];
	        			for (int q = 0; q < n; q++)
	        			    permutation[q] = board[q].clone();
	        			// j goes one down (left) and i stays the same
	        			int toSwap = board[i][j-1];							
	        			permutation[i][j-1] = board[i][j];
	        			permutation[i][j] = toSwap;
	        			Board permutation3 = new Board(permutation);
	        			toReturn.enqueue(permutation3);
	        		}
	        		
	        		if (isRightAvailable) {
	        			int[][] permutation = new int[n][n];
	        			for (int q = 0; q < n; q++)
	        			    permutation[q] = board[q].clone();
	        			// j goes one up (right) and i stays the same
	        			int toSwap = board[i][j+1];							
	        			permutation[i][j+1] = board[i][j];	
	        			permutation[i][j] = toSwap;
	        			Board permutation4 = new Board(permutation);
	        			toReturn.enqueue(permutation4);
	        		}
	        	}
	        }
		}
	return toReturn;
	}

	/**
	 * method taken from spec
	 */
	public String toString() 			
	{ 
		StringBuilder s = new StringBuilder();
	    s.append(n + "\n");
	    for (int i = 0; i < n; i++) {
	        for (int j = 0; j < n; j++) {
	            s.append(String.format("%2d ", board[i][j]));
	        }
	        s.append("\n");
	    }
	    return s.toString();
	}
	
	public static void main(String[] args)
	{
		
		int[][] tile = new int[4][4];
		tile[0][0] = 1;
		tile[0][1] = 2;
		tile[0][2] = 3;
		tile[0][3] = 4;
		tile[1][0] = 5;
		tile[1][1] = 0;
		tile[1][2] = 7;
		tile[1][3] = 8;
		tile[2][0] = 9;
		tile[2][1] = 6;
		tile[2][2] = 11;
		tile[2][3] = 12;
		tile[3][0] = 13;
		tile[3][1] = 10;
		tile[3][2] = 14;
		tile[3][3] = 15;
		
		int[][] tile1 = new int[3][3];
		tile1[0][0] = 0;
		tile1[0][1] = 2;
		tile1[0][2] = 3;
		tile1[1][0] = 4;
		tile1[1][1] = 1;
		tile1[1][2] = 5;
		tile1[2][0] = 7;
		tile1[2][1] = 8;
		tile1[2][2] = 6;
		
		Board initial = new Board(tile1); 
		System.out.println(initial.toString());
		System.out.println("Dimensions \t" + initial.dimension());
		System.out.println("Hamming \t" +initial.hamming());
		System.out.println("Manhattan \t" + initial.manhattan());
		System.out.println("Goal Board \t" + initial.isGoal());
		//System.out.println("Solvable \t" + initial.isSolvable());
		//System.out.println("Permutations: \n" + initial.neighbors());
		System.out.println("Twin: \n" + initial.twin());
		
	}
}
