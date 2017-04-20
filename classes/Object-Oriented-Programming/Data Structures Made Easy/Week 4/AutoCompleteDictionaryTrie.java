package spelling;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/** 
 * An trie data structure that implements the Dictionary and the AutoComplete ADT
 * @author You
 *
 */
public class AutoCompleteDictionaryTrie implements  Dictionary, AutoComplete {

    private TrieNode root;
    private int size;

    public AutoCompleteDictionaryTrie()
	{
		root = new TrieNode();
	}
	
	/** Insert a word into the trie.
	 * For the basic part of the assignment (part 2), you should convert the 
	 * string to all lower case before you insert it. 
	 * 
	 * This method adds a word by creating and linking the necessary trie nodes 
	 * into the trie, as described outlined in the videos for this week. It 
	 * should appropriately use existing nodes in the trie, only creating new 
	 * nodes when necessary. E.g. If the word "no" is already in the trie, 
	 * then adding the word "now" would add only one additional node 
	 * (for the 'w').
	 * 
	 * @return true if the word was successfully added or false if it already exists
	 * in the dictionary.
	 */
	public boolean addWord(String word) 
	{
	    //TODO: Implement this method.
		word = word.toLowerCase();		
		
		TrieNode crawler = new TrieNode();
		//this set to root
		crawler = root;										
		
		for (int i = 0; i < word.length(); i++) {
			char ch = word.charAt(i);
			//check if there is duplicate character in map
			TrieNode checker = crawler.getChild(ch);	
			//if null, put new char in old hashmap (node)	
			if (checker == null) {							
				checker = new TrieNode();
				//next is returned, so crawler is next
				crawler = crawler.insert(ch);				
			}												
			//if this is not a new character, move down that chain, the next getChild(ch) call
			else {											
				crawler = checker;
			}
			
		}
		//at the end of the loop, if this is still true the word must not exist
		if (crawler.endsWord() == true) {					
			return false; 
		}
		//because we ended our loop, word is complete
		crawler.setEndsWord(true);	
		//now we can add size, not before when dealing with characters						
		size++;												
		return true; 
	}
	
	/** 
	 * Return the number of words in the dictionary.  This is NOT necessarily the same
	 * as the number of TrieNodes in the trie.
	 */
	public int size()
	{
	    //TODO: Implement this method
	    return size;
	}
	
	/** Returns whether the string is a word in the trie, using the algorithm
	 * described in the videos for this week. */
	@Override
	public boolean isWord(String s)
	{
	    // TODO: Implement this method
		//if the string is empty
		if (s == "") {										
			return false; 
		}
		s = s.toLowerCase();
		TrieNode crawler = new TrieNode();
		crawler = root;	
		
		
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			//check if there is duplicate character in map
			TrieNode checker = crawler.getChild(ch);
			//if we have a valid character, it's the hashmap		
			if (checker != null) {	
				//move on to the net one						
				crawler = checker;							
			}
			//if this is not in the map, the word is not in the dictionary
			else {											
				return false;
			}
		}

		String test = crawler.getText(); 
		System.out.println(test);
		
		if (crawler.endsWord() == false) {
			return false;
		}
		return true; 
	}

	/** 
     * Return a list, in order of increasing (non-decreasing) word length,
     * containing the numCompletions shortest legal completions 
     * of the prefix string. All legal completions must be valid words in the 
     * dictionary. If the prefix itself is a valid word, it is included 
     * in the list of returned words. 
     * 
     * The list of completions must contain 
     * all of the shortest completions, but when there are ties, it may break 
     * them in any order. For example, if there the prefix string is "ste" and 
     * only the words "step", "stem", "stew", "steer" and "steep" are in the 
     * dictionary, when the user asks for 4 completions, the list must include 
     * "step", "stem" and "stew", but may include either the word 
     * "steer" or "steep".
     * 
     * If this string prefix is not in the trie, it returns an empty list.
     * 
     * @param prefix The text to use at the word stem
     * @param numCompletions The maximum number of predictions desired.
     * @return A list containing the up to numCompletions best predictions
     */@Override
     public List<String> predictCompletions(String prefix, int numCompletions) 
     {
    	 // TODO: Implement this method
    	 // This method should implement the following algorithm:
    	 // 1. Find the stem in the trie (the ea which branches from there).  If the stem does not appear in the trie, return an
    	 //    empty list
    	 // 2. Once the stem is found, perform a breadth first search to generate completions
    	 //    using the following algorithm:
    	 //    Create a queue (LinkedList) and add the node that completes the stem to the back
    	 //       of the list.
    	 //    Create a list of completions to return (initially empty)
    	 //    While the queue is not empty and you don't have enough completions:
    	 //       remove the first Node from the queue
    	 //       If it is a word, add it to the completions list
    	 //       Add all of its child nodes to the back of the queue
    	 // Return the list of completions
    	 
    	LinkedList<String> completions = new LinkedList<String>();
    	TrieNode crawler = new TrieNode();
 		crawler = root;	
 
 		if (numCompletions == 0) {
    		return completions;
    	}
 		
 		//first find the starting point
 			for (int i = 0; i < prefix.length(); i++) {
 				char ch = prefix.charAt(i);
				//check if there is duplicate character in map
 				TrieNode checker = crawler.getChild(ch);	
				//if we have a valid character, it's the hashmap	
 				if (checker != null) {							
 					crawler = checker;		
 				}
 				else {
					//because it is null
 					return completions; 						
 				}
 				
 			}
 		//now that I have stem, ====>crawler<==== i need to perform breadth-first search and put results in linked list
 			
 		stack_queue(crawler, numCompletions, completions);				
 		//System.out.println(completions);
 		//System.out.println(numCompletions);
 		//System.out.println(completions.size());
 		return completions;
	}
 
	public void stack_queue(TrieNode curr, int numCompletions, LinkedList<String> completions)
	{
		Queue<TrieNode> q = new LinkedList<TrieNode>();
		//adding our "root" to the start
		q.add(curr);											
		
		//until we bottom-out the queue
		while (!q.isEmpty() && numCompletions != 0) {			
			//remove first element (first is current in first instance)
			TrieNode temp = q.remove();							
			TrieNode next = null;
			if (temp.endsWord() == true) {
				completions.add(temp.getText());	
				numCompletions--;
			}
			//count down one character
			for (Character c : temp.getValidNextCharacters()) {	
				//if there is another character down the chain
				if (temp != null) {			
					//set this to next, and add it to the list, so that it will be checked for word else continue on to next character	
					//adding it to the end of the queue				
					next = temp.getChild(c);					
					((LinkedList<TrieNode>) q).addLast(next);	
				}
			}
		}													
	}
 	// For debugging
 	public void printTree()
 	{
 		printNode(root);
 	}
 	
 	/** Do a pre-order traversal from this node down */
 	public void printNode(TrieNode curr)
 	{
 		if (curr == null) {
 			System.out.println("all nodes are nulled out!");
 			return;
 		}
 		System.out.println("checking nodes . . . ");
 		System.out.println(curr.getText());
 		
 		TrieNode next = null;
 		for (Character c : curr.getValidNextCharacters()) {
 			next = curr.getChild(c);
 			printNode(next);
 		}
 	}
 	

	
}