/******************************************************************************
 *  Dependencies:  javac PercolationVisualizer.java  ==> for visualization 
 *
 *  This program takes as input the file representation of a grid.
 *  From that file, it:
 *
 *    - Reads the grid size n of the percolation system.
 *    - Creates an n-by-n grid of nodes (also called sites)
 *    - Initially represents these nodes as "closed" or non-conductive
 *    - Reads in a sequence of sites (row i, column j) to open (i.e., connect).
 *    - Connects the sites to adjacent nodes and a virtual top/virtual bottom.
 *    - Checks whether the grid "percolates" --i.e, is connected from top to bottom.
 *
 ******************************************************************************/
package Week1;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
 
 private int gridSize; 
 private boolean[][] grid;
 private int virtualTop; 
 private int virtualBottom; 
 private WeightedQuickUnionUF uf;
 private WeightedQuickUnionUF uf2;

 // create n-by-n grid, with all sites blocked
 // using 1D array, searchable representation of 2D grid
 public Percolation(int n)           
 { 
  if (n <= 0) { throw new IllegalArgumentException("n is out of bounds"); }
  grid = new boolean[n+1][n+1];
  gridSize = n*n;
  virtualTop = 0;
  virtualBottom = gridSize + 1;
  uf = new WeightedQuickUnionUF(gridSize + 2); 
  uf2 = new WeightedQuickUnionUF(gridSize + 2);
 }
 
 // returns 1d array position from 2d array's location
 // we accept indices starting at 1 and not 0
 // because I used real grid size (not row grid size) use sqrt for row/column length
 private int xyTo1D(int x, int y)         
 {
  x = x-1;              
  y = y-1;
  return (int) ((x* (Math.sqrt(gridSize)) + y) + 1);    
 }
 
 // either row/column more than row/column length of grid or 0 number
 private void validIndex(int x, int y)
 {
  if (xyTo1D(x, y) > gridSize || x > Math.sqrt(gridSize) || y > Math.sqrt(gridSize) || x <= 0 || y <= 0)
   throw new IndexOutOfBoundsException("index out of bounds!");
 }
 
 // open site (row i, column j) if it is not open already
 // open this location for later union
 // checks index for validity
 public void open(int i, int j)          
 {
  validIndex(i, j);             
  int index1d = xyTo1D(i, j);
  grid[i][j] = true;             

  // grab all adjacent nodes (down, up, left, right) if they are not out-of-bounds
  boolean boundsDown = (i != 1) && (grid[i-1][j]);  
  boolean boundsUp = (i != Math.sqrt(gridSize)) && (grid[i+1][j]);
  boolean boundsLeft = (j != 1) && (grid[i][j-1]);
  boolean boundsRight = (j != Math.sqrt(gridSize)) && (grid[i][j+1]);
    
  // if any adjacent node is open, connect it 
  if (boundsDown) uf.union(index1d, xyTo1D(i-1, j));
      if (boundsDown) uf2.union(index1d, xyTo1D(i-1, j)); 
  if (boundsUp) uf.union(index1d, xyTo1D(i+1, j));
      if (boundsUp) uf2.union(index1d, xyTo1D(i+1, j));
  if (boundsLeft) uf.union(index1d, xyTo1D(i, j-1)); 
      if (boundsLeft) uf2.union(index1d, xyTo1D(i, j-1));
  if (boundsRight) uf.union(index1d, xyTo1D(i, j+1));
      if (boundsRight) uf2.union(index1d, xyTo1D(i, j+1));
  
  // if index1d appears in top row, connect it to virtualTop
  if (index1d <= Math.sqrt(gridSize)) uf.union(virtualTop, index1d);
      if (index1d <= Math.sqrt(gridSize)) uf2.union(virtualTop, index1d);
  
  
  // if index1d appears in bottom row, connect it to virtualBottom
  if (index1d >= gridSize - Math.sqrt(gridSize)+1) {   
   uf.union(virtualBottom, index1d);
  }
 }
 
 // is site (row i, column j) open?
 public boolean isOpen(int i, int j)         
 {
  validIndex(i, j);
  return grid[i][j];          
 }
 
 // is site (row i, column j) full?
 // is the passed in site connected to the virtual top (which connects to top row)
 public boolean isFull(int i, int j)         
 {
  validIndex(i, j);
  return uf2.connected(xyTo1D(i, j), virtualTop);     
 }
 
 // does the system percolate? (is top connected to bottom)
 // merely check top to bottom, because they are connected to first-row/law-row nodes
 public boolean percolates()           
 {
  return uf.connected(virtualTop, virtualBottom);    
 }
}