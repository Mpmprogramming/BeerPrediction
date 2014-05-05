/**
 * 
 */
package scripts;

import java.util.Properties;

import models.Corpus;

/**
 * @author Michi
 *
 */
public class ConvertData2CSV {
	
	/**
	 * Maximum number of reviews to keep
	 */
	public static int topX = Integer.MAX_VALUE;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Setup properties here
		Properties props = new Properties();
	    props.put("annotators", "tokenize");
		Corpus co = new Corpus(props);
		
		int size = co.loadFromFile("data/test.txt", topX);
		System.out.println("Amount of reviews loaded: " + size);
		co.writeToCSV("data/output/test.csv");
		System.out.println("Output written.");
	}

}
