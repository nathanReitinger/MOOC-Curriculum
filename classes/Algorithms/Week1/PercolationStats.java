/******************************************************************************
 *  Dependencies: Percolation.java
 *  Compilation: java PercolationStats <grid_size> <no._of_trials>
 *
 *  This program takes as command-line input n (grid size) and trials (number of trials).
 *  It generates trials for Monte Carlo simulation to obtain a percolation threshold.
 *  From the input of trials and grid size, it:
 *
 *    - Creates a percolation grid of n-by-n size.
 *    - Reads in a sequence of randomly generated numbers between 1 and n (e.g., (1,1) or (1,3)).
 *    - Checks whether the number representing a grid location creates a percolation.
 *     - A percolation is a connection from the top row of the grid to the bottom row.
 *    - Loop through iterations of non-percolation, counting ("count") each iteration of the loop 
 *    - Once percolation is complete, record the fraction of sites that are open
 *     - This step allows us to estimate the percolation threshold
 *    - Print out mean, standard deviation, and 95% confidence interval 
 *  
 *  For more information, see http://coursera.cs.princeton.edu/algs4/assignments/percolation.html 
 *
 ******************************************************************************/

package Week1;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

 private double[] trialResults;
 
 // perform trials independent experiments on an n-by-n grid
 public PercolationStats(int n, int trials)        
 {
  if (trials <= 0 || n <= 0) {
   throw new IllegalArgumentException("please enter appropriate number of trials and grid size");
  }
  // double array where I will hold my trial heuristic 
  trialResults = new double[trials];      
  
  // the resulting string of doubles (used by StdStats) must come from percentages of connected nodes
  for (int i = 0; i < trials; i++) {      
   Percolation tester = new Percolation(n);
   trialResults[i] = (runTest(tester, n)/(n*n));   
  }              
  
 }
 // count should be reset each trial
 // random number within grid (1, 1) to (n, n), stated simply 1-n
 // do not count those random numbers' graph locations that are already opened
 // increment upon opening new node
 private double runTest(Percolation tester, int n) {
  double count = 0;          
  while (!tester.percolates()) {
   int row = StdRandom.uniform(1, n+1);     
   int column = StdRandom.uniform(1, n+1);
   if (!tester.isOpen(row, column)) {     
    tester.open(row, column);
    count++;          
   }
  }
  return count;
 }
 
 // sample mean of percolation threshold
 public double mean()                              
 {
  return StdStats.mean(trialResults);
 }
 
 // sample standard deviation of percolation threshold
 public double stddev()                            
 {
 return StdStats.stddev(trialResults);
 }
 
 // low  endpoint of 95% confidence interval
 // using provided formula, see http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 
 public double confidenceLo()                      
 {               
  return (StdStats.mean(trialResults) - 1.96*StdStats.stddev(trialResults)/Math.sqrt(trialResults.length));
 }
 
 // high endpoint of 95% confidence interval
 public double confidenceHi()                      
 {
  return (StdStats.mean(trialResults) + 1.96*StdStats.stddev(trialResults)/Math.sqrt(trialResults.length));
 }
 
 // test client (described below)
 public static void main(String[] args)         
 {
  PercolationStats stats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
  System.out.println("mean" + "\t\t\t = " + stats.mean());
  System.out.println("stddev" + "\t\t\t = " + stats.stddev());
  System.out.println("95% confidence interval" + "\t = " + stats.confidenceLo() + "," + stats.confidenceHi());
 }
}