/**
 * 
 */
package spelling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author UC San Diego Intermediate MOOC team
 *
 */
public class NearbyWords implements SpellingSuggest {
	// THRESHOLD to determine how many words to look through when looking
	// for spelling suggestions (stops prohibitively long searching)
	// For use in the Optional Optimization in Part 2.
	private static final int THRESHOLD = 1000; 

	Dictionary dict;

	public NearbyWords (Dictionary dict) 
	{
		this.dict = dict;
	}

	/** Return the list of Strings that are one modification away
	 * from the input string.  
	 * @param s The original String
	 * @param wordsOnly controls whether to return only words or any String
	 * @return list of Strings which are nearby the original string
	 */
	public List<String> distanceOne(String s, boolean wordsOnly )  {
		   List<String> retList = new ArrayList<String>();
		   insertions(s, retList, wordsOnly);
		   substitution(s, retList, wordsOnly);
		   deletions(s, retList, wordsOnly);
		   return retList;
	}

	/** Add to the currentList Strings that are one character mutation away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void substitution(String s, List<String> currentList, boolean wordsOnly) {
		
		// for each letter in the s and for all possible replacement characters
		for(int index = 0; index < s.length(); index++){
			for(int charCode = (int)'a'; charCode <= (int)'z'; charCode++) {
				// use StringBuffer for an easy interface to permuting the 
				// letters in the String
				StringBuffer sb = new StringBuffer(s);
				sb.setCharAt(index, (char)charCode);

				// if the item isn't in the list, isn't the original string, and
				// (if wordsOnly is true) is a real word, add to the list
				if(!currentList.contains(sb.toString()) && 
						(!wordsOnly||dict.isWord(sb.toString())) &&
						!s.equals(sb.toString())) {
					currentList.add(sb.toString());
				}
			}
		}
	}
	
	/** Add to the currentList Strings that are one character insertion away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 * 
	 * 
	 * One away word Strings for "or" are:
	 * [ora, orb, ore, for, nor, orr, tor]
	 * 
	 * 
	 */
	public void insertions(String s, List<String> currentList, boolean wordsOnly ) {
		// TODO: Implement this method 
		// for each letter in the s and for all possible replacement characters
		for(int index = 0; index < s.length(); index++){
			for(int charCode = (int)'a'; charCode <= (int)'z'; charCode++) {
				// use StringBuffer for an easy interface to permuting the letters in the String
				StringBuffer sb_1 = new StringBuffer(s);
				//insert before the character, for all characters in word
				sb_1.insert(index, ((char)charCode));															
				// if the item isn't in the list and (if wordsOnly is true) is a real word, add to the list
				if(!currentList.contains(sb_1.toString()) && (!wordsOnly||dict.isWord(sb_1.toString()))) {
					currentList.add(sb_1.toString());
				}
				
				StringBuffer sb_2 = new StringBuffer(s);
				//insert after the character, for all characters in word
				sb_2.insert(index+1, ((char)charCode));															

				if(!currentList.contains(sb_2.toString()) && (!wordsOnly||dict.isWord(sb_2.toString()))) {
					currentList.add(sb_2.toString());	
				}
			}
		}
	}

	/** Add to the currentList Strings that are one character deletion away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void deletions(String s, List<String> currentList, boolean wordsOnly ) {
		// TODO: Implement this method	
		//hat	
		//at	drop 1, return 2 and 3 		deleteCharAt 1
		//ht	drop 2, return 1 and 3		deleteCharAt 2
		//ha	drop 3, return 1 and 2		deleteCharAt 3
		
		// for each letter in the s and for all possible replacement characters
		for(int index = 0; index < s.length(); index++){
			for(int charCode = (int)'a'; charCode <= (int)'z'; charCode++) {
				// use StringBuffer for an easy interface to permuting the letters in the String
				StringBuffer sb = new StringBuffer(s);
				//delete the first character, followed by next . . . 
				sb.deleteCharAt(index);																					
				// if the item isn't in the list, isn't the original string, and (if wordsOnly is true) is a real word, add to the list
				if(!currentList.contains(sb.toString()) && (!wordsOnly||dict.isWord(sb.toString())) && !s.equals(sb.toString())) {
					//System.out.println(sb.toString());
					currentList.add(sb.toString());
				}
			}
		}
	}

	/** Add to the currentList Strings that are one character deletion away
	 * from the input string.  
	 * @param word The misspelled word
	 * @param numSuggestions is the maximum number of suggestions to return 
	 * @return the list of spelling suggestions
	 */
	@Override
	public List<String> suggestions(String word, int numSuggestions) {
		
		// initial variables
		// String to explore
		List<String> queue = new LinkedList<String>();  
		// to avoid exploring the same string multiple times   				
		HashSet<String> visited = new HashSet<String>();   
		// words to return				
		List<String> retList = new LinkedList<String>();   			
		 
		// insert first node
		queue.add(word);
		visited.add(word);
		
		// TODO: Implement the remainder of this method, see assignment for algorithm 
		while (!queue.isEmpty() && numSuggestions != 0) {
			String curr = queue.remove(0);								
						
			NearbyWords word_to_test = new NearbyWords(dict);
	        List<String> d1 = word_to_test.distanceOne(curr, true);
			
			for (String s : d1) {
				if (!visited.contains(s)) {
					visited.add(s);
					queue.add(s);
					if (dict.isWord(s)) {
						retList.add(s);
						numSuggestions--;
					}
				}
			}
		}
		return retList;
	}	

   public static void main(String[] args) {
	   //basic testing code to get started
	   String word = "or";
	   // Pass NearbyWords any Dictionary implementation you prefer
	   Dictionary d = new DictionaryHashSet();
	   DictionaryLoader.loadDictionary(d, "data/dict.txt");
	   NearbyWords w = new NearbyWords(d);
	   List<String> l = w.distanceOne(word, true);
	   System.out.println("One away word Strings for \""+word+"\" are:");
	   System.out.println(l+"\n");

	   word = "tailo";
	   List<String> suggest = w.suggestions(word, 3);
	   System.out.println("Spelling Suggestions for \""+word+"\" are:");
	   System.out.println(suggest);
	   
   }

}
