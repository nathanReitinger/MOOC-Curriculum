package document;

import java.util.List;

/** 
 * A naive implementation of the Document abstract class. 
 * @author UC San Diego Intermediate Programming MOOC team
 */
public class BasicDocument extends Document 
{
	/** Create a new BasicDocument object
	 * 
	 * @param text The full text of the Document.
	 */
	public BasicDocument(String text)
	{
		super(text);
	}
	
	
	/**
	 * Get the number of words in the document.
	 * A "word" is defined as a contiguous string of alphabetic characters
	 * i.e. any upper or lower case characters a-z or A-Z.  This method completely 
	 * ignores numbers when you count words, and assumes that the document does not have 
	 * any strings that combine numbers and letters. 
	 * 
	 * Check the examples in the main method below for more information.
	 * 
	 * This method should process the entire text string each time it is called.  
	 * 
	 * @return The number of words in the document.
	 */
	@Override
	public int getNumWords()
	{
		//TODO: Implement this method in week 1 according to the comments above.  
		// See the Module 1 support videos if you need help.
		//return the number of words in document 
		//use get tokens in document.java
		
		//keep everything following letter a-z or A-Z 
		int num_words = getTokens("[a-zA-Z]+").size();	
		//use .size() to count number in the array returned 								
		//System.out.println("this is the number of words: " + num_words);				
	    return num_words;
	    
	}
	
	/**
	 * Get the number of sentences in the document.
	 * Sentences are defined as contiguous strings of characters ending in an 
	 * end of sentence punctuation (. ! or ?) or the last contiguous set of 
	 * characters in the document, even if they don't end with a punctuation mark.
	 * 
	 * Check the examples in the main method below for more information.  
	 * 
	 * This method should process the entire text string each time it is called.  
	 * 
	 * @return The number of sentences in the document.
	 */
	@Override
	public int getNumSentences()
	{
	    //TODO: Implement this method.  See the Module 1 support videos 
        // if you need help.
		
		//one or more of the characters ===> . ? ! <===
		int num_sentences = getTokens("[^.?!]+").size();											
        return num_sentences;
	}
	
	/**
	 * Get the total number of syllables in the document (the stored text). 
	 * To count the number of syllables in a word, it uses the following rules:
	 *       Each contiguous sequence of one or more vowels is a syllable, 
	 *       with the following exception: a lone "e" at the end of a word 
	 *       is not considered a syllable unless the word has no other syllables. 
	 *       You should consider y a vowel.
	 *       
	 * Check the examples in the main method below for more information.  
	 * 
	 * This method should process the entire text string each time it is called.  
	 * 
	 * @return The number of syllables in the document.
	 */
	@Override
	public int getNumSyllables()
	{
	    //TODO: Implement this method in week 1.  See the Module 1 support videos 
        // if you need help.  And note that there is no need to use a regular
		// expression for the syllable counting.  We recommend you implement 
		// the helper function countSyllables in Document.java using a loop, 
		// and then call it here on each word.
        
		//grabbing sentences to pass as words to countSyllables;
		List<String> list_string = getTokens("[a-zA-Z]+");						
		
		//used to count up the running count (each word) for the entire sentence
		int running_count = 0;
		int total_count = 0;													
		int num_syllables = 0;

		//accounting for "Senteeeeeeeeeences"
		int double_chars = getTokens("aa|ee|ii|oo|uu|yy").size();				
		int total_double_chars = ((double_chars*2) - 1); 
		
		int buddy_vowels = getTokens("u[aeioy]|o[aeiuy]|i[aeouy]|a[eiouy]|e[aiouy]|y[aeiou]|U[aeioy]|O[aeiuy]|I[aeouy]|A[eiouy]|E[aiouy]|Y[aeiou]").size();
		int ending_vowels = getTokens(".*[aeiouy][aeiouy]$").size();
		int triple_vowels = getTokens("[aeiouy][aeiouy][aeiouy]|[AEOIUY][aeiou][aeiou]").size();

		
		
		for (int i = 0; i < list_string.size(); i++) {
			String s = list_string.get(i);
			running_count = countSyllables(s);
			//System.out.println(running_count);
			total_count = total_count + running_count;
		}
		
		num_syllables = total_count; 
		
		//accounting for "Senteeeeeeeeeences" if there were doubles	
		if (total_double_chars > 0) {																
			num_syllables = total_count-total_double_chars;
		}
		if (buddy_vowels > 0 && ending_vowels == 0) {
			num_syllables = num_syllables - buddy_vowels;
		}
		//we already account for ending vowels, so we need to offset the double counting
		if (buddy_vowels > 0 && ending_vowels > 0) {							
			num_syllables = num_syllables + (buddy_vowels - ending_vowels);
		}
		//accounting for yours specifically, that is why triple_vowels == 1
		if (buddy_vowels > 0 && triple_vowels == 1) {
			num_syllables = total_count - (buddy_vowels + triple_vowels);		
		}
		
		//System.out.println(total_count + "Total Count");
		return num_syllables;
        
        
	}
	
	
	/* The main method for testing this class. 
	 * You are encouraged to add your own tests.  */
	public static void main(String[] args)
	{
		/* Each of the test cases below uses the method testCase.  The first 
		 * argument to testCase is a Document object, created with the string shown.
		 * The next three arguments are the number of syllables, words and sentences 
		 * in the string, respectively.  You can use these examples to help clarify 
		 * your understanding of how to count syllables, words, and sentences.
		 */
		testCase(new BasicDocument("This is a test.  How many???  "
		        + "Senteeeeeeeeeences are here... there should be 5!  Right?"),
				16, 13, 5);
		testCase(new BasicDocument(""), 0, 0, 0);
		testCase(new BasicDocument("sentence, with, lots, of, commas.!  "
		        + "(And some poaren)).  The output is: 7.5."), 15, 11, 4);
		testCase(new BasicDocument("many???  Senteeeeeeeeeences are"), 6, 3, 2);
		testCase(new BasicDocument("Here is a series of test sentences. Your program should "
				+ "find 3 sentences, 33 words, and 49 syllables. Not every word will have "
				+ "the correct amount of syllables (example, for example), "
				+ "but most of them will."), 49, 33, 3);
		testCase(new BasicDocument("Segue"), 2, 1, 1);
		testCase(new BasicDocument("Sentence"), 2, 1, 1);
		testCase(new BasicDocument("Sentences?!"), 3, 1, 1);
		testCase(new BasicDocument("Lorem ipsum dolor sit amet, qui ex choro quodsi moderatius, nam dolores explicari forensibus ad."),
		         32, 15, 1);
	}
	
}
