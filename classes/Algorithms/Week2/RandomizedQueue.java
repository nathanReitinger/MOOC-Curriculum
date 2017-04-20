/******************************************************************************
 *
 *  Non-Iterator Operations:   Constant worst-case time
 *  Iterator Constructor:      Linear in current number of items
 *  Other Iterator Operations: Constant worst-case time
 *  Non-Iterator Memory Use:   Linear in current number of items
 *  Memory per Iterator:       Linear in current number of items 
 *  
 *  This class represents a queue which stores (enqueue) and outputs (dequeue) 
 *  random numbers. The queue implements a dynamic array by: (1) resizing the array when 
 *  the array reaches capacity (size == queue.length); and (2) resizing the array
 *  when the array lacks elements to a quarter of its length (size == queue.length/4).
 *  The array uses a Knuth Shuffle to achieve output of random items.
 *  
 *  Given input, and using generics, this class will:
 *
 *    - Create a queue to be filled with user-specified objects
 *    - Support enqueue by inserting an element into a randomly-selected index
 *    - Support dequeue by removing a randomly-selected item 
 *
 ******************************************************************************/

package Week2;
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;


public class RandomizedQueue<Item> implements Iterable<Item> 
{
	// queue array of elements 
	private Item[] queue; 											
	private int size; 
	private int tail, head;

	public RandomizedQueue()
	{
		// initializes empty queue ==> uses casting
		queue = (Item[]) new Object[2]; 							
		size = 0;
		// setting tail to 1 and not 0 allows the first element to be 0 indexed
		tail = 1;													
	}

	public boolean isEmpty() 	{ return size == 0; }
	public int size() 			{ return size; }
	public void enqueue(Item item)
	{
		if (item == null) throw new NullPointerException("Please do not add a null objecet");
		
		// resizing array 
		// taken from lecture, copies over appropriate elements
		// head stays constant 
		// adjust tail so that modulo picks up the end of the queue when it resizes
		if (size == queue.length) {
			Item[] temp = (Item[]) new Object[2*queue.length];
			for (int i = 0; i < size; i ++) {
				temp[i] = queue[i];
			}
			queue = temp;											
			head = 0; 												
			tail = size-1; 											
		}
		
		// places item in the order the items are enqueued
		// provides a random number to switch with tail so that we keep the queue random
		// grabs random number to switch with tail
		// set the tail to our randomly-generated-but-still-within-list item
		// put the randomly generated item at the tail
		tail = (tail+1) % queue.length;								
		size++;
		int randomIndex =  StdRandom.uniform(size);					
		Item toSwitch = queue[randomIndex];							
		queue[tail] = toSwitch;										
		queue[randomIndex] = item;									
		
		
	}
	public Item dequeue()
	{
		if (isEmpty()) throw new NoSuchElementException("There are no items to remove!");
		else {

			// keep track of random index in order to switch the two elements
			int randomIndex =  StdRandom.uniform(size);				
			Item toDelete = queue[randomIndex]; 
			Item toSwitch = queue[tail];
			// switching the two elements, toDelete goes to the tail and tail goes to toDelete's location
			queue[randomIndex] = toSwitch; 							
			queue[tail] = toDelete;
			// mitigate loitering 
			queue[tail] = null; 									
			size--;
			tail--;			
			
			// adjust array
			if (size > 0 && size == queue.length/4) {
				Item[] temp = (Item[]) new Object[queue.length/2];
				for (int i = 0; i < size; i ++) {
					temp[i] = queue[i];
				}
				queue = temp;
				tail = size-1;			
			}	
			return toDelete;
		}	
	}
	public Item sample()
	{
		if (isEmpty()) throw new NoSuchElementException("There are no items to remove!");
		// returns but does not delete an element
		int randomIndex =  StdRandom.uniform(size);					
		return queue[randomIndex];
	}
	
	public Iterator<Item> iterator() { return new RandomizedQueueIterator(); }
	
	// see http://algs4.cs.princeton.edu/13stacks/ResizingArrayStack.java.html
	private class RandomizedQueueIterator implements Iterator<Item>	
	{
		// need to create a new randomizedQueue to return for random iterator values
		private Item[] copy; 
		private RandomizedQueueIterator() {
			copy = (Item[]) new Object[size];
			for (int i = 0; i < queue.length; i++) {
				if (queue[i] != null) {
					copy[i] = queue[i];								
				}
			}
			// works like Knuth shuffle
			for (int i = 0; i < copy.length; i++) {					
				int randomIndex =  StdRandom.uniform(size);	
				Item toSwitch = copy[randomIndex];
				copy[randomIndex] = copy[i];
				copy[i] = toSwitch;
			}
		}
		
		public void remove() { throw new UnsupportedOperationException();  }
		private int count = 0;
		public boolean hasNext() {return count < size; }
		public Item next()
		{
			if (!hasNext()) throw new NoSuchElementException(); 
			// use copy instead of queue for nested iterator randomness 
			Item item = copy[(count + head) % copy.length];			
			count++;
			return item;
		}
	}
	
	public static void main(String[] args)
	{
		
		 RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();

	        rq.enqueue(0);
	        rq.enqueue(1);
	        rq.enqueue(2);
	        rq.enqueue(3);
	        rq.enqueue(4);
	        rq.enqueue(5);
	        rq.enqueue(6);
	        rq.enqueue(7);
	        rq.enqueue(8);
	        rq.enqueue(9);

	        System.out.println("items: " + rq.size);

	        System.out.println(rq.toString());

	        System.out.println(rq.dequeue());
	        System.out.println(rq.dequeue());
	        System.out.println(rq.dequeue());

	        //System.out.println(rq.to`String());
	        System.out.println("items: " + rq.size);

	        Iterator it1 = rq.iterator();
	        Iterator it2 = rq.iterator();

	        while (it1.hasNext()) {
	            System.out.print(it1.next() + ",");
	        }
	        System.out.println("\n");
	        while (it2.hasNext()) {
	            System.out.print(it2.next() + ",");
	        }

	}
}
