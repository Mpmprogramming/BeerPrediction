/**
 * 
 */
package experiments;

import java.util.Properties;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import models.Experiment;

/**
 * @author apfelbaum24
 *
 */
public class RunSingleExperiment {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Setup properties here
		Properties props = new Properties();
		
		// Affects stanford core
		props.put("posToKeep", "NN ADJ");// TODO not implemented yet (Marc) See Review.analyze()
		props.put("useLemma", "true");// TODO:Check if lemma actually working
		props.put("includePOS", "false");// TODO: Will mess up word vector
		props.put("annotators", "tokenize, ssplit, pos, lemma");
		
		//Wordlist creation parameters
		props.put("idfTransform", "true");
		props.put("tfTransform", "true");
		props.put("outputWordCounts", "true");
		props.put("lowerCaseTokens", "true");
		props.put("useStoplist", "true");
		props.put("stopwordsFile", "data/english-stop-words-small.txt");
		props.put("wordsToKeep", "5000");
		props.put("minTermFreq", "3");
		props.put("minTopRatingscore", "1.0");
		props.put("maxLowRatingscore", "0.4");
		
		//Evlauation parameters
		props.put("minTopClassScore", "0.7");
		props.put("minSentimentTopScore", "5");
		
		Experiment experiment = new Experiment(props, new StanfordCoreNLP(props));
		experiment.run();

	}

}
