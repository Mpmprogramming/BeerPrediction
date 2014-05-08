/**
 * 
 */
package experiments;

import java.util.Properties;

import models.Corpus;
import models.WordListGenerator;

/**
 * @author Michi
 *
 */
public class GenerateWordLists {


	/**
	 * Maximum number of reviews to load
	 */
	public static int topX = 2000;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		// Setup properties here
		Properties props = new Properties();
		props.put("idfTransform", "true");
		props.put("tfTransform", "true");
		props.put("outputWordCounts", "true");
		props.put("lowerCaseTokens", "true");
		props.put("useStoplist", "true");
		props.put("stopwordsFile", "data/english-stop-words-small.txt");
		props.put("wordsToKeep", 2000);
		props.put("minTermFreq", 3);
		props.put("minTopRatingscore", "0.9");
		props.put("maxLowRatingscore", "0.4");

		// Affects stanford core
		props.put("posToKeep", "NN JJ");// TODO not implemented yet (Marc) See
											// Review.analyze()
		props.put("useLemma", "true");// TODO:Check if lemma actually working
		props.put("includePOS", "true");// TODO: Will mess up word vector
											// creation
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		Corpus co = new Corpus(props);

		System.out.println(props.toString().replaceAll(", ", "\n"));

		int size = co.loadFromFile("data/ratebeer.txt", topX);
		System.out.println("Total amount of reviews loaded: " + size);
		
		WordListGenerator wlGenerator = new WordListGenerator("data/output/wordlists/");

		wlGenerator.generateWordlists(co);

	}

}
