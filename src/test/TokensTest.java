/**
 * 
 */
package test;

import java.util.Properties;

import models.Corpus;
import models.Review.Aspect;
import models.SentiWordlist;

/**
 * @author Michi
 *
 */
public class TokensTest {

	/**
	 * Maximum number of reviews to keep
	 */
	public static int topX = 100;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Setup properties here
		Properties props = new Properties();
	    props.put("annotators", "tokenize, ssplit, pos, lemma");
		Corpus co = new Corpus(props);
		
		int size = co.loadFromFile("data/ratebeer.txt", topX);
		System.out.println("Amount of reviews loaded: " + size);
		
		Corpus topTaste = co.getTopReviews(Aspect.TASTE);
		topTaste.analyze();
		System.out.println(topTaste.getTokenConcatenation(Aspect.TASTE));
		
		SentiWordlist wl = new SentiWordlist();
		int wordcount = wl.loadFromFile("data/output/wordlists/TASTE.txt");
		System.out.println(wl.toString());
		System.out.println(wordcount);
		

	}

}
