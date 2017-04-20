/******************************************************************************
 *  Compilation:  javac Solver.java
 *  Dependencies: Board.java
 *  
 *  See http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
 *  
 *  Solves the 8 puzzle by representing possible moves as search nodes. The
 *  search nodes are stored in a priority queue ranked by Manhattan distance. 
 *  The solvable versus not-solvable board problem is overcome by concurrently 
 *  solving a "twin" board. If the twin board reaches the goal state, then the 
 *  initial board is not solvable; if the initial board reaches the goal state, 
 *  then the board is solvable. Optionally, using the method isSolvable in Board.java
 *  handles the solvability issue by checking the polarity of each board. 
 *  
 *  Example:
 *  
 *  			Initial Search Node (SN)
 *				0  1  3
 *		    ___________	4  2  5 ____________ 
 *		   |		7  8  6 	    |
 * 		   |				    |
 *		1  0  3				 4  1  3
 * next SN ==>	4  2  5				 0  2  5 <=== currently on PQ
 * 	_______	7  8  6	_______			 7  8  6 
 *     |	   |	 	|
 *     |	   |	  	|
 *  0  1  3	1  2  3	    1  3  0 
 *  4  2  5	4  0  5	    4  2  4
 *  7  8  6	7  8  6	    7  8  6
 *  ^		^	    ^
 * not added	on PQ	    on PQ
 * to PQ, see
 * initial SN
 *	
 ******************************************************************************/
package Week4;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MaxPQ;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
	
	private boolean solvable; 
	private int totalMoves;
	private Stack<Board> solution = new Stack<Board>();
	
	// comparator to sort MinPQ and grab top-most nodes 
	private class manhattanComparator implements Comparator<node>					
	{
		@Override
		public int compare(node one, node two)
		{
			// priority takes into account moves and Manhattan distance
			if (one.priority > two.priority)  { return +1; }						
			if (one.priority < two.priority)  { return -1; }
			else { return 0; }
		}
	}
	
	private class node {
		private int priority; 
		private int moves; 
		private int manhattan; 
		private Board board; 
		private node previous; 
		
		// add board and previous as arguments to properly link each board to previous
		public node(Board board, node previous) {									
			this.board = board;
			this.previous = previous;
			this.manhattan = board.manhattan();
			
			// moves need to aggregate not depending on each pass, but when the node is created
			if (null == this.previous) {											
				this.moves = 0; 
			}
			// if not the first pass, we need to add the last move to 1
			else { this.moves = this.previous.moves + 1; }	
			// simplification for the comparator 
			this.priority = manhattan + moves; 										
		}
	}

	/******************************************************************************
	 *  Uses the A* algorithm (relying on a Manhattan heuristic) to determine a best
	 *  path for a 0 to travel through an n-by-n grid. 
	 * 
	 *  This method proceeds as follows:
	 *  	- take out first node in priority queue (initial) 
	 *  	- add to solution queue
	 *  	- check against goal
	 *  	- get neighbors of that node
	 *  	- sort neighbors into priority queue by Manhattan distance (comparator) 
	 *  	- repeat until goal is reached
	 * 		===> repeat this process with twin if using twin method
	 *
	 ******************************************************************************/
	public Solver(Board initial)			
	{ 
		// using the comparator, we will keep minimum distance nodes at the front
		MinPQ<node> boardsInitial = new MinPQ<node>(new manhattanComparator());
		// parallel for our twin (this PQ is unnecessary when using the parity version)
		MinPQ<node> boardsTwin = new MinPQ<node>(new manhattanComparator());		
		// our starting position, its pointer leads to null because it is first (not doubly connected)
		node initialSN = new node(initial, null);									
		node initialTwin = new node(initial.twin(), null);
		
		boardsInitial.insert(initialSN);
		boardsTwin.insert(initialTwin);

		while (!boardsInitial.isEmpty()) {
			// the current node changes on each iteration as 
			// we continually add and remove the highest priority node
			node currentSN = boardsInitial.delMin();			
			node currentTwin = boardsTwin.delMin();
			
			if (currentSN.board.isGoal()) {
				// global trigger to use when responding to isSolvable() method
				solvable = true; 
				// global trigger to use when responding to moves() method
				totalMoves = currentSN.moves;					
				node temp = currentSN;
				// using a stack for reverse order, walk backwards through our connected, highest priority nodes
				while (null != temp.previous) {					
					solution.push(temp.board);
					// loop to continue until the initial node is reached
					temp = temp.previous;						
				}
				// add initial to the top, for return on solution() method
				solution.push(initial);							
				break; 
			}
			if (currentTwin.board.isGoal()) {
				// if our twin is solvable, we know that initial is not a solvable grid
				solvable = false; 								
				totalMoves = -1;
				solution = null;
				break; 
			}
			
			// only add nodes that are not identical to current.previous.board (the last nodes, previous board)
			// this is a slight efficiency boost
			for (Board b : currentSN.board.neighbors()) {		
				if (currentSN.previous == null || !b.equals(currentSN.previous.board)) {
					node neighborSN = new node(b, currentSN);
					// add a new node to the priority queue, make sure to add its previous pointer
					boardsInitial.insert(neighborSN);			 
				}
			}

			for (Board b : currentTwin.board.neighbors()) {
				if (currentTwin.previous == null || !b.equals(currentTwin.previous.board)) {
					node neighborTwin = new node(b, currentTwin); 
					boardsTwin.insert(neighborTwin);
				}
			}
		}
	}
	
	public boolean isSolvable()			{ return solvable;   }
	public int moves()				{ return totalMoves; }
	public Iterable<Board> solution()		{ return solution;   }
	
	public static void main(String[] args)
	{
		
		int[][] tile = new int[3][3];
		tile[0][0] = 1;
		tile[0][1] = 2;
		tile[0][2] = 3;
		tile[1][0] = 4;
		tile[1][1] = 5;
		tile[1][2] = 6;
		tile[2][0] = 7;
		tile[2][1] = 8;
		tile[2][2] = 0;

		Board initial = new Board(tile);
		Solver newnew = new Solver(initial);
		
		System.out.println("Solvable: \t" + newnew.solvable);
		System.out.println("Moves: \t\t" + newnew.totalMoves);
		System.out.println("Solution: \n" + newnew.solution());
		
		/** PROVIDED TEST CLIENT 
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] blocks = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
				StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
		*/
	}
}
