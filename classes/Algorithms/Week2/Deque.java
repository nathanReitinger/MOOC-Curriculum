/******************************************************************************
 *
 *  Non-Iterator Operations:   Constant worst-case time
 *  Iterator Constructor:      Constant worst-case time
 *  Other Iterator Operations: Constant worst-case time
 *  Non-Iterator Memory Use:   Linear in current number of items
 *  Memory per Iterator:       Constant 
 *  
 *  This class represents a doubly-linked, add-end or add-beginning queue.
 *  This data structure is also known as a deque.
 *  
 *  Given input, and using generics, this class will:
 *
 *    - Create sentinel head and tail nodes ("sentinelFirst" and "sentinelLast")
 *    - Store new nodes at the beginning or end of the linked list
 *    - The beginning is after the sentinalFirst and end is before sentinalEnd
 *
 ******************************************************************************/

package Week2;
import java.util.Iterator;
import java.util.NoSuchElementException;
 

public class Deque<Item> implements Iterable<Item>
{	
    // before and after first and last elements are sentinel nodes
	private Node sentinelFirst;							
	private Node sentinelLast; 							
	private Node last; 
	private int size;
	
	public Deque() 
	{ 
		sentinelFirst = new Node(); 
		sentinelLast = new Node(); 
		sentinelFirst.next = sentinelLast;
		sentinelLast.previous = sentinelFirst;
	}
	
	private class Node 
	{
		Item item; 
		Node next;
		Node previous; 
	}
	
	public boolean isEmpty() { return size == 0; }
	public int size() { return size; }
	
	/** 
	 * create a new first node
	 * point first's next at sentinel's next (adding it to front)
	 * point first's previous at sentinel 
	 * node originally pointing to sentinel should point now at first
	 * sentinel's next should point to the new first
	 * 
	 * @param item
	 */
	public void addFirst(Item item)
	{
		if (item == null) throw new NullPointerException("Please do not add a null objecet");
		
		Node first = new Node();					
		first.item = item;
		first.next = sentinelFirst.next;			
		first.previous = sentinelFirst;				
		sentinelFirst.next.previous = first; 		
		sentinelFirst.next = first; 				 
		size++;
	}
	
	/**
	 * create a new node in last's original location
	 * set new node's previous pointer to node before sentinel last
	 * point new node at sentinel last
	 * node originally pointing to sentinel last now points at new node
	 * sentinel last's previous pointer now points at new node
	 * 
	 * @param item
	 */
	public void addLast(Item item) 
	{
		if (item == null) throw new NullPointerException("Please do not add a null item");
		
		last = new Node(); 							
		last.item = item; 
		last.previous = sentinelLast.previous; 		
		last.next = sentinelLast; 					
		sentinelLast.previous.next = last; 			
		sentinelLast.previous = last; 				
		size++;
	}
	
	/**
	 * per spec, grab item to return
	 * temp points to sentinel as a side-road to sentinel first's next node
	 * temp's next pointer points to the third node in the queue
	 * sentinel first now points to temp's next, i.e., the third node
	 * the third node in the set now points to sentinel node
	 * 
	 */
	public Item removeFirst() 
	{ 
		if (size == 0) throw new NoSuchElementException("There are no items to remove!");
		
		Node temp = sentinelFirst.next; 
		Item item = temp.item; 							
		temp.previous = sentinelFirst; 					
		temp.next = sentinelFirst.next.next; 			
		sentinelFirst.next = temp.next; 				
		sentinelFirst.next.previous = temp.previous; 	
		temp = null;
		size--; 
		return item;     	
	}
	
	/**
	 * grab item to return
	 * the third to last node (including sentinelLast) is now temp's previous pointer
	 * temp's next pointer points to sentinelLast
	 * sentinelLast now points through temp's previous pointer to skip the node
	 * the node before temp should now point at sentinel last
	 * (above) ==> could also used sentinalLast.previous.next = sentinelLast
	 * 
	 */
	public Item removeLast()
	{
		if (size == 0) throw new NoSuchElementException("There are no items to remove!");
		
		Node temp = sentinelLast.previous; 
		Item item = temp.item; 							
		temp.previous = sentinelLast.previous.previous; 
		temp.next = sentinelLast; 						
		sentinelLast.previous = temp.previous;			
		sentinelLast.previous.next = temp.next;			 
		temp = null;									
		size--; 
		return item;
	}
	
	// note the FIFO order requirement 
	public Iterator<Item> iterator() 					
		{ return new DequeIterator(); }
		
	private class DequeIterator implements Iterator<Item> 
	{
		private Node current;
		
		public DequeIterator() {
			// given FIFO, we start at the "first in" element
			this.current = sentinelFirst.next; 			
		}
		
		public void remove()      { throw new UnsupportedOperationException();  }
		public boolean hasNext() { return current.next != null; }
		public Item next() 
		{
			if (!this.hasNext()) throw new NoSuchElementException(); 
			Item item = current.item; 
			// given FIFO, walk back from front of the queue to back of the queue
			current = current.next; 					
			return item;
		}
	}
	
	public static void main(String[] args)
	{
        Deque<Integer> test = new Deque<Integer>();

        System.out.println("dtest: " + test.toString());
       
        test.addFirst(1);
        test.addFirst(2);
        test.addFirst(3);
        test.addFirst(4);
        test.addFirst(5);
        
        test.removeLast();
        test.removeFirst();
        test.removeFirst();
        
        test.removeLast();
        test.removeLast();
        
        test.addFirst(1);
        test.addLast(2);
        test.addFirst(3);
        test.addLast(4);
        
        Iterator itr = test.iterator();

        System.out.println(itr.next());
        System.out.println(itr.next());
        System.out.println(itr.next());
        System.out.println(itr.next());

        
	}
	
}
