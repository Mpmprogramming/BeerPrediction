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
	public static int topX = 100000;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Corpus co = new Corpus(new Properties());
		int size = co.loadFromFile("data/ratebeer.txt", topX);
		System.out.println("Amount of reviews loaded: " + size);
		co.writeToCSV("data/output/ratebeer100K.csv");
		System.out.println("Output written.");
	}

}
