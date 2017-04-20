package textgen;

import java.util.AbstractList;


/** A class that implements a doubly linked list
 * 
 * @author UC San Diego Intermediate Programming MOOC team
 *
 * @param <E> The type of the elements stored in the list
 */
public class MyLinkedList<E> extends AbstractList<E> {
	LLNode<E> head;
	LLNode<E> tail;
	int size;

	/** Create a new empty LinkedList */
	public MyLinkedList() {
		// TODO: Implement this method
		
		size = 0;
		head = new LLNode<E> (null);
		tail = new LLNode<E> (null);
		//linking the head and tail
		head.next = tail;													
		tail.prev = head; 
		
		head.prev = null;
		tail.next = null;
	}

	/**
	 * Appends an element to the end of the list
	 * @param element The element to add
	 */
	public boolean add(E element) throws NullPointerException
	{
		
		// TODO: Implement this method
		LLNode<E> to_add = new LLNode<E>(element);
		
		//someone is trying to add a null pointer, impermissible 
		if (to_add.data == null) {												
			throw new NullPointerException();
		}

		//adding to end of list (tail), new node's next points to tail
		//new node's prev points to whatever tail was pointing to prev
		//tail's previous node now points to new node instead of tail
		//tail's previous node points to to add
		//don't forget to increase our size because of the additional node
		to_add.next = tail; 												
		to_add.prev = tail.prev;											
		tail.prev.next = to_add; 											
		tail.prev = to_add;													
		size++;																
		return true ; 
		
	}

	/** Get the element at position index 
	 * @throws IndexOutOfBoundsException if the index is out of bounds. */
	public E get(int index) throws IndexOutOfBoundsException				
	{
		// TODO: Implement this method. 
		
		LLNode<E> traverse = new LLNode<E>(null);
		traverse = head.next;
		
		if (index < 0 || index > size - 1) {
			throw new IndexOutOfBoundsException("Out of Bounds!!");
		}
	
		//if we are at the correct index location
		//return the "element" at that position 
		for (int i = 0; i < size - 1; i++) {
			if (i == index) {												
				return (E) traverse.data;									
			}
			//if not correct index, keep looking by moving next
			else {
				traverse = traverse.next;									
			}
		}
		
	return (E) traverse.data;
	}

	/**
	 * Add an element to the list at the specified index
	 * @param The index where the element should be added
	 * @param element The element to add
	 */
	public void add(int index, E element ) throws IndexOutOfBoundsException 
	{
		// TODO: Implement this method
		//System.out.println(size + "is the size");
		//System.out.println(index+ "is the index");
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Out of Bounds!!");
		}
		
		
		
				LLNode<E> to_add = new LLNode<E>(element);	
				LLNode<E> traverse = new LLNode<E>(null);

				if (to_add.data == null) {								
					throw new NullPointerException();
				}	
				
				//putting the new node before the old index	
				//pointing new node's prev at traversal node's prev
				//pointing old node's prev at new node, thereby growing the list
				//pointing old node's previous node at new node
				traverse = head.next;
				for (int i = 0; i < size - 1; i++) {						
					if (i == index) {
						to_add.next = traverse;								 
						to_add.prev = traverse.prev; 						
						to_add.next.prev = to_add; 							
						to_add.prev.next = to_add; 							
						break;									
					}
					else {
						traverse = traverse.next;
					}
				}
	size++;
	}
	
	/** Return the size of the list */
	public int size() 
	{
		// TODO: Implement this method
		return size;
	}

	/** Remove a node at the specified index and return its data element.
	 * @param index The index of the element to remove
	 * @return The data element removed
	 * @throws IndexOutOfBoundsException If index is outside the bounds of the list
	 * 
	 */
	public E remove(int index) throws IndexOutOfBoundsException
	{
		
		// TODO: Implement this method
		//we need a temporary node because we don't want to lose the list
		LLNode<E> traverse = new LLNode<E>(null);
		LLNode<E> temp = new LLNode<E>(null);								
		traverse = head.next;
		temp = head.next;
		E value = null;
		
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Out of Bounds!!");
		}
		
		//DON'T forget the returned value as part of unit testing
		//use temp node next to keep pointers to traverse's next 
		//use temp node prev to keep pointer to traverse's prev
		//traverse's previous node should now point to traverse's next node, through temp's next node
		//traverse's next node should now point to traverse's prev node, through temp's prev node
		//null out temp to delete it
		for (int q = 0; q < size-1; q++) {							
			if (q == index) {
				value = traverse.data;										
				temp.next = traverse.next;									
				temp.prev = traverse.prev;									
				traverse.prev.next = temp.next;								
				traverse.next.prev = temp.prev;								
				temp = null;												
				break;
				//System.out.println("Successfully deleted one node");
			}
			else {
				//System.out.println("Successfully moved one node over");
				//need to move the temporary node and the traversal node
				traverse = traverse.next;									
				temp = temp.next;
			}
		}
	size--;
	return value;
	}

	/**
	 * Set an index position in the list to a new element
	 * @param index The index of the element to change
	 * @param element The new element
	 * @return The element that was replaced
	 * @throws IndexOutOfBoundsException if the index is out of bounds.
	 */
	public E set(int index, E element) throws IndexOutOfBoundsException
	{
		// TODO: Implement this method
		
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException("Out of Bounds!!");
		}
		
		LLNode<E> to_change = new LLNode<E>(element);	
		LLNode<E> traverse = new LLNode<E>(null);
		traverse = head.next;
		if (element == null) {										
			throw new NullPointerException();
		}
		//if we found the location we were looking for
		//point replacement node's next value at index's next value
		//point replacement node's prev value at index's (traverse's) prev value
		//have traverse's next node point instead to replacement node
		//have traverse's prev node point instead to replacement node
		for (int q = 0; q < size-1; q++) {								
			if (q == index) {												
				to_change.next = traverse.next; 								
				to_change.prev = traverse.prev; 							
				to_change.next.prev = to_change; 							
				to_change.prev.next = to_change;							
				break;
				
			}
			else {
				traverse = traverse.next;
			}
		}

	return (E) traverse.data;
	}   
	
}

class LLNode<E> 
{
	LLNode<E> prev;
	LLNode<E> next;
	E data;

	// TODO: Add any other methods you think are useful here
	// E.g. you might want to add another constructor

	public LLNode(E e) 
	{
		this.data = e;
		this.prev = null;
		this.next = null;
	}

	

}
